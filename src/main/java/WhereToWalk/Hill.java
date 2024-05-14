package WhereToWalk;

public class Hill {
    private String name;
    private double lat;
    private double lon;

    protected Hill(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }


    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
