package pojo;

public class TopMember {
    private int id, place, points;
    private String secondName, firstName, lastName;

    public TopMember(int id, int place, int points, String secondName, String firstName, String lastName) {
        this.id = id;
        this.place = place;
        this.points = points;
        this.secondName = secondName;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
