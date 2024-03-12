package model;

public class Employee extends Person {
    private String username, password;

    public Employee(String firstName, String lastName, String username, String password) {
        super(firstName, lastName);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Employee { " +
                "id=" + id +
                ", fistName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", username=" + username +
                ", password=" + password +
                " }";
    }
}
