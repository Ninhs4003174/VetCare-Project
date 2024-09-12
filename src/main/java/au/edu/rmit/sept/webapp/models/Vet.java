package au.edu.rmit.sept.webapp.models;

public class Vet {
    private Long id;
    private String name;
    private String clinic;

    // Constructors, getters, and setters
    public Vet() {}

    public Vet(Long id, String name, String clinic) {
        this.id = id;
        this.name = name;
        this.clinic = clinic;
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

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }
}
