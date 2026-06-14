package Controller;

import Model.*;
import View.ManageCoursePage;
import View.MateriView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ManageCourseController implements ActionListener {
    private ManageCoursePage frame;
    private AdminModel model;
    CourseModel courseModel;
    public ManageCourseController(ManageCoursePage frame, AdminModel model) {
        this.frame = frame;
        this.model = model;
        this.courseModel = new CourseModel(model.getConn());

        String[] jenjangList = courseModel.getNamaJenjang();
        DefaultComboBoxModel<String> jenjangModel = new DefaultComboBoxModel(jenjangList);
        frame.jenjangBox.setModel(jenjangModel);

        String[] jenjangListFilter = {"All", "Kelas 1", "Kelas 2", "Kelas 3", "Kelas 4", "Kelas 5", "Kelas 6", "Kelas 7", "Kelas 8", "Kelas 9", "Kelas 10", "Kelas 11", "Kelas 12"};
        DefaultComboBoxModel<String> filterModel = new DefaultComboBoxModel(jenjangListFilter);
        frame.filterBox.setModel(filterModel);
        String selectedItem = frame.filterBox.getSelectedItem().toString();
        try {
            frame.table.setModel(courseModel.setTableCourse(selectedItem));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == frame.materiButton) {
            int row = frame.table.getSelectedRow();
            if (row != -1) {
                String id = (String) frame.table.getValueAt(row, 0);
                String kelas = (String) frame.table.getValueAt(row, 1);
                String mapel = (String) frame.table.getValueAt(row, 2);
                String gabungan  = kelas + " - " + mapel;
                MateriView materiView = new MateriView(model, gabungan, id);
                materiView.setVisible(true);
            }
        }
        if (source == frame.tambahButton) {
            String namaMapel = frame.namaField.getText();
            String jenjang = frame.jenjangBox.getSelectedItem().toString();
            String tipe = frame.tipeBox.getSelectedItem().toString();
            if (namaMapel.equals("") || jenjang.equals("") || tipe.equals("")) {
                JOptionPane.showMessageDialog(frame, "Mohon mengisi semua data yang diperlukan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean check = courseModel.checkExists(namaMapel, jenjang, tipe);
            if (!check){
                JOptionPane.showMessageDialog(frame, "Mata pelajaran sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                courseModel.addCourse(jenjang, namaMapel, tipe);
                JOptionPane.showMessageDialog(frame,"Berhasil menambah mata pelajaran!");
                String selectedItem = frame.filterBox.getSelectedItem().toString();
                frame.table.setModel(courseModel.setTableCourse(selectedItem));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }else if (source == frame.filterBox){
            String selectedItem = frame.filterBox.getSelectedItem().toString();
            try {
                frame.table.setModel(courseModel.setTableCourse(selectedItem));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }else if (source == frame.resetButton){
            frame.namaField.setText("");
            frame.jenjangBox.setSelectedIndex(0);
            frame.tipeBox.setSelectedIndex(0);
        }
    }
}
