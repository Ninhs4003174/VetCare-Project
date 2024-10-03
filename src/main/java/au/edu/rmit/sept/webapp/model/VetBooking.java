package au.edu.rmit.sept.webapp.model;

public class VetBooking {
    private Long id;
    private Long vetUserId; // Add this field
    private String vetName;
    private String clinic;
    private String serviceType; 
    // Constructors, getters, and setters
    public VetBooking() {}

    public VetBooking(Long id, Long vetUserId, String vetName, String clinic,String serviceType) {
        this.id = id;
        this.vetUserId = vetUserId;
        this.vetName = vetName;
        this.clinic = clinic;
        this.serviceType = serviceType;
    }

    public Long getId() {
        return id;    
    }

    public void setId(Long id) {
        this.id = id;
    }

   // Getters and setters
 public Long getVetUserId() {
            return vetUserId;
       }

       public void setVetUserId(Long vetUserId) {
           this.vetUserId = vetUserId;
       }
    
    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }
    
    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}