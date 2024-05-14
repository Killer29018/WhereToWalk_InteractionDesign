// This applies Newman-Girvan method to group hills together.
#include <fstream>
#include <string>
#include <vector>
#include <cmath>
#include <cstring>
#include <queue>
#include <iostream>

using edge_vec = std::vector<std::vector<std::pair<int, double>>>;
struct hill {
    std::string name;
    double lon, lat;
    int id;
};

// In km
double distance(hill a, hill b) {
    constexpr double R = 6371;

    double latDistance = M_PI / 180 * (a.lat - b.lat);
    double lonDistance = M_PI / 180 * (a.lon - b.lon);
    double v = sin(latDistance / 2) * sin(latDistance / 2)
            + cos(M_PI / 180 * a.lat) * cos(M_PI / 180 * b.lat)
            * sin(lonDistance / 2) * sin(lonDistance / 2);
    double c = 2 * atan2(sqrt(v), sqrt(1 - v));

    return R * c;
}

int main() {
    // The json structure is pretty much fixed.
    std::ifstream ifs("../../hills.json");
    std::ofstream ofs("../../hillgroups.txt");
    std::string str;
    std::vector<hill> hills;
    hills.reserve(21000);

    // Removes { "hills": [ at the beginning
    getline(ifs, str);
    getline(ifs, str);

    int id = 0;

    while (getline(ifs, str) && str != "\t]") {
        getline(ifs, str);
        // \t\t"name": "..."
        std::string name = str.substr(10);
        getline(ifs, str);
        // \t\t"lat": 0.0
        double lat = atof(str.substr(9).c_str());
        getline(ifs, str);
        // \t\t"lon": 0.0
        double lon = atof(str.substr(9).c_str());
        hills.push_back({ name, lat, lon, id++ });
        // },
        getline(ifs, str);
    }

    std::vector<std::vector<hill>> groups;
    for (auto h : hills) {
        bool grouped = false;
        for (auto& vec : groups) {
            if (distance(h, vec[0]) < 10) {
                grouped = true;
                vec.push_back(h);
            }
        }
        if (!grouped)
            groups.push_back(std::vector<hill> { h });
    }

    ofs << groups.size() << std::endl;
    for (auto vec : groups) {
        for (auto h : vec) {
            ofs << h.id << ", ";
        }
        ofs << "\n";
    }
    return 0;
}