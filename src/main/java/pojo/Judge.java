package pojo;

public class Judge {
    private int id, organizationId, contestId;
    private String firstName, secondName, lastName;

    public Judge(int id, String secondName, String firstName, String lastName, int organizationId, int contestId) {
        this.id = id;
        this.organizationId = organizationId;
        this.contestId = contestId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public int getContestId() {
        return contestId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getLastName() {
        return lastName;
    }
}
