package View;

import Controller.HeaderController;
import Model.AdminModel;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    public JPanel profilePanel;
    public AdminModel model;
    public HeaderPanel(AdminModel model) {
        this.model = model;
        setBackground(Color.white);
        setLayout(new BorderLayout());
        HeaderController controller = new HeaderController(this);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        leftPanel.setBackground(Color.white);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));

        JLabel sLabel = new JLabel("S");
        sLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        sLabel.setForeground(Color.decode("#063f5c"));

        JLabel martLabel = new JLabel("mart");
        martLabel.setFont(new Font("SansSerif", Font.PLAIN, 26));
        martLabel.setForeground(Color.decode("#063f5c"));

        JLabel eduLabel = new JLabel("Edu");
        eduLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        eduLabel.setForeground(Color.decode("#f2af1a"));

        leftPanel.add(sLabel);
        leftPanel.add(martLabel);
        leftPanel.add(eduLabel);

        profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        profilePanel.setBackground(Color.white);
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
        profilePanel.addMouseListener(controller);
        profilePanel.setOpaque(true);

        JLabel nameLbl = new JLabel(model.getAdminName());
        nameLbl.setFont(new Font("SansSerif", Font.PLAIN, 16));

        ImageIcon profileLogo = new ImageIcon("images/profile.jpg");
        Image scaledProfile = profileLogo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledProfile));

        profilePanel.add(nameLbl);
        profilePanel.add(Box.createHorizontalStrut(10));
        profilePanel.add(logoLabel);

        add(leftPanel, BorderLayout.WEST);
        add(profilePanel, BorderLayout.EAST);
    }
}
