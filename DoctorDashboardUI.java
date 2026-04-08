import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DoctorDashboardUI extends JFrame {

    JTable table;
    DefaultTableModel model;

    public DoctorDashboardUI(int doctorId) {

        setTitle("Doctor Dashboard");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table columns
        String[] cols = {"Appointment ID", "Patient Name", "Date", "Status"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

        loadAppointments(doctorId);

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        // Buttons panel
        JPanel panel = new JPanel();

        JButton completeBtn = new JButton("Mark Completed");
        JButton pendingBtn = new JButton("Mark Pending");

        completeBtn.addActionListener(e -> updateStatus("Completed"));
        pendingBtn.addActionListener(e -> updateStatus("Pending"));
        JButton billBtn = new JButton("Generate Bill");

        billBtn.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an appointment!");
                return;
            }

            int appointmentId = (int) model.getValueAt(row, 0);
            String patientName = (String) model.getValueAt(row, 1);

            int patientId = getPatientIdByName(patientName); // helper method

            // doctorId already available in constructor
            new BillingUI(patientId, doctorId, appointmentId);
        });

        panel.add(billBtn);
        panel.add(completeBtn);
        panel.add(pendingBtn);

        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Load appointments
    private void loadAppointments(int doctorId) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT a.appointment_id, p.name, a.appointment_date, a.status " +
                    "FROM Appointment a " +
                    "JOIN Patient p ON a.patient_id = p.patient_id " +
                    "WHERE a.doctor_id = ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, doctorId);

            ResultSet rs = pst.executeQuery();

            model.setRowCount(0); // clear table

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("appointment_id"),
                        rs.getString("name"),
                        rs.getDate("appointment_date"),
                        rs.getString("status")
                });
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int getPatientIdByName(String name) {
        int id = -1;

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT patient_id FROM Patient WHERE name=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, name);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                id = rs.getInt("patient_id");
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    private void updateStatus(String status) {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an appointment!");
            return;
        }

        int appointmentId = (int) model.getValueAt(row, 0);

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "UPDATE Appointment SET status=? WHERE appointment_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, status);
            pst.setInt(2, appointmentId);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Status Updated!");

            // Refresh table
            model.setValueAt(status, row, 3);

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}