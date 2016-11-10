/*
Navicat PGSQL Data Transfer

Source Server         : Default
Source Server Version : 90305
Source Host           : localhost:5432
Source Database       : github.com
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 90305
File Encoding         : 65001

Date: 2015-01-15 12:44:36
*/


-- ----------------------------
-- Table structure for "public"."Branches"
-- ----------------------------
DROP TABLE IF EXISTS "public"."Branches" cascade;
CREATE TABLE "public"."Branches" (
"name" text NOT NULL,
"headCommit" char(3) NOT NULL,
"reponame" text NOT NULL,
"repoowner" varchar(20) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of Branches
-- ----------------------------
INSERT INTO "public"."Branches" VALUES ('feature_branch', '10e', 'Information-Theory', 'antonkov');
INSERT INTO "public"."Branches" VALUES ('master', '0ca', 'Information-Theory', 'antonkov');
INSERT INTO "public"."Branches" VALUES ('master', '222', 'TCPFileShare', 'antonkov');
INSERT INTO "public"."Branches" VALUES ('master', '222', 'TCPFileShare', 'grtkachenko');

-- ----------------------------
-- Table structure for "public"."Collabos"
-- ----------------------------
DROP TABLE IF EXISTS "public"."Collabos" cascade;
CREATE TABLE "public"."Collabos" (
"user" varchar(20) NOT NULL,
"reponame" text NOT NULL,
"repoowner" varchar(20) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of Collabos
-- ----------------------------
INSERT INTO "public"."Collabos" VALUES ('antonkov', 'Information-Theory', 'antonkov');
INSERT INTO "public"."Collabos" VALUES ('antonkov', 'MachineLearning', 'grtkachenko');
INSERT INTO "public"."Collabos" VALUES ('antonkov', 'TCPFileShare', 'antonkov');
INSERT INTO "public"."Collabos" VALUES ('BorysMinaiev', 'Information-Theory', 'antonkov');
INSERT INTO "public"."Collabos" VALUES ('grtkachenko', 'Information-Theory', 'antonkov');
INSERT INTO "public"."Collabos" VALUES ('grtkachenko', 'MachineLearning', 'grtkachenko');
INSERT INTO "public"."Collabos" VALUES ('ZumZoom', 'Information-Theory', 'antonkov');
INSERT INTO "public"."Collabos" VALUES ('ZumZoom', 'TicTacToe', 'ZumZoom');

-- ----------------------------
-- Table structure for "public"."CommitInRepos"
-- ----------------------------
DROP TABLE IF EXISTS "public"."CommitInRepos" cascade;
CREATE TABLE "public"."CommitInRepos" (
"commitHash" char(3) NOT NULL,
"reponame" text NOT NULL,
"repoowner" varchar(20) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of CommitInRepos
-- ----------------------------
INSERT INTO "public"."CommitInRepos" VALUES ('0ca', 'Information-Theory', 'antonkov');
INSERT INTO "public"."CommitInRepos" VALUES ('10e', 'Information-Theory', 'antonkov');
INSERT INTO "public"."CommitInRepos" VALUES ('111', 'Information-Theory', 'antonkov');
INSERT INTO "public"."CommitInRepos" VALUES ('222', 'TCPFileShare', 'antonkov');
INSERT INTO "public"."CommitInRepos" VALUES ('222', 'TCPFileShare', 'grtkachenko');
INSERT INTO "public"."CommitInRepos" VALUES ('333', 'TCPFileShare', 'antonkov');
INSERT INTO "public"."CommitInRepos" VALUES ('444', 'TCPFileShare', 'antonkov');
INSERT INTO "public"."CommitInRepos" VALUES ('4fc', 'Information-Theory', 'antonkov');
INSERT INTO "public"."CommitInRepos" VALUES ('555', 'MachineLearning', 'grtkachenko');

-- ----------------------------
-- Table structure for "public"."Commits"
-- ----------------------------
DROP TABLE IF EXISTS "public"."Commits" cascade;
CREATE TABLE "public"."Commits" (
"commitHash" char(3) NOT NULL,
"msg" text NOT NULL,
"data" text,
"roothash" char(3) NOT NULL,
"user" varchar(20) NOT NULL,
"reponame" text NOT NULL,
"repoowner" varchar(20) NOT NULL,
"time" timestamp(6) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of Commits
-- ----------------------------
INSERT INTO "public"."Commits" VALUES ('0ca', 'second commit', null, '5eb', 'grtkachenko', 'Information-Theory', 'antonkov', '2015-01-07 07:28:35');
INSERT INTO "public"."Commits" VALUES ('10e', 'third commit', null, 'e9c', 'BorysMinaiev', 'Information-Theory', 'antonkov', '2015-01-08 07:28:38');
INSERT INTO "public"."Commits" VALUES ('111', 'test', null, '29f', 'antonkov', 'Information-Theory', 'antonkov', '2015-01-13 07:28:42');
INSERT INTO "public"."Commits" VALUES ('222', 'test', null, '29f', 'antonkov', 'TCPFileShare', 'antonkov', '2015-01-14 07:28:46');
INSERT INTO "public"."Commits" VALUES ('333', 'test', null, '29f', 'antonkov', 'TCPFileShare', 'antonkov', '2015-01-13 09:28:32');
INSERT INTO "public"."Commits" VALUES ('444', 'test', null, '29f', 'antonkov', 'TCPFileShare', 'antonkov', '2015-01-12 09:28:32');
INSERT INTO "public"."Commits" VALUES ('4fc', 'first commit', null, '29f', 'antonkov', 'Information-Theory', 'antonkov', '2015-01-13 07:28:32');
INSERT INTO "public"."Commits" VALUES ('555', 'test', null, '29f', 'grtkachenko', 'MachineLearning', 'grtkachenko', '2015-01-14 09:28:32');

-- ----------------------------
-- Table structure for "public"."Following"
-- ----------------------------
DROP TABLE IF EXISTS "public"."Following" cascade;
CREATE TABLE "public"."Following" (
"whom" varchar(20) NOT NULL,
"who" varchar(20) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of Following
-- ----------------------------
INSERT INTO "public"."Following" VALUES ('antonkov', 'ZumZoom');
INSERT INTO "public"."Following" VALUES ('BorysMinaiev', 'antonkov');
INSERT INTO "public"."Following" VALUES ('grtkachenko', 'antonkov');
INSERT INTO "public"."Following" VALUES ('grtkachenko', 'BorysMinaiev');
INSERT INTO "public"."Following" VALUES ('grtkachenko', 'ZumZoom');
INSERT INTO "public"."Following" VALUES ('ZumZoom', 'BorysMinaiev');

-- ----------------------------
-- Table structure for "public"."Issues"
-- ----------------------------
DROP TABLE IF EXISTS "public"."Issues" cascade;
CREATE TABLE "public"."Issues" (
"issueId" int4 NOT NULL,
"text" text NOT NULL,
"status" text NOT NULL,
"author" varchar(20) NOT NULL,
"reponame" text NOT NULL,
"repoowner" varchar(20) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of Issues
-- ----------------------------
INSERT INTO "public"."Issues" VALUES ('1', 'Make library with common functions', 'open', 'grtkachenko', 'MachineLearning', 'grtkachenko');
INSERT INTO "public"."Issues" VALUES ('2', 'Huffman algorithm has wrong ouput format', 'closed', 'antonkov', 'Information-Theory', 'antonkov');

-- ----------------------------
-- Table structure for "public"."Objects"
-- ----------------------------
DROP TABLE IF EXISTS "public"."Objects" cascade;
CREATE TABLE "public"."Objects" (
"objectHash" char(3) NOT NULL,
"data" bytea
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of Objects
-- ----------------------------
INSERT INTO "public"."Objects" VALUES ('29f', E'"');
INSERT INTO "public"."Objects" VALUES ('5eb', E'\\020');
INSERT INTO "public"."Objects" VALUES ('9b0', E'6$e');
INSERT INTO "public"."Objects" VALUES ('a7a', E'\\020');
INSERT INTO "public"."Objects" VALUES ('aae', E'\\020');
INSERT INTO "public"."Objects" VALUES ('e9c', E'TdVeFF');

-- ----------------------------
-- Table structure for "public"."prevCommit"
-- ----------------------------
DROP TABLE IF EXISTS "public"."prevCommit" cascade;
CREATE TABLE "public"."prevCommit" (
"curHash" char(3) NOT NULL,
"prevHash" char(3) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of prevCommit
-- ----------------------------
INSERT INTO "public"."prevCommit" VALUES ('0ca', '10e');
INSERT INTO "public"."prevCommit" VALUES ('4fc', '0ca');

-- ----------------------------
-- Table structure for "public"."prevCommit_merge"
-- ----------------------------
DROP TABLE IF EXISTS "public"."prevCommit_merge" cascade;
CREATE TABLE "public"."prevCommit_merge" (
"curHash" char(3) NOT NULL,
"prevHash" char(3) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of prevCommit_merge
-- ----------------------------

-- ----------------------------
-- Table structure for "public"."PullRequests"
-- ----------------------------
DROP TABLE IF EXISTS "public"."PullRequests" cascade;
CREATE TABLE "public"."PullRequests" (
"pullId" int4 NOT NULL,
"text" text NOT NULL,
"status" text NOT NULL,
"author" varchar(20) NOT NULL,
"fromBranch" int4 NOT NULL,
"toBranch" int4 NOT NULL,
"fromname" text NOT NULL,
"fromrepo" text NOT NULL,
"fromowner" varchar(20) NOT NULL,
"toname" text NOT NULL,
"torepo" text NOT NULL,
"toowner" varchar(20) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of PullRequests
-- ----------------------------
INSERT INTO "public"."PullRequests" VALUES ('1', 'old version backuped', 'open', 'BorysMinaiev', '2', '1', 'feature_branch', 'Information-Theory', 'antonkov', 'master', 'Information-Theory', 'antonkov');

-- ----------------------------
-- Table structure for "public"."Repos"
-- ----------------------------
DROP TABLE IF EXISTS "public"."Repos" cascade;
CREATE TABLE "public"."Repos" (
"name" text NOT NULL,
"info" text,
"owner" varchar(20) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of Repos
-- ----------------------------
INSERT INTO "public"."Repos" VALUES ('Information-Theory', null, 'antonkov');
INSERT INTO "public"."Repos" VALUES ('MachineLearning', 'Machine learning course', 'grtkachenko');
INSERT INTO "public"."Repos" VALUES ('TCPFileShare', null, 'antonkov');
INSERT INTO "public"."Repos" VALUES ('TCPFileShare', null, 'grtkachenko');
INSERT INTO "public"."Repos" VALUES ('TicTacToe', 'TicTacToe with deep gameplay', 'ZumZoom');

-- ----------------------------
-- Table structure for "public"."RepoStars"
-- ----------------------------
DROP TABLE IF EXISTS "public"."RepoStars" cascade;
CREATE TABLE "public"."RepoStars" (
"user" varchar(20) NOT NULL,
"reponame" text NOT NULL,
"repoowner" varchar(20) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of RepoStars
-- ----------------------------
INSERT INTO "public"."RepoStars" VALUES ('antonkov', 'Information-Theory', 'antonkov');
INSERT INTO "public"."RepoStars" VALUES ('antonkov', 'TicTacToe', 'ZumZoom');
INSERT INTO "public"."RepoStars" VALUES ('grtkachenko', 'MachineLearning', 'grtkachenko');
INSERT INTO "public"."RepoStars" VALUES ('ZumZoom', 'Information-Theory', 'antonkov');
INSERT INTO "public"."RepoStars" VALUES ('ZumZoom', 'MachineLearning', 'grtkachenko');

-- ----------------------------
-- Table structure for "public"."TreeObjects"
-- ----------------------------
DROP TABLE IF EXISTS "public"."TreeObjects" cascade;
CREATE TABLE "public"."TreeObjects" (
"parent" char(3) NOT NULL,
"object" char(3) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of TreeObjects
-- ----------------------------
INSERT INTO "public"."TreeObjects" VALUES ('29f', 'aae');
INSERT INTO "public"."TreeObjects" VALUES ('5eb', '9b0');
INSERT INTO "public"."TreeObjects" VALUES ('5eb', 'a7a');
INSERT INTO "public"."TreeObjects" VALUES ('e9c', '29f');
INSERT INTO "public"."TreeObjects" VALUES ('e9c', '9b0');
INSERT INTO "public"."TreeObjects" VALUES ('e9c', 'a7a');

-- ----------------------------
-- Table structure for "public"."Users"
-- ----------------------------
DROP TABLE IF EXISTS "public"."Users" cascade;
CREATE TABLE "public"."Users" (
"nickname" varchar(20) NOT NULL,
"password" text NOT NULL,
"info" text
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of Users
-- ----------------------------
INSERT INTO "public"."Users" VALUES ('antonkov', '10191019', 'Anton Kovsharov Joined on Oct 11, 2012');
INSERT INTO "public"."Users" VALUES ('BorysMinaiev', 'qwerty787788', 'Borys Minaiev Joined on Jul 20, 2012');
INSERT INTO "public"."Users" VALUES ('grtkachenko', 'supersecurepass', 'Grigory Tkachenko Joined on Oct 11, 2012');
INSERT INTO "public"."Users" VALUES ('ZumZoom', 'zumzumzum', 'Mikhail Melnik Joined on Apr 17, 2011');

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------

-- ----------------------------
-- Uniques structure for table "public"."Branches"
-- ----------------------------
ALTER TABLE "public"."Branches" ADD UNIQUE ("name", "reponame", "repoowner");

-- ----------------------------
-- Primary Key structure for table "public"."Branches"
-- ----------------------------
ALTER TABLE "public"."Branches" ADD PRIMARY KEY ("name", "reponame", "repoowner");

-- ----------------------------
-- Primary Key structure for table "public"."Collabos"
-- ----------------------------
ALTER TABLE "public"."Collabos" ADD PRIMARY KEY ("user", "reponame", "repoowner");

-- ----------------------------
-- Primary Key structure for table "public"."CommitInRepos"
-- ----------------------------
ALTER TABLE "public"."CommitInRepos" ADD PRIMARY KEY ("commitHash", "reponame", "repoowner");

-- ----------------------------
-- Primary Key structure for table "public"."Commits"
-- ----------------------------
ALTER TABLE "public"."Commits" ADD PRIMARY KEY ("commitHash");

-- ----------------------------
-- Primary Key structure for table "public"."Following"
-- ----------------------------
ALTER TABLE "public"."Following" ADD PRIMARY KEY ("whom", "who");

-- ----------------------------
-- Primary Key structure for table "public"."Issues"
-- ----------------------------
ALTER TABLE "public"."Issues" ADD PRIMARY KEY ("issueId");

-- ----------------------------
-- Primary Key structure for table "public"."Objects"
-- ----------------------------
ALTER TABLE "public"."Objects" ADD PRIMARY KEY ("objectHash");

-- ----------------------------
-- Primary Key structure for table "public"."prevCommit"
-- ----------------------------
ALTER TABLE "public"."prevCommit" ADD PRIMARY KEY ("curHash");

-- ----------------------------
-- Primary Key structure for table "public"."prevCommit_merge"
-- ----------------------------
ALTER TABLE "public"."prevCommit_merge" ADD PRIMARY KEY ("curHash");

-- ----------------------------
-- Primary Key structure for table "public"."PullRequests"
-- ----------------------------
ALTER TABLE "public"."PullRequests" ADD PRIMARY KEY ("pullId");

-- ----------------------------
-- Uniques structure for table "public"."Repos"
-- ----------------------------
ALTER TABLE "public"."Repos" ADD UNIQUE ("name", "owner");

-- ----------------------------
-- Primary Key structure for table "public"."Repos"
-- ----------------------------
ALTER TABLE "public"."Repos" ADD PRIMARY KEY ("name", "owner");

-- ----------------------------
-- Primary Key structure for table "public"."RepoStars"
-- ----------------------------
ALTER TABLE "public"."RepoStars" ADD PRIMARY KEY ("user", "reponame", "repoowner");

-- ----------------------------
-- Primary Key structure for table "public"."TreeObjects"
-- ----------------------------
ALTER TABLE "public"."TreeObjects" ADD PRIMARY KEY ("object", "parent");

-- ----------------------------
-- Primary Key structure for table "public"."Users"
-- ----------------------------
ALTER TABLE "public"."Users" ADD PRIMARY KEY ("nickname");

-- ----------------------------
-- Foreign Key structure for table "public"."Branches"
-- ----------------------------
ALTER TABLE "public"."Branches" ADD FOREIGN KEY ("reponame", "repoowner") REFERENCES "public"."Repos" ("name", "owner") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."Branches" ADD FOREIGN KEY ("headCommit") REFERENCES "public"."Commits" ("commitHash") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."Collabos"
-- ----------------------------
ALTER TABLE "public"."Collabos" ADD FOREIGN KEY ("reponame", "repoowner") REFERENCES "public"."Repos" ("name", "owner") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."Collabos" ADD FOREIGN KEY ("user") REFERENCES "public"."Users" ("nickname") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."CommitInRepos"
-- ----------------------------
ALTER TABLE "public"."CommitInRepos" ADD FOREIGN KEY ("commitHash") REFERENCES "public"."Commits" ("commitHash") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "public"."CommitInRepos" ADD FOREIGN KEY ("reponame", "repoowner") REFERENCES "public"."Repos" ("name", "owner") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."Commits"
-- ----------------------------
ALTER TABLE "public"."Commits" ADD FOREIGN KEY ("reponame", "repoowner") REFERENCES "public"."Repos" ("name", "owner") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."Commits" ADD FOREIGN KEY ("user") REFERENCES "public"."Users" ("nickname") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."Commits" ADD FOREIGN KEY ("roothash") REFERENCES "public"."Objects" ("objectHash") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."Following"
-- ----------------------------
ALTER TABLE "public"."Following" ADD FOREIGN KEY ("who") REFERENCES "public"."Users" ("nickname") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."Following" ADD FOREIGN KEY ("whom") REFERENCES "public"."Users" ("nickname") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."Issues"
-- ----------------------------
ALTER TABLE "public"."Issues" ADD FOREIGN KEY ("author") REFERENCES "public"."Users" ("nickname") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."Issues" ADD FOREIGN KEY ("reponame", "repoowner") REFERENCES "public"."Repos" ("name", "owner") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."prevCommit"
-- ----------------------------
ALTER TABLE "public"."prevCommit" ADD FOREIGN KEY ("prevHash") REFERENCES "public"."Commits" ("commitHash") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."prevCommit" ADD FOREIGN KEY ("curHash") REFERENCES "public"."Commits" ("commitHash") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."prevCommit_merge"
-- ----------------------------
ALTER TABLE "public"."prevCommit_merge" ADD FOREIGN KEY ("prevHash") REFERENCES "public"."Commits" ("commitHash") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."prevCommit_merge" ADD FOREIGN KEY ("curHash") REFERENCES "public"."Commits" ("commitHash") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."PullRequests"
-- ----------------------------
ALTER TABLE "public"."PullRequests" ADD FOREIGN KEY ("toname", "torepo", "toowner") REFERENCES "public"."Branches" ("name", "reponame", "repoowner") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."PullRequests" ADD FOREIGN KEY ("author") REFERENCES "public"."Users" ("nickname") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."PullRequests" ADD FOREIGN KEY ("fromname", "fromrepo", "fromowner") REFERENCES "public"."Branches" ("name", "reponame", "repoowner") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."Repos"
-- ----------------------------
ALTER TABLE "public"."Repos" ADD FOREIGN KEY ("owner") REFERENCES "public"."Users" ("nickname") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."RepoStars"
-- ----------------------------
ALTER TABLE "public"."RepoStars" ADD FOREIGN KEY ("reponame", "repoowner") REFERENCES "public"."Repos" ("name", "owner") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."RepoStars" ADD FOREIGN KEY ("user") REFERENCES "public"."Users" ("nickname") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."TreeObjects"
-- ----------------------------
ALTER TABLE "public"."TreeObjects" ADD FOREIGN KEY ("object") REFERENCES "public"."Objects" ("objectHash") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."TreeObjects" ADD FOREIGN KEY ("parent") REFERENCES "public"."Objects" ("objectHash") ON DELETE NO ACTION ON UPDATE NO ACTION;
