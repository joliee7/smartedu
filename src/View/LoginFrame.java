package View;
import Controller.LoginController;
import Model.AdminModel;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class LoginFrame extends JFrame {
    public JTextField emailField = new JTextField();
    public JPasswordField passwordField = new JPasswordField();
    public JButton loginBtn = new JButton("Login");
    public AdminModel model;

    Color softBlue = Color.decode("#D9E8EE");
    Color navy = new Color(3, 83, 92);

    public LoginFrame(AdminModel model) {
        this.model = model;
        this.setTitle("SmartEdu Login Page");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 650);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        this.getContentPane().setBackground(softBlue);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.anchor = GridBagConstraints.CENTER;

        String mascotPath = randomMascots();
        JLabel mascotLabel = new JLabel(new ImageIcon(new ImageIcon(mascotPath).getImage()
                .getScaledInstance(220, 220, Image.SCALE_SMOOTH)));

        this.add(mascotLabel, gbc);

        gbc.gridx = 1;
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setPreferredSize(new Dimension(420, 320));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(navy, 2),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        this.add(centerPanel, gbc);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.X_AXIS));
        welcomePanel.setBackground(Color.white);

        JLabel welcomeLbl = new JLabel("Welcome to ");
        welcomeLbl.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel sLabel = new JLabel("S");
        sLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        sLabel.setForeground(navy);

        JLabel martLabel = new JLabel("mart");
        martLabel.setFont(new Font("SansSerif", Font.PLAIN, 22));
        martLabel.setForeground(navy);

        JLabel eduLabel = new JLabel("Edu");
        eduLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        eduLabel.setForeground(new Color(255, 188, 0));

        welcomePanel.add(welcomeLbl);
        welcomePanel.add(sLabel);
        welcomePanel.add(martLabel);
        welcomePanel.add(eduLabel);

        JLabel infoLbl = new JLabel("Please enter your email and password to login");
        infoLbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.setBackground(Color.white);
        JLabel emailLbl = new JLabel("Email:       ");
        emailLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        emailField.setPreferredSize(new Dimension(240, 30));
        emailPanel.add(emailLbl);
        emailPanel.add(emailField);

        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passPanel.setBackground(Color.white);
        JLabel passLbl = new JLabel("Password:");
        passLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        passwordField.setPreferredSize(new Dimension(240, 30));
        passPanel.add(passLbl);
        passPanel.add(passwordField);

        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setBackground(navy);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(100, 35));

        centerPanel.add(welcomePanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(infoLbl);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(emailPanel);
        centerPanel.add(passPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(loginBtn);

        new LoginController(this);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private String randomMascots() {
        String[] mascots = {"images/maskot_cewe.png", "images/maskot_cowo.png"};
        Random random = new Random();
        return mascots[random.nextInt(mascots.length)];
    }
}
