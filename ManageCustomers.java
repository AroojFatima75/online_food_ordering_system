import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageCustomers extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextField txtId, txtFname, txtLname, txtEmail, txtPhone;
    JButton btnAdd, btnUpdate, btnDelete, btnClear, btnBack;

    public ManageCustomers() {
        setTitle("Admin Panel - Manage Customers");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // --- BACKGROUND IMAGE SETUP ---
        ImageIcon i1 = new ImageIcon("images/image.jpg");
        Image i2 = i1.getImage().getScaledInstance(1000, 700, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setLayout(null);
        setContentPane(background);
        // ------------------------------

        // Heading
        JLabel title = new JLabel("Manage Customers");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.BLACK); // Ensure visibility
        title.setBounds(380, 20, 300, 30);
        add(title);

        // --- TABLE SECTION ---
        String[] columns = {"ID", "First Name", "Last Name", "Email", "Phone"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(50, 70, 900, 300);
        add(scroll);

        // --- FORM SECTION ---
        // Labels need to be visible on the background, adding Bold font often helps
        addLabel("Cust ID:", 50, 400);
        txtId = addTextField(120, 400, 150);

        addLabel("First Name:", 300, 400);
        txtFname = addTextField(380, 400, 150);

        addLabel("Last Name:", 550, 400);
        txtLname = addTextField(630, 400, 150);

        addLabel("Email:", 50, 450);
        txtEmail = addTextField(120, 450, 250);

        addLabel("Phone:", 400, 450);
        txtPhone = addTextField(480, 450, 150);

        // --- BUTTONS ---
        btnAdd = createButton("ADD", 50, 520, new Color(0, 100, 0));
        btnUpdate = createButton("UPDATE", 200, 520, new Color(0, 0, 139));
        btnDelete = createButton("DELETE", 350, 520, new Color(139, 0, 0));
        btnClear = createButton("CLEAR", 500, 520, Color.GRAY);
        btnBack = createButton("BACK TO DASHBOARD", 650, 520, Color.BLACK);
        btnBack.setSize(200, 40);

        // --- LOAD DATA ---
        loadData();

        // --- EVENTS ---
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtId.setText(model.getValueAt(row, 0).toString());
                txtFname.setText(model.getValueAt(row, 1).toString());
                txtLname.setText(model.getValueAt(row, 2).toString());
                txtEmail.setText(model.getValueAt(row, 3).toString());
                Object phone = model.getValueAt(row, 4);
                txtPhone.setText(phone != null ? phone.toString() : "");
            }
        });

        btnAdd.addActionListener(e -> {
            try {
                Connection con = DBConnection.connect();
                String sql = "INSERT INTO customer (custid, fname, lname, emailid, phoneno, pwd) VALUES (?, ?, ?, ?, ?, '1234')";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                pst.setString(2, txtFname.getText());
                pst.setString(3, txtLname.getText());
                pst.setString(4, txtEmail.getText());
                pst.setString(5, txtPhone.getText());
                pst.executeUpdate();
                con.close();
                JOptionPane.showMessageDialog(null, "Customer Added Successfully!");
                loadData();
            } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                Connection con = DBConnection.connect();
                String sql = "UPDATE customer SET fname=?, lname=?, emailid=?, phoneno=? WHERE custid=?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, txtFname.getText());
                pst.setString(2, txtLname.getText());
                pst.setString(3, txtEmail.getText());
                pst.setString(4, txtPhone.getText());
                pst.setInt(5, Integer.parseInt(txtId.getText()));
                pst.executeUpdate();
                con.close();
                JOptionPane.showMessageDialog(null, "Customer Updated Successfully!");
                loadData();
            } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); }
        });

        btnDelete.addActionListener(e -> {
            try {
                Connection con = DBConnection.connect();
                String sql = "DELETE FROM customer WHERE custid=?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(txtId.getText()));
                pst.executeUpdate();
                con.close();
                JOptionPane.showMessageDialog(null, "Customer Deleted Successfully!");
                loadData();
            } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); }
        });

        btnClear.addActionListener(e -> {
            txtId.setText(""); txtFname.setText(""); txtLname.setText(""); txtEmail.setText(""); txtPhone.setText("");
        });

        btnBack.addActionListener(e -> {
            new Dashboard().setVisible(true);
            dispose();
        });
    }

    private void loadData() {
        try {
            model.setRowCount(0);
            Connection con = DBConnection.connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("custid"), rs.getString("fname"), rs.getString("lname"),
                        rs.getString("emailid"), rs.getString("phoneno")
                });
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private JButton createButton(String text, int x, int y, Color bg) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 130, 40);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(btn);
        return btn;
    }

    // Helper for labels
    private void addLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 100, 30);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Bold for better visibility
        add(l);
    }

    // Helper for Text Fields
    private JTextField addTextField(int x, int y, int width) {
        JTextField t = new JTextField();
        t.setBounds(x, y, width, 30);
        add(t);
        return t;
    }
}