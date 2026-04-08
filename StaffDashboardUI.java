
import java.awt.*;
import javax.swing.*;

public class StaffDashboardUI extends JFrame {

    public StaffDashboardUI(String email) {
        setTitle("Staff Dashboard");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Staff Dashboard: " + email);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel panel = new JPanel();

        JButton manageInventory = new JButton("Manage Inventory");
        JButton records = new JButton("Patient Records");
        JButton logout = new JButton("Logout");

        manageInventory.addActionListener(e
                -> JOptionPane.showMessageDialog(this, "Inventory Management Coming Soon")
        );

        records.addActionListener(e -> new ViewPatientUI());
        panel.add(manageInventory);
        panel.add(records);
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
