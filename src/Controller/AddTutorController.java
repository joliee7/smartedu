package Controller;

import Model.*;
import View.AddTutorView;
import View.AdminTutorView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddTutorController implements ActionListener, DocumentListener {
    private AddTutorView frame;
    private AdminModel model;
    private TutorModel tutorModel;
    private CourseModel courseModel;
    String jenjang;
    public AddTutorController(AddTutorView frame, AdminModel model) {
        this.frame = frame;
        this.model = model;
        this.tutorModel = new TutorModel(model.getConn());
        this.courseModel = new CourseModel(model.getConn());

        String[] jenjangList = tutorModel.getNamaJenjang();
        DefaultComboBoxModel<String> jenjangModel = new DefaultComboBoxModel<>();
        for (String jenjang2 : jenjangList) {
            jenjangModel.addElement(jenjang2);
        }
        frame.jenjangCombo.setModel(jenjangModel);
        frame.jenjangCombo.setSelectedIndex(-1);

        String[] mapelList = tutorModel.getNamaMapel().toArray(new String[0]);
        DefaultComboBoxModel<String> mapelModel = new DefaultComboBoxModel<>();
        for (String mapel2 : mapelList) {
            mapelModel.addElement(mapel2);
            System.out.println("ini " +mapel2);
        }
        frame.mapelCombo.setModel(mapelModel);
        frame.mapelCombo.setSelectedIndex(-1);

        String[] mapelList2 = tutorModel.getNamaMapel().toArray(new String[0]);
        DefaultComboBoxModel<String> mapelModel2 = new DefaultComboBoxModel<>();
        for (String mapel2 : mapelList2) {
            mapelModel2.addElement(mapel2);
            System.out.println("ini " +mapel2);
        }
        frame.mapelCombo2.setModel(mapelModel2);
        frame.mapelCombo2.setSelectedIndex(-1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == frame.jenjangCombo) {
            jenjang = frame.jenjangCombo.getSelectedItem().toString();
            courseModel.setNamaMapel(jenjang);
            String[] mapelList = courseModel.getNamaMapel().toArray(new String[0]);
            DefaultComboBoxModel<String> mapelModel = new DefaultComboBoxModel<>();
            for (String m : mapelList) {
                mapelModel.addElement(m);
            }
            frame.mapelCombo.setModel(mapelModel);
        }

        if (source == frame.mapelCombo) {
            courseModel.setNamaMapel(jenjang);
            String[] mapelList = courseModel.getNamaMapel().toArray(new String[0]);
            DefaultComboBoxModel<String> mapelModel = new DefaultComboBoxModel<>();
            for (String m : mapelList) {
                mapelModel.addElement(m);
            }
            frame.mapelCombo2.setModel(mapelModel);
            frame.mapelCombo2.setEnabled(true);
            frame.mapelCombo2.setSelectedIndex(-1);
        }

        if (source == frame.mapelCombo2) {
            if (frame.mapelCombo2.getSelectedItem() == frame.mapelCombo.getSelectedItem()) {
                JOptionPane.showMessageDialog(frame, "Dilarang memilih mata pelajaran yang sama!");
            }
        }
        if (source == frame.daftarButton) {
            String id = frame.idTutorField.getText();
            String nama = frame.nameTutorField.getText();
            String email = frame.emailTutorField.getText();
            String kota = frame.kotaTutorField.getText();
            String telp = frame.telpTutorField.getText();
            int biaya = 0;
            try {
                biaya = Integer.parseInt(frame.biayaTutorField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Biaya harus berupa angka!");
            }

            List<String> listMapel = new ArrayList<>();
            Object selectedMapel1 = frame.mapelCombo.getSelectedItem();
            Object selectedMapel2 = frame.mapelCombo2.getSelectedItem();

            if (selectedMapel1 != null) {
                listMapel.add(selectedMapel1.toString());
            }
            if (selectedMapel2 != null) {
                listMapel.add(selectedMapel2.toString());
            }
            String jenjang = frame.jenjangCombo.getSelectedItem().toString();
            if (id.isEmpty() || nama.isEmpty() || email.isEmpty() || kota.isEmpty() || telp.isEmpty()
                    || listMapel.isEmpty() || jenjang == null) {
                JOptionPane.showMessageDialog(frame, "Silakan lengkapi semua data terlebih dahulu!");
                return;
            }
            int konfirmasi = JOptionPane.showConfirmDialog(
                    frame,
                    "Apakah data yang diisi sudah benar?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (konfirmasi == JOptionPane.YES_OPTION) {
                tutorModel.setNewTutor(id, nama, email, kota, telp, listMapel, jenjang, biaya);
                JOptionPane.showMessageDialog(frame, "Tutor berhasil didaftarkan!");
                resetFields();
            }
        }

        if (source == frame.kembaliButton) {
            if (frame.idTutorField.getText().isEmpty() && frame.nameTutorField.getText().isEmpty() && frame.emailTutorField.getText().isEmpty()
                    && frame.kotaTutorField.getText().isEmpty() && frame.telpTutorField.getText().isEmpty()
                    && frame.mapelCombo.getSelectedIndex() == -1 && frame.jenjangCombo.getSelectedIndex() == -1 && frame.mapelCombo2.getSelectedIndex() == -1
                    && frame.jenjangCombo.getSelectedItem() == null && frame.mapelCombo.getSelectedItem() == null && frame.mapelCombo2.getSelectedItem() == null) {
                int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "Apakah anda ingin kembali?",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    AdminTutorView a = new AdminTutorView(model);
                    a.setVisible(true);
                    frame.dispose();
                }
            } else {
                int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "Anda belum selesai mengisi pendaftaran tutor. Apakah yakin ingin kembali?",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    AdminTutorView a = new AdminTutorView(model);
                    a.setVisible(true);
                    frame.dispose();
                }
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateIdTutor();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateIdTutor();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateIdTutor();
    }

    public void updateIdTutor() {
        String nama = frame.nameTutorField.getText().trim();
        if (!nama.isEmpty()) {
            String generatedId = tutorModel.generateIdTutor(nama);
            frame.idTutorField.setText(generatedId);
        } else {
            frame.idTutorField.setText("");
        }
    }

    public void resetFields(){
        frame.idTutorField.setText("");
        frame.nameTutorField.setText("");
        frame.emailTutorField.setText("");
        frame.kotaTutorField.setText("");
        frame.telpTutorField.setText("");
        frame.biayaTutorField.setText("");
        frame.mapelCombo.setSelectedIndex(-1);
        frame.jenjangCombo.setSelectedIndex(-1);
        frame.mapelCombo2.setSelectedIndex(-1);
    }
}
