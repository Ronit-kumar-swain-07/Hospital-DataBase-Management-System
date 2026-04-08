
import java.awt.*;
import javax.swing.*;

public class AdminDashboardUI extends JFrame {

    public AdminDashboardUI(String email) {
        setTitle("Admin Dashboard");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel
        JLabel title = new JLabel("Admin Dashboard - " + email);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons Panel
        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 20));

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addDoctor = new JButton("Add Doctors");
        JButton managePatients = new JButton("Add Patients");
        JButton viewPatients = new JButton("View Patients");
        JButton appointmentBtn = new JButton("Appointments");
        JButton billing = new JButton("Billing");
        JButton logout = new JButton("Logout");


        // Button Actions
        addDoctor.addActionListener(e -> new AddDoctorUI());

        managePatients.addActionListener(e -> new AddPatientUI());
        viewPatients.addActionListener(e -> new ViewPatientUI());
        appointmentBtn.addActionListener(e -> new AddAppointmentUI());

        billing.addActionListener(e
                -> JOptionPane.showMessageDialog(this, "Billing is in Doctor Dashboard.")
        );

        panel.add(addDoctor);
        panel.add(managePatients);
        panel.add(viewPatients);
        panel.add(appointmentBtn);
        panel.add(billing);
        panel.add(logout);

        logout.addActionListener(e -> {
            new HMSLoginUI();
            dispose();
        });

        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }
}
