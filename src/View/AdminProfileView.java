package View;

import Controller.AdminProfileController;
import Controller.NavControllerAdmin;
import Model.*;

import javax.swing.*;
import java.awt.*;

public class AdminProfileView extends JFrame {
    public AdminModel model;
    HeaderPanel headerPanel;
    NavPanelAdmin navPanel = new NavPanelAdmin();
    public JTextField idField = new JTextField(15),
            nameField = new JTextField(15),
            emailField = new JTextField(15);
    public JPasswordField passwordField = new JPasswordField(15);
    public JButton editButton, saveEditButton, logoutButton;

    public AdminProfileView(AdminModel model) {
        this.model = model;
        this.setLayout(new BorderLayout());
        this.setSize(1250, 650);
        this.getContentPane().setBackground(Color.decode("#D9E8EE"));
        this.setLocationRelativeTo(null);
        new NavControllerAdmin(model.getConn(), navPanel, this, model);
        headerPanel = new HeaderPanel(model);
        AdminProfileController controller = new AdminProfileController(this);

        JPanel panelUtama = new JPanel();
        panelUtama.setLayout(new BorderLayout());
        panelUtama.setBackground(Color.decode("#D9E8EE"));
        panelUtama.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));

        JLabel titleLabel = new JLabel("Profil Admin");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titleLabel.setForeground(Color.decode("#063f5c"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelUtama.add(titleLabel, BorderLayout.NORTH);

        // Panel untuk isi (foto + form)
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contentPanel.setBackground(Color.white); // putih semi transparan
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel foto di kiri
        // Panel foto di kiri
        JPanel fotoPanel = new JPanel();
        fotoPanel.setPreferredSize(new Dimension(250, 300)); // Tambah tinggi sedikit
        fotoPanel.setLayout(new BoxLayout(fotoPanel, BoxLayout.Y_AXIS));
        fotoPanel.setBackground(Color.decode("#D9E8EE"));

        JLabel fotoLabel = new JLabel();
        fotoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon fotoSiswa = new ImageIcon(new ImageIcon("images/maskot_cowo.png").getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH));
        fotoLabel.setIcon(fotoSiswa);

        editButton = new JButton("Edit Profil");
        editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editButton.setBackground(Color.blue);
        editButton.setForeground(Color.WHITE);

        fotoPanel.add(fotoLabel);
        fotoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fotoPanel.add(editButton);

        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setOpaque(false);
        dataPanel.setPreferredSize(new Dimension(500, 300));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"ID Admin:", "Nama:","Email:", "Password:"};
        Component[] fields = {idField, nameField, emailField, passwordField};
        String[] dataAdmin = model.getDataAdmin(model.getId_admin());
        idField.setText(dataAdmin[0]);
        nameField.setText(dataAdmin[1]);
        emailField.setText(dataAdmin[2]);
        passwordField.setText(dataAdmin[3]);
        saveEditButton = new JButton("Simpan Perubahan");
        logoutButton = new JButton("Log Out");

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.fill = GridBagConstraints.NONE;
            JLabel label = new JLabel(labels[i]);
            label.setPreferredSize(new Dimension(200, 30));
            dataPanel.add(label, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.NONE;
            fields[i].setPreferredSize(new Dimension(350, 30));
            dataPanel.add(fields[i], gbc);
        }
        gbc.gridy = 4; gbc.gridx = 0;
        dataPanel.add(saveEditButton, gbc);
        gbc.gridx = 1;
        dataPanel.add(logoutButton, gbc);
        dataPanel.setBackground(Color.decode("#D9E8EE"));
        idField.setEditable(false);
        nameField.setEditable(false);
        emailField.setEditable(false);
        passwordField.setEditable(false);
        saveEditButton.setEnabled(false);

        contentPanel.add(fotoPanel);
        contentPanel.add(dataPanel);
        contentPanel.setBackground(Color.decode("#D9E8EE"));

        panelUtama.add(contentPanel, BorderLayout.CENTER);

        this.add(navPanel, BorderLayout.WEST);
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(panelUtama, BorderLayout.CENTER);

        editButton.addActionListener(controller);
        saveEditButton.addActionListener(controller);
        logoutButton.addActionListener(controller);
    }
}
