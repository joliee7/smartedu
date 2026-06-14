package Controller;

import Model.*;
import View.AdminPaymentView;
import View.DetailGajiTutor;
import View.DetailPembayaranSiswa;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPaymentController implements ActionListener{
    private AdminPaymentView frame;
    private AdminModel model;
    private TutorModel tutorModel;
    private StudentModel studentModel;
    private TransaksiModel transaksiModel;
    String[] bulan = {"All", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    String[] statusBayar = {"All", "Terbayar", "Belum Terbayar"};
    String[] metodeBayar = {"All", "Cash", "Transfer"};

    public AdminPaymentController(AdminPaymentView frame, AdminModel model) {
        this.frame = frame;
        this.model = model;
        this.tutorModel = new TutorModel(model.getConn());
        this.studentModel = new StudentModel(model.getConn());
        this.transaksiModel = new TransaksiModel(model.getConn());

        DefaultComboBoxModel<String> bulanTutor = new DefaultComboBoxModel<>(bulan);
        frame.bulanComboBox.setModel(bulanTutor);
        DefaultComboBoxModel<String> bulanSiswa =  new DefaultComboBoxModel<>(bulan);
        frame.bulanSiswaComboBox.setModel(bulanSiswa);
        DefaultComboBoxModel<String> statusTutor = new DefaultComboBoxModel<>(statusBayar);
        frame.statusGajiComboBox.setModel(statusTutor);
        DefaultComboBoxModel<String> statusSiswa =  new DefaultComboBoxModel<>(metodeBayar);
        frame.statusBayarComboBox.setModel(statusSiswa);

        loadGajiTutorTable();

        loadPembayaranSiswaTable();

        frame.bayarButton.setEnabled(false);
        frame.detailGajiButton.setEnabled(false);

        frame.gajiTutorTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = frame.gajiTutorTable.getSelectedRow();
            if (selectedRow != -1) {
                frame.detailGajiButton.setEnabled(true);
                String statusGaji = frame.gajiTutorTable.getValueAt(selectedRow, 5).toString();
                if (statusGaji.equalsIgnoreCase("Terbayar")) {
                    frame.bayarButton.setEnabled(false);
                } else {
                    frame.bayarButton.setEnabled(true);
                }
            }
        });

        frame.filterNamaGaji.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterNamaTutor();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterNamaTutor();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterNamaTutor();
            }
        });

        frame.filterNamaSiswa.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterNamaSiswa();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterNamaSiswa();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterNamaSiswa();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == frame.bulanComboBox) {
            loadGajiTutorTable();
        }else if (source == frame.statusGajiComboBox) {
            loadGajiTutorTable();
        }else if (source == frame.bayarButton){
            int selectedRow = frame.gajiTutorTable.getSelectedRow();
            if (selectedRow != -1) {
                String tutorName = frame.gajiTutorTable.getValueAt(selectedRow, 0).toString();
                tutorModel.setStatusGajiTutor(tutorName);
                String msg = "Berhasil membayar gaji tutor " + tutorName;
                JOptionPane.showMessageDialog(frame, msg);
            }
            loadGajiTutorTable();
        }else if (source == frame.detailGajiButton){
            int selectedRow = frame.gajiTutorTable.getSelectedRow();
            if (selectedRow != -1) {
                String tutorName = frame.gajiTutorTable.getValueAt(selectedRow, 0).toString();
                Object[] detail = tutorModel.getDetailGajiTutor(tutorName);
                if (detail != null) {
                    new DetailGajiTutor(tutorName, detail).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Tutor belum mengajar. Data gaji tidak ditemukan");
                }
            }
        }else if (source == frame.detailPembayaranButton){
            int selectedRow = frame.pembayaranSiswaTable.getSelectedRow();
            if (selectedRow != -1) {
                String siswaName = frame.pembayaranSiswaTable.getValueAt(selectedRow, 2).toString();
                Object[] detail = studentModel.getDetailPembayaranSiswa(siswaName);
                new DetailPembayaranSiswa(siswaName, detail).setVisible(true);
            }
        }else if (source == frame.bulanSiswaComboBox){
            loadPembayaranSiswaTable();
        }else if (source == frame.statusBayarComboBox){
            loadPembayaranSiswaTable();
        }
    }

    public void filterNamaTutor() {
        String filterText = frame.filterNamaGaji.getText().toLowerCase();

        DefaultTableModel originalModel = tutorModel.setGajiTutorTable(
                frame.bulanComboBox.getSelectedItem().toString(),
                frame.statusGajiComboBox.getSelectedItem().toString()
        );

        DefaultTableModel filteredModel = new DefaultTableModel();

        for (int i = 0; i < originalModel.getColumnCount(); i++) {
            filteredModel.addColumn(originalModel.getColumnName(i));
        }

        for (int i = 0; i < originalModel.getRowCount(); i++) {
            String namaTutor = originalModel.getValueAt(i, 0).toString().toLowerCase();
            if (namaTutor.contains(filterText)) {
                Object[] rowData = new Object[originalModel.getColumnCount()];
                for (int j = 0; j < originalModel.getColumnCount(); j++) {
                    rowData[j] = originalModel.getValueAt(i, j);
                }
                filteredModel.addRow(rowData);
            }
        }

        frame.gajiTutorTable.setModel(filteredModel);
        setForegroundStatus();
    }

    public void filterNamaSiswa() {
        String filterText = frame.filterNamaSiswa.getText().toLowerCase();

        DefaultTableModel originalModel = transaksiModel.setPembayaranSiswaTable(
                frame.bulanSiswaComboBox.getSelectedItem().toString(),
                frame.statusBayarComboBox.getSelectedItem().toString()
        );

        DefaultTableModel filteredModel = new DefaultTableModel();

        for (int i = 0; i < originalModel.getColumnCount(); i++) {
            filteredModel.addColumn(originalModel.getColumnName(i));
        }

        for (int i = 0; i < originalModel.getRowCount(); i++) {
            String namaSiswa = originalModel.getValueAt(i, 2).toString().toLowerCase();
            if (namaSiswa.contains(filterText)) {
                Object[] rowData = new Object[originalModel.getColumnCount()];
                for (int j = 0; j < originalModel.getColumnCount(); j++) {
                    rowData[j] = originalModel.getValueAt(i, j);
                }
                filteredModel.addRow(rowData);
            }
        }

        frame.pembayaranSiswaTable.setModel(filteredModel);

        int totalRow = filteredModel.getRowCount();
        int total = 0;
        for (int i = 0; i < totalRow; i++) {
            String totalBiaya = filteredModel.getValueAt(i, 3).toString();
            total += Integer.parseInt(totalBiaya);
        }
        frame.subtotalField.setText(String.format("%,d", total).replace(',', '.'));
    }



    public void loadGajiTutorTable(){
        String bulan = frame.bulanComboBox.getSelectedItem().toString();
        String status = frame.statusGajiComboBox.getSelectedItem().toString();
        frame.gajiTutorTable.setModel(tutorModel.setGajiTutorTable(bulan,status));
        setForegroundStatus();
    }

    public void loadPembayaranSiswaTable(){
        String bulan2 = frame.bulanSiswaComboBox.getSelectedItem().toString();
        String metodeByr = frame.statusBayarComboBox.getSelectedItem().toString();
        frame.pembayaranSiswaTable.setModel(transaksiModel.setPembayaranSiswaTable(bulan2, metodeByr));
        int totalRow = frame.pembayaranSiswaTable.getRowCount();
        int total = 0;
        for (int i = 0 ; i < totalRow ; i++) {
            String totalBiaya = frame.pembayaranSiswaTable.getValueAt(i, 3).toString();
            total += Integer.parseInt(totalBiaya);
        }
        frame.subtotalField.setText(String.format("%,d", total).replace(',', '.'));
    }

    public void setForegroundStatus(){
        frame.gajiTutorTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = value.toString().toLowerCase();
                if (status.equalsIgnoreCase("belum terbayar")) {
                    c.setForeground(Color.RED);
                } else if (status.equalsIgnoreCase("terbayar")){
                    c.setForeground(Color.GREEN);
                } else {
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });
    }
}
