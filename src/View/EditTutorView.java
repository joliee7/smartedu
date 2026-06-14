package View;

import Controller.AdminTutorController;
import Controller.EditTutorController;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

public class EditTutorView extends JFrame {
    public JComboBox<String> jenjangCombo;
    public JList<String> mapelList;
    public JTextField idField, namaField, emailField, kotaField, telpField, ratingField, biayaField;
    public JComboBox<String> statusCombo;
    public JButton updateButton, backButton;
    AdminModel model;
    public String idLama, namaLama, emailLama, kotaLama, telpLama, ratingLama, statusLama, jenjangLama;
    public int biayaLama;
    public List<String> mapelListLama;
    TutorModel tutorModel;
    CourseModel courseModel;
    public AdminTutorView tutor;
    public AdminTutorController controllerTutor;

    EditTutorController controller;

    public EditTutorView(AdminModel model, AdminTutorView tutorView, AdminTutorController tutorControl, String id, String nama, String email, String kota, String telp,
                         String rating, List<String> mapelListAwal, String status) {
        this.model = model;
        this.courseModel = new CourseModel(model.getConn());
        this.tutorModel = new TutorModel(model.getConn());
        this.tutor = tutorView;
        this.controllerTutor = tutorControl;
        this.setTitle("Detail/Edit Tutor");
        this.setSize(500, 600);
        this.getContentPane().setBackground(Color.decode("#D9E8EE"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        controller = new EditTutorController(this, model);

        idLama = id;
        namaLama = nama;
        emailLama = email;
        kotaLama = kota;
        telpLama = telp;
        ratingLama = rating;
        statusLama = status;
        mapelListLama = mapelListAwal;
        int biaya = tutorModel.getBiayaFromIdTutor(id);
        biayaLama = biaya;


        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Edit Tutor");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        JLabel idLabel = new JLabel("ID Tutor:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(idLabel, gbc);

        idField = new JTextField(id);
        idField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(idField, gbc);

        JLabel namaLabel = new JLabel("Nama:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(namaLabel, gbc);

        namaField = new JTextField(nama);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(namaField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(emailLabel, gbc);

        emailField = new JTextField(email);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(emailField, gbc);

        JLabel kotaLabel = new JLabel("Kota:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(kotaLabel, gbc);

        kotaField = new JTextField(kota);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(kotaField, gbc);

        JLabel telpLabel = new JLabel("Telepon:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(telpLabel, gbc);

        telpField = new JTextField(telp);
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(telpField, gbc);

        JLabel ratingLabel = new JLabel("Rating:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(ratingLabel, gbc);

        ratingField = new JTextField(rating);
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(ratingField, gbc);

        JLabel biayaLabel = new JLabel("Biaya:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(biayaLabel, gbc);

        biayaField = new JTextField(String.valueOf(biaya));
        gbc.gridx = 1;
        gbc.gridy = 7;
        add(biayaField, gbc);

        JLabel jenjangLabel = new JLabel("Jenjang:");
        gbc.gridx = 0;
        gbc.gridy = 8;
        add(jenjangLabel, gbc);

        jenjangCombo = new JComboBox<>(tutorModel.getNamaJenjang());
        gbc.gridx = 1;
        gbc.gridy = 8;
        add(jenjangCombo, gbc);

        JLabel mapelLabel = new JLabel("Mapel (bisa pilih lebih dari satu):");
        gbc.gridx = 0;
        gbc.gridy = 9;
        add(mapelLabel, gbc);

        mapelList = new JList<>();
        mapelList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane mapelScroll = new JScrollPane(mapelList);
        mapelScroll.setPreferredSize(new Dimension(200, 80));
        gbc.gridx = 1;
        gbc.gridy = 9;
        add(mapelScroll, gbc);

        JLabel statusLabel = new JLabel("Status:");
        gbc.gridx = 0;
        gbc.gridy = 10;
        add(statusLabel, gbc);

        statusCombo = new JComboBox<>(new String[]{"Aktif", "Tidak Aktif"});
        statusCombo.setSelectedItem(status);
        gbc.gridx = 1;
        gbc.gridy = 10;
        add(statusCombo, gbc);

        updateButton = new JButton("Simpan Perubahan");
        backButton = new JButton("Kembali");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.decode("#D9E8EE"));
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        String jenjangDefault = tutorModel.getJenjangFromTutor(id);
        jenjangCombo.setSelectedItem(jenjangDefault);
        jenjangLama = jenjangCombo.getSelectedItem().toString();

        updateMapelList();

        ListModel<String> listModel = mapelList.getModel();
        int[] selectedIndices = IntStream.range(0, listModel.getSize())
                .filter(i -> mapelListAwal.contains(listModel.getElementAt(i)))
                .toArray();
        mapelList.setSelectedIndices(selectedIndices);

        namaField.setEditable(false);
        idField.setEditable(false);

        addAction();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addAction(){
        jenjangCombo.addActionListener(controller);
        statusCombo.addActionListener(controller);
        backButton.addActionListener(controller);
        updateButton.addActionListener(controller);
    }

    public void updateMapelList() {
        String selectedJenjang = jenjangCombo.getSelectedItem().toString();
        courseModel.setNamaMapel(selectedJenjang);
        List<String> mapelBaru = courseModel.getNamaMapel();
        mapelList.setListData(mapelBaru.toArray(new String[0]));
    }
}
