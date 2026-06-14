package Controller;

import Model.*;
import View.EditStudentView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EditStudentController implements ActionListener {
    private EditStudentView frame;
    private AdminModel model;
    private StudentModel studentModel;
    public EditStudentController(EditStudentView frame, AdminModel model) {
        this.frame = frame;
        this.model = model;
        this.studentModel = new StudentModel(model.getConn());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == frame.updateButton) {
            String id = frame.idField.getText();
            String newNama = frame.namaField.getText();
            String newEmail = frame.emailField.getText();
            String newKota = frame.kotaField.getText();
            String newTelp = frame.telpField.getText();
            String newStatus = frame.statusCombo.getSelectedItem().toString();
            String newNamaOrtu = frame.namaOrtuField.getText();
            String newTelpOrtu = frame.telpOrtuField.getText();

            if (id.isEmpty() || newNama.isEmpty() || newEmail.isEmpty() || newKota.isEmpty() || newTelp.isEmpty() || newStatus.isEmpty() || newNamaOrtu.isEmpty() || newTelpOrtu.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Harap mengisi semua kolom!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if (frame.idLama.equals(id) && frame.namaLama.equals(newNama) && frame.emailLama.equals(newEmail) && frame.kotaLama.equals(newKota) &&
            frame.telpLama.equals(newTelp) && frame.statusLama.equals(newStatus) && frame.namaOrtuLama.equals(newNamaOrtu) && frame.telpOrtuLama.equals(newTelpOrtu)) {
                JOptionPane.showMessageDialog(frame, "Maaf, tidak ada perubahan dari data sebelumnya!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                studentModel.updateSiswa(id, newNama, newEmail, newKota, newTelp, newStatus, newNamaOrtu, newTelpOrtu);
                JOptionPane.showMessageDialog(frame, "Data siswa berhasil diupdate!");
                frame.c.updateTable();
                frame.dispose();
            }
        }

        if (source == frame.backButton) {
            frame.dispose();
        }
    }
}
