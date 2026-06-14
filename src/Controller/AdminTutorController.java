package Controller;

import Model.*;
import View.AddTutorView;
import View.AdminTutorView;
import View.EditTutorView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class AdminTutorController implements ActionListener, DocumentListener {
    private AdminTutorView frame;
    private AdminModel model;
    private TutorModel tutorModel;
    private CourseModel courseModel;

    private boolean isUpdatingComboBox = false;

    public AdminTutorController(AdminTutorView frame, AdminModel model) {
        this.frame = frame;
        this.model = model;
        this.tutorModel = new TutorModel(model.getConn());
        this.courseModel = new CourseModel(model.getConn());

        loadComboBox();
        updateTable();
    }

    private void loadComboBox() {
        String[] jenjangList = tutorModel.getNamaJenjang();
        DefaultComboBoxModel<String> jenjangModel = new DefaultComboBoxModel<>();
        jenjangModel.addElement("All");
        for (String jenjang : jenjangList) {
            jenjangModel.addElement(jenjang);
        }
        frame.jenjangComboBox.setModel(jenjangModel);

        List<String> mapelList = tutorModel.getNamaMapel();
        DefaultComboBoxModel<String> mapelModel = new DefaultComboBoxModel<>();
        mapelModel.addElement("All");
        for (String mapel : mapelList) {
            mapelModel.addElement(mapel);
        }
        frame.mapelComboBox.setModel(mapelModel);

        DefaultComboBoxModel<String> statusModel = new DefaultComboBoxModel<>();
        statusModel.addElement("All");
        statusModel.addElement("Aktif");
        statusModel.addElement("Tidak Aktif");
        frame.statusComboBox.setModel(statusModel);
    }

    public void updateTable() {
        String jenjang = frame.jenjangComboBox.getSelectedItem().toString();
        String mapel = frame.mapelComboBox.getSelectedItem().toString();
        String status = frame.statusComboBox.getSelectedItem().toString();
        String keyword = frame.filterNamaTutor.getText().trim().toLowerCase();

        DefaultTableModel tableModel = tutorModel.setTableTutorAdmin(jenjang, mapel, status, keyword);
        frame.listTutortable.setModel(tableModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (isUpdatingComboBox) return;

        if (source == frame.jenjangComboBox) {
            isUpdatingComboBox = true;
            String jenjang = frame.jenjangComboBox.getSelectedItem().toString();
            courseModel.setNamaMapel(jenjang);
            List<String> mapelList = courseModel.getNamaMapel();
            DefaultComboBoxModel<String> mapelModel = new DefaultComboBoxModel<>();
            mapelModel.addElement("All");
            for (String mapel : mapelList) {
                mapelModel.addElement(mapel);
            }
            frame.mapelComboBox.setModel(mapelModel);
            frame.mapelComboBox.setSelectedItem("All");
            isUpdatingComboBox = false;
            updateTable();
        } else if (source == frame.mapelComboBox || source == frame.statusComboBox) {
            updateTable();
        }
        if (source == frame.addTutorButton) {
            AddTutorView a = new AddTutorView(model);
            a.setVisible(true);
            frame.dispose();
        }
        if (source == frame.nonaktifButton) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Apakah anda yakin ingin menonaktifkan tutor ini?");
            if (confirm == JOptionPane.YES_OPTION) {
                int row = frame.listTutortable.getSelectedRow();
                if (row != -1) {
                    String id = frame.listTutortable.getValueAt(row, 0).toString();
                    String status = frame.listTutortable.getValueAt(row, 7).toString();
                    if (status.equalsIgnoreCase("Aktif")) {
                        tutorModel.softDeleteTutor(id);
                        JOptionPane.showMessageDialog(frame, "Siswa berhasil dinonaktifkan!");
                        frame.listTutortable.setValueAt("Tidak Aktif", row, 7);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Status siswa sudah tidak aktif.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Silahkan memilih baris siswa untuk dinonaktifkan!");
                }
            }
        }
        if (source == frame.detailTutorButton) {
            int viewRow = frame.listTutortable.getSelectedRow();
            if (viewRow != -1) {
                int row = frame.listTutortable.convertRowIndexToModel(viewRow);
                String id = frame.listTutortable.getValueAt(row, 0).toString();
                String nama = frame.listTutortable.getValueAt(row, 1).toString();
                String email = frame.listTutortable.getValueAt(row, 2).toString();
                String kota = frame.listTutortable.getValueAt(row, 3).toString();
                String telp = frame.listTutortable.getValueAt(row, 4).toString();
                String rating = frame.listTutortable.getValueAt(row, 5).toString();
                String status = frame.listTutortable.getValueAt(row, 7).toString();
                List<String> mapelList = tutorModel.getMapelByTutor(id);

                EditTutorView et = new EditTutorView(model, frame, this, id, nama, email, kota, telp, rating, mapelList, status);
                et.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Pilih data tutor yang ingin dilihat/diedit.");
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateTable();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateTable();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateTable();
    }
}
