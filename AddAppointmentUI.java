import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class AddAppointmentUI extends JFrame {

    JComboBox<String> patientBox;
    JComboBox<String> doctorBox;
    JTextField dateField;

    public AddAppointmentUI() {
        setTitle("Book Appointment");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Select Patient:"));
        patientBox = new JComboBox<>();
        loadPatients();
        add(patientBox);

        add(new JLabel("Select Doctor:"));
        doctorBox = new JComboBox<>();
        loadDoctors();
        add(doctorBox);

        add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        add(dateField);

        JButton btn = new JButton("Book Appointment");
        btn.addActionListener(e -> insertAppointment());

        add(new JLabel());
        add(btn);

        setVisible(true);
    }

    private void loadPatients() {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT patient_id, name FROM Patient");

            while (rs.next()) {
                patientBox.addItem(rs.getInt("patient_id") + " - " + rs.getString("name"));
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDoctors() {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT doctor_id, name FROM Doctor");

            while (rs.next()) {
                doctorBox.addItem(rs.getInt("doctor_id") + " - " + rs.getString("name"));
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertAppointment() {
        try {
            Connection conn = DBConnection.getConnection();

            String patient = (String) patientBox.getSelectedItem();
            String doctor = (String) doctorBox.getSelectedItem();

            int patientId = Integer.parseInt(patient.split(" - ")[0]);
            int doctorId = Integer.parseInt(doctor.split(" - ")[0]);

            String sql = "INSERT INTO Appointment(patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1, patientId);
            pst.setInt(2, doctorId);

            // ✅ Convert String → SQL Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(dateField.getText());
            pst.setDate(3, sqlDate);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Appointment Booked!");

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Invalid Date Format! Use YYYY-MM-DD");
        }
    }
}