package au.edu.rmit.sept.webapp.repository;

import java.util.List;

import au.edu.rmit.sept.webapp.model.Vets;

public interface VetsRepository {
    List<Vets> findAll();
}

