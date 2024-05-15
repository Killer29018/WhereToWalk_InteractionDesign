# This converts raw .csv data into .json file.
import pandas as pd;

def process(name, lat, lon):
    return f'\t{{\n\t\t"name": "{name}",\n\t\t"lat": {lat},\n\t\t"lon": {lon}\n\t}},\n';

data = pd.read_csv("DoBIH_v18.csv");
file = open("DoBIH_v18.csv", "r");
output = open("hills.json", "w");
output.write("""{
\t"hills": [
""");
for _, row in data.iterrows():
    output.write(process(row["Name"], row["Latitude"], row["Longitude"]));

output.write("""\n\t]
}""");