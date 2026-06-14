package View;

import Model.AdminModel;
import Model.CourseModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MateriView extends JFrame {
    public JTable materiTable;
    CourseModel courseModel;
    AdminModel model;

    public MateriView(AdminModel model, String gabungan, String id) {
        this.model = model;
        this.courseModel = new CourseModel(model.getConn());
        setTitle("Daftar Materi" );
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Materi untuk: " + gabungan, SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        materiTable = new JTable(courseModel.setMateriTable(id));
        JScrollPane scrollPane = new JScrollPane(materiTable);
        add(scrollPane, BorderLayout.CENTER);
    }

}
