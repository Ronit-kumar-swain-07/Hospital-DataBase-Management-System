import java.awt.*;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HMSLoginUI extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    private String selectedRole = "Admin"; // default

    public HMSLoginUI() {
        setTitle("MediCore HMS Login");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // LEFT PANEL
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(30, 58, 138));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Silicon Hospital");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Hospital Management System");
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(title);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(subtitle);
        leftPanel.add(Box.createVerticalGlue());

        // RIGHT PANEL
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginLabel.setBounds(150, 20, 200, 30);

        // 🔥 ROLE SELECTION
        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setBounds(50, 60, 100, 20);

        String[] roles = {"Admin", "Doctor", "Staff", "Patient"};
        JPanel rolePanel = new JPanel();
        rolePanel.setBounds(50, 80, 300, 40);

        ButtonGroup group = new ButtonGroup();

        for (String role : roles) {
            JRadioButton btn = new JRadioButton(role);

            if (role.equals("Admin")) btn.setSelected(true);

            btn.addActionListener(e -> selectedRole = role);

            group.add(btn);
            rolePanel.add(btn);
        }

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 130, 100, 20);

        emailField = new JTextField();
        emailField.setBounds(50, 150, 250, 30);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 190, 100, 20);

        passwordField = new JPasswordField();
        passwordField.setBounds(50, 210, 250, 30);

        // Error Label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(50, 250, 300, 20);

        // Login Button
        JButton loginBtn = new JButton("Sign In");
        loginBtn.setBounds(50, 290, 250, 40);

        loginBtn.addActionListener(e -> handleLogin());

        // ADD COMPONENTS
        rightPanel.add(loginLabel);
        rightPanel.add(roleLabel);
        rightPanel.add(rolePanel);
        rightPanel.add(emailLabel);
        rightPanel.add(emailField);
        rightPanel.add(passLabel);
        rightPanel.add(passwordField);
        rightPanel.add(errorLabel);
        rightPanel.add(loginBtn);

        add(leftPanel);
        add(rightPanel);

        setVisible(true);
    }

    // ✅ EMAIL VALIDATION
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email.matches(emailRegex);
    }

    // 🔐 LOGIN VALIDATION FROM DB
    private String validateLogin(String email, String password) {
        String role = null;

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT role FROM Users WHERE email=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                role = rs.getString("role");
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return role;
    }

    // 🔎 GET DOCTOR ID
    private int getDoctorIdByEmail(String email) {
        int doctorId = -1;

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT doctor_id FROM Doctor WHERE email=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, email);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                doctorId = rs.getInt("doctor_id");
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return doctorId;
    }

    // 🚀 LOGIN LOGIC
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill all fields!");
        }
        else if (!isValidEmail(email)) {
            errorLabel.setText("Invalid Email!");
        }
        else {
            String roleFromDB = validateLogin(email, password);

            if (roleFromDB == null) {
                errorLabel.setText("❌ Invalid Credentials!");
            }
            else if (!roleFromDB.equals(selectedRole)) {
                errorLabel.setText("❌ Wrong role selected!");
            }
            else {
                errorLabel.setForeground(new Color(0, 128, 0));
                errorLabel.setText("✅ Login Successful!");

                SwingUtilities.invokeLater(() -> {

                    switch (roleFromDB) {

                        case "Admin":
                            new AdminDashboardUI(email);
                            break;

                        case "Doctor":
                            int doctorId = getDoctorIdByEmail(email);
                            new DoctorDashboardUI(doctorId);
                            break;

                        case "Patient":
                            new PatientDashboardUI(email);
                            break;

                        case "Staff":
                            new StaffDashboardUI(email);
                            break;
                    }

                    dispose();
                });
            }
        }
    }

    public static void main(String[] args) {
        new HMSLoginUI();
    }
}