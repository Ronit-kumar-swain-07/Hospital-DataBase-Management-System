import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddPatientUI extends JFrame {

    JTextField nameField, phoneField;
    JComboBox<String> genderBox;

    public AddPatientUI() {
        setTitle("Add Patient");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Gender:"));
        genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        add(genderBox);

        add(new JLabel("Phone:"));
        phoneField = new JTextField();
        add(phoneField);

        JButton addBtn = new JButton("Add Patient");
        addBtn.addActionListener(e -> insertPatient());

        add(addBtn);

        setVisible(true);
    }

    private void insertPatient() {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO Patient(name, gender, phone) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, nameField.getText());
            pst.setString(2, (String) genderBox.getSelectedItem());
            pst.setString(3, phoneField.getText());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Patient Added!");

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error!");
        }
    }
}