package View;
import Controller.NavControllerAdmin;
import Model.AdminModel;
import Model.ScheduleModel;
import Controller.HomeAdminController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class HomeAdminFrame extends JFrame {
    public JButton nextBtn = new JButton(">");
    public JButton prevBtn = new JButton("<");
    public JLabel monthLbl = new JLabel();

    // add schedule
    public JComboBox<String> tutorCb = new JComboBox<>();
    public JComboBox<String> timeCb = new JComboBox<>();
    public JComboBox<String> roomCb = new JComboBox<>();
    public JComboBox<String> mapelCb = new JComboBox<>();
    public JList<String> studentList = new JList<>();
    public JRadioButton onehourRb = new JRadioButton("1 hour");
    public JRadioButton onethirtyRb = new JRadioButton("1 hour 30 min");
    public JButton addBtn = new JButton("Tambah Jadwal");
    public JLabel girlMascot = new JLabel(" ");
    public ButtonGroup buttonGroup = new ButtonGroup();

    // Edit schedule
    public JComboBox<String> tutorCbEdit = new JComboBox<>();
    public JComboBox<String> timeCbEdit = new JComboBox<>();
    public JComboBox<String> roomCbEdit = new JComboBox<>();
    public JComboBox<String> mapelCbEdit = new JComboBox<>();
    public JLabel studentListLbl = new JLabel("");
    public JRadioButton onehourRbEdit = new JRadioButton("1 hour");
    public JRadioButton onethirtyRbEdit = new JRadioButton("1 hour 30 min");
    public JButton editBtn = new JButton("Edit Jadwal");
    public JButton deleteBtn = new JButton("Hapus Jadwal");
    public JLabel guyMascot = new JLabel("-");
    public JComboBox<String> dayCb = new JComboBox<>();
    public JComboBox<String> monthCb = new JComboBox<>();
    public JComboBox<String> yearCb = new JComboBox<>();

    public JTabbedPane tabbedPane;
    public JTable scheduleTable;
    public JButton[] dayButton = new JButton[31];
    public JPanel monthPanel;
    public ScheduleModel scheduleModel;
    AdminModel model;

    public HomeAdminFrame(AdminModel model, ScheduleModel scheduleModel) {
        this.model = model;
        this.scheduleModel = scheduleModel;
        this.setTitle("SmartEdu Admin Home Page");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1250, 650);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        Color lightBlue = new Color(138, 247, 245);
        Color blue = new Color(138, 194, 221);
        Color softBlue = Color.decode("#D9E8EE");
        Color navy = new Color(3, 83, 92);
        Color yellow = Color.decode("#FFFAAA");
        Color tosca = Color.decode("#C8F2EF");
        Color softGreen = Color.decode("#DFF1A8");

        // Top Panel
        HeaderPanel header = new HeaderPanel(model);

        // Navigation Panel
        NavPanelAdmin navPanel = new NavPanelAdmin();
        new NavControllerAdmin(model.getConn(), navPanel, this, model);

        // Calendar Panel
        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBackground(yellow);
        calendarPanel.setBorder(BorderFactory.createTitledBorder("Calendar"));
        JPanel headerCalendar = new JPanel(new BorderLayout());
        headerCalendar.setBackground(yellow);
        headerCalendar.add(prevBtn, BorderLayout.WEST);
        headerCalendar.add(monthLbl, BorderLayout.CENTER);
        headerCalendar.add(nextBtn, BorderLayout.EAST);
        monthLbl.setHorizontalAlignment(SwingConstants.CENTER);
        monthLbl.setFont(new Font("Arial", Font.BOLD, 14));
        monthLbl.setText(YearMonth.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + YearMonth.now().getYear());

        monthPanel = new JPanel(new GridLayout(7, 7, 5, 5));
        monthPanel.setBackground(yellow);

        calendarPanel.add(monthPanel, BorderLayout.CENTER);
        calendarPanel.add(headerCalendar, BorderLayout.NORTH);

        // Add Schedule Panel
        JPanel addSchedulePanel = new JPanel(new BorderLayout());
        addSchedulePanel.setBorder(BorderFactory.createTitledBorder(" "));
        addSchedulePanel.setBackground(tosca);
        JLabel addLbl = new JLabel("Tambah Jadwal Les", SwingConstants.CENTER);
        addLbl.setFont(new Font("Arial", Font.BOLD, 14));

        // radio btn
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        durationPanel.setBackground(tosca);
        onehourRb.setBackground(tosca);
        onethirtyRb.setBackground(tosca);
        durationPanel.add(onehourRb);
        durationPanel.add(onethirtyRb);

        buttonGroup.add(onehourRb);
        buttonGroup.add(onethirtyRb);
        onehourRb.setMargin(new Insets(0, 0, 0, 0));
        onethirtyRb.setMargin(new Insets(0, 0, 0, 0));

        // Student combo
        studentList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

        JPanel detailsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        detailsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        detailsPanel.setBackground(tosca);
        detailsPanel.add(new JLabel("Nama Tutor:"));
        detailsPanel.add(tutorCb);
        detailsPanel.add(new JLabel("Jam:"));
        detailsPanel.add(timeCb);
        detailsPanel.add(new JLabel("Ruangan:"));
        detailsPanel.add(roomCb);
        detailsPanel.add(new JLabel("Durasi:"));
        detailsPanel.add(durationPanel);
        detailsPanel.add(new JLabel("Mata Pelajaran:"));
        detailsPanel.add(mapelCb);
        addBtn.setPreferredSize(new Dimension(150, 30));

        JPanel siswaAddPanel = new JPanel();
        siswaAddPanel.setLayout(new BoxLayout(siswaAddPanel, BoxLayout.X_AXIS));
        siswaAddPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        siswaAddPanel.setBackground(tosca);
        siswaAddPanel.add(new JLabel("Siswa:           "));
        JScrollPane studentScroll = new JScrollPane(studentList);
        studentScroll.setPreferredSize(new Dimension(100, 60));
        siswaAddPanel.add(studentScroll);

        addPanel.add(detailsPanel);
        addPanel.add(siswaAddPanel);

        ImageIcon mascotGirl = new ImageIcon(new ImageIcon("images/maskot_cewe.png").getImage().
                getScaledInstance(180, 180, Image.SCALE_SMOOTH));
        girlMascot.setIcon(mascotGirl);

        JPanel addBtnPanel = new JPanel(new FlowLayout());
        addBtnPanel.setBackground(tosca);
        addBtnPanel.add(addBtn);

        addSchedulePanel.add(addLbl, BorderLayout.NORTH);
        addSchedulePanel.add(new JScrollPane(addPanel), BorderLayout.CENTER);
        addSchedulePanel.add(addBtnPanel, BorderLayout.SOUTH);
        addSchedulePanel.add(girlMascot, BorderLayout.WEST);

        //edit panel
        JPanel editPanel = new JPanel(new BorderLayout());
        editPanel.setBorder(BorderFactory.createTitledBorder(" "));
        editPanel.setBackground(tosca);
        JLabel editLbl = new JLabel("Edit Jadwal Les", SwingConstants.CENTER);
        editLbl.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel durationPanelEdit = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        durationPanelEdit.setBackground(tosca);
        onehourRbEdit.setBackground(tosca);
        onethirtyRbEdit.setBackground(tosca);
        durationPanelEdit.add(onehourRbEdit);
        durationPanelEdit.add(onethirtyRbEdit);
        ButtonGroup buttonGroupEdit = new ButtonGroup();
        buttonGroupEdit.add(onehourRbEdit);
        buttonGroupEdit.add(onethirtyRbEdit);
        onehourRbEdit.setMargin(new Insets(0, 0, 0, 0));
        onethirtyRbEdit.setMargin(new Insets(0, 0, 0, 0));

        for (int i = 1; i <= 31; i++) dayCb.addItem(String.valueOf(i));
        for (int i = 1; i <= 12; i++) monthCb.addItem(String.valueOf(i));
        for (int i = 2025; i <= 2026; i++) yearCb.addItem(String.valueOf(i));
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(dayCb); datePanel.add(monthCb); datePanel.add(yearCb);
        datePanel.setBackground(tosca);

        JPanel detailPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        detailPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        detailPanel.setBackground(tosca);
        detailPanel.add(new JLabel("Tanggal:"));
        detailPanel.add(datePanel);
        detailPanel.add(new JLabel("Nama Tutor:"));
        detailPanel.add(tutorCbEdit);
        detailPanel.add(new JLabel("Jam:"));
        detailPanel.add(timeCbEdit);
        detailPanel.add(new JLabel("Ruangan:"));
        detailPanel.add(roomCbEdit);
        detailPanel.add(new JLabel("Durasi:"));
        detailPanel.add(durationPanelEdit);
        detailPanel.add(new JLabel("Mata Pelajaran:"));
        detailPanel.add(mapelCbEdit);
        detailPanel.add(new JLabel("Siswa:"));
        detailPanel.add(studentListLbl);
        editBtn.setPreferredSize(new Dimension(150, 30));
        deleteBtn.setPreferredSize(new Dimension(150, 30));

        ImageIcon mascotGuy = new ImageIcon(new ImageIcon("images/maskot_cowo.png").getImage().
                getScaledInstance(180, 180, Image.SCALE_SMOOTH));
        guyMascot.setIcon(mascotGuy);

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(tosca);
        btnPanel.add(deleteBtn);
        btnPanel.add(editBtn);

        editPanel.add(editLbl, BorderLayout.NORTH);
        editPanel.add(new JScrollPane(detailPanel), BorderLayout.CENTER);
        editPanel.add(btnPanel, BorderLayout.SOUTH);
        editPanel.add(guyMascot, BorderLayout.WEST);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(tosca);
        tabbedPane.addTab("Add Schedule", addSchedulePanel);
        tabbedPane.addTab("Edit Schedule", editPanel);

        // Information Panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        scheduleTable = new JTable();
        adjustTable();

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Ringkasan Jadwal"));
        infoPanel.add(scrollPane, BorderLayout.CENTER);

        JSplitPane upSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, calendarPanel, tabbedPane);
        upSplit.setDividerLocation(350);
        upSplit.setBackground(Color.white);

        JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upSplit, infoPanel);
        mainPanel.setBackground(Color.white);
        mainPanel.setDividerLocation(300);
        mainPanel.setResizeWeight(0.3);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        this.add(header, BorderLayout.NORTH);
        this.add(navPanel, BorderLayout.WEST);
        this.add(mainPanel, BorderLayout.CENTER);

        new HomeAdminController(this);
    }

    public void refreshCalendar(YearMonth yearMonth) {
        monthPanel.removeAll();
        monthLbl.setText(yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + YearMonth.now().getYear());

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String dayName : days) {
            JLabel dayLbl = new JLabel(dayName, SwingConstants.CENTER);
            dayLbl.setFont(new Font("Arial", Font.BOLD, 12));
            monthPanel.add(dayLbl);
        }

        LocalDate firstOfMonth = yearMonth.atDay(1);

        int daysInMonth = yearMonth.lengthOfMonth();
        int shift = firstOfMonth.getDayOfWeek().getValue() % 7; //fill blank before day 1
        // firstOfMonth.getDayOfWeek() to know starting day of the week

        for (int i = 0; i < shift; i++) {
            monthPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            dayButton[day-1] = new JButton(String.valueOf(day));
            dayButton[day-1].setBackground(Color.white);
            dayButton[day-1].setBorder(BorderFactory.createLineBorder(new Color(3, 83, 92)));
            dayButton[day-1].setFocusPainted(false);
            dayButton[day-1].setFont(new Font("SansSerif", Font.PLAIN, 12));
            dayButton[day-1].setPreferredSize(new Dimension(40, 40));
            dayButton[day-1].setUI(new BasicButtonUI());
            monthPanel.add(dayButton[day-1]);
        }

        int totalCells = 42;
        int filledCells = shift + daysInMonth;
        for (int i = filledCells; i < totalCells; i++) {
            monthPanel.add(new JLabel(""));
        }
        monthPanel.revalidate();
        monthPanel.repaint();
    }

    public void adjustTable() {
        int[] columnWidths = {150, 150, 200, 150, 150, 400};
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < scheduleTable.getColumnCount(); i++) {
            TableColumn column = scheduleTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidths[i]);
            column.setCellRenderer(centerRenderer); // Center align
        }
        scheduleTable.setFillsViewportHeight(true); // Ensures background color fills full height
        scheduleTable.setRowHeight(25); // add space
        scheduleTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        scheduleTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
    }
}