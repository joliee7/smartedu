package Controller;

import Model.*;
import View.ManageRoomPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ManageRoomController implements ActionListener {
    private ManageRoomPage frame;
    private AdminModel model;
    private RoomModel roomModel;
    public ManageRoomController(ManageRoomPage frame, AdminModel model) {
        this.frame = frame;
        this.model = model;
        this.roomModel = new RoomModel(model.getConn());
        frame.table.setModel(roomModel.setTableRoom());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == frame.tambahButton){
            String namaRuangan = frame.namaField.getText();
            String kapasitas = frame.kapasitasField.getText();
            if (namaRuangan.equals("") || kapasitas.equals("")){
                JOptionPane.showMessageDialog(frame, "Tolong mengisi semua data yang diperlukan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean checkExists = roomModel.checkExists(namaRuangan);
            if (!checkExists){
                JOptionPane.showMessageDialog(frame, "Ruangan sudah terdaftar!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                roomModel.addRoom(namaRuangan, kapasitas);
                JOptionPane.showMessageDialog(frame, "Berhasil menambahkan ruangan!");
                frame.table.setModel(roomModel.setTableRoom());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }else if (source == frame.resetButton){
            frame.namaField.setText("");
            frame.kapasitasField.setText("");
        }else if (source == frame.deleteButton){
            int selectedRow = frame.table.getSelectedRow();
            if (selectedRow != -1){
                int result = JOptionPane.showConfirmDialog(frame, "Apakah anda yakin ingin menghapus ruangan ini?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION){
                    String idRuangan = frame.table.getValueAt(selectedRow, 0).toString();
                    roomModel.deleteRoom(idRuangan);
                    frame.table.setModel(roomModel.setTableRoom());
                }
            }else {
                JOptionPane.showMessageDialog(frame, "Pilih salah satu ruangan terlebih dahulu!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }else if (source == frame.editButton){
            int selectedRow = frame.table.getSelectedRow();
            if (selectedRow != -1) {
                String idRoom = frame.table.getValueAt(frame.table.getSelectedRow(), 0).toString();
                String currentNama  = frame.table.getValueAt(frame.table.getSelectedRow(), 1).toString();
                String currentKapasitas  = frame.table.getValueAt(frame.table.getSelectedRow(), 2).toString();

                String newNama = JOptionPane.showInputDialog(frame, "Edit Nama Ruangan:", currentNama);
                if (newNama == null) return;
                String newKapasitas = JOptionPane.showInputDialog(frame, "Edit Kapasitas:", currentKapasitas);
                if (newKapasitas == null) return;

                try {
                    int kapasitasInt = Integer.parseInt(newKapasitas);
                    roomModel.editRoom(idRoom, newNama, kapasitasInt);
                    frame.table.setModel(roomModel.setTableRoom());
                    JOptionPane.showMessageDialog(frame, "Data berhasil diperbarui!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Kapasitas harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }else {
                JOptionPane.showMessageDialog(frame, "Pilih salah satu ruangan terlebih dahulu!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
