package Controller;
import View.HomeAdminFrame;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class HomeAdminController {
    HomeAdminFrame frame;
    YearMonth currentYearMonth = YearMonth.now();
    String selectedDate = "";
    JButton previouslySelectedButton = null;
    String idDetailLes = "";
    String listSiswa = "";
    public HomeAdminController(HomeAdminFrame frame) {
        this.frame = frame;
        frame.refreshCalendar(currentYearMonth);
        attachListener();
        frame.scheduleTable.setModel(frame.scheduleModel.getModel(selectedDate));
        frame.adjustTable();

        // Combo
        frame.tutorCb.setModel(frame.scheduleModel.loadTutorCb());
        frame.tutorCbEdit.setModel(frame.scheduleModel.loadTutorCb());
        frame.timeCb.setModel(frame.scheduleModel.loadJamCb());
        frame.timeCbEdit.setModel(frame.scheduleModel.loadJamCb());
        frame.roomCb.setModel(frame.scheduleModel.loadRoomCb());
        frame.roomCbEdit.setModel(frame.scheduleModel.loadRoomCb());

        // Add tab
        String id_tutor = frame.scheduleModel.getIdTutor(frame.tutorCb.getSelectedItem().toString());
        frame.mapelCb.setModel(frame.scheduleModel.loadMapelCb(id_tutor));
        frame.studentList.setModel(frame.scheduleModel.loadStudentList(id_tutor));

        // Edit tab
        String id_tutor_edit = frame.scheduleModel.getIdTutor(frame.tutorCb.getSelectedItem().toString());
        frame.mapelCbEdit.setModel(frame.scheduleModel.loadMapelCb(id_tutor_edit));

        frame.nextBtn.addActionListener(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            frame.refreshCalendar(currentYearMonth);
            attachListener();
        });
        frame.prevBtn.addActionListener(e -> {
            if (!Objects.equals(currentYearMonth, YearMonth.now())) {
                currentYearMonth = currentYearMonth.minusMonths(1);
                frame.refreshCalendar(currentYearMonth);
                attachListener();
            } else {
                JOptionPane.showMessageDialog(frame, "You can't see past schedule");
            }
        });

        // Add
        frame.tutorCb.addActionListener(e -> {
            String selectedTutor = frame.tutorCb.getSelectedItem().toString();
            String newIdTutor = frame.scheduleModel.getIdTutor(selectedTutor);
            frame.mapelCb.setModel(frame.scheduleModel.loadMapelCb(newIdTutor));
            frame.studentList.setModel(frame.scheduleModel.loadStudentList(newIdTutor));
        });
        frame.addBtn.addActionListener(e -> {
            boolean canAdd = true;
            if (selectedDate.equals("")) {
                JOptionPane.showMessageDialog(frame, "Pilih Tanggal pada kalender", "Error", JOptionPane.ERROR_MESSAGE);
            }
            String tutorID = frame.scheduleModel.getIdTutor(frame.tutorCb.getSelectedItem().toString());
            String jam_mulai = frame.timeCb.getSelectedItem().toString();
            int durasi_jam = 0;
            int durasi_menit = 0;
            if (frame.onehourRb.isSelected()) {
                durasi_jam = 1;
            } else if (frame.onethirtyRb.isSelected()) {
                durasi_jam = 1;
                durasi_menit = 30;
            } else {
                JOptionPane.showMessageDialog(frame, "Pilih Durasi", "Error", JOptionPane.ERROR_MESSAGE);
                canAdd = false;
                return;
            }
            Duration durasi = Duration.ofHours(durasi_jam).plusMinutes(durasi_menit);
            LocalTime jamMulai = LocalTime.parse(jam_mulai);
            LocalTime jamAkhir = jamMulai.plus(durasi);
            String jamBerakhirStr = jamAkhir.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String idRuangan = frame.scheduleModel.getIdRuangan(frame.roomCb.getSelectedItem().toString());
            String idMapel = frame.scheduleModel.getIdMapel(frame.mapelCb.getSelectedItem().toString());
            String siswaList = frame.studentList.getSelectedValuesList().toString();
            System.out.println("Siswa: " + siswaList);
            if (siswaList.equals("[]")) {
                JOptionPane.showMessageDialog(frame,"Pilih siswa", "Error", JOptionPane.ERROR_MESSAGE);
                canAdd = false;
                return;
            }
            String siswa = siswaList.substring(1, siswaList.length()-2);
            String[] nama_siswa = siswa.split(", ");
            String[] id_siswa = new String[nama_siswa.length];
            for (int i = 0; i < nama_siswa.length; i++) {
                id_siswa[i] = frame.scheduleModel.getIdSiswa(nama_siswa[i]);
                if (!frame.scheduleModel.checkStudentAvailability(id_siswa[i], selectedDate, jam_mulai, jamBerakhirStr, "")) {
                    JOptionPane.showMessageDialog(frame, nama_siswa[i] + " berhalangan", "Error", JOptionPane.ERROR_MESSAGE);
                    canAdd = false;
                    break;
                }
            }
            if (!frame.scheduleModel.checkTutorAvailability(tutorID, selectedDate, jam_mulai, jamBerakhirStr, "")) {
                JOptionPane.showMessageDialog(frame, "Tutor berhalangan", "Error", JOptionPane.ERROR_MESSAGE);
                canAdd = false;
            }
            if (!frame.scheduleModel.checkRoomAvailability(idRuangan, selectedDate, jam_mulai, jamBerakhirStr, "")) {
                JOptionPane.showMessageDialog(frame, "Ruangan sudah digunakan", "Error", JOptionPane.ERROR_MESSAGE);
                canAdd = false;
            }
            if (canAdd) {
                String newId = frame.scheduleModel.getNewDetailLesId();
                System.out.println(newId);
                frame.scheduleModel.addSchedule(newId, tutorID, idMapel, idRuangan,jam_mulai,jamBerakhirStr,selectedDate);
                for (int i = 0; i < nama_siswa.length; i++) {
                    frame.scheduleModel.addSiswaDetailLes(newId, id_siswa[i]);
                }
                frame.scheduleTable.setModel(frame.scheduleModel.getModel(selectedDate));
                frame.adjustTable();
                frame.studentList.clearSelection();
                frame.buttonGroup.clearSelection();
                JOptionPane.showMessageDialog(frame, "Jadwal sudah ditambah", "Added", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        // Edit
        frame.tutorCbEdit.addActionListener(e -> {
            String selectedTutorEdit = frame.tutorCbEdit.getSelectedItem().toString();
            String newIdTutorEdit = frame.scheduleModel.getIdTutor(selectedTutorEdit);
            frame.mapelCbEdit.setModel(frame.scheduleModel.loadMapelCb(newIdTutorEdit));
        });
        frame.scheduleTable.getSelectionModel().addListSelectionListener(event -> {
            idDetailLes = "";
            if (!event.getValueIsAdjusting()) {
                int selectedRow = frame.scheduleTable.getSelectedRow();
                if (selectedRow != -1) {
                    idDetailLes = setEditTabFromTable(selectedRow);
                }
            }
        });
        frame.deleteBtn.addActionListener(e -> {
            if (idDetailLes.equals("")) {
                JOptionPane.showMessageDialog(frame, "Tidak ada baris yang dipilih", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int confirm = JOptionPane.showConfirmDialog(frame, "Apakah Anda yakin akan menghapus jadwal ini?",
                        "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    frame.scheduleModel.deleteSchedule(idDetailLes);
                    frame.scheduleTable.setModel(frame.scheduleModel.getModel(selectedDate));
                    frame.adjustTable();
                    JOptionPane.showMessageDialog(frame, "Jadwal sudah dihapus", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        frame.editBtn.addActionListener(e -> {
            if (idDetailLes.equals("")) {
                JOptionPane.showMessageDialog(frame, "Tidak ada baris yang dipilih", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean canEdit = true;
                String tutorID = frame.scheduleModel.getIdTutor(frame.tutorCbEdit.getSelectedItem().toString());
                String jam_mulai = frame.timeCbEdit.getSelectedItem().toString();
                int durasi_jam = 0;
                int durasi_menit = 0;
                if (frame.onehourRbEdit.isSelected()) {
                    durasi_jam = 1;
                } else if (frame.onethirtyRbEdit.isSelected()) {
                    durasi_jam = 1;
                    durasi_menit = 30;
                }
                Duration durasi = Duration.ofHours(durasi_jam).plusMinutes(durasi_menit);
                LocalTime jamMulai = LocalTime.parse(jam_mulai);
                LocalTime jamAkhir = jamMulai.plus(durasi);
                String jamBerakhirStr = jamAkhir.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                String idRuangan = frame.scheduleModel.getIdRuangan(frame.roomCbEdit.getSelectedItem().toString());
                String idMapel = frame.scheduleModel.getIdMapel(frame.mapelCbEdit.getSelectedItem().toString());
                String[] nama_siswa = listSiswa.split(", ");
                String[] id_siswa = new String[nama_siswa.length];
                for (int i = 0; i < nama_siswa.length; i++) {
                    id_siswa[i] = frame.scheduleModel.getIdSiswa(nama_siswa[i]);
                    if (!frame.scheduleModel.checkStudentAvailability(id_siswa[i], selectedDate, jam_mulai, jamBerakhirStr, idDetailLes)) {
                        JOptionPane.showMessageDialog(frame, nama_siswa[i] + " berhalangan", "Error", JOptionPane.ERROR_MESSAGE);
                        canEdit = false;
                        break;
                    }
                }
                if (!frame.scheduleModel.checkTutorAvailability(tutorID, selectedDate, jam_mulai, jamBerakhirStr, idDetailLes)) {
                    JOptionPane.showMessageDialog(frame, "Tutor berhalangan", "Error", JOptionPane.ERROR_MESSAGE);
                    canEdit = false;
                }
                if (!frame.scheduleModel.checkRoomAvailability(idRuangan, selectedDate, jam_mulai, jamBerakhirStr, idDetailLes)) {
                    JOptionPane.showMessageDialog(frame, "Ruangan sudah digunakan", "Error", JOptionPane.ERROR_MESSAGE);
                    canEdit = false;
                }
                String newDate = frame.yearCb.getSelectedItem() + "-" +
                        String.format("%02d", Integer.parseInt(frame.monthCb.getSelectedItem().toString())) + "-" +
                        String.format("%02d", Integer.parseInt(frame.dayCb.getSelectedItem().toString()));
                if (LocalDate.parse(newDate).isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(frame, "Tanggal tidak boleh sebelum hari ini!",
                            "Invalid Date", JOptionPane.ERROR_MESSAGE);
                    canEdit = false;
                    return;
                }
                if (canEdit) {
                    frame.scheduleModel.editSchedule(idDetailLes, tutorID,idMapel,idRuangan,jam_mulai,jamBerakhirStr,newDate);
                    frame.scheduleTable.setModel(frame.scheduleModel.getModel(newDate));
                    frame.adjustTable();
                    JOptionPane.showMessageDialog(frame, "Jadwal sudah disesuaikan", "Edited", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
    private void attachListener() {
        for (int day = 1; day <= currentYearMonth.lengthOfMonth(); day++) {
            final int d = day; // new final variable
            // (will always use the final value of day after the loop ends)
            frame.dayButton[day-1].addActionListener(e -> {
                LocalDate selectedDatee = currentYearMonth.atDay(d);
                String date = selectedDatee.toString();

                JButton clickedButton = frame.dayButton[d - 1];

                if (date.equals(selectedDate)) {
                    // Unselect if already selected
                    clickedButton.setBackground(Color.white);
                    previouslySelectedButton = null;
                    selectedDate = "";
                    frame.scheduleTable.setModel(frame.scheduleModel.getModel(selectedDate));
                    frame.adjustTable();
                } else {
                    // Unselect previous selection
                    if (previouslySelectedButton != null) {
                        previouslySelectedButton.setBackground(Color.white);
                    }

                    // Select current
                    clickedButton.setBackground(new Color(168, 255, 120));
                    selectedDate = date;
                    previouslySelectedButton = clickedButton;
                    frame.scheduleTable.setModel(frame.scheduleModel.getModel(selectedDate));
                    frame.adjustTable();
                    System.out.println(selectedDate);
                }
            });
        }
    }

    private String setEditTabFromTable(int row) {
        // also return id_detail_les
        frame.tabbedPane.setSelectedIndex(1);  // tab 1 = Edit tab

        LocalDate chosenDate = LocalDate.parse(selectedDate);
        frame.dayCb.setSelectedItem(String.valueOf(chosenDate.getDayOfMonth()));
        frame.monthCb.setSelectedItem(String.valueOf(chosenDate.getMonthValue()));
        frame.yearCb.setSelectedItem(String.valueOf(chosenDate.getYear()));

        String jadwal = frame.scheduleTable.getValueAt(row, 0).toString();
        String tutorName = frame.scheduleTable.getValueAt(row, 1).toString().trim();
        String mapel = frame.scheduleTable.getValueAt(row, 2).toString();
        String ruangan = frame.scheduleTable.getValueAt(row, 3).toString();
        String durasi = frame.scheduleTable.getValueAt(row, 4).toString();
        listSiswa = frame.scheduleTable.getValueAt(row, 5).toString();

        String[] jam = jadwal.split(" - ");
        String jamMulai = jam[0];
        String id_detail_les = frame.scheduleModel.getDetailLesId(tutorName, selectedDate, jamMulai);

        frame.tutorCbEdit.setSelectedItem(tutorName);
        frame.roomCbEdit.setSelectedItem(ruangan);
        frame.timeCbEdit.setSelectedItem(jamMulai);

        // MapelCb sama terhubung sama tutor, jadi perlu refresh dulu
        String idTutor = frame.scheduleModel.getIdTutor(tutorName);
        frame.mapelCbEdit.setModel(frame.scheduleModel.loadMapelCb(idTutor));
        frame.mapelCbEdit.setSelectedItem(mapel);

        if (durasi.equals("01:00")) {
            frame.onehourRbEdit.setSelected(true);
            frame.onethirtyRbEdit.setSelected(false);
        } else if (durasi.equals("01:30")) {
            frame.onethirtyRbEdit.setSelected(true);
            frame.onehourRb.setSelected(false);
        }

        frame.studentListLbl.setText(listSiswa);
        return id_detail_les;
    }
}
