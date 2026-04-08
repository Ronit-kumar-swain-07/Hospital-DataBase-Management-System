import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddDoctorUI extends JFrame {

    JTextField nameField, specializationField, emailField;
    JPasswordField passwordField;

    public AddDoctorUI() {
        setTitle("Add Doctor");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Doctor Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Specialization:"));
        specializationField = new JTextField();
        add(specializationField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton addBtn = new JButton("Add Doctor");
        addBtn.addActionListener(e -> insertDoctor());

        add(new JLabel());
        add(addBtn);

        setVisible(true);
    }

    private void insertDoctor() {
        try {
            Connection conn = DBConnection.getConnection();

            String name = nameField.getText();
            String specialization = specializationField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            // 🔹 1. Insert into Doctor table
            String doctorSql = "INSERT INTO Doctor(name, specialization, email) VALUES (?, ?, ?)";
            PreparedStatement pst1 = conn.prepareStatement(doctorSql);

            pst1.setString(1, name);
            pst1.setString(2, specialization);
            pst1.setString(3, email);

            pst1.executeUpdate();

            // 🔹 2. Insert into Users table (for login)
            String userSql = "INSERT INTO Users(email, password, role) VALUES (?, ?, ?)";
            PreparedStatement pst2 = conn.prepareStatement(userSql);

            pst2.setString(1, email);
            pst2.setString(2, password);
            pst2.setString(3, "Doctor");

            pst2.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Doctor Added Successfully!");

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error Adding Doctor!");
        }
    }
}