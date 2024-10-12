package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

@Entity
public class PetRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String breed;
    private String dateOfBirth;
    private String lastVisit;
    private String allergies;
    private String prescriptions;
    private String vaccinationHistory;
    private String recentTests;
    private String recentSurgeries;
    private String dietaryRecommendations;
    private String notes;
    private String veterinarian;

    @ManyToOne
    @JoinColumn(name = "vet_id")
    private Vet vet; // Reference to Vet entity

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // User entity as a foreign key

    // Constructors, getters, and setters

    public PetRecord() {
    }

    public PetRecord(String name, String breed, String dateOfBirth, String lastVisit, String allergies,
            String prescriptions, String vaccinationHistory, String recentTests, String recentSurgeries,
            String dietaryRecommendations, String notes, String veterinarian, Vet vet, User user) {
        this.name = name;
        this.breed = breed;
        this.dateOfBirth = dateOfBirth;
        this.lastVisit = lastVisit;
        this.allergies = allergies;
        this.prescriptions = prescriptions;
        this.vaccinationHistory = vaccinationHistory;
        this.recentTests = recentTests;
        this.recentSurgeries = recentSurgeries;
        this.dietaryRecommendations = dietaryRecommendations;
        this.notes = notes;
        this.veterinarian = veterinarian;
        this.vet = vet;
        this.user = user;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(String prescriptions) {
        this.prescriptions = prescriptions;
    }

    public String getVaccinationHistory() {
        return vaccinationHistory;
    }

    public void setVaccinationHistory(String vaccinationHistory) {
        this.vaccinationHistory = vaccinationHistory;
    }

    public String getRecentTests() {
        return recentTests;
    }

    public void setRecentTests(String recentTests) {
        this.recentTests = recentTests;
    }

    public String getRecentSurgeries() {
        return recentSurgeries;
    }

    public void setRecentSurgeries(String recentSurgeries) {
        this.recentSurgeries = recentSurgeries;
    }

    public String getDietaryRecommendations() {
        return dietaryRecommendations;
    }

    public void setDietaryRecommendations(String dietaryRecommendations) {
        this.dietaryRecommendations = dietaryRecommendations;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(String veterinarian) {
        this.veterinarian = veterinarian;
    }

    public Vet getVet() {
        return vet;
    }

    public void setVet(Vet vet) {
        this.vet = vet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
