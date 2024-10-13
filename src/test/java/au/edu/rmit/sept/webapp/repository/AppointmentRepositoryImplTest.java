package au.edu.rmit.sept.webapp.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;

public class AppointmentRepositoryImplTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private AppointmentRepositoryImpl appointmentRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void testFindByUser() throws SQLException {
        User user = new User(1L, "testUser", "password", "test@example.com", "1234567890", "123 Test St", "CLIENT", 1L);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getLong("pet_id")).thenReturn(1L);
        when(resultSet.getString("pet_name")).thenReturn("Buddy");
        when(resultSet.getLong("vet_id")).thenReturn(1L);
        when(resultSet.getString("date")).thenReturn("2023-10-01");
        when(resultSet.getString("time")).thenReturn("10:00");
        when(resultSet.getString("status")).thenReturn("Scheduled");

        List<Appointment> result = appointmentRepository.findByUser(user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(1L, result.get(0).getPetId());
        assertEquals("Buddy", result.get(0).getPetName());
        assertEquals(1L, result.get(0).getVetId());
        assertEquals("2023-10-01", result.get(0).getDate());
        assertEquals("10:00", result.get(0).getTime());
        assertEquals("Scheduled", result.get(0).getStatus());
        assertEquals(user, result.get(0).getUser());

        verify(connection, times(1)).close();
    }

    @Test
    public void testSave() throws SQLException {
        User user = new User(1L, "testUser", "password", "test@example.com", "1234567890", "123 Test St", "CLIENT", 1L);
        Appointment appointment = new Appointment(1L, 1L, "Buddy", 1L, "2023-10-01", "10:00", "Scheduled", user);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        appointmentRepository.save(appointment);

        verify(preparedStatement, times(1)).setLong(1, appointment.getPetId());
        verify(preparedStatement, times(1)).setString(2, appointment.getPetName());
        verify(preparedStatement, times(1)).setLong(3, appointment.getVetId());
        verify(preparedStatement, times(1)).setString(4, appointment.getDate());
        verify(preparedStatement, times(1)).setString(5, appointment.getTime());
        verify(preparedStatement, times(1)).setString(6, appointment.getStatus());
        verify(preparedStatement, times(1)).setLong(7, appointment.getUser().getId());
        verify(preparedStatement, times(1)).executeUpdate();
        verify(connection, times(1)).close();
    }

    @Test
    public void testDeleteById() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        appointmentRepository.deleteById(1L);

        verify(preparedStatement, times(1)).setLong(1, 1L);
        verify(preparedStatement, times(1)).executeUpdate();
        verify(connection, times(1)).close();
    }

}