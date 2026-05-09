import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class OrderFood extends JFrame {

    // Components
    JTextField txtName, txtPizza, txtBurger, txtCoke;
    JTextArea areaReceipt;
    JButton btnCalculate, btnBack;

    public OrderFood() {
        setTitle("Order Menu");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // --- BACKGROUND IMAGE SETUP ---
        // Using the generic 'image.jpg' found in your images folder
        ImageIcon i1 = new ImageIcon("images/image.jpg");
        Image i2 = i1.getImage().getScaledInstance(1000, 700, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setLayout(null);
        setContentPane(background);
        // ------------------------------

        // --- HEADING ---
        JLabel title = new JLabel("Food Menu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(255, 140, 0)); // Orange
        title.setBounds(400, 20, 200, 40);
        add(title);

        // --- CUSTOMER NAME FIELD ---
        JLabel lblName = new JLabel("Enter Your Name:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setForeground(Color.BLACK); // Ensure text is visible on bg
        lblName.setBounds(100, 80, 150, 30);
        add(lblName);

        txtName = new JTextField();
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtName.setBounds(250, 80, 250, 30);
        add(txtName);

        // --- HEADERS ---
        JLabel h1 = new JLabel("Item");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 16));
        h1.setBounds(100, 130, 100, 30);
        add(h1);

        JLabel h2 = new JLabel("Price");
        h2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        h2.setBounds(250, 130, 100, 30);
        add(h2);

        JLabel h3 = new JLabel("Quantity");
        h3.setFont(new Font("Segoe UI", Font.BOLD, 16));
        h3.setBounds(400, 130, 100, 30);
        add(h3);

        // --- MENU ITEMS ---
        addFoodLabel("Pizza", 100, 180);
        addFoodLabel("$20", 250, 180);
        txtPizza = addQuantityField(400, 180);

        addFoodLabel("Burger", 100, 230);
        addFoodLabel("$10", 250, 230);
        txtBurger = addQuantityField(400, 230);

        addFoodLabel("Coke", 100, 280);
        addFoodLabel("$5", 250, 280);
        txtCoke = addQuantityField(400, 280);

        // --- BUTTONS ---
        btnCalculate = new JButton("GENERATE BILL");
        btnCalculate.setBackground(new Color(0, 128, 0)); // Green
        btnCalculate.setForeground(Color.WHITE);
        btnCalculate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCalculate.setBounds(150, 400, 200, 40);
        add(btnCalculate);

        btnBack = new JButton("BACK");
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setBounds(380, 400, 100, 40);
        add(btnBack);

        // --- RECEIPT AREA ---
        JLabel lblReceipt = new JLabel("Your Receipt:");
        lblReceipt.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblReceipt.setBounds(600, 130, 200, 30);
        add(lblReceipt);

        areaReceipt = new JTextArea();
        areaReceipt.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaReceipt.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaReceipt);
        scroll.setBounds(600, 170, 300, 350);
        add(scroll);

        // --- LOGIC ---
        btnCalculate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtName.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter your name first!");
                        return;
                    }

                    int qPizza = txtPizza.getText().isEmpty() ? 0 : Integer.parseInt(txtPizza.getText());
                    int qBurger = txtBurger.getText().isEmpty() ? 0 : Integer.parseInt(txtBurger.getText());
                    int qCoke = txtCoke.getText().isEmpty() ? 0 : Integer.parseInt(txtCoke.getText());

                    if (qPizza == 0 && qBurger == 0 && qCoke == 0) {
                        JOptionPane.showMessageDialog(null, "Please select at least one item!");
                        return;
                    }

                    int total = (qPizza * 20) + (qBurger * 10) + (qCoke * 5);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                    String date = sdf.format(new Date());

                    saveOrderToDB(txtName.getText(), total, date);

                    StringBuilder sb = new StringBuilder();
                    sb.append("**********************\n");
                    sb.append("   FOOD DELIVERY APP  \n");
                    sb.append("**********************\n");
                    sb.append("Date: " + date + "\n");
                    sb.append("Customer: " + txtName.getText() + "\n\n");

                    if(qPizza > 0)  sb.append("Pizza  x " + qPizza + " : " + (qPizza*20) + "\n");
                    if(qBurger > 0) sb.append("Burger x " + qBurger + " : " + (qBurger*10) + "\n");
                    if(qCoke > 0)   sb.append("Coke   x " + qCoke + " : " + (qCoke*5) + "\n");

                    sb.append("\n----------------------\n");
                    sb.append("TOTAL PAYABLE: $" + total);
                    sb.append("\n----------------------\n");
                    sb.append("\nOrder Saved to Database!");

                    areaReceipt.setText(sb.toString());

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers only!");
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Dashboard().setVisible(true);
                dispose();
            }
        });
    }

    private void saveOrderToDB(String name, int amount, String date) {
        try {
            Connection con = DBConnection.connect();
            String sql = "INSERT INTO orders (customer_name, total_bill, order_date) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setInt(2, amount);
            pst.setString(3, date);
            pst.executeUpdate();
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
        }
    }

    private void addFoodLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        l.setBounds(x, y, 100, 30);
        add(l);
    }

    private JTextField addQuantityField(int x, int y) {
        JTextField t = new JTextField();
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBounds(x, y, 100, 30);
        add(t);
        return t;
    }
}