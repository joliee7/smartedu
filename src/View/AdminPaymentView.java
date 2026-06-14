package View;

import Controller.AdminPaymentController;
import Controller.NavControllerAdmin;
import Model.*;

import javax.swing.*;
import java.awt.*;

public class AdminPaymentView extends JFrame {
    AdminModel model;
    HeaderPanel headerPanel;
    NavPanelAdmin navPanel = new NavPanelAdmin();
    JTabbedPane tabbedPane;
    public JPanel gajiTutorPanel = new JPanel(),
            pembayaranSiswaPanel = new JPanel();
    public JComboBox<String> bulanComboBox = new JComboBox<>(),
            statusGajiComboBox = new JComboBox<>(),
            bulanSiswaComboBox = new JComboBox<>(),
            statusBayarComboBox = new JComboBox<>();
    public JTextField filterNamaGaji =  new JTextField(),
            filterNamaSiswa = new JTextField(),
            subtotalField = new JTextField();
    public JTable gajiTutorTable = new JTable(),
            pembayaranSiswaTable = new JTable();
    public JButton bayarButton = new JButton("Bayar"),
            detailGajiButton = new JButton("Detail"),
            detailPembayaranButton = new JButton("Detail");

    public AdminPaymentView(AdminModel model) {
        this.model = model;
        this.setLayout(new BorderLayout());
        this.setSize(1250, 650);
        this.getContentPane().setBackground(Color.decode("#D9E8EE"));
        this.setLocationRelativeTo(null);
        new NavControllerAdmin(model.getConn(), navPanel, this, model);
        AdminPaymentController paymentController = new AdminPaymentController(this, model);
        headerPanel = new HeaderPanel(model);

        JPanel panelUtama = new JPanel();
        panelUtama.setLayout(new BorderLayout());
        panelUtama.setBackground(Color.decode("#D9E8EE"));

        gajiTutorView(gajiTutorPanel);
        pembayaranSiswaView(pembayaranSiswaPanel);

        tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.addTab("Penggajian Tutor", gajiTutorPanel);
        tabbedPane.addTab("Pembayaran Siswa", pembayaranSiswaPanel);
        tabbedPane.setBackground(Color.white);
        panelUtama.add(tabbedPane, BorderLayout.CENTER);

        this.add(navPanel, BorderLayout.WEST);
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(panelUtama, BorderLayout.CENTER);

        bulanComboBox.addActionListener(paymentController);
        bulanSiswaComboBox.addActionListener(paymentController);
        statusGajiComboBox.addActionListener(paymentController);
        statusBayarComboBox.addActionListener(paymentController);
        bayarButton.addActionListener(paymentController);
        detailGajiButton.addActionListener(paymentController);
        detailPembayaranButton.addActionListener(paymentController);
    }

    public void gajiTutorView(JPanel g) {
        g.setLayout(new BorderLayout());
        g.setBackground(Color.decode("#D9E8EE"));
        g.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));

        JLabel titleLabel = new JLabel("Rekap Gaji Tutor");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titleLabel.setForeground(Color.decode("#063f5c"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        g.add(titleLabel, BorderLayout.NORTH);

        JPanel panelTengah = new JPanel(new BorderLayout());
        panelTengah.setBackground(Color.decode("#D9E8EE"));

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comboPanel.setBackground(Color.decode("#D9E8EE"));
        bulanComboBox.setPreferredSize(new Dimension(100, 30));
        statusGajiComboBox.setPreferredSize(new Dimension(100, 30));
        comboPanel.add(new JLabel("Bulan: "));
        comboPanel.add(bulanComboBox);
        comboPanel.add(new JLabel("Status: "));
        comboPanel.add(statusGajiComboBox);
        comboPanel.add(new JLabel("Filter Nama: "));
        filterNamaGaji.setPreferredSize(new Dimension(100, 30));
        comboPanel.add(filterNamaGaji);
        panelTengah.add(comboPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(gajiTutorTable);
        panelTengah.add(scrollPane, BorderLayout.CENTER);
        g.add(panelTengah, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setBackground(Color.decode("#D9E8EE"));
        bayarButton.setBackground(Color.BLUE);
        bayarButton.setForeground(Color.WHITE);
        detailGajiButton.setBackground(Color.BLUE);
        detailGajiButton.setForeground(Color.WHITE);
        panelBawah.add(bayarButton, BorderLayout.WEST);
        panelBawah.add(detailGajiButton, BorderLayout.EAST);
        g.add(panelBawah, BorderLayout.SOUTH);
    }

    public void pembayaranSiswaView(JPanel p) {
        p.setLayout(new BorderLayout());
        p.setBackground(Color.decode("#D9E8EE"));
        p.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));

        JLabel titleLabel = new JLabel("Pembayaran Siswa");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titleLabel.setForeground(Color.decode("#063f5c"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(titleLabel, BorderLayout.NORTH);

        JPanel panelTengah = new JPanel(new BorderLayout());
        panelTengah.setBackground(Color.decode("#D9E8EE"));

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comboPanel.setBackground(Color.decode("#D9E8EE"));
        bulanSiswaComboBox.setPreferredSize(new Dimension(100, 30));
        statusBayarComboBox.setPreferredSize(new Dimension(100, 30));
        comboPanel.add(new JLabel("Bulan: "));
        comboPanel.add(bulanSiswaComboBox);
        comboPanel.add(new JLabel("Metode Pembayaran: "));
        comboPanel.add(statusBayarComboBox);
        comboPanel.add(new JLabel("Filter Nama: "));
        filterNamaSiswa.setPreferredSize(new Dimension(100, 30));
        comboPanel.add(filterNamaSiswa);
        panelTengah.add(comboPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(pembayaranSiswaTable);
        panelTengah.add(scrollPane, BorderLayout.CENTER);
        p.add(panelTengah, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setBackground(Color.decode("#D9E8EE"));

        JPanel subtotalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        subtotalPanel.setBackground(Color.decode("#D9E8EE"));
        subtotalField.setHorizontalAlignment(SwingConstants.RIGHT);
        subtotalField.setForeground(Color.green);
        subtotalField.setPreferredSize(new Dimension(100, 20));
        subtotalField.setFont(new Font("Calibri", Font.BOLD, 15));
        subtotalPanel.add(new JLabel("Total Penerimaan: Rp. "));
        subtotalPanel.add(subtotalField);

        panelBawah.add(subtotalPanel, BorderLayout.EAST);

        panelBawah.add(detailPembayaranButton, BorderLayout.WEST);

        p.add(panelBawah, BorderLayout.SOUTH);
    }
}
