// package au.edu.rmit.sept.webapp.repository;

// import au.edu.rmit.sept.webapp.model.Appointment;
// import au.edu.rmit.sept.webapp.model.User;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import javax.sql.DataSource;
// import java.sql.*;
// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class AppointmentRepositoryImplTest {

//     @Mock
//     private DataSource dataSource;

//     @Mock
//     private Connection connection;

//     @Mock
//     private PreparedStatement preparedStatement;

//     @Mock
//     private ResultSet resultSet;

//     @InjectMocks
//     private AppointmentRepositoryImpl appointmentRepository;

//     @BeforeEach
//     void setUp() throws SQLException {
//         MockitoAnnotations.openMocks(this);
//         when(dataSource.getConnection()).thenReturn(connection);
//     }

//     @Test
//     void findAll_shouldReturnListOfAppointments() throws SQLException {
//         when(connection.prepareStatement("SELECT * FROM appointments;")).thenReturn(preparedStatement);
//         when(preparedStatement.executeQuery()).thenReturn(resultSet);
//         when(resultSet.next()).thenReturn(true).thenReturn(false);
//         when(resultSet.getLong(1)).thenReturn(1L);
//         when(resultSet.getString(2)).thenReturn("Fluffy");
//         when(resultSet.getString(3)).thenReturn("Dr. Smith");
//         when(resultSet.getString(4)).thenReturn("2024-09-22");
//         when(resultSet.getString(5)).thenReturn("10:00");
//         when(resultSet.getString(6)).thenReturn("Scheduled");

//         List<Appointment> appointments = appointmentRepository.findAll();

//         assertEquals(1, appointments.size());
//         assertEquals("Fluffy", appointments.get(0).getPetName());
//         verify(preparedStatement).executeQuery();
//     }

//     @Test
//     void findByVetName_shouldReturnListOfAppointments() throws SQLException {
//         String vetName = "Dr. Smith";
//         when(connection.prepareStatement("SELECT * FROM appointments WHERE vet_name = ?;")).thenReturn(preparedStatement);
//         when(preparedStatement.executeQuery()).thenReturn(resultSet);
//         when(resultSet.next()).thenReturn(true).thenReturn(false);
//         when(resultSet.getLong(1)).thenReturn(1L);
//         when(resultSet.getString(2)).thenReturn("Fluffy");
//         when(resultSet.getString(3)).thenReturn(vetName);
//         when(resultSet.getString(4)).thenReturn("2024-09-22");
//         when(resultSet.getString(5)).thenReturn("10:00");
//         when(resultSet.getString(6)).thenReturn("Scheduled");

//         List<Appointment> appointments = appointmentRepository.findByVetName(vetName);

//         assertEquals(1, appointments.size());
//         assertEquals("Fluffy", appointments.get(0).getPetName());
//         verify(preparedStatement).setString(1, vetName);
//         verify(preparedStatement).executeQuery();
//     }

//     @Test
//     void findByUser_shouldReturnListOfAppointments() throws SQLException {
//         User user = new User();
//         user.setId(1L);
        
//         when(connection.prepareStatement("SELECT * FROM appointments WHERE user_id = ?;")).thenReturn(preparedStatement);
//         when(preparedStatement.executeQuery()).thenReturn(resultSet);
//         when(resultSet.next()).thenReturn(true).thenReturn(false);
//         when(resultSet.getLong(1)).thenReturn(1L);
//         when(resultSet.getString(2)).thenReturn("Fluffy");
//         when(resultSet.getString(3)).thenReturn("Dr. Smith");
//         when(resultSet.getString(4)).thenReturn("2024-09-22");
//         when(resultSet.getString(5)).thenReturn("10:00");
//         when(resultSet.getString(6)).thenReturn("Scheduled");

//         List<Appointment> appointments = appointmentRepository.findByUser(user);

//         assertEquals(1, appointments.size());
//         assertEquals("Fluffy", appointments.get(0).getPetName());
//         verify(preparedStatement).setLong(1, user.getId());
//         verify(preparedStatement).executeQuery();
//     }

//     @Test
//     void save_shouldInsertNewAppointment() throws SQLException {
//         Appointment appointment = new Appointment(null, "Fluffy", "Dr. Smith", "2024-09-22", "10:00", "Scheduled", new User(1L));

//         when(connection.prepareStatement("INSERT INTO appointments (pet_name, vet_name, date, time, status, user_id) VALUES (?, ?, ?, ?, ?, ?)")).thenReturn(preparedStatement);
        
//         appointmentRepository.save(appointment);

//         verify(preparedStatement).setString(1, "Fluffy");
//         verify(preparedStatement).setString(2, "Dr. Smith");
//         verify(preparedStatement).setString(3, "2024-09-22");
//         verify(preparedStatement).setString(4, "10:00");
//         verify(preparedStatement).setString(5, "Scheduled");
//         verify(preparedStatement).setLong(6, 1L);
//         verify(preparedStatement).executeUpdate();
//     }

//     @Test
//     void deleteById_shouldRemoveAppointment() throws SQLException {
//         long appointmentId = 1L;

//         when(connection.prepareStatement("DELETE FROM appointments WHERE id = ?;")).thenReturn(preparedStatement);

//         appointmentRepository.deleteById(appointmentId);

//         verify(preparedStatement).setLong(1, appointmentId);
//         verify(preparedStatement).executeUpdate();
//     }

//     @Test
//     void findById_shouldReturnAppointment() throws SQLException {
//         long appointmentId = 1L;

//         when(connection.prepareStatement("SELECT * FROM appointments WHERE id = ?")).thenReturn(preparedStatement);
//         when(preparedStatement.executeQuery()).thenReturn(resultSet);
//         when(resultSet.next()).thenReturn(true);
//         when(resultSet.getLong(1)).thenReturn(appointmentId);
//         when(resultSet.getString(2)).thenReturn("Fluffy");
//         when(resultSet.getString(3)).thenReturn("Dr. Smith");
//         when(resultSet.getString(4)).thenReturn("2024-09-22");
//         when(resultSet.getString(5)).thenReturn("10:00");
//         when(resultSet.getString(6)).thenReturn("Scheduled");

//         Appointment appointment = appointmentRepository.findById(appointmentId);

//         assertEquals(appointmentId, appointment.getId());
//         assertEquals("Fluffy", appointment.getPetName());
//         verify(preparedStatement).setLong(1, appointmentId);
//         verify(preparedStatement).executeQuery();
//     }

//     @Test
//     void findById_invalidId_shouldThrowException() throws SQLException {
//         long appointmentId = 999L;

//         when(connection.prepareStatement("SELECT * FROM appointments WHERE id = ?")).thenReturn(preparedStatement);
//         when(preparedStatement.executeQuery()).thenReturn(resultSet);
//         when(resultSet.next()).thenReturn(false); // No result found

//         IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
//             appointmentRepository.findById(appointmentId);
//         });

//         assertEquals("Invalid appointment ID: " + appointmentId, thrown.getMessage());
//     }
// }

