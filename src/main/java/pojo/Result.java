package pojo;

public class Result {
    private int id, contestId, place;
    private float points;

    public Result(int id, int contestId, int place, float points) {
        this.id = id;
        this.contestId = contestId;
        this.place = place;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public int getContestId() {
        return contestId;
    }

    public int getPlace() {
        return place;
    }

    public float getPoints() {
        return points;
    }
}
