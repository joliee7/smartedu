package Model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ScheduleModel {
    private Connection conn;

    public ScheduleModel(Connection conn) {
        this.conn = conn;
    }
    public String getIdTutor(String nama) {
        String id_tutor = "";
        try {
            String sql = "select id_tutor from tutor where nama = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nama);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id_tutor = rs.getString("id_tutor");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id_tutor;
    }

    public String getIdMapel(String mapel) {
        String id = "";
        try {
            String sql = "select id_mapel from mapel where nama_mapel = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mapel);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getString("id_mapel");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public String getIdRuangan(String ruangan) {
        String id = "";
        try {
            String sql = "select id_ruangan from ruangan where nama_ruangan = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ruangan);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getString("id_ruangan");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public String getIdSiswa(String siswa) {
        String id = "";
        try {
            String sql = "select id_siswa from siswa where nama like ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, siswa + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getString("id_siswa");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public DefaultTableModel getModel(String selectedDate) {
        String[] columns = {"Jam", "Tutor", "Mata Pelajaran","Ruangan", "Durasi", "Siswa"};
        DefaultTableModel scheduleModel = new DefaultTableModel(columns, 0);
        if (selectedDate.equals("")) {
            scheduleModel.setRowCount(0);
            return scheduleModel;
        }
        try {
            String sql = "select d.id_detail_les, d.jam_mulai, d.jam_berakhir, r.nama_ruangan, t.nama, m.nama_mapel, " +
                    "timediff(d.jam_berakhir,d.jam_mulai) as durasi from detail_les d " +
                    "left join tutor t on d.id_tutor = t.id_tutor left join mapel m on d.id_mapel = m.id_mapel " +
                    "left join ruangan r on d.id_ruangan = r.id_ruangan where d.tanggal = ? && d.status_del = 0 order by d.jam_mulai";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, selectedDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String jam = rs.getString("jam_mulai") + " - " + rs.getString("jam_berakhir");
                String tutor = rs.getString("nama");
                String ruangan = rs.getString("nama_ruangan");
                String mapel = rs.getString("nama_mapel");
                String durasi = rs.getString("durasi").substring(0,5);
                String id_detailLes = rs.getString("id_detail_les");

                String siswa = "";
                String getSiswa = "select s.nama from siswa_detail_les d " +
                        "left join siswa s on s.id_siswa = d.id_siswa where d.id_detail_les = ? && s.status_del = 0";
                PreparedStatement ps = conn.prepareStatement(getSiswa);
                ps.setString(1, id_detailLes);
                ResultSet rs2 = ps.executeQuery();
                while (rs2.next()) {
                    if (!siswa.isEmpty()) {
                        siswa += ", ";
                    }
                    String namaLengkap = rs2.getString("nama");
                    String[] nama = namaLengkap.split(" ");
                    siswa += nama[0] + " " + nama[1].substring(0,1);
                }
                scheduleModel.addRow(new Object[]{jam, tutor, mapel, ruangan, durasi, siswa});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleModel;
    }

    public DefaultComboBoxModel<String> loadTutorCb() {
        DefaultComboBoxModel<String> tutorCbModel = new DefaultComboBoxModel<>();
        try {
            String sql = "select nama from tutor where status_del = 0";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tutorCbModel.addElement(rs.getString("nama"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tutorCbModel;
    }

    public DefaultComboBoxModel<String> loadJamCb() {
        DefaultComboBoxModel<String> jamCbModel = new DefaultComboBoxModel<>();
        try {
            String sql = "select distinct jam_mulai from detail_les order by 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                jamCbModel.addElement(rs.getString("jam_mulai"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jamCbModel;
    }

    public DefaultComboBoxModel<String> loadRoomCb() {
        DefaultComboBoxModel<String> roomCbModel = new DefaultComboBoxModel<>();
        try {
            String sql = "select nama_ruangan from ruangan where status_del = 0 order by id_ruangan";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roomCbModel.addElement(rs.getString("nama_ruangan"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomCbModel;
    }

    public DefaultComboBoxModel<String> loadMapelCb(String id_tutor) {
        DefaultComboBoxModel<String> mapelCbModel = new DefaultComboBoxModel<>();
        try {
            String sql = "select m.nama_mapel from biaya_tutor b left join mapel m on b.id_mapel = m.id_mapel where b.id_tutor = ? && b.status_del = 0";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id_tutor);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                mapelCbModel.addElement(rs.getString("nama_mapel"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapelCbModel;
    }

    public DefaultListModel<String> loadStudentList(String id_tutor) {
        DefaultListModel<String> studentListModel = new DefaultListModel<>();
        try {
            String sql = "select s.nama from detail_transaksi d left join transaksi t on d.id_transaksi= t.id_transaksi " +
                    "left join siswa s on t.id_siswa = s.id_siswa where d.id_tutor = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id_tutor);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                studentListModel.addElement(rs.getString("nama"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentListModel;
    }

    public void addSchedule(String newId, String id_tutor, String id_mapel, String id_ruangan, String jam_mulai,
                            String jam_berakhir, String tanggal) {
        String query = "INSERT INTO detail_les (id_detail_les, id_tutor, id_mapel, id_ruangan, jam_mulai, " +
                "jam_berakhir, tanggal, keterangan, status_les, status_del) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newId);
            stmt.setString(2, id_tutor);
            stmt.setString(3, id_mapel);
            stmt.setString(4, id_ruangan);
            stmt.setTime(5, Time.valueOf(jam_mulai));
            stmt.setTime(6, Time.valueOf(jam_berakhir));
            stmt.setDate(7, Date.valueOf(tanggal));
            stmt.setString(8, "");
            stmt.setString(9, "0");
            stmt.setString(10, "0");
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSiswaDetailLes(String id_detail_les, String id_siswa) {
        String query = "INSERT INTO siswa_detail_les (id_detail_les, id_siswa, status_hadir, catatan, status_del) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id_detail_les);
            stmt.setString(2, id_siswa);
            stmt.setString(3, "1");
            stmt.setString(4, "");
            stmt.setString(5, "0");
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNewDetailLesId() {
        String newId = "";
        try {
            String query = "SELECT id_detail_les FROM detail_les ORDER BY id_detail_les DESC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("id_detail_les");
                int lastIdInt = Integer.parseInt(lastId.substring(5));
                int nextId = lastIdInt + 1;
                if (nextId < 100) {
                    newId = "DL25" + "00" + nextId;
                } else if (nextId < 1000) {
                    newId = "DL25" + "0" + nextId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newId;
    }

    public void editSchedule(String detail_les, String id_tutor, String id_mapel, String id_ruangan, String jam_mulai,
                             String jam_berakhir, String tanggal) {
        String query = "update detail_les set id_tutor = ?, id_mapel = ?, id_ruangan = ?, jam_mulai = ?, jam_berakhir = ?, tanggal = ? where id_detail_les = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id_tutor);
            stmt.setString(2, id_mapel);
            stmt.setString(3, id_ruangan);
            stmt.setTime(4, Time.valueOf(jam_mulai));
            stmt.setTime(5, Time.valueOf(jam_berakhir));
            stmt.setDate(6, Date.valueOf(tanggal));
            stmt.setString(7, detail_les);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSchedule(String detail_les) {
        String query = "update detail_les set status_del = 1 where id_detail_les = ?";
        String sql = "update siswa_detail_les set status_del = 1 where id_detail_les = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, detail_les);
            stmt.executeUpdate();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, detail_les);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkTutorAvailability(String id_tutor, String tanggal, String jam_mulai, String jam_akhir, String id_detailLes) {
        boolean available = false;
        try {
            String sql = "SELECT * FROM detail_les " +
                    "WHERE id_tutor = ? AND tanggal = ? AND status_del = 0 AND id_detail_les != ? " +
                    "AND NOT (jam_berakhir <= ? OR jam_mulai >= ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id_tutor);
            stmt.setDate(2, Date.valueOf(tanggal));
            stmt.setString(3, id_detailLes);
            stmt.setTime(4, Time.valueOf(jam_mulai));
            stmt.setTime(5, Time.valueOf(jam_akhir));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                available = false;
            } else {
                available = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return available;
    }

    public boolean checkRoomAvailability(String id_ruangan, String tanggal, String jam_mulai, String jam_akhir, String id_detailLes) {
        boolean available = false;
        try {
            String sql = "SELECT * FROM detail_les " +
                    "WHERE id_ruangan = ? AND tanggal = ? AND status_del = 0 AND id_detail_les != ? " +
                    "AND NOT (jam_berakhir <= ? OR jam_mulai >= ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id_ruangan);
            stmt.setDate(2, Date.valueOf(tanggal));
            stmt.setString(3, id_detailLes);
            stmt.setTime(4, Time.valueOf(jam_mulai));
            stmt.setTime(5, Time.valueOf(jam_akhir));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                available = false;
            } else {
                available = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return available;
    }

    public boolean checkStudentAvailability(String id_siswa, String tanggal, String jam_mulai, String jam_akhir, String id_detailLes) {
        boolean available = false;
        try {
            String sql = "SELECT dl.* FROM siswa_detail_les sdl " +
                    "JOIN detail_les dl ON sdl.id_detail_les = dl.id_detail_les " +
                    "WHERE sdl.id_siswa = ? AND dl.tanggal = ? AND dl.status_del = 0 " +
                    "AND sdl.status_del = 0 AND dl.id_detail_les != ? " +
                    "AND NOT (dl.jam_berakhir <= ? OR dl.jam_mulai >= ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id_siswa);
            stmt.setDate(2, Date.valueOf(tanggal));
            stmt.setString(3, id_detailLes);
            stmt.setTime(4, Time.valueOf(jam_mulai));
            stmt.setTime(5, Time.valueOf(jam_akhir));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                available = false;
            } else {
                available = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return available;
    }

    public String getDetailLesId(String tutorName, String tanggal, String jamMulai) {
        String id = "";
        try {
            String sql = "SELECT id_detail_les FROM detail_les " +
                    "WHERE id_tutor = ? AND tanggal = ? AND jam_mulai = ? AND status_del = 0";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, getIdTutor(tutorName));
            stmt.setDate(2, Date.valueOf(tanggal));
            stmt.setTime(3, Time.valueOf(jamMulai));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getString("id_detail_les");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
