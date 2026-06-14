package Controller;

import Model.*;
import View.AdminStudentView;
import View.ExtendCourseView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ExtendCourseController implements ActionListener {
    private ExtendCourseView frame;
    AdminModel model;
    TransaksiModel transaksiModel;
    TutorModel tutorModel;
    StudentModel studentModel;
    CourseModel courseModel;
    int grandTotal;
    String idJenjang;
    public ExtendCourseController (ExtendCourseView frame, AdminModel model) {
        this.frame = frame;
        this.model = model;
        this.transaksiModel = new TransaksiModel(model.getConn());
        this.tutorModel = new TutorModel(model.getConn());
        this.studentModel = new StudentModel(model.getConn());
        this.courseModel = new CourseModel(model.getConn());

        idJenjang = studentModel.getJenjangBySiswa(frame.idSiswa);
        courseModel.setNamaMapel(idJenjang);
        String[] mapelList = courseModel.getNamaMapel().toArray(new String[0]);
        DefaultComboBoxModel<String> mapelModel = new DefaultComboBoxModel<>();
        for (String m : mapelList) {
            mapelModel.addElement(m);
        }
        DefaultComboBoxModel<String> mapelModel2 = new DefaultComboBoxModel<>();
        for (String m : mapelList) {
            mapelModel2.addElement(m);
        }
        DefaultComboBoxModel<String> mapelModel3 = new DefaultComboBoxModel<>();
        for (String m : mapelList) {
            mapelModel3.addElement(m);
        }
        frame.mapelCombo1.setModel(mapelModel);
        frame.mapelCombo2.setModel(mapelModel2);
        frame.mapelCombo3.setModel(mapelModel3);
        frame.mapelCombo1.setSelectedIndex(-1);
        frame.mapelCombo2.setSelectedIndex(-1);
        frame.mapelCombo3.setSelectedIndex(-1);
        loadTable();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
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

        if (source == frame.trfButton || source == frame.cashButton) {
            frame.daftarButton.setEnabled(true);
        }

        if (source == frame.prevButtonmapel) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Apakah anda yakin ingin kembali? Perpanjangan les tidak akan tersimpan!");
            if (confirm == JOptionPane.YES_OPTION) {
                AdminStudentView a = new AdminStudentView(model);
                a.setVisible(true);
                frame.dispose();
            }
        }

        if (source == frame.daftarButton) {
            if (frame.mapelTable.getRowCount() == 0 || !(frame.cashButton.isSelected() || frame.trfButton.isSelected())) {
                JOptionPane.showMessageDialog(frame, "Mohon isi semua data dengan lengkap!");
            } else {
                int confirm = JOptionPane.showConfirmDialog(frame, "Apakah anda yakin ingin memperpanjang les siswa ini?");
                if (confirm == JOptionPane.YES_OPTION) {
                    String metode = null;
                    if (frame.cashButton.isSelected()) {
                        metode = "Cash";
                    } else {
                        metode = "Transfer";
                    }
                    String idTransaksi = transaksiModel.generateIdTransaksi();
                    String siswaID = model.getIdSIswa();
                    Date tanggal = new Date(System.currentTimeMillis());
                    Date period_mulai = transaksiModel.getPeriodAkhirByTransaksi(siswaID);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(period_mulai);
                    cal.add(Calendar.MONTH, 1);
                    java.sql.Date period_akhir = new java.sql.Date(cal.getTimeInMillis());
                    int nominal = grandTotal;
                    System.out.println("Period mulai:" + period_mulai + "period akhir" + period_akhir);

                    ArrayList<String> mapelList = new ArrayList<>();
                    ArrayList<String> tutorList = new ArrayList<>();
                    ArrayList<Integer> durasiList = new ArrayList<>();
                    ArrayList<Integer> hargaSesiList = new ArrayList<>();

                    transaksiModel.addTransaksi(idTransaksi, siswaID, model.getId_admin(), tanggal, nominal, metode, period_mulai, period_akhir);
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
                    JOptionPane.showMessageDialog(frame, "Berhasil memperpanjang les!");
                    AdminStudentView a = new AdminStudentView(model);
                    a.setVisible(true);
                    frame.dispose();
                }
            }

        }
    }

    public void loadTable() {
        String mapel1 = "", tutor1 = "", durasi1 = "";
        String mapel2 = "", tutor2 = "", durasi2 = "";
        String mapel3 = "", tutor3 = "", durasi3 = "";

        if (frame.mapelCombo1.getSelectedItem() != null && frame.tutorCombo1.getSelectedItem() != null && frame.durasiCombo1.getSelectedItem() != null) {
            mapel1 = frame.mapelCombo1.getSelectedItem().toString();
            String selected = frame.tutorCombo1.getSelectedItem().toString();
            tutor1 = selected.split("\\|")[0].trim(); // ambil nama tutor saja
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

        // Cek apakah kombinasi Mapel+Tutor+Durasi lengkap di salah satu panel
        if ((mapel1 != null && tutor1 != null && durasi1 != null) ||
                (mapel2 != null && tutor2 != null && durasi2 != null) ||
                (mapel3 != null && tutor3 != null && durasi3 != null)) {

            loadTable();
        }
    }

    private void updateTutorCombo(JComboBox<String> mapelCombo, JComboBox<String> tutorCombo) {
        if (mapelCombo != null && mapelCombo.getSelectedItem() != null) {
            String mapel = mapelCombo.getSelectedItem().toString();
            ArrayList<String> namaList = (ArrayList<String>) tutorModel.getTutorByMapel(mapel, idJenjang);
            ArrayList<String> ratingList = (ArrayList<String>) tutorModel.getRatingByMapel(mapel, idJenjang);

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
