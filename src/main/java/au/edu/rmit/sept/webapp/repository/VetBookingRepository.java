package au.edu.rmit.sept.webapp.repository;

import java.util.List;

import au.edu.rmit.sept.webapp.model.VetBooking;

public interface VetBookingRepository {
    List<VetBooking> findAll();
}

