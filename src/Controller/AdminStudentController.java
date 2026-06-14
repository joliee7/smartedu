package Controller;

import Model.*;
import View.AddStudentView;
import View.AdminStudentView;
import View.EditStudentView;
import View.ExtendCourseView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class AdminStudentController implements ActionListener, DocumentListener {
    private AdminStudentView frame;
    private StudentModel studentModel;
    private TransaksiModel transaksiModel;
    private AdminModel model;
    String idSiswa;
    public AdminStudentController(AdminStudentView frame, AdminModel model) {
        this.frame = frame;
        this.model = model;
        this.idSiswa = model.getIdSIswa();
        this.studentModel = new StudentModel(model.getConn());
        this.transaksiModel = new TransaksiModel(model.getConn());
        loadComboBox();
        updateTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == frame.jenjangComboBox) {
            updateTable();
        } else if (source == frame.statusComboBox) {
            updateTable();
        }
        if (source == frame.addSiswaButton) {
            AddStudentView a = new AddStudentView(frame.adminModel);
            a.setVisible(true);
            frame.dispose();
        }
        if (source == frame.perpanjangButton) {
            int selectedRow = frame.listSiswatable.getSelectedRow();
            if (selectedRow != -1) {
                String idSiswa = frame.listSiswatable.getValueAt(selectedRow, 0).toString();
                model.setIDSiswa(idSiswa);
                ExtendCourseView view = new ExtendCourseView(model);
                view.setVisible(true);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Mohon memilih baris siswa yang akan diperpanjang!");
            }
        }
        if (source == frame.deleteButton) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Apakah anda yakin ingin menonaktifkan siswa ini?");
            if (confirm == JOptionPane.YES_OPTION) {
                int row = frame.listSiswatable.getSelectedRow();
                if (row != -1) {
                    String id = frame.listSiswatable.getValueAt(row, 0).toString();
                    String status = frame.listSiswatable.getValueAt(row, 7).toString();
                    if (status.equalsIgnoreCase("Aktif")) {
                        studentModel.softDeleteSiswa(id);
                        JOptionPane.showMessageDialog(frame, "Siswa berhasil dinonaktifkan!");
                        frame.listSiswatable.setValueAt("Tidak Aktif", row, 7);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Status siswa sudah tidak aktif.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Silahkan memilih baris siswa untuk dinonaktifkan!");
                }
            }
        }
        if (source == frame.detailSiswaButton) {
            int viewRow = frame.listSiswatable.getSelectedRow();
            if (viewRow != -1) {
                int row = frame.listSiswatable.convertRowIndexToModel(viewRow);
                String id = frame.listSiswatable.getValueAt(row, 0).toString();
                String nama = frame.listSiswatable.getValueAt(row, 1).toString();
                String email = frame.listSiswatable.getValueAt(row, 2).toString();
                String kota = frame.listSiswatable.getValueAt(row, 3).toString();
                String telp = frame.listSiswatable.getValueAt(row, 4).toString();
                String status = frame.listSiswatable.getValueAt(row, 7).toString();
                String jenjang = studentModel.getJenjangBySiswa(id);

                List<String[]> lesList = transaksiModel.getDetailLesByStudent(id);

                String[][] dataLes = new String[lesList.size()][3];
                for (int i = 0; i < lesList.size(); i++) {
                    dataLes[i] = lesList.get(i);
                }

                EditStudentView et = new EditStudentView(model, this, id, nama, email, kota, telp, jenjang, dataLes, status);
                et.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Pilih data siswa yang ingin dilihat/diedit.");
            }
        }
    }


    public void updateTable() {
        String jenjang = frame.jenjangComboBox.getSelectedItem().toString();
        String status = frame.statusComboBox.getSelectedItem().toString();
        String keyword = frame.filterNamaSiswa.getText().trim().toLowerCase();

        DefaultTableModel tableModel = studentModel.setTableStudentAdmin(frame.listSiswatable, jenjang, status, keyword);
        frame.listSiswatable.setModel(tableModel);
    }

    private void loadComboBox() {
        String[] jenjangList = studentModel.getNamaJenjang();
        DefaultComboBoxModel<String> jenjangModel = new DefaultComboBoxModel<>();
        jenjangModel.addElement("All");
        for (String jenjang : jenjangList) {
            jenjangModel.addElement(jenjang);
        }
        frame.jenjangComboBox.setModel(jenjangModel);

        DefaultComboBoxModel<String> statusModel = new DefaultComboBoxModel<>();
        statusModel.addElement("All");
        statusModel.addElement("Aktif");
        statusModel.addElement("Tidak Aktif");
        frame.statusComboBox.setModel(statusModel);
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
