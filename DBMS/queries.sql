-- 1) ActiveUsers
create or replace function ActiveUsers(rowner varchar(20), rname text) returns table (usr varchar(20)) as $$
begin
	return query
			(select DISTINCT u."user" from "public"."Users" u("user")
			 NATURAL JOIN "public"."Commits" cmts
			 NATURAL JOIN "public"."CommitInRepos" cmrp("commitHash", crname, crowner) where crname = rname and crowner = rowner);
end;
$$ language plpgsql;

select ActiveUsers('antonkov', 'Information-Theory');

-- 2) CurrentBranchState
create or replace function CurrentBranchState(bname text, rname text, rowner varchar(20)) returns table (objHash char(3), objData bytea) as $$
DECLARE
		headCommit char(3);
		rootHash char(3);
begin
		select b."headCommit" into headCommit from "public"."Branches" as b where b."reponame" = rname and b."repoowner" = rowner and b."name" = bname;
		select c.rootHash into rootHash from "public"."Commits" as c where c."commitHash" = headCommit;
		return query
		(select * from "public"."Objects" as objs where exists (
			(with recursive recursesubobj(obj, parent) as (
					select t1."object", t1.parent from "public"."TreeObjects" as t1
				union
					select t."object", rs.parent from "public"."TreeObjects" as t
					join recursesubobj rs on rs.obj = t."parent"
			)
			select * from recursesubobj as r where r.obj = objs."objectHash" and r.parent = rootHash)) or objs."objectHash" = rootHash);
end;
$$ language plpgsql;

select CurrentBranchState('feature_branch', 'Information-Theory', 'antonkov');

-- 3) CurrentStreak
create or replace function CurrentStreak(usr varchar(20)) returns int4 as $$
declare
	cday timestamp;
	res int4;
begin
	cday := date_trunc('day', localtimestamp);
	res := 0;
	loop
		if exists (select * from "public"."Commits" coms
			where coms."user" = usr and cday >= coms."time" and cday - coms."time" <= interval '1 day') THEN
			cday := cday - interval '1 day';
			res := res + 1;
		ELSE
			exit;
		end if;
	end loop;
	return res;
--return query
	--		(select DISTINCT who from "public"."Following" where whom = usr);
end;
$$ language plpgsql;

drop materialized view if exists CurrentStreaks;
drop trigger if exists update_streak_com on "public"."Commits" CASCADE;
drop trigger if exists update_streak_usr on "public"."Users" CASCADE;

drop function if exists update_streak_com_f() CASCADE;
drop function if exists update_streak_usr_f() CASCADE;

create materialized view CurrentStreaks as 
    select usrs.nickname, CurrentStreak(usrs.nickname) from "public"."Users" usrs;

create function update_streak_usr_f() returns trigger AS $$
    BEGIN
			 REFRESH MATERIALIZED VIEW CurrentStreaks;   
       RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

create function update_streak_com_f() returns trigger AS $$
    BEGIN
			 REFRESH MATERIALIZED VIEW currentstreaks;   
       RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

create trigger update_streak_com after update or insert on "public"."Commits"
    for each row execute procedure update_streak_com_f();
create trigger update_streak_usr after update or insert or delete on "public"."Users"
		for each row execute procedure update_streak_usr_f();

delete from "public"."Commits" cmts where cmts."commitHash" = '444' or cmts."commitHash" = '555';
insert into "public"."Commits" values ('444', 'test', null, '29f', 'antonkov', 'TCPFileShare', 'antonkov', '2015-01-12 09:28:32');
insert into "public"."Commits" values ('555', 'test', null, '29f', 'grtkachenko', 'MachineLearning', 'grtkachenko', '2015-01-14 09:28:32');

select * from CurrentStreaks;

-- 4) UserFollowers
create or replace function UserFollowers(usr varchar(20)) returns table (usrs varchar(20)) as $$
begin
	return query
			(select DISTINCT who from "public"."Following" where whom = usr);
end;
$$ language plpgsql;

select UserFollowers('antonkov');

-- 5) ForkRepo
create or replace function ForkRepo(rname text, rowner varchar(20), newrname text, newrowner varchar(20)) returns void as $$
declare
	myrow "public"."CommitInRepos"%ROWTYPE;
	mybranch "public"."Branches"%ROWTYPE;
begin
	insert into "public"."Repos" values (newrname, null, newrowner);
	for mybranch in select * from "public"."Branches" as brs where brs.reponame = rname and brs.repoowner = rowner loop
		mybranch.reponame = newrname;
		mybranch.repoowner = newrowner;
		insert into "public"."Branches" select mybranch.*;
	end loop;
	for myrow in select * from "public"."CommitInRepos" as com where com.reponame = rname and com.repoowner = rowner loop
		myrow.reponame = newrname;
		myrow.repoowner = newrowner;
		--RAISE NOTICE 'i want to print %', myrow.*;
		insert into "public"."CommitInRepos" select myrow.*;
	end loop;
end;
$$ language plpgsql;

--select ForkRepo('TCPFileShare', 'antonkov', 'TCPFileShare', 'grtkachenko');

-- 6) LastActivityOfUser
create or replace function LastActivityOfUser(rowner varchar(20), lasttime interval) returns table (res char(3)) as $$
begin
	return query
			(select coms."commitHash" from "public"."Commits" coms where coms."user" = rowner and localtimestamp - coms."time" <= lasttime);
end;
$$ language plpgsql;

select LastActivityOfUser('antonkov', '2 days');

-- 7) RepoRating
drop view if exists RepoRating CASCADE;

create view RepoRating AS
		select rps."name", count(*) as stars from ("public"."Repos"
			natural join "public"."RepoStars" strs("user", "name", "owner")) rps group by rps."name" order by rps."name";

drop view if exists MostPopularRepos CASCADE;

create view MostPopularRepos as 
    select rng."name" from RepoRating rng where 
        rng.stars = (select max(RepoRating.stars) from RepoRating);

select * from RepoRating;
select * from MostPopularRepos;

-- 8) CommitTrigger
create or replace function 	make_commit() returns trigger as $$
	BEGIN
		if NEW.user not in (select "public"."Collabos".user from "public"."Collabos" where reponame = NEW.reponame and repoowner = NEW.repoowner) then
			raise EXCEPTION 'This user cant commit to this repo';
		else
			insert into "public"."CommitInRepos" values (NEW."commitHash", NEW.reponame, NEW.repoowner);
			return new;
		end if;
	END
$$ LANGUAGE plpgsql;

drop trigger if exists OnlyCollabCanCommit on "public"."Commits";

create trigger OnlyCollabCanCommit after insert or update on "public"."Commits"
for each row 
execute procedure make_commit();

--insert into "public"."Commits" values ('111', 'test', null, '29f', 'antonkov', 'Information-Theory', 'antonkov');
delete from "public"."Commits" cmts where cmts."commitHash" = '333';
insert into "public"."Commits" values ('333', 'test', null, '29f', 'antonkov', 'TCPFileShare', 'antonkov', '2015-01-13 09:28:32');