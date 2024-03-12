package model;

public class Participant extends Person {
    private int age;

    public Participant(String firstName, String lastName, int age) {
        super(firstName, lastName);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Participant { " +
                "id=" + id +
                ", fistName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", age=" + age +
                " }";
    }
}
