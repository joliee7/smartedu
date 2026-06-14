package View;

import Controller.ManageCourseController;
import Controller.NavControllerAdmin;
import Model.*;

import javax.swing.*;
import java.awt.*;

public class ManageCoursePage extends JFrame {
    NavPanelAdmin navPanel = new NavPanelAdmin();
    public JTable table = new JTable();
    public JTextField namaField;
    public JComboBox<String> jenjangBox = new JComboBox<>();
    String[] nat = {"Nasional", "Internasional"};
    public JComboBox<String> tipeBox = new JComboBox<>(nat);
    public JComboBox<String> filterBox = new JComboBox<>();
    public JButton tambahButton, resetButton, materiButton;
    AdminModel model;

    public ManageCoursePage(AdminModel model) {
        this.model = model;
        this.setTitle("Manage Course");
        this.setSize(1250, 650);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(217,234,241));
        ManageCourseController courseController = new ManageCourseController(this,model);
        new NavControllerAdmin(model.getConn(), navPanel, this, model);
        HeaderPanel headerPanel = new HeaderPanel(model);

        JPanel panelUtama = new JPanel();
        panelUtama.setLayout(new BorderLayout());
        panelUtama.setBackground(new Color(217, 234, 241));

        JLabel titleLabel = new JLabel("Manajemen Mata Pelajaran");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titleLabel.setForeground(Color.decode("#063f5c"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelUtama.add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        mainPanel.setBackground(new Color(217, 234, 241));
        panelUtama.add(mainPanel, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Daftar Mata Pelajaran"));
        leftPanel.add(filterBox, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        materiButton = new JButton("Lihat Materi");
        leftPanel.add(materiButton, BorderLayout.SOUTH);
        materiButton.addActionListener(courseController);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Tambah Mata Pelajaran"));

        namaField = new JTextField();

        tambahButton = new JButton("Tambah Mata Pelajaran");
        resetButton = new JButton("Reset Form");

        tambahButton.setBackground(new Color(84, 153, 199));
        tambahButton.setForeground(Color.WHITE);
        resetButton.setBackground(new Color(200, 200, 200));

        tambahButton.setFocusPainted(false);
        resetButton.setFocusPainted(false);

        tambahButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        resetButton.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel tambahPanel = new JPanel(new GridBagLayout());
        tambahPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        tambahPanel.add(new JLabel("Nama Mata Pelajaran:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        tambahPanel.add(namaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        tambahPanel.add(new JLabel("Tipe:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        tambahPanel.add(tipeBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        tambahPanel.add(new JLabel("Jenjang:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        tambahPanel.add(jenjangBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        tambahPanel.add(tambahButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        tambahPanel.add(resetButton, gbc);

        rightPanel.add(tambahPanel, BorderLayout.NORTH);

        JLabel imgLabel = new JLabel();
        ImageIcon mascot = new ImageIcon(new ImageIcon("images/maskot_cewe.png").getImage().
                getScaledInstance(180, 180, Image.SCALE_SMOOTH));
        imgLabel.setIcon(mascot);
        rightPanel.add(imgLabel, BorderLayout.SOUTH);

        mainPanel.add(rightPanel);

        this.add(navPanel, BorderLayout.WEST);
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(panelUtama, BorderLayout.CENTER);

        tambahButton.addActionListener(courseController);
        resetButton.addActionListener(courseController);
        filterBox.addActionListener(courseController);
        jenjangBox.addActionListener(courseController);
        tipeBox.addActionListener(courseController);
    }
}
