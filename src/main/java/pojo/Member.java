package pojo;

public class Member {
    private int id, number, contestId, organizationId, resultId, groupId;
    private String secondName, firstName, lastName;

    public Member(int id, String secondName, String firstName, String lastName, int number, int contestId, int organizationId, int resultId, int groupId) {
        this.id = id;
        this.number = number;
        this.contestId = contestId;
        this.organizationId = organizationId;
        this.resultId = resultId;
        this.groupId = groupId;
        this.secondName = secondName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public int getContestId() {
        return contestId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public int getResultId() {
        return resultId;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
