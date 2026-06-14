package View;

import Controller.AddStudentController;
import Controller.NavControllerAdmin;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AddStudentView extends JFrame {
    Color lightBlue = Color.decode("#D9E8EE");
    Color navy = Color.decode("#063f5c");
    Color yellow = Color.decode("#FFFAAA");
    Color tosca = Color.decode("#C8F2EF");
    NavPanelAdmin navPanel = new NavPanelAdmin();
    public JTabbedPane tabbedPane = new JTabbedPane();
    public JPanel studentPanel = new JPanel(),
            parentPanel = new JPanel(),
            mapelPanel = new JPanel(),
            mapelContainer = new JPanel();
    public JTextField idSiswaField = new JTextField(4),
            nameSiswaField = new JTextField(60),
            emailSiswaField = new JTextField(100),
            kotaSiswaField = new JTextField(40),
            telpSiswaField = new JTextField(13),
            namaOrtuField = new JTextField(),
            telpOrtuField = new JTextField(),
            totalHargaField = new JTextField();
    public JComboBox<String> jenjangCombo = new JComboBox<>(),
            mapelCombo1 = new JComboBox<>(),
            tutorCombo1 = new JComboBox<>(),
            mapelCombo2 = new JComboBox<>(),
            tutorCombo2 = new JComboBox<>(),
            mapelCombo3 = new JComboBox<>(),
            tutorCombo3 = new JComboBox<>(),
            durasiCombo1 = new JComboBox<>(),
            durasiCombo2 = new JComboBox<>(),
            durasiCombo3 = new JComboBox<>();
    public JButton nextButtonSiswa = new JButton("Lanjut"),
            prevButtonOrtu = new JButton("Kembali"),
            nextButtonOrtu = new JButton("Lanjut"),
            backButtonSiswa = new JButton("Kembali"),
            tambahMapelBtn = new JButton(" + "),
            kurangMapelBtn = new JButton(" - "),
            saveButton = new JButton("Simpan"),
            daftarButton = new JButton("Daftar"),
            prevButtonmapel = new JButton("Kembali");
    public JRadioButton cashButton = new JRadioButton("Tunai"),
            trfButton = new JRadioButton("Transfer");
    public ButtonGroup group = new ButtonGroup();
    public ArrayList<Object> mapelPanels = new ArrayList<>();
    public JTable mapelTable = new JTable();
    public AdminModel adminModel;
    AddStudentController controller;
    public int index =1;
    String idSiswa;

    public AddStudentView(AdminModel adminModel) {
        this.adminModel = adminModel;
        this.idSiswa = idSiswa;
        this.setSize(1250, 650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        new NavControllerAdmin(adminModel.getConn(), navPanel, this, adminModel);

        HeaderPanel headerPanel = new HeaderPanel(adminModel);
        controller = new AddStudentController(this, adminModel);

        JPanel panelUtama = new JPanel();
        panelUtama.setLayout(new BorderLayout());
        panelUtama.setBackground(lightBlue);
        panelUtama.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));

        mapelCombo1.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        mapelCombo2.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        mapelCombo3.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        tutorCombo1.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        tutorCombo2.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        tutorCombo3.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        durasiCombo1.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        durasiCombo2.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
        durasiCombo3.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));

        studentProfile(studentPanel);
        parentProfile(parentPanel);
        mapelRegist(mapelPanel);

        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.addTab("Data Siswa", studentPanel);
        tabbedPane.addTab("Data Orang Tua Siswa", parentPanel);
        tabbedPane.addTab("Daftar Mata Pelajaran", mapelPanel);
        tabbedPane.setBackground(Color.white);
        panelUtama.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.setEnabledAt(0, false);
        tabbedPane.setEnabledAt(1, false);
        tabbedPane.setEnabledAt(2, false);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(navPanel, BorderLayout.WEST);
        this.add(tabbedPane, BorderLayout.CENTER);
        addAction();
    }

    public void studentProfile(JPanel s) {
        s.setLayout(new BorderLayout());
        s.setBackground(tosca);
        s.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));

        JLabel titleLabel = new JLabel("Profil Siswa");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
        titleLabel.setForeground(navy);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        s.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
        contentPanel.setBackground(Color.white); // putih semi transparan
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setOpaque(false);
        dataPanel.setPreferredSize(new Dimension(500, 300));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"ID Siswa:", "Nama:",  "Email:", "Kota:", "Telepon:", "Jenjang Pendidikan:"};
        Component[] fields = {idSiswaField, nameSiswaField, emailSiswaField, kotaSiswaField, telpSiswaField, jenjangCombo};

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
        s.add(dataPanel, BorderLayout.CENTER);
        idSiswaField.setEditable(false);

        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBawah.add(backButtonSiswa);
        panelBawah.add(nextButtonSiswa);
        panelBawah.setBackground(tosca);
        s.add(panelBawah, BorderLayout.SOUTH);
    }

    public void parentProfile(JPanel s) {
        s.setLayout(new BorderLayout());
        s.setBackground(tosca);
        s.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));

        JLabel titleLabel = new JLabel("Data Orang Tua Siswa");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
        titleLabel.setForeground(navy);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        s.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
        contentPanel.setBackground(Color.white); // putih semi transparan
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setOpaque(false);
        dataPanel.setPreferredSize(new Dimension(400, 300));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Nama Orang Tua:",  "Telepon:"};
        Component[] fields = {namaOrtuField, telpOrtuField};

        for (int i = 0; i < labels.length; i++) {
            gbc.weightx = 0;
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.fill = GridBagConstraints.NONE;
            JLabel label = new JLabel(labels[i]);
            label.setPreferredSize(new Dimension(150, 30));
            dataPanel.add(label, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.NONE;
            fields[i].setPreferredSize(new Dimension(200, 25));
            fields[i].setMaximumSize(new Dimension(200, 25));
            dataPanel.add(fields[i], gbc);
        }
        dataPanel.setBackground(tosca);
        s.add(dataPanel, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBawah.add(prevButtonOrtu);
        panelBawah.add(nextButtonOrtu);
        panelBawah.setBackground(tosca);
        s.add(panelBawah, BorderLayout.SOUTH);
    }

    public void mapelRegist(JPanel s) {
        s.setLayout(new BorderLayout());
        s.setBackground(tosca);
        s.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));

        JLabel titleLabel = new JLabel("Registrasi Mata Pelajaran");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
        titleLabel.setForeground(navy);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        s.add(titleLabel, BorderLayout.NORTH);

        JPanel isiPanel = new JPanel(new BorderLayout());
        isiPanel.setBackground(tosca);

        mapelContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        mapelContainer.setBackground(tosca);

        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tombolPanel.setBackground(tosca);
        tombolPanel.add(kurangMapelBtn);
        tombolPanel.add(tambahMapelBtn);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(tosca);
        buttonPanel.add(tombolPanel, BorderLayout.WEST);
        mapelBaru(index);

        isiPanel.add(mapelContainer, BorderLayout.NORTH);
        isiPanel.add(buttonPanel, BorderLayout.CENTER);

        s.add(isiPanel, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(mapelTable);
        scrollPane.setPreferredSize(new Dimension(620, 120));
        panelBawah.add(scrollPane, BorderLayout.CENTER);

        JPanel subtotalPanel = new JPanel(new BorderLayout());
        subtotalPanel.setBackground(tosca);
        JPanel hargaTotalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        hargaTotalPanel.setBackground(tosca);
        hargaTotalPanel.add(new JLabel("Total Biaya: Rp "));
        totalHargaField.setPreferredSize(new Dimension(100,25));
        hargaTotalPanel.add(totalHargaField);
        subtotalPanel.add(hargaTotalPanel, BorderLayout.CENTER);

        cashButton.setBackground(tosca);
        trfButton.setBackground(tosca);
        group.add(cashButton);
        group.add(trfButton);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel2.setBackground(tosca);
        panel2.add(new JLabel("Metode Pembayaran: "));
        panel2.add(cashButton);
        panel2.add(trfButton);
        panel2.add(daftarButton);

        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.setBackground(tosca);
        panel3.add(prevButtonmapel, BorderLayout.WEST);
        panel3.add(panel2, BorderLayout.EAST);
        subtotalPanel.add(panel3, BorderLayout.SOUTH);
        panelBawah.add(subtotalPanel, BorderLayout.SOUTH);

        s.add(panelBawah, BorderLayout.SOUTH);
        kurangMapelBtn.setEnabled(false);
        daftarButton.setEnabled(false);
    }

    public void mapelBaru(int index) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(yellow);
        panel.setPreferredSize(new Dimension(310, 140));
        panel.setBorder(BorderFactory.createTitledBorder("Mapel " + (mapelPanels.size() + 1)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel mapelLabel = new JLabel("Pilih Mapel:");
        JLabel durasiLabel = new JLabel("Durasi Sesi:");
        JLabel tutorLabel = new JLabel("Pilih Tutor:");

        JComboBox<String> comboMapel = null;
        JComboBox<String> comboTutor = null;
        JComboBox<String> comboDurasi = new JComboBox<>(new String[]{"60 Menit", "90 Menit"});

        if (index == 1) {
            comboMapel = mapelCombo1;
            comboTutor = tutorCombo1;
            durasiCombo1 = comboDurasi;
        } else if (index == 2) {
            comboMapel = mapelCombo2;
            comboTutor = tutorCombo2;
            durasiCombo2 = comboDurasi;
        } else if (index == 3) {
            comboMapel = mapelCombo3;
            comboTutor = tutorCombo3;
            durasiCombo3 = comboDurasi;
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(mapelLabel, gbc);

        gbc.gridx = 1;
        panel.add(comboMapel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(durasiLabel, gbc);

        gbc.gridx = 1;
        panel.add(comboDurasi, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(tutorLabel, gbc);

        gbc.gridy = 3;
        panel.add(comboTutor, gbc);

        mapelPanels.add(panel);
        mapelContainer.add(panel);
        mapelContainer.revalidate();
        mapelContainer.repaint();
    }



    public void kurangiMapel(int index) {
        if (!mapelPanels.isEmpty()) {
            JPanel lastPanel = (JPanel) mapelPanels.get(mapelPanels.size() - 1);
            mapelContainer.remove(lastPanel);
            mapelPanels.remove(mapelPanels.size() - 1);

            if (index == 3) {
                mapelCombo3.setSelectedItem(null);
                tutorCombo3.setSelectedItem(null);
            } else if (index == 2) {
                mapelCombo2.setSelectedItem(null);
                tutorCombo2.setSelectedItem(null);
            }

            mapelContainer.revalidate();
            mapelContainer.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Tidak ada panel mapel yang bisa dihapus.");
        }
    }



    public void addAction(){
        saveButton.addActionListener(controller);
        backButtonSiswa.addActionListener(controller);
        nextButtonSiswa.addActionListener(controller);
        daftarButton.addActionListener(controller);
        trfButton.addActionListener(controller);
        cashButton.addActionListener(controller);
        nextButtonOrtu.addActionListener(controller);
        prevButtonOrtu.addActionListener(controller);
        kurangMapelBtn.addActionListener(controller);
        tambahMapelBtn.addActionListener(controller);
        prevButtonmapel.addActionListener(controller);
        nameSiswaField.getDocument().addDocumentListener(controller);
        mapelCombo2.addActionListener(controller);
        mapelCombo1.addActionListener(controller);
        mapelCombo3.addActionListener(controller);
        jenjangCombo.addActionListener(controller);
        tutorCombo1.addActionListener(controller);
        tutorCombo2.addActionListener(controller);
        tutorCombo3.addActionListener(controller);
        durasiCombo1.addActionListener(controller);
        durasiCombo2.addActionListener(controller);
        durasiCombo3.addActionListener(controller);
    }


}
