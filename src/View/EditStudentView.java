package View;

import Controller.AdminStudentController;
import Controller.EditStudentController;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EditStudentView extends JFrame {
    public JTextField idField, namaField, emailField, kotaField, telpField, jenjangField, namaOrtuField, telpOrtuField;
    public JTable lesTable;
    public JButton updateButton, backButton;
    public JComboBox<String> statusCombo;
    StudentModel studentModel;
    AdminModel model;
    public AdminStudentController c;

    public String idLama, namaLama, emailLama, kotaLama, telpLama, statusLama, namaOrtuLama, telpOrtuLama;

    public EditStudentView(AdminModel model, AdminStudentController c, String id, String nama, String email, String kota, String telp,
                           String jenjang, String[][] dataLes, String status) {
        this.model = model;
        this.c = c;
        this.studentModel = new StudentModel(model.getConn());
        setTitle("Detail/Edit Siswa");
        setSize(550, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(220, 235, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idLama = id;
        namaLama = nama;
        emailLama = email;
        kotaLama = kota;
        telpLama = telp;
        statusLama = status;

        JLabel titleLabel = new JLabel("Detail Siswa");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("ID Siswa:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(id);
        idField.setEditable(false);
        add(idField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1;
        namaField = new JTextField(nama);
        add(namaField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(email);
        add(emailField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Kota:"), gbc);
        gbc.gridx = 1;
        kotaField = new JTextField(kota);
        add(kotaField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Telepon:"), gbc);
        gbc.gridx = 1;
        telpField = new JTextField(telp);
        add(telpField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Jenjang:"), gbc);
        gbc.gridx = 1;
        jenjangField = new JTextField(jenjang);
        jenjangField.setEditable(false);
        add(jenjangField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusCombo = new JComboBox<>();
        statusCombo.addItem("Aktif");
        statusCombo.addItem("Tidak Aktif");
        statusCombo.setSelectedItem(status);
        add(statusCombo, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        String[] ortu = studentModel.getDataOrtuByStudent(id);
        namaOrtuField = new JTextField(ortu[0]);
        telpOrtuField = new JTextField(ortu[1]);
        namaOrtuLama = ortu[0];
        telpOrtuLama = ortu[1];
        JPanel ortuPanel = new JPanel(new GridLayout(2, 2));
        ortuPanel.setBorder(BorderFactory.createTitledBorder("Data Orang Tua"));
        ortuPanel.add(new JLabel("Nama Orang Tua:"));
        ortuPanel.add(namaOrtuField);
        ortuPanel.add(new JLabel("No HP Orang Tua:"));
        ortuPanel.add(telpOrtuField);
        add(ortuPanel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        String[] kolom = {"Mata Pelajaran", "Tutor", "Durasi"};
        lesTable = new JTable(new DefaultTableModel(dataLes, kolom) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        JScrollPane scrollPane = new JScrollPane(lesTable);
        scrollPane.setPreferredSize(new Dimension(480, 100));
        add(scrollPane, gbc);

        gbc.gridy++;
        updateButton = new JButton("Simpan Perubahan");
        backButton = new JButton("Kembali");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(220, 235, 245));
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);
        add(buttonPanel, gbc);

        namaField.setEditable(false);
        idField.setEditable(false);

        setLocationRelativeTo(null);
        setVisible(true);

        EditStudentController controller = new EditStudentController(this, model);
        updateButton.addActionListener(controller);
        backButton.addActionListener(controller);
    }
}
