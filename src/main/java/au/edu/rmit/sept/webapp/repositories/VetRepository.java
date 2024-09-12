package au.edu.rmit.sept.webapp.repositories;

import java.util.List;

import au.edu.rmit.sept.webapp.models.Vet;

public interface VetRepository {
    List<Vet> findAll();
}

