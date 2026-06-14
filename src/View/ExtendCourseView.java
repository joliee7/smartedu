package View;

import Controller.ExtendCourseController;
import Controller.NavControllerAdmin;
import Model.AdminModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ExtendCourseView extends JFrame {
    public JPanel mapelContainer = new JPanel();
    public JTable mapelTable = new JTable();
    public JButton tambahMapelBtn = new JButton(" + "),
            kurangMapelBtn = new JButton(" - ");
    Color lightBlue = Color.decode("#D9E8EE");
    Color navy = Color.decode("#063f5c");
    Color yellow = Color.decode("#FFFAAA");
    Color tosca = Color.decode("#C8F2EF");
    AdminModel model;
    NavPanelAdmin navPanel = new NavPanelAdmin();
    ExtendCourseController controller;
    public JTextField totalHargaField = new JTextField();
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
    public JButton daftarButton = new JButton("Perpanjang"),
            prevButtonmapel = new JButton("Kembali");
    public JRadioButton cashButton = new JRadioButton("Tunai"),
            trfButton = new JRadioButton("Transfer");
    public ButtonGroup group = new ButtonGroup();
    public ArrayList<Object> mapelPanels = new ArrayList<>();
    public int index =1;
    public String idSiswa;

    public ExtendCourseView(AdminModel model) {
        this.model = model;
        this.idSiswa = model.getIdSIswa();
        this.setSize(1250, 650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        new NavControllerAdmin(model.getConn(), navPanel, this, model);
        controller = new ExtendCourseController(this, model);

        JPanel panelUtama = new JPanel();
        mapelRegist(panelUtama);

        HeaderPanel headerPanel = new HeaderPanel(model);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(navPanel, BorderLayout.WEST);
        this.add(panelUtama, BorderLayout.CENTER);
        addAction();
        daftarButton.setEnabled(false);
    }


    public void mapelRegist(JPanel s) {
        s.setLayout(new BorderLayout());
        s.setBackground(tosca);
        s.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));

        JLabel titleLabel = new JLabel("Perpanjang Les");
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
        daftarButton.addActionListener(controller);
        trfButton.addActionListener(controller);
        cashButton.addActionListener(controller);
        kurangMapelBtn.addActionListener(controller);
        tambahMapelBtn.addActionListener(controller);
        prevButtonmapel.addActionListener(controller);
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
