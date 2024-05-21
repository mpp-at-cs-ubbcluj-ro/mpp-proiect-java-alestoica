package model;

public class Participant extends Entity<Long> {
    private String firstName, lastName;
    private int age;

    public Participant(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
                ", firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", age=" + age +
                " }";
    }
}

//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import jakarta.validation.constraints.NotNull;
//
//@Entity
//@Table(name = "participants")
//public class Participant extends model.Entity<Long> {
//    @Id
//    private Long id;
//    @NotNull
//    @Column(name = "first_name")
//    private String firstName;
//    @NotNull
//    @Column(name = "last_name")
//    private String lastName;
//    @NotNull
//    private int age;
//
//    public Participant() {}
//
//    public Participant(String firstName, String lastName, int age) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.age = age;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    @Override
//    public String toString() {
//        return "Participant { " +
//                "id=" + id +
//                ", firstName=" + getFirstName() +
//                ", lastName=" + getLastName() +
//                ", age=" + age +
//                " }";
//    }
//}