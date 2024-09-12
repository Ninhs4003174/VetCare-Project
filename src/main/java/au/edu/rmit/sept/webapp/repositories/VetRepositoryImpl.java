package au.edu.rmit.sept.webapp.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.models.Vet;

@Repository
public class VetRepositoryImpl implements VetRepository {

    private final DataSource source;

    public VetRepositoryImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public List<Vet> findAll() {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM vets;");
            ResultSet rs = stm.executeQuery();
            List<Vet> vets = new ArrayList<>();
            while (rs.next()) {
                Vet vet = new Vet(rs.getLong(1), rs.getString(2), rs.getString(3));
                vets.add(vet);
            }
            connection.close();
            return vets;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving vets", e);
        }
    }
}
