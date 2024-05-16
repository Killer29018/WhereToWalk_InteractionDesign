# Inserts Group ID generated by grouper.cpp into hills.json
# And generates a groupcoord.json
import json;

fhills = open("../src/main/resources/WhereToWalk/hills.json");
data: dict = json.load(fhills);
arr: list = data["hills"];

fgroups = open("../src/main/resources/WhereToWalk/hillgroups.txt");
strs = [*fgroups][1:];
groups = {}
for id, g in enumerate(strs):
    for each in list(map(lambda s: int(s.strip()), g.split(',')[:-1])):
        groups[each] = id;

for i, obj in enumerate(arr):
    obj["groupid"] = groups[i];

fres = open("../src/main/resources/WhereToWalk/hills_with_id.json", "w");
json.dump(data, fres, indent=4);