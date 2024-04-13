package dto;

public class ParticipantDTO {
    private Long id;
    private String name;
    private int age;
    private int noRegistrations;

    public ParticipantDTO(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNoRegistrations() {
        return noRegistrations;
    }

    public void setNoRegistrations(int noRegistrations) {
        this.noRegistrations = noRegistrations;
    }
}
