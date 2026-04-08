import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewPatientUI extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ViewPatientUI() {
        setTitle("View Patients");
        setSize(600, 400);
        setLocationRelativeTo(null);

        model = new DefaultTableModel();
        table = new JTable(model);

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Gender");
        model.addColumn("Phone");

        loadData();

        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadData() {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            String sql = "SELECT * FROM Patient";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getString("phone")
                });
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}