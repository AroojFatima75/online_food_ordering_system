import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class LoginFrame extends JFrame {

    JTextField txtEmail;
    JPasswordField txtPassword;
    JButton btnLogin, btnSignup, btnExit;

    public LoginFrame() {
        // 1. Window Setup
        setTitle("Food Delivery System - Login");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // --- BACKGROUND IMAGE SETUP ---
        // Load the image from the 'images' folder
        ImageIcon i1 = new ImageIcon("images/login.jpg");
        // Resize image to fit the window (900x600)
        Image i2 = i1.getImage().getScaledInstance(900, 600, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        // Create a label with the image
        JLabel background = new JLabel(i3);
        // Set layout to null SO WE CAN PLACE BUTTONS ON TOP
        background.setLayout(null);
        // Set this label as the Content Pane
        setContentPane(background);
        // ------------------------------

        // 2. Headings and Labels
        JLabel title = new JLabel("Welcome Back!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(0, 0, 0));
        title.setBounds(330, 50, 300, 50);
        add(title);

        JLabel lblEmail = new JLabel("Email Address:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Made Bold for better visibility on image
        lblEmail.setBounds(250, 150, 150, 30);
        add(lblEmail);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPass.setBounds(250, 230, 150, 30);
        add(lblPass);

        // 3. Text Fields
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBounds(250, 180, 400, 35);
        add(txtEmail);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(250, 260, 400, 35);
        add(txtPassword);

        // 4. Buttons
        btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(new Color(0, 0, 0));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBounds(250, 330, 120, 40);
        add(btnLogin);

        btnSignup = new JButton("SIGN UP");
        btnSignup.setBackground(new Color(50, 205, 50));
        btnSignup.setForeground(Color.WHITE);
        btnSignup.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSignup.setBounds(390, 330, 120, 40);
        add(btnSignup);

        btnExit = new JButton("EXIT");
        btnExit.setBackground(new Color(220, 20, 60));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExit.setBounds(530, 330, 120, 40);
        add(btnExit);

        // --- EVENTS ---
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText();
                String pwd = new String(txtPassword.getPassword());

                if (email.isEmpty() || pwd.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both Email and Password.");
                    return;
                }

                try {
                    Connection con = DBConnection.connect();
                    String sql = "SELECT * FROM customer WHERE emailid = ? AND pwd = ?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, email);
                    pst.setString(2, pwd);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Login Successful! Welcome " + rs.getString("fname"));
                        if (email.equalsIgnoreCase("admin@gmail.com")) {
                            new Dashboard().setVisible(true);
                        } else {
                            new OrderFood().setVisible(true);
                        }
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Email or Password.");
                    }
                    con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        btnSignup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SignupFrame().setVisible(true);
                dispose();
            }
        });

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}