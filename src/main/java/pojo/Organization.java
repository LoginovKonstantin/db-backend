package pojo;

public class Organization {
    private int id, locationId;
    private String name;

    public Organization(int id, String name, int locationId) {
        this.id = id;
        this.locationId = locationId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }
}
