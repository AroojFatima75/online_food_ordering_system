import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;

public class SignupFrame extends JFrame {

    JTextField txtFname, txtLname, txtEmail, txtPhone, txtAddress;
    JPasswordField txtPassword;
    JButton btnRegister, btnBack;

    public SignupFrame() {
        setTitle("New User Registration");
        setSize(900, 700); // 700 Height
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // --- BACKGROUND IMAGE SETUP ---
        ImageIcon i1 = new ImageIcon("images/signup.jpg");
        // Resize to match frame size (900x700)
        Image i2 = i1.getImage().getScaledInstance(900, 700, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setLayout(null);
        setContentPane(background);
        // ------------------------------

        // Heading
        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(50, 205, 50));
        title.setBounds(300, 30, 400, 40);
        add(title);

        // Fields
        addLabel("First Name:", 200, 100);
        txtFname = addTextField(400, 100);

        addLabel("Last Name:", 200, 150);
        txtLname = addTextField(400, 150);

        addLabel("Email ID:", 200, 200);
        txtEmail = addTextField(400, 200);

        addLabel("Password:", 200, 250);
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(400, 250, 250, 30);
        add(txtPassword);

        addLabel("Phone No:", 200, 300);
        txtPhone = addTextField(400, 300);

        addLabel("Address:", 200, 350);
        txtAddress = addTextField(400, 350);

        // Buttons
        btnRegister = new JButton("REGISTER NOW");
        btnRegister.setBackground(new Color(50, 205, 50));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setBounds(300, 450, 150, 40);
        add(btnRegister);

        btnBack = new JButton("BACK TO LOGIN");
        btnBack.setBackground(new Color(100, 100, 100));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setBounds(480, 450, 150, 40);
        add(btnBack);

        // Events
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection con = DBConnection.connect();
                    String sql = "INSERT INTO customer (custid, fname, lname, emailid, pwd, phoneno, address) VALUES (cust_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, txtFname.getText());
                    pst.setString(2, txtLname.getText());
                    pst.setString(3, txtEmail.getText());
                    pst.setString(4, new String(txtPassword.getPassword()));
                    pst.setString(5, txtPhone.getText());
                    pst.setString(6, txtAddress.getText());

                    int rows = pst.executeUpdate();
                    if(rows > 0) {
                        JOptionPane.showMessageDialog(null, "Registration Successful! Please Login.");
                        new LoginFrame().setVisible(true);
                        dispose();
                    }
                    con.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
    }

    // Helpers
    private void addLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setBounds(x, y, 150, 30);
        add(l);
    }

    private JTextField addTextField(int x, int y) {
        JTextField t = new JTextField();
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBounds(x, y, 250, 30);
        add(t);
        return t;
    }
}