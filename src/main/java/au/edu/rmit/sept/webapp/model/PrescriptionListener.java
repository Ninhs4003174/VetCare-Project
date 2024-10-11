package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class PrescriptionListener {

    @PreUpdate
    @PrePersist
    public void handleDeliveryStatusChange(Prescription prescription) {
        if ("DELIVERED".equals(prescription.getDeliveryStatus()) && prescription.isRefillRequest()) {
            prescription.setRefillRequest(false);
            prescription.setDeliveryStatus("PENDING");
            prescription.setRefillsAvailable(prescription.getRefillsAvailable() + 1);
        }
    }
}