package Controller;
import View.*;
import Model.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;

public class NavControllerAdmin implements MouseListener {
    AdminModel model;
    NavPanelAdmin panel;
    JFrame frame;
    private Connection conn;
    public NavControllerAdmin(Connection conn, NavPanelAdmin panel, JFrame frame, AdminModel model) {
        this.panel = panel;
        this.frame = frame;
        this.model = model;
        this.conn = conn;
        panel.scheduleBtn.addActionListener(e -> {
            HomeAdminFrame dashboardFrame = new HomeAdminFrame(model, new ScheduleModel(model.getConn()));
            dashboardFrame.setVisible(true);
            frame.dispose();
        });
        panel.tutorBtn.addActionListener(e -> {
            AdminTutorView tutorView = new AdminTutorView(model);
            tutorView.setVisible(true);
            frame.dispose();
        });
        panel.studentBtn.addActionListener(e -> {
            AdminStudentView studentView = new AdminStudentView(model);
            studentView.setVisible(true);
            frame.dispose();
        });
        panel.paymentBtn.addActionListener(e -> {
            AdminPaymentView adminPayment = new AdminPaymentView(model);
            adminPayment.setVisible(true);
            frame.dispose();
        });
        panel.coursesBtn.addActionListener(e -> {
            ManageCoursePage manageCourse = new ManageCoursePage(model);
            manageCourse.setVisible(true);
            frame.dispose();
        });
        panel.classBtn.addActionListener(e -> {
            ManageRoomPage manageRoom = new ManageRoomPage(model);
            manageRoom.setVisible(true);
            frame.dispose();
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
