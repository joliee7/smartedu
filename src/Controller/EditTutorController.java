package Controller;

import Model.*;
import View.EditTutorView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EditTutorController implements ActionListener {
     private EditTutorView frame;
     private AdminModel model;
     private TutorModel tutorModel;
     public EditTutorController(EditTutorView frame, AdminModel model) {
         this.frame = frame;
         this.model = model;
         this.tutorModel = new TutorModel(model.getConn());
     }

     @Override
     public void actionPerformed(ActionEvent e) {
         Object source = e.getSource();
         if (source == frame.jenjangCombo) {
             frame.updateMapelList();
         }

         if (source == frame.updateButton) {
             String id = frame.idField.getText();
             String newNama = frame.namaField.getText();
             String newEmail = frame.emailField.getText();
             String newKota = frame.kotaField.getText();
             String newTelp = frame.telpField.getText();
             String newRating = frame.ratingField.getText();
             String newJenjang = frame.jenjangCombo.getSelectedItem().toString();
             int newBiaya = 0;
             try {
                 newBiaya = Integer.parseInt(frame.biayaField.getText());
             } catch (NumberFormatException ex) {
                 JOptionPane.showMessageDialog(null, "Nama tidak valid");
             }
             List<String> newMapelList = frame.mapelList.getSelectedValuesList();
             String newStatus = frame.statusCombo.getSelectedItem().toString();

             if (id.isEmpty() || newNama.isEmpty() || newEmail.isEmpty() || newKota.isEmpty() || newTelp.isEmpty()
             || newRating.isEmpty() || newJenjang.isEmpty() || newMapelList.isEmpty() || newStatus.isEmpty() || newBiaya == 0) {
                 JOptionPane.showMessageDialog(frame, "Harap mengisi semua kolom!", "Error", JOptionPane.ERROR_MESSAGE);
             }
             else if (frame.idLama.equals(id) && frame.namaLama.equals(newNama) && frame.emailLama.equals(newEmail) && frame.kotaLama.equals(newKota)
             && frame.ratingLama.equals(newRating) && frame.mapelListLama.equals(newMapelList) && frame.statusLama.equals(newStatus) && frame.biayaLama == newBiaya
             && frame.jenjangLama.equals(newJenjang)) {
                 JOptionPane.showMessageDialog(frame, "Maaf, tidak ada perubahan dari data sebelumnya!", "Error", JOptionPane.ERROR_MESSAGE);
             }
             else {
                 tutorModel.updateTutor(id, newNama, newEmail, newKota, newTelp, newRating, newJenjang, newMapelList, newStatus);
                 JOptionPane.showMessageDialog(frame, "Data tutor berhasil diupdate!");
                 frame.dispose();
                 frame.controllerTutor.updateTable();
             }
         }

         if (source == frame.backButton) {
             frame.dispose();
         }
     }
}
