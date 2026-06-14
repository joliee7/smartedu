package View;

import Controller.AdminStudentController;
import Controller.NavControllerAdmin;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class AdminStudentView extends JFrame {
    public AdminModel adminModel;
    NavPanelAdmin navPanel = new NavPanelAdmin();
    public JTable listSiswatable = new JTable();
    public JButton detailSiswaButton = new JButton("Detail"),
            addSiswaButton = new JButton("Tambah Siswa"),
            perpanjangButton = new JButton("Perpanjang Les"),
            deleteButton = new JButton("Nonaktifkan Siswa");
    public JTextField filterNamaSiswa = new JTextField();
    public JComboBox<String> jenjangComboBox = new JComboBox<>(),
            statusComboBox = new JComboBox<>();
    AdminStudentController action;
    public AdminStudentView(AdminModel adminModel) {
        this.adminModel = adminModel;
        this.setLayout(new BorderLayout());
        this.setSize(1250, 650);
        this.getContentPane().setBackground(Color.decode("#D9E8EE"));
        this.setLocationRelativeTo(null);

        new NavControllerAdmin(adminModel.getConn(), navPanel, this, adminModel);
        action = new AdminStudentController(this, adminModel);
        String name = adminModel.getAdminName();
        HeaderPanel headerPanel = new HeaderPanel(adminModel);

        JPanel panelUtama = new JPanel();
        panelUtama.setLayout(new BorderLayout());
        panelUtama.setBackground(Color.decode("#D9E8EE"));
        panelUtama.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));

        JLabel titleLabel = new JLabel("Daftar Siswa");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titleLabel.setForeground(Color.decode("#063f5c"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelUtama.add(titleLabel, BorderLayout.NORTH);

        JPanel panelTengah = new JPanel(new BorderLayout());
        panelTengah.setBackground(Color.decode("#D9E8EE"));

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comboPanel.setBackground(Color.decode("#D9E8EE"));
        jenjangComboBox.setPreferredSize(new Dimension(100, 30));
        statusComboBox.setPreferredSize(new Dimension(100, 30));
        comboPanel.add(new JLabel("Jenjang: "));
        comboPanel.add(jenjangComboBox);
        comboPanel.add(new JLabel("Status: "));
        comboPanel.add(statusComboBox);
        comboPanel.add(new JLabel("Filter Nama: "));
        filterNamaSiswa.setPreferredSize(new Dimension(100, 30));
        comboPanel.add(filterNamaSiswa);
        panelTengah.add(comboPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(listSiswatable);
        panelTengah.add(scrollPane, BorderLayout.CENTER);
        panelUtama.add(panelTengah, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setBackground(Color.decode("#D9E8EE"));

        JPanel panelA = new JPanel(new FlowLayout(FlowLayout.LEFT));
        detailSiswaButton.setBackground(Color.BLUE);
        detailSiswaButton.setForeground(Color.WHITE);
        deleteButton.setBackground(Color.BLUE);
        deleteButton.setForeground(Color.WHITE);
        panelA.add(detailSiswaButton);
        panelA.add(deleteButton);

        JPanel panelB = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addSiswaButton.setBackground(Color.BLUE);
        addSiswaButton.setForeground(Color.WHITE);
        perpanjangButton.setBackground(Color.BLUE);
        perpanjangButton.setForeground(Color.WHITE);
        panelB.add(perpanjangButton);
        panelB.add(addSiswaButton);

        panelBawah.add(panelA, BorderLayout.WEST);
        panelBawah.add(panelB, BorderLayout.EAST);
        panelUtama.add(panelBawah, BorderLayout.SOUTH);

        this.add(navPanel, BorderLayout.WEST);
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(panelUtama, BorderLayout.CENTER);

        addAction();
    }

    public void addAction(){
        perpanjangButton.addActionListener(action);
        deleteButton.addActionListener(action);
        jenjangComboBox.addActionListener(action);
        statusComboBox.addActionListener(action);
        addSiswaButton.addActionListener(action);
        detailSiswaButton.addActionListener(action);
        filterNamaSiswa.getDocument().addDocumentListener(action);

    }
}
