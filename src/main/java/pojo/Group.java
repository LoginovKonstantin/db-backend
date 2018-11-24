package pojo;

public class Group {
    private int id, age, sex;
    private float weight;
    private String rank;

    public Group(int id, int age, int sex, float weight, String rank) {
        this.id = id;
        this.age = age;
        this.sex = sex;
        this.weight = weight;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getSex() {
        return sex;
    }

    public float getWeight() {
        return weight;
    }

    public String getRank() {
        return rank;
    }
}
