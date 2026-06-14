package View;

import Controller.ManageRoomController;
import Controller.NavControllerAdmin;
import Model.*;

import javax.swing.*;
import java.awt.*;

public class ManageRoomPage extends JFrame {
    AdminModel model;
    NavPanelAdmin navPanel = new NavPanelAdmin();
    public JTable table = new JTable();
    public JTextField namaField, kapasitasField;
    public JButton tambahButton, resetButton, deleteButton, editButton;

    public ManageRoomPage(AdminModel model) {
        setTitle("Manajemen Ruangan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1250, 650);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(217, 234, 241));
        this.setLocationRelativeTo(null);
        this.model = model;
        new NavControllerAdmin(model.getConn(), navPanel, this, model);
        ManageRoomController roomController = new ManageRoomController(this, model);
        HeaderPanel headerPanel = new HeaderPanel(model);

        JPanel panelUtama = new JPanel();
        panelUtama.setLayout(new BorderLayout());
        panelUtama.setBackground(new Color(217, 234, 241));

        JLabel titleLabel = new JLabel("Manajemen Ruangan");
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
        leftPanel.setBorder(BorderFactory.createTitledBorder("Daftar Ruangan"));
        JScrollPane scrollPane = new JScrollPane(table);

        leftPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        deleteButton = new JButton("Hapus Ruangan");
        editButton = new JButton("Edit Ruangan");
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Tambah Ruangan"));

        namaField = new JTextField();
        kapasitasField = new JTextField();

        tambahButton = new JButton("Tambah Ruangan");
        resetButton = new JButton("Reset Form");

        tambahButton.setBackground(new Color(84, 153, 199));
        tambahButton.setForeground(Color.WHITE);
        resetButton.setBackground(new Color(200, 200, 200));

        tambahButton.setFocusPainted(false);
        resetButton.setFocusPainted(false);

        tambahButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        resetButton.setFont(new Font("SansSerif", Font.PLAIN, 14));

        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(new JLabel("Nama Ruangan:"));
        rightPanel.add(namaField);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(new JLabel("Kapasitas:"));
        rightPanel.add(kapasitasField);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(tambahButton);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(resetButton);

        for (Component c : rightPanel.getComponents()) {
            if (c instanceof JTextField || c instanceof JButton) {
                ((JComponent) c).setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            }
        }

        mainPanel.add(rightPanel);

        this.add(navPanel, BorderLayout.WEST);
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(panelUtama, BorderLayout.CENTER);

        tambahButton.addActionListener(roomController);
        resetButton.addActionListener(roomController);
        deleteButton.addActionListener(roomController);
        editButton.addActionListener(roomController);
    }
}
