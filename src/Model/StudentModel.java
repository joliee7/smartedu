package Model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class StudentModel {
    private Connection conn;
    private String[] idJenjang = {"J01", "J02", "J03", "J04", "J05", "J06", "J07", "J08", "J09", "J10", "J11", "J12"};
    private String[] namaJenjang = {"Kelas 1", "Kelas 2", "Kelas 3", "Kelas 4", "Kelas 5", "Kelas 6", "Kelas 7", "Kelas 8", "Kelas 9", "Kelas 10", "Kelas 11", "Kelas 12"};
    private ArrayList<String> namaMapel = new ArrayList<>();
    public StudentModel(Connection conn) {
        this.conn = conn;
    }

    public void addStudent(String idSiswa, String namaSis, String emailSis, String kotaSis,
                           String telpSis, String jenjangSis, String namaOrtu, String telpOrtu) {
        String idOrtu = generateIdOrtu();
        String idJenjang = getIdJenjangByNama(jenjangSis);
        try {
            String sql2 = "insert into orangtua (id_ortu, nama, telepon, status_del)\n" +
                    "values (?,?,?,0);";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, idOrtu);
            ps2.setString(2, namaOrtu);
            ps2.setString(3, telpOrtu);
            ps2.execute();

            String sql = "insert into siswa(id_siswa, id_ortu, id_jenjang, nama, email, kota, telepon, status_del)\n" +
                    "values (?,?,?,?,?,?,?,0)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idSiswa);
            ps.setString(2, idOrtu);
            ps.setString(3, idJenjang);
            ps.setString(4, namaSis);
            ps.setString(5, emailSis);
            ps.setString(6, kotaSis);
            ps.setString(7, telpSis);
            ps.execute();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSiswa(String id, String newNama, String newEmail, String newKota, String newTelp, String newStatus, String namaOrtu, String telpOrtu) {
        try {
            String sql = "update siswa set nama = ?, email = ?, kota = ?, telepon = ?, status_del = ?\n" +
                    "where id_siswa = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newNama);
            ps.setString(2, newEmail);
            ps.setString(3, newKota);
            ps.setString(4, newTelp);
            int del = 0;
            if (newStatus.equals("Aktif")) {
                del = 0;
            } else if (newStatus.equals("Tidak Aktif")) {
                del = 1;
            }
            ps.setInt(5, del);
            ps.setString(6, id);
            ps.executeUpdate();

            String idOrtu = "";
            String idTutorsql = "select id_ortu from siswa where id_siswa = ?";
            PreparedStatement ps2 = conn.prepareStatement(idTutorsql);
            ps2.setString(1, id);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                idOrtu = rs2.getString("id_ortu");
            }

            String sqlOrtu = "update orangtua set nama = ?, telepon  = ?\n" +
                    "where id_ortu = ?;";
            PreparedStatement ps3 = conn.prepareStatement(sqlOrtu);
            ps3.setString(1, namaOrtu);
            ps3.setString(2, telpOrtu);
            ps3.setString(3, idOrtu);
            ps3.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generatePasswordFromName(String nama) {
        String cleanName = "";
        for (int i = 0; i < nama.length(); i++) {
            char c = nama.charAt(i);
            if (c != ' ') {
                cleanName += Character.toLowerCase(c);
            }
        }
        String firstPart;
        if (cleanName.length() >= 5) {
            firstPart = cleanName.substring(0, 5);
        } else {
            firstPart = cleanName;
        }
        Random rand = new Random();
        int number = rand.nextInt(90) + 10;
        return firstPart + number;
    }

    public String getIdJenjangByNama(String namaJenjang) {
        String idJenjang = null;
        String angka = namaJenjang.substring(6);
        try {
            String sql = "SELECT id_jenjang FROM jenjang WHERE level = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, angka);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idJenjang = rs.getString("id_jenjang");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idJenjang;
    }

    public DefaultTableModel setTableStudentAdmin(JTable tableStudent, String jenjang, String status, String keyword) {
        String[] column = {"ID Siswa", "Nama", "Email", "Kota", "Telepon", "Mata Pelajaran", "Periode Akhir Les", "Status", "Periode Raw"};
        DefaultTableModel model = new DefaultTableModel(column, 0);

        try {
            String sql = "SELECT s.id_siswa, s.nama, s.email, s.kota, s.telepon, m.nama_mapel, " +
                    "t.periode_akhir, DATE_FORMAT(t.periode_akhir, '%e %M %Y') AS periode, " +
                    "IF(s.status_del = 0, 'Aktif', 'Tidak Aktif') AS status " +
                    "FROM siswa s " +
                    "JOIN transaksi t ON s.id_siswa = t.id_siswa " +
                    "JOIN detail_transaksi d ON t.id_transaksi = d.id_transaksi " +
                    "JOIN mapel m ON d.id_mapel = m.id_mapel " +
                    "JOIN jenjang j ON m.id_jenjang = j.id_jenjang " +
                    "WHERE 1=1";

            String jenjangID = "";
            if (!jenjang.equals("All")) {
                int index = 0;
                for (int i = 0; i < namaJenjang.length; i++) {
                    if (jenjang.equals(namaJenjang[i])) {
                        index = i;
                        break;
                    }
                }
                jenjangID = idJenjang[index];
                sql += " AND j.id_jenjang = ?";
            }

            if (!status.equals("All")) {
                if (status.equals("Aktif")) {
                    sql += " AND s.status_del = 0";
                } else if (status.equals("Tidak Aktif")) {
                    sql += " AND s.status_del = 1";
                }
            }

            if (keyword != null && !keyword.isEmpty()) {
                sql += " AND LOWER(s.nama) LIKE ?";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            int paramIndex = 1;
            if (!jenjang.equals("All")) {
                ps.setString(paramIndex++, jenjangID);
            }
            if (keyword != null && !keyword.isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword.toLowerCase() + "%");
            }

            ResultSet rs = ps.executeQuery();
            HashMap<String, Object[]> dataMap = new HashMap<>();

            while (rs.next()) {
                String id = rs.getString("id_siswa");
                String mapelName = rs.getString("nama_mapel");
                String formattedPeriode = rs.getString("periode");
                Date rawPeriode = rs.getDate("periode_akhir");

                if (dataMap.containsKey(id)) {
                    Object[] row = dataMap.get(id);
                    row[5] = row[5] + ", " + mapelName;
                } else {
                    Object[] row = new Object[]{
                            rs.getString("id_siswa"),
                            rs.getString("nama"),
                            rs.getString("email"),
                            rs.getString("kota"),
                            rs.getString("telepon"),
                            mapelName,
                            formattedPeriode,
                            rs.getString("status"),
                            rawPeriode
                    };
                    dataMap.put(id, row);
                }
            }

            for (Object[] row : dataMap.values()) {
                model.addRow(row);
            }

            tableStudent.setModel(model);

            tableStudent.getColumnModel().getColumn(8).setMinWidth(0);
            tableStudent.getColumnModel().getColumn(8).setMaxWidth(0);
            tableStudent.getColumnModel().getColumn(8).setWidth(0);

            tableStudent.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setForeground(Color.BLACK);

                    if (column == 6) {
                        Object rawObj = table.getModel().getValueAt(row, 8);
                        if (rawObj instanceof Date) {
                            Date periodeAkhir = (Date) rawObj;
                            Date today = new Date(System.currentTimeMillis());
                            long diff = periodeAkhir.getTime() - today.getTime();
                            long diffDays = diff / (1000 * 60 * 60 * 24);

                            if (diffDays < 0) {
                                c.setForeground(Color.RED);
                            } else if (diffDays <= 7) {
                                c.setForeground(Color.ORANGE);
                            }
                        }
                    }

                    if (column == 7) {
                        Object statusObj = table.getModel().getValueAt(row, 7);
                        if (statusObj instanceof String) {
                            String status = (String) statusObj;
                            if (status.equalsIgnoreCase("Aktif")) {
                                c.setForeground(new Color(0, 128, 0));
                            } else {
                                c.setForeground(Color.RED);
                            }
                        }
                    }

                    return c;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saat load data siswa: " + e.getMessage());
        }
        return model;
    }


    public String generateIdSiswa(String nama) {
        String hurufDepan = nama.trim().substring(0, 1).toUpperCase();
        String newId = "";
        try {
            String sql = "select id_siswa from siswa where id_siswa like ? order by id_siswa desc";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, hurufDepan + "%");
            ResultSet rs = ps.executeQuery();

            int nomorTerakhir = 0;
            if (rs.next()) {
                String lastId = rs.getString("id_siswa");
                String angka = lastId.substring(1);
                nomorTerakhir = Integer.parseInt(angka);
            }
            int nomorBaru = nomorTerakhir + 1;
            String formattedNumber;
            if (nomorBaru < 10) {
                formattedNumber = "00" + nomorBaru;
            } else if (nomorBaru < 100){
                formattedNumber = "0" + nomorBaru;
            } else {
                formattedNumber = String.valueOf(nomorBaru);
            }
            newId = hurufDepan + formattedNumber;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newId;
    }

    public String getJenjangBySiswa(String idSiswa) {
        String jenjang = "";
        try {
            String sql = "select concat('Kelas ', j.level) as level\n" +
                    "from siswa s\n" +
                    "join jenjang j on s.id_jenjang = j.id_jenjang\n" +
                    "where id_siswa = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idSiswa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jenjang = rs.getString("level");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jenjang;
    }

    public String[] getDataOrtuByStudent(String idSiswa) {
        String[] dataOrtu = new String[2];
        try {
            String sql = "select o.nama, o.telepon\n" +
                    "from orangtua o\n" +
                    "join siswa s on o.id_ortu = s.id_ortu\n" +
                    "where s.id_siswa = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idSiswa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dataOrtu[0] = rs.getString("nama");
                dataOrtu[1] = rs.getString("telepon");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataOrtu;
    }



    public String getIdMapelByNama(String namaMapel) {
        String idMapel = null;
        try {
            String sql = "SELECT id_mapel FROM mapel WHERE nama_mapel = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaMapel);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idMapel = rs.getString("id_mapel");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idMapel;
    }

    public String generateIdOrtu() {
        String lastId = null;
        try {
            String query = "SELECT id_ortu FROM orangtua ORDER BY id_ortu DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lastId = rs.getString("id_ortu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int nextNumber = 1;
        if (lastId != null && lastId.startsWith("OT")) {
            String numberPart = lastId.substring(2);
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }
        return String.format("OT%03d", nextNumber);
    }

    public void softDeleteSiswa(String idSiswa) {
        try {
            String sql = "update siswa set status_del = 1\n" +
                    "where id_siswa = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idSiswa);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object[] getDetailPembayaranSiswa(String siswaNama) {
        String sql = "SELECT t.tanggal, t.id_siswa, s.nama, t.nominal, t.metode_bayar, t.periode_mulai, t.periode_akhir " +
                "FROM transaksi t JOIN siswa s ON t.id_siswa = s.id_siswa WHERE s.nama = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, siswaNama);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Object[]{
                        rs.getDate(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getDate(6),
                        rs.getDate(7)
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String[] getNamaJenjang(){
        return namaJenjang;
    }


    public ArrayList<String> getNamaMapel() {
        return namaMapel;
    }
}
