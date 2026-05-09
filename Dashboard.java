import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Dashboard extends JFrame {

    JButton btnOrder, btnAdmin, btnLogout;

    public Dashboard() {
        setTitle("Main Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // --- BACKGROUND IMAGE SETUP ---
        ImageIcon i1 = new ImageIcon("images/image.jpg");
        Image i2 = i1.getImage().getScaledInstance(900, 600, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setLayout(null);
        setContentPane(background);
        // ------------------------------

        // Heading
        JLabel welcome = new JLabel("What would you like to do today?");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcome.setForeground(new Color(0, 0, 128));
        welcome.setBounds(250, 50, 500, 50);
        add(welcome);

        // Buttons
        btnOrder = new JButton("ORDER FOOD");
        btnOrder.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnOrder.setBackground(new Color(255, 140, 0));
        btnOrder.setForeground(Color.WHITE);
        btnOrder.setBounds(150, 200, 250, 150);
        add(btnOrder);

        btnAdmin = new JButton("MANAGE USERS");
        btnAdmin.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnAdmin.setBackground(new Color(70, 130, 180));
        btnAdmin.setForeground(Color.WHITE);
        btnAdmin.setBounds(500, 200, 250, 150);
        add(btnAdmin);

        btnLogout = new JButton("LOGOUT");
        btnLogout.setBounds(750, 20, 100, 30);
        btnLogout.setBackground(Color.RED);
        btnLogout.setForeground(Color.WHITE);
        add(btnLogout);

        // Events
        btnOrder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new OrderFood().setVisible(true);
                dispose();
            }
        });

        btnAdmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManageCustomers().setVisible(true);
                dispose();
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
    }
}