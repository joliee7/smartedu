package View;

import Controller.AdminTutorController;
import Controller.NavControllerAdmin;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class AdminTutorView extends JFrame {
    NavPanelAdmin navPanel = new NavPanelAdmin();
    public JTable listTutortable = new JTable();
    public JPanel daftarTutorPanel = new JPanel();
    public JButton detailTutorButton = new JButton("Detail / Edit"),
            addTutorButton = new JButton("Tambah Tutor"),
            nonaktifButton = new JButton("Nonaktifkan Tutor");
    public JTextField filterNamaTutor = new JTextField();
    public JComboBox<String> mapelComboBox = new JComboBox<>(),
            jenjangComboBox = new JComboBox<>(),
            statusComboBox = new JComboBox<>();
    public AdminModel model;
    AdminTutorController controller;

    public AdminTutorView(AdminModel model) {
        this.model = model;
        this.setLayout(new BorderLayout());
        this.setSize(1250, 650);
        this.getContentPane().setBackground(Color.decode("#D9E8EE"));
        this.setLocationRelativeTo(null);
        controller = new AdminTutorController(this, model);
        new NavControllerAdmin(model.getConn(), navPanel, this, model);
        addAction();

        String a = model.getAdminName();
        HeaderPanel headerPanel = new HeaderPanel(model);

        JPanel panelUtama = new JPanel();
        panelUtama.setLayout(new BorderLayout());
        panelUtama.setBackground(Color.decode("#D9E8EE"));

        daftarTutor(daftarTutorPanel);
        panelUtama.add(daftarTutorPanel, BorderLayout.CENTER);

        listTutortable.setRowSelectionAllowed(true);
        listTutortable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.add(navPanel, BorderLayout.WEST);
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(panelUtama, BorderLayout.CENTER);
    }

    public void daftarTutor(JPanel d) {
        d.setLayout(new BorderLayout());
        d.setBackground(Color.decode("#D9E8EE"));
        d.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));

        JLabel titleLabel = new JLabel("Daftar Tutor");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titleLabel.setForeground(Color.decode("#063f5c"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        d.add(titleLabel, BorderLayout.NORTH);

        JPanel panelTengah = new JPanel(new BorderLayout());
        panelTengah.setBackground(Color.decode("#D9E8EE"));

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comboPanel.setBackground(Color.decode("#D9E8EE"));
        mapelComboBox.setPreferredSize(new Dimension(100, 30));
        statusComboBox.setPreferredSize(new Dimension(100, 30));
        jenjangComboBox.setPreferredSize(new Dimension(100, 30));
        jenjangComboBox.setSelectedIndex(0);
        mapelComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        comboPanel.add(new JLabel("Jenjang: "));
        comboPanel.add(jenjangComboBox);
        comboPanel.add(new JLabel("Mata Pelajaran: "));
        comboPanel.add(mapelComboBox);
        comboPanel.add(new JLabel("Status: "));
        comboPanel.add(statusComboBox);
        comboPanel.add(new JLabel("Filter Nama: "));
        filterNamaTutor.setPreferredSize(new Dimension(100, 30));
        comboPanel.add(filterNamaTutor);
        panelTengah.add(comboPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(listTutortable);
        panelTengah.add(scrollPane, BorderLayout.CENTER);
        d.add(panelTengah, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setBackground(Color.decode("#D9E8EE"));

        JPanel panelA = new JPanel(new FlowLayout(FlowLayout.LEFT));
        detailTutorButton.setBackground(Color.BLUE);
        detailTutorButton.setForeground(Color.WHITE);
        nonaktifButton.setBackground(Color.BLUE);
        nonaktifButton.setForeground(Color.WHITE);
        panelA.add(detailTutorButton);
        panelA.add(nonaktifButton);

        JPanel panelB = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addTutorButton.setBackground(Color.BLUE);
        addTutorButton.setForeground(Color.WHITE);
        panelB.add(addTutorButton);

        panelBawah.add(panelA, BorderLayout.WEST);
        panelBawah.add(panelB, BorderLayout.EAST);

        d.add(panelBawah, BorderLayout.SOUTH);
    }

    public void addAction(){
        mapelComboBox.addActionListener(controller);
        jenjangComboBox.addActionListener(controller);
        statusComboBox.addActionListener(controller);
        addTutorButton.addActionListener(controller);
        detailTutorButton.addActionListener(controller);
        filterNamaTutor.getDocument().addDocumentListener(controller);
        nonaktifButton.addActionListener(controller);
    }
}
