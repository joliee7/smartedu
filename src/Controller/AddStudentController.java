package Controller;

import Model.*;
import View.AddStudentView;
import View.AdminStudentView;
import View.AdminTutorView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.Locale;

public class AddStudentController implements ActionListener, DocumentListener {
    private AddStudentView frame;
    StudentModel model;
    CourseModel courseModel;
    TransaksiModel transaksiModel;
    TutorModel tutorModel;
    AdminModel adminModel;
    int grandTotal;
    Object selected;
    String idJenjang = "";
    String idSiswa, namaSiswa, emailSiswa, kotaSiswa, telpSiswa, jenjangSiswa, namaOrtu, telpOrtu;
    public AddStudentController(AddStudentView frame, AdminModel adminModel) {
        this.frame = frame;
        this.adminModel = adminModel;
        this.model = new StudentModel(adminModel.getConn());
        this.courseModel = new CourseModel(adminModel.getConn());
        this.transaksiModel = new TransaksiModel(adminModel.getConn());
        this.tutorModel = new TutorModel(adminModel.getConn());

        String[] jenjangList = model.getNamaJenjang();
        DefaultComboBoxModel<String> jenjangModel = new DefaultComboBoxModel<>();
        for (String jenjang2 : jenjangList) {
            jenjangModel.addElement(jenjang2);
        }
        frame.jenjangCombo.setModel(jenjangModel);
        frame.jenjangCombo.setSelectedIndex(-1);

        loadTable();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == frame.nextButtonSiswa) {
            idSiswa = frame.idSiswaField.getText();
            namaSiswa = frame.nameSiswaField.getText();
            emailSiswa = frame.emailSiswaField.getText();
            kotaSiswa = frame.kotaSiswaField.getText();
            telpSiswa = frame.telpSiswaField.getText();

            selected = frame.jenjangCombo.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(frame, "Mohon mengisi semua kolom!");
                return;
            }
            jenjangSiswa = selected.toString();
            idJenjang = tutorModel.getIdJenjangByNama(jenjangSiswa);
            if (idSiswa.isEmpty() || namaSiswa.isEmpty() || emailSiswa.isEmpty() || kotaSiswa.isEmpty() ||
                    telpSiswa.isEmpty() || jenjangSiswa.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Mohon mengisi semua kolom!");
            } else {
                boolean validation = true;
                if (!(emailSiswa.contains("@") && emailSiswa.contains(".")) || emailSiswa.length() > 100){
                    JOptionPane.showMessageDialog(frame, "Email tidak valid!");
                    validation = false;
                }
                if (namaSiswa.length() > 60){
                    JOptionPane.showMessageDialog(frame, "Nama tidak boleh melebihi dari 60 karakter!");
                    validation = false;
                }
                if (kotaSiswa.length() > 40){
                    JOptionPane.showMessageDialog(frame, "Nama kota tidak boleh melebihi dari 40 karakter!");
                    validation = false;
                }
                if (!telpSiswa.matches("\\d+")) {
                    JOptionPane.showMessageDialog(frame, "Mohon mengisi telepon dengan angka saja!");
                    validation = false;
                } else if (telpSiswa.length() > 13){
                    JOptionPane.showMessageDialog(frame, "Telepon tidak boleh melebihi dari 13 karakter!");
                    validation = false;
                }
                if (validation) {
                    int currentIndex = frame.tabbedPane.getSelectedIndex();
                    int totalTabs = frame.tabbedPane.getTabCount();
                    if (currentIndex < totalTabs - 1) {
                        frame.tabbedPane.setSelectedIndex(currentIndex + 1);
                    }
                    courseModel.setNamaMapel(jenjangSiswa);
                    String[] mapelList = courseModel.getNamaMapel().toArray(new String[0]);
                    frame.mapelCombo1.setModel(new DefaultComboBoxModel<>(mapelList));
                    frame.mapelCombo2.setModel(new DefaultComboBoxModel<>(mapelList));
                    frame.mapelCombo3.setModel(new DefaultComboBoxModel<>(mapelList));
                    frame.mapelCombo1.setSelectedIndex(-1);
                    frame.mapelCombo2.setSelectedIndex(-1);
                    frame.mapelCombo3.setSelectedIndex(-1);
                }
            }
        }

        if (source == frame.backButtonSiswa) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Apakah anda yakin ingin kembali? Data siswa tidak akan tersimpan!");
            if (confirm == JOptionPane.YES_OPTION) {
                AdminStudentView a = new AdminStudentView(frame.adminModel);
                a.setVisible(true);
                frame.dispose();
                frame.idSiswaField.setText("");
                frame.nameSiswaField.setText("");
                frame.emailSiswaField.setText("");
                frame.kotaSiswaField.setText("");
                frame.telpSiswaField.setText("");
                frame.jenjangCombo.setSelectedIndex(-1);
            }
        }

        if (source == frame.nextButtonOrtu) {
            namaOrtu = frame.namaOrtuField.getText();
            telpOrtu = frame.telpOrtuField.getText();
            if (!telpSiswa.matches("\\d+")) {
                JOptionPane.showMessageDialog(frame, "Mohon mengisi telepon dengan angka saja!");
            }
            if (namaOrtu.isEmpty() || telpOrtu.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Mohon mengisi semua kolom!");
            } else {
                boolean validation = true;
                if (namaOrtu.length() > 60) {
                    JOptionPane.showMessageDialog(frame, "Nama orang tua tidak boleh melebihi dari 60 karakter!");
                    validation = false;
                }
                if (telpOrtu.length() > 13) {
                    JOptionPane.showMessageDialog(frame, "Telepon orang tua tidak boleh melebihi 13 karakter!");
                    validation = false;
                }
                if (validation) {
                    int currentIndex = frame.tabbedPane.getSelectedIndex();
                    int totalTabs = frame.tabbedPane.getTabCount();
                    if (currentIndex < totalTabs - 1) {
                        frame.tabbedPane.setSelectedIndex(currentIndex + 1);
                    }
                }
            }
        }
        if (source == frame.prevButtonOrtu) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Apakah anda yakin ingin kembali? Data orang tua tidak akan tersimpan!");
            if (confirm == JOptionPane.YES_OPTION) {
                int currentIndex = frame.tabbedPane.getSelectedIndex();
                if (currentIndex > 0) {
                    frame.tabbedPane.setSelectedIndex(currentIndex - 1);
                }
                frame.namaOrtuField.setText("");
                frame.telpOrtuField.setText("");
            }
        }

        if (source == frame.tambahMapelBtn){
            if (frame.index < 3) {
                frame.index++;
                frame.mapelBaru(frame.index);
                frame.kurangMapelBtn.setEnabled(true);
                if (frame.index == 3) {
                    frame.tambahMapelBtn.setEnabled(false);
                }
            }
            cekDanUpdateTable();
        }

        if (source == frame.kurangMapelBtn){
            if (frame.index > 1) {
                frame.kurangiMapel(frame.index);
                frame.index--;
                frame.tambahMapelBtn.setEnabled(true);
                if (frame.index == 1) {
                    frame.kurangMapelBtn.setEnabled(false);
                }
            }
            cekDanUpdateTable();
        }

        if (source == frame.mapelCombo1 || source == frame.tutorCombo1 || source == frame.durasiCombo1) {
            String selected = (String) frame.mapelCombo1.getSelectedItem();
            if (selected != null) {
                if (selected.equals(frame.mapelCombo2.getSelectedItem()) ||
                        selected.equals(frame.mapelCombo3.getSelectedItem())) {
                    JOptionPane.showMessageDialog(frame, "Mata pelajaran sudah dipilih, silahkan pilih mata pelajaran yang lain!");
                    frame.mapelCombo1.setSelectedIndex(-1);
                    return;
                }
            }
            updateTutorCombo(frame.mapelCombo1, frame.tutorCombo1);
            if (frame.mapelCombo1.getSelectedItem() != null &&
                    frame.tutorCombo1.getSelectedItem() != null &&
                    frame.durasiCombo1.getSelectedItem() != null) {
                cekDanUpdateTable();
            }
        }

        if (source == frame.mapelCombo2 || source == frame.tutorCombo2 || source == frame.durasiCombo2) {
            String selected = (String) frame.mapelCombo2.getSelectedItem();
            if (selected != null) {
                if (selected.equals(frame.mapelCombo1.getSelectedItem()) ||
                        selected.equals(frame.mapelCombo3.getSelectedItem())) {
                    JOptionPane.showMessageDialog(frame, "Mata pelajaran sudah dipilih, silahkan pilih mata pelajaran yang lain!");
                    frame.mapelCombo2.setSelectedIndex(-1);
                    return;
                }
            }
            updateTutorCombo(frame.mapelCombo2, frame.tutorCombo2);
            if (frame.mapelCombo2.getSelectedItem() != null &&
                    frame.tutorCombo2.getSelectedItem() != null &&
                    frame.durasiCombo2.getSelectedItem() != null) {
                cekDanUpdateTable();
            }
        }

        if (source == frame.mapelCombo3 || source == frame.tutorCombo3 || source == frame.durasiCombo3) {
            String selected = (String) frame.mapelCombo3.getSelectedItem();
            if (selected != null) {
                if (selected.equals(frame.mapelCombo1.getSelectedItem()) ||
                        selected.equals(frame.mapelCombo2.getSelectedItem())) {
                    JOptionPane.showMessageDialog(frame, "Mata pelajaran sudah dipilih, silahkan pilih mata pelajaran yang lain!");
                    frame.mapelCombo3.setSelectedIndex(-1);
                    return;
                }
            }
            updateTutorCombo(frame.mapelCombo3, frame.tutorCombo3);
            if (frame.mapelCombo3.getSelectedItem() != null &&
                    frame.tutorCombo3.getSelectedItem() != null &&
                    frame.durasiCombo3.getSelectedItem() != null) {
                cekDanUpdateTable();
            }
        }

        if (source == frame.cashButton || source == frame.trfButton || frame.cashButton.isSelected() || frame.trfButton.isSelected()) {
            frame.daftarButton.setEnabled(true);
        }

        if (source == frame.daftarButton) {
            if (frame.mapelTable.getRowCount() == 0 || !(frame.cashButton.isSelected() || frame.trfButton.isSelected())) {
                JOptionPane.showMessageDialog(frame, "Mohon isi semua data dengan lengkap!");
            } else {
                int confirm = JOptionPane.showConfirmDialog(frame, "Apakah anda yakin ingin mendaftarkan siswa ini?");
                if (confirm == JOptionPane.YES_OPTION) {
                    String metode = frame.cashButton.isSelected() ? "Cash" : "Transfer";
                    String idTransaksi = transaksiModel.generateIdTransaksi();
                    String siswaID = idSiswa;
                    Date tanggal = new Date(System.currentTimeMillis());
                    Date period_mulai = tanggal;
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(period_mulai);
                    cal.add(Calendar.MONTH, 1);
                    Date period_akhir = new Date(cal.getTimeInMillis());
                    int nominal = grandTotal;

                    model.addStudent(idSiswa, namaSiswa, emailSiswa, kotaSiswa, telpSiswa, jenjangSiswa, namaOrtu, telpOrtu);
                    transaksiModel.addTransaksi(idTransaksi, siswaID, adminModel.getId_admin(), tanggal, nominal, metode, period_mulai, period_akhir);

                    ArrayList<String> mapelList = new ArrayList<>();
                    ArrayList<String> tutorList = new ArrayList<>();
                    ArrayList<Integer> durasiList = new ArrayList<>();
                    ArrayList<Integer> hargaSesiList = new ArrayList<>();

                    int jumlahSesi = (int) frame.mapelTable.getValueAt(0, 5);
                    int subtotal = 0;

                    for (int i = 0; i < frame.mapelTable.getRowCount(); i++) {
                        mapelList.add(frame.mapelTable.getValueAt(i, 1).toString());  // kolom 0: mapel
                        tutorList.add(frame.mapelTable.getValueAt(i, 2).toString());  // kolom 1: tutor
                        String durasiText = frame.mapelTable.getValueAt(i, 3).toString();
                        String durasiAngka = durasiText.split(" ")[0]; // ambil kata pertama
                        int durasi = Integer.parseInt(durasiAngka);
                        String hargaText = frame.mapelTable.getValueAt(i, 4).toString();
                        String cleanHarga = hargaText.replace("Rp.", "").replace(".", "").replace(" ", "");
                        int harga = Integer.parseInt(cleanHarga);
                        durasiList.add(durasi);
                        hargaSesiList.add(harga);
                    }
                    String idDetailTrans = transaksiModel.generateIdDetailTrans();
                    transaksiModel.addDetailTransaksi(idDetailTrans, idTransaksi, mapelList, tutorList, durasiList, hargaSesiList, jumlahSesi, subtotal);
                    JOptionPane.showMessageDialog(frame, "Siswa berhasil didaftarkan!");
                    AdminStudentView a = new AdminStudentView(adminModel);
                    a.setVisible(true);
                    frame.dispose();
                }
            }
        }

        if (source == frame.prevButtonmapel) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Apakah anda yakin ingin kembali? Data mata pelajaran yang diambil tidak akan tersimpan!");
            if (confirm == JOptionPane.YES_OPTION) {
                int currentIndex = frame.tabbedPane.getSelectedIndex();
                if (currentIndex > 0) {
                    frame.tabbedPane.setSelectedIndex(currentIndex - 1);
                }
                frame.mapelCombo1.setSelectedItem(null);
                frame.mapelCombo2.setSelectedItem(null);
                frame.mapelCombo3.setSelectedItem(null);
                frame.tutorCombo1.setSelectedItem(null);
                frame.tutorCombo2.setSelectedItem(null);
                frame.tutorCombo3.setSelectedItem(null);
                frame.durasiCombo1.setSelectedItem(null);
                frame.durasiCombo2.setSelectedItem(null);
                frame.durasiCombo3.setSelectedItem(null);
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateIdStudent();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateIdStudent();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateIdStudent();
    }

    public void loadTable() {
        String mapel1 = "", tutor1 = "", durasi1 = "";
        String mapel2 = "", tutor2 = "", durasi2 = "";
        String mapel3 = "", tutor3 = "", durasi3 = "";

        if (frame.mapelCombo1.getSelectedItem() != null && frame.tutorCombo1.getSelectedItem() != null && frame.durasiCombo1.getSelectedItem() != null) {
            mapel1 = frame.mapelCombo1.getSelectedItem().toString();
            String selected = frame.tutorCombo1.getSelectedItem().toString();
            tutor1 = selected.split("\\|")[0].trim();
            durasi1 = frame.durasiCombo1.getSelectedItem().toString();
        }

        if (frame.mapelCombo2.getSelectedItem() != null && frame.tutorCombo2.getSelectedItem() != null && frame.durasiCombo2.getSelectedItem() != null) {
            mapel2 = frame.mapelCombo2.getSelectedItem().toString();
            String selected = frame.tutorCombo2.getSelectedItem().toString();
            tutor2 = selected.split("\\|")[0].trim();
            durasi2 = frame.durasiCombo2.getSelectedItem().toString();
        }

        if (frame.mapelCombo3.getSelectedItem() != null && frame.tutorCombo3.getSelectedItem() != null && frame.durasiCombo3.getSelectedItem() != null) {
            mapel3 = frame.mapelCombo3.getSelectedItem().toString();
            String selected = frame.tutorCombo3.getSelectedItem().toString();
            tutor3 = selected.split("\\|")[0].trim();
            durasi3 = frame.durasiCombo3.getSelectedItem().toString();
        }

        DefaultTableModel tableModel = transaksiModel.setRegistMapelTable(
                mapel1, tutor1, durasi1,
                mapel2, tutor2, durasi2,
                mapel3, tutor3, durasi3
        );
        frame.mapelTable.setModel(tableModel);

        grandTotal = 0;
        for (int i = 0; i < frame.mapelTable.getRowCount(); i++) {
            String hargaString = frame.mapelTable.getValueAt(i, 6).toString().replace("Rp. ", "").replace(".", "").trim();
            int harga = Integer.parseInt(hargaString);
            grandTotal += harga;
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        String formattedTotal = formatter.format(grandTotal);

        frame.totalHargaField.setText(formattedTotal);
        frame.totalHargaField.setForeground(Color.RED);
        frame.totalHargaField.setFont(new Font("Calibri", Font.BOLD, 20));
    }

    public void updateIdStudent() {
        String nama = frame.nameSiswaField.getText().trim();
        if (!nama.isEmpty()) {
            String generatedId = model.generateIdSiswa(nama);
            frame.idSiswaField.setText(generatedId);
        } else {
            frame.idSiswaField.setText("");
        }
    }

    public void cekDanUpdateTable() {
        String mapel1 = (String) frame.mapelCombo1.getSelectedItem();
        String tutor1 = (String) frame.tutorCombo1.getSelectedItem();
        String durasi1 = (String) frame.durasiCombo1.getSelectedItem();

        String mapel2 = (String) frame.mapelCombo2.getSelectedItem();
        String tutor2 = (String) frame.tutorCombo2.getSelectedItem();
        String durasi2 = (String) frame.durasiCombo2.getSelectedItem();

        String mapel3 = (String) frame.mapelCombo3.getSelectedItem();
        String tutor3 = (String) frame.tutorCombo3.getSelectedItem();
        String durasi3 = (String) frame.durasiCombo3.getSelectedItem();

        if ((mapel1 != null && tutor1 != null && durasi1 != null) ||
                (mapel2 != null && tutor2 != null && durasi2 != null) ||
                (mapel3 != null && tutor3 != null && durasi3 != null)) {
            loadTable();
        }
    }

    private void updateTutorCombo(JComboBox<String> mapelCombo, JComboBox<String> tutorCombo) {
        if (mapelCombo != null && mapelCombo.getSelectedItem() != null) {
            String mapel = mapelCombo.getSelectedItem().toString();
            ArrayList<String> namaList = (ArrayList<String>) tutorModel.getTutorByMapel(mapel, jenjangSiswa);
            ArrayList<String> ratingList = (ArrayList<String>) tutorModel.getRatingByMapel(mapel, jenjangSiswa);

            DefaultComboBoxModel<String> tutorModel = new DefaultComboBoxModel<>();
            for (int i = 0; i < namaList.size(); i++) {
                tutorModel.addElement(namaList.get(i) + "|" + ratingList.get(i));
            }

            tutorCombo.setModel(tutorModel);
            tutorCombo.setSelectedIndex(-1);

            tutorCombo.setRenderer(new ListCellRenderer<String>() {
                @Override
                public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
                                                              boolean isSelected, boolean cellHasFocus) {
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    panel.setOpaque(true);

                    if (value == null) return new JLabel("");
                    String[] parts = value.split("\\|");
                    String nama = parts[0];
                    double rating = Double.parseDouble(parts[1]);

                    JLabel namaLabel = new JLabel(nama + " | Rating:");
                    namaLabel.setForeground(Color.BLACK);

                    int fullStars = (int) rating;
                    boolean halfStar = (rating - fullStars >= 0.25 && rating - fullStars <= 0.75);
                    int emptyStars = 5 - fullStars - (halfStar ? 1 : 0);

                    StringBuilder stars = new StringBuilder();
                    for (int i = 0; i < fullStars; i++) stars.append("★");
                    if (halfStar) stars.append("☆");
                    for (int i = 0; i < emptyStars; i++) stars.append("✩");

                    JLabel starLabel = new JLabel(stars.toString());
                    starLabel.setForeground(new Color(218, 165, 32));

                    if (isSelected) {
                        panel.setBackground(list.getSelectionBackground());
                        namaLabel.setForeground(Color.WHITE);
                        starLabel.setForeground(Color.YELLOW);
                    } else {
                        panel.setBackground(list.getBackground());
                    }

                    panel.add(namaLabel);
                    panel.add(starLabel);
                    return panel;
                }
            });

            for (ActionListener al : tutorCombo.getActionListeners()) {
                tutorCombo.removeActionListener(al);
            }
            tutorCombo.addActionListener(e -> cekDanUpdateTable());
        }
    }
}


