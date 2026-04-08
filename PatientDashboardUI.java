
import java.awt.*;
import javax.swing.*;

public class PatientDashboardUI extends JFrame {

    public PatientDashboardUI(String email) {
        setTitle("Patient Dashboard");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Welcome Patient: " + email);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel panel = new JPanel();

        JButton appointmentBtn = new JButton("Book Appointment");
        JButton viewHistory = new JButton("View History");
        JButton logout = new JButton("Logout");

        appointmentBtn.addActionListener(e -> new AddAppointmentUI());

        viewHistory.addActionListener(e
                -> JOptionPane.showMessageDialog(this, "Medical History Coming Soon")
        );
        panel.add(appointmentBtn);
        panel.add(viewHistory);
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
