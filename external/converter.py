# This converts raw .csv data into .json file.
import pandas as pd;

def process(name, lat, lon, alt, county):
    return f'\t{{\n\t\t"name": "{name}",\n\t\t"lat": {lat},\n\t\t"lon": {lon},\n\t\t"alt": {alt},\n\t\t"county": "{county}"\n\t}},\n';


data = pd.read_csv("../src/main/resources/WhereToWalk/DoBIH_v18.csv");
output = open("../src/main/resources/WhereToWalk/hills.json", "w");
output.write("""{
\t"hills": [
""");
for _, row in data.iterrows():
    output.write(process(row["Name"], row["Latitude"], row["Longitude"], row["Metres"], row["County"]));

output.write("""\n\t]
}""");