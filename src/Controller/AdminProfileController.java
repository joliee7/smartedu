package Controller;

import View.AdminProfileView;
import View.LoginFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminProfileController implements ActionListener {
    private AdminProfileView frame;
    public AdminProfileController(AdminProfileView frame){
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == frame.editButton){
            frame.nameField.setEditable(true);
            frame.emailField.setEditable(true);
            frame.passwordField.setEditable(true);
            frame.saveEditButton.setEnabled(true);
        }

        if (source == frame.saveEditButton) {
            String name = frame.nameField.getText();
            String email = frame.emailField.getText();
            if (!(email.contains("@") || email.contains("."))) {
                JOptionPane.showMessageDialog(frame, "Email tidak valid!");
                return;
            }
            String password = frame.passwordField.getPassword().toString();
            if (name == null || email == null || password == null) {
                JOptionPane.showMessageDialog(frame, "Harap mengisi semua data profil!");
            } else {
                frame.model.updateProfilAdmin(frame.model.getId_admin(), name, email, password);
                JOptionPane.showMessageDialog(frame, "Profil anda berhasil diperbarui!");
                frame.nameField.setEditable(false);
                frame.emailField.setEditable(false);
                frame.passwordField.setEditable(false);
                frame.saveEditButton.setEnabled(false);
            }
        }

        if (source == frame.logoutButton) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Apakah anda yakin ingin logout?");
            if (confirm == JOptionPane.YES_OPTION) {
                LoginFrame loginFrame = new LoginFrame(frame.model);
                loginFrame.setVisible(true);
                frame.dispose();
            }
        }
    }
}
