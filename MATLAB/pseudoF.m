function y = pseudoF(D, kfs, pws, cnt)
res = 0;
for i=1:cnt
    res = res + kfs(i) * D^pws(i);
end
y = res;
end