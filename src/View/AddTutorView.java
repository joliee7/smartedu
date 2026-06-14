package View;

import Controller.AddTutorController;
import Controller.NavControllerAdmin;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AddTutorView extends JFrame {
    AdminModel model;
    Color lightBlue = Color.decode("#D9E8EE");
    Color navy = Color.decode("#063f5c");
    Color yellow = Color.decode("#FFFAAA");
    Color tosca = Color.decode("#C8F2EF");
    Color ijo = new Color(69, 182, 95);
    NavPanelAdmin navPanel = new NavPanelAdmin();
    HeaderPanel headerPanel;
    public JTextField idTutorField = new JTextField(4),
            nameTutorField = new JTextField(60),
            emailTutorField = new JTextField(100),
            kotaTutorField = new JTextField(40),
            telpTutorField = new JTextField(13),
            biayaTutorField = new JTextField(40);
    public JComboBox<String> jenjangCombo = new JComboBox<>(),
            mapelCombo2 = new JComboBox<>(),
            mapelCombo = new JComboBox<>();
    public JButton daftarButton = new JButton("Daftar"),
            kembaliButton = new JButton("Kembali");
    AddTutorController controller;

    public AddTutorView(AdminModel model) {
        this.model = model;
        this.setSize(1250, 650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        controller = new AddTutorController(this, model);
        new NavControllerAdmin(model.getConn(), navPanel, this, model);
        addAction();
        headerPanel = new HeaderPanel(model);

        JPanel panelUtama = new JPanel();
        panelUtama.setLayout(new BorderLayout());
        panelUtama.setBackground(lightBlue);
        panelUtama.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));

        JLabel titleLabel = new JLabel("Pendaftaran Tutor Baru");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titleLabel.setForeground(navy);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelUtama.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(1,2, 10, 10));
        contentPanel.setBackground(lightBlue);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setPreferredSize(new Dimension(500, 300));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"ID Tutor:", "Nama:",  "Email:", "Kota:", "Telepon:", "Biaya Tutor per 30 Menit Sesi (Rp.):"};
        Component[] fields = {idTutorField, nameTutorField, emailTutorField, kotaTutorField, telpTutorField, biayaTutorField};
        idTutorField.setEditable(false);

        for (int i = 0; i < labels.length; i++) {
            gbc.weightx = 0;
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.fill = GridBagConstraints.NONE;
            JLabel label = new JLabel(labels[i]);
            label.setPreferredSize(new Dimension(200, 30));
            dataPanel.add(label, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.NONE;
            fields[i].setPreferredSize(new Dimension(350, 25));
            fields[i].setMinimumSize(new Dimension(200, 25));
            dataPanel.add(fields[i], gbc);
        }
        dataPanel.setBackground(tosca);
        dataPanel.setBorder(BorderFactory.createTitledBorder("Data Tutor"));

        JPanel mapelPanel = new JPanel(new GridBagLayout());
        mapelPanel.setPreferredSize(new Dimension(500, 300));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 10, 5, 5);
        gbc2.anchor = GridBagConstraints.WEST;

        String[] labelsMapel = {"Jenjang Pendidikan:", "Mata Pelajaran Ajaran 1:", "Mata Pelajaran Ajaran 2:"};
        Component[] fieldsMapel = {jenjangCombo, mapelCombo, mapelCombo2};

        gbc2.gridx = 0;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(5, 0, 5, 0);

        for (int i = 0; i < labelsMapel.length; i++) {
            gbc2.gridy = i * 2;
            JLabel label = new JLabel(labelsMapel[i]);
            label.setPreferredSize(new Dimension(200, 25));
            mapelPanel.add(label, gbc2);

            gbc2.gridy = i * 2 + 1;
            fieldsMapel[i].setPreferredSize(new Dimension(350, 25));
            mapelPanel.add(fieldsMapel[i], gbc2);
        }
        mapelCombo2.setEnabled(false);
        mapelPanel.setBackground(tosca);
        mapelPanel.setBorder(BorderFactory.createTitledBorder("Data Mapel Ajaran"));

        contentPanel.add(dataPanel);
        contentPanel.add(mapelPanel);
        panelUtama.add(contentPanel, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBawah.add(kembaliButton);
        panelBawah.add(daftarButton);
        panelBawah.setBackground(lightBlue);
        panelUtama.add(panelBawah, BorderLayout.SOUTH);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(navPanel, BorderLayout.WEST);
        this.add(panelUtama, BorderLayout.CENTER);
    }

    public void addAction(){
        daftarButton.addActionListener(controller);
        kembaliButton.addActionListener(controller);
        jenjangCombo.addActionListener(controller);
        mapelCombo.addActionListener(controller);
        nameTutorField.getDocument().addDocumentListener(controller);
    }


}
