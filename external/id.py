# Inserts Group ID generated by grouper.cpp into hills.json
# And generates a groupcoord.json
import json;
import math;

def genhills():
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
    
def gengroup():
    fhills = open("../src/main/resources/WhereToWalk/hills.json");
    data: dict = json.load(fhills);
    arr: list = data["hills"];
    
    groups = {};
    for obj in arr:
        id = obj["groupid"];
        if id not in groups:
            groups[id] = {
                "cnt": 0,
                "lon": 0.0,
                "lat": 0.0,
            };
        groups[id]["cnt"] += 1;
        groups[id]["lon"] += obj["lon"];
        groups[id]["lat"] += obj["lat"];
    
    for k, v in groups.items():
        groups[k] = {
            "lon": math.ceil(1e6 * v["lon"] / v["cnt"]) / 1e6,
            "lat": math.ceil(1e6 * v["lat"] / v["cnt"]) / 1e6
        };
    fres = open("../src/main/resources/WhereToWalk/groupcoords.json", "w");
    json.dump(groups, fres, indent=4);

genhills();
gengroup();