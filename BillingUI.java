import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BillingUI extends JFrame {

    JTextField consultField, medicineField, totalField;
    JLabel patientLabel, doctorLabel;

    int patientId, doctorId, appointmentId;

    public BillingUI(int patientId, int doctorId, int appointmentId) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;

        setTitle("Billing System");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));

        // ✅ Patient Display
        add(new JLabel("Patient:"));
        patientLabel = new JLabel(getPatientName(patientId));
        add(patientLabel);

        // ✅ Doctor Display
        add(new JLabel("Doctor:"));
        doctorLabel = new JLabel(getDoctorName(doctorId));
        add(doctorLabel);

        // Fees
        add(new JLabel("Consultation Fee:"));
        consultField = new JTextField();
        add(consultField);

        add(new JLabel("Medicine Fee:"));
        medicineField = new JTextField();
        add(medicineField);

        add(new JLabel("Total Amount:"));
        totalField = new JTextField();
        totalField.setEditable(false);
        add(totalField);

        JButton calcBtn = new JButton("Calculate");
        JButton saveBtn = new JButton("Generate Bill");

        calcBtn.addActionListener(e -> calculateTotal());
        saveBtn.addActionListener(e -> saveBill());

        add(calcBtn);
        add(saveBtn);

        setVisible(true);
    }

    // 🔹 Get Patient Name
    private String getPatientName(int id) {
        String name = "";
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT name FROM Patient WHERE patient_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    // 🔹 Get Doctor Name
    private String getDoctorName(int id) {
        String name = "";
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT name FROM Doctor WHERE doctor_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    // 🔹 Calculate Total
    private void calculateTotal() {
        try {
            int consult = Integer.parseInt(consultField.getText());
            int medicine = Integer.parseInt(medicineField.getText());

            int total = consult + medicine;
            totalField.setText(String.valueOf(total));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers!");
        }
    }

    // 🔹 Save Bill
    private void saveBill() {
        try {
            Connection conn = DBConnection.getConnection();

            int consult = Integer.parseInt(consultField.getText());
            int medicine = Integer.parseInt(medicineField.getText());
            int total = Integer.parseInt(totalField.getText());

            String sql = "INSERT INTO Billing(patient_id, doctor_id, appointment_id, consultation_fee, medicine_fee, total_amount) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1, patientId);
            pst.setInt(2, doctorId);
            pst.setInt(3, appointmentId);
            pst.setInt(4, consult);
            pst.setInt(5, medicine);
            pst.setInt(6, total);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Bill Generated Successfully!");

            conn.close();
            dispose(); // close window after save

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error generating bill!");
        }
    }
}