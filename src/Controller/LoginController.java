package Controller;
import Model.ScheduleModel;
import View.LoginFrame;
import View.HomeAdminFrame;

import javax.swing.*;

public class LoginController {
    LoginFrame frame;
    public LoginController(LoginFrame frame) {
        this.frame = frame;

        frame.loginBtn.addActionListener(e -> {
            String email = frame.emailField.getText();
            String password = frame.passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Email dan password harus diisi",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Boolean checkAdmin = frame.model.checkAdmin(email, password);
            if (checkAdmin) {
                HomeAdminFrame homeAdminFrame = new HomeAdminFrame(frame.model, new ScheduleModel(frame.model.getConn()));
                homeAdminFrame.setVisible(true);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Email atau password invalid",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
