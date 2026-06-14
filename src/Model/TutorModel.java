package Model;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TutorModel {
    private Connection conn;
    private String[] idJenjang = {"J01", "J02", "J03", "J04", "J05", "J06", "J07", "J08", "J09", "J10", "J11", "J12"};
    private String[] namaJenjang = {"Kelas 1", "Kelas 2", "Kelas 3", "Kelas 4", "Kelas 5", "Kelas 6", "Kelas 7", "Kelas 8", "Kelas 9", "Kelas 10", "Kelas 11", "Kelas 12"};
    private ArrayList<String> namaMapel = new ArrayList<>();
    public TutorModel(Connection conn) {

        this.conn = conn;
    }

    public void setNewTutor(String id, String name, String email, String kota, String telepon, List<String> listMapel, String jenjang, int biaya) {
        try {
            String sql = "insert into tutor\n" +
                    "values (?, ?, ?, ?, ?, 5, 0);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, kota);
            ps.setString(5, telepon);
            ps.executeUpdate();

            int index = 0;
            for (int i = 0; i < namaJenjang.length; i++) {
                if (jenjang.equals(namaJenjang[i])) {
                    index = i;
                    break;
                }
            }
            String jenjangID = idJenjang[index];
            System.out.println(jenjangID);

            for (String mapel : listMapel) {
                String sql2 = "select id_mapel\n" +
                        "from mapel\n" +
                        "where id_jenjang = ? and nama_mapel = ?";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ps2.setString(1, jenjangID);
                ps2.setString(2, mapel);
                ResultSet rs2 = ps2.executeQuery();
                String idMapel = "";
                if (rs2.next()) {
                    idMapel = rs2.getString("id_mapel");
                }

                String sql3 = "INSERT INTO biaya_tutor (id_mapel, id_jenjang, id_tutor, biaya, status_del) " +
                        "VALUES (?, ?, ?, ?, 0)\n";
                PreparedStatement ps3 = conn.prepareStatement(sql3);
                ps3.setString(1, idMapel);
                ps3.setString(2, jenjangID);
                ps3.setString(3, id);
                ps3.setInt(4, biaya);
                ps3.executeUpdate();
            }
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

    public String generateIdTutor(String nama) {
        String hurufDepan = nama.trim().substring(0, 1).toUpperCase();
        String newId = "";
        try {
            String sql = "select id_tutor from tutor where id_tutor like ? order by id_tutor desc";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, hurufDepan + "%");
            ResultSet rs = ps.executeQuery();

            int nomorTerakhir = 0;
            if (rs.next()) {
                String lastId = rs.getString("id_tutor");
                String angka = lastId.substring(1);
                nomorTerakhir = Integer.parseInt(angka);
            }
            int nomorBaru = nomorTerakhir + 1;
            String formattedNumber;
            if (nomorBaru < 10) {
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

    public DefaultTableModel setTableTutorAdmin(String jenjang, String mapel, String status, String keyword) {
        String[] column = {"ID Tutor", "Nama", "Email", "Kota", "Telepon", "Rating", "Mata Pelajaran Ajaran", "Status"};
        DefaultTableModel model = new DefaultTableModel(column, 0);

        String sql = "SELECT t.id_tutor, t.nama, t.email, t.kota, t.telepon, t.rating, m.nama_mapel, " +
                "IF(t.status_del = 0, 'Aktif', 'Tidak Aktif') AS status " +
                "FROM tutor t " +
                "JOIN biaya_tutor b ON t.id_tutor = b.id_tutor " +
                "JOIN mapel m ON b.id_mapel = m.id_mapel " +
                "JOIN jenjang j ON b.id_jenjang = j.id_jenjang " +
                "WHERE 1=1 ";

        ArrayList<String> params = new ArrayList<>();

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
            params.add(jenjangID);
        }

        if (!mapel.equals("All")) {
            sql += " AND m.nama_mapel = ?";
            params.add(mapel);
        }

        if (!status.equals("All")) {
            if (status.equals("Aktif")) {
                sql += " AND t.status_del = 0";
            } else if (status.equals("Tidak Aktif")) {
                sql += " AND t.status_del = 1";
            }
        }

        if (keyword != null && !keyword.isEmpty()) {
            sql += " AND LOWER(t.nama) LIKE ?";
            params.add(keyword);
        }

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            int index = 1;
            if (!jenjang.equals("All")) {
                ps.setString(index, jenjangID);
                index++;
            }
            if (!mapel.equals("All")) {
                ps.setString(index, mapel);
                index++;
            }
            if (keyword != null && !keyword.isEmpty()) {
                ps.setString(index, "%" + keyword.toLowerCase() + "%");
                index++;
            }
            ResultSet rs = ps.executeQuery();

            HashMap<String, Object[]> dataMap = new HashMap<>();

            while (rs.next()) {
                String id = rs.getString("id_tutor");
                String mapelName = rs.getString("nama_mapel");

                if (dataMap.containsKey(id)) {
                    Object[] row = dataMap.get(id);
                    row[6] = row[6] + ", " + mapelName;
                } else {
                    Object[] row = new Object[]{
                            rs.getString("id_tutor"),
                            rs.getString("nama"),
                            rs.getString("email"),
                            rs.getString("kota"),
                            rs.getString("telepon"),
                            rs.getString("rating"),
                            mapelName, // pertama kali isi mapel
                            rs.getString("status"),
                    };
                    dataMap.put(id, row);
                }
            }

            for (Object[] row : dataMap.values()) {
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return model;
    }

    public void updateTutor(String id, String nama, String email, String kota, String telp, String rating, String jenjang, List<String> newMapelList, String status) {
        try {
            String sql = "update tutor set nama = ?, email = ?, kota = ?, telepon = ?, rating = ?, status_del = ? WHERE id_tutor = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setString(2, email);
            ps.setString(3, kota);
            ps.setString(4, telp);
            ps.setString(5, rating);
            int del = 0;
            if (status.equals("Aktif")) {
                del = 0;
            } else if (status.equals("Tidak Aktif")) {
                del = 1;
            }
            ps.setInt(6, del);
            ps.setString(7, id);
            ps.executeUpdate();

            List<String> currentMapel = getCurrentMapelByTutor(id);
            boolean mapelSama = currentMapel.size() == newMapelList.size()
                    && currentMapel.containsAll(newMapelList);
            if (!mapelSama) {
                String nonAktifkanSql = "UPDATE biaya_tutor SET status_del = 1 WHERE id_tutor = ?";
                PreparedStatement nonAktifkan = conn.prepareStatement(nonAktifkanSql);
                nonAktifkan.setString(1, id);
                nonAktifkan.executeUpdate();

                for (String namaMapel : newMapelList) {
                    String idJenjangg = getIdJenjangByNama(jenjang);
                    String idMapel = getIdMapelByNama(namaMapel, idJenjangg);
                    if (idMapel == null) continue;

                    String cekSql = "SELECT * FROM biaya_tutor WHERE id_tutor = ? AND id_mapel = ?";
                    PreparedStatement cekPs = conn.prepareStatement(cekSql);
                    cekPs.setString(1, id);
                    cekPs.setString(2, idMapel);
                    ResultSet rs = cekPs.executeQuery();

                    if (rs.next()) {
                        String aktifkanSql = "UPDATE biaya_tutor SET status_del = 0 WHERE id_tutor = ? AND id_mapel = ?";
                        PreparedStatement aktifkan = conn.prepareStatement(aktifkanSql);
                        aktifkan.setString(1, id);
                        aktifkan.setString(2, idMapel);
                        aktifkan.executeUpdate();
                    } else {
                        String idJenjang = getIdJenjangByNama(jenjang);
                        String insertSql = "INSERT INTO biaya_tutor (id_mapel, id_jenjang, id_tutor, biaya, status_del) " +
                                "VALUES (?,?,?,100000,0)";
                        PreparedStatement insert = conn.prepareStatement(insertSql);
                        insert.setString(1, idMapel);
                        insert.setString(2, idJenjang);
                        insert.setString(3, id);
                        insert.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getIdJenjangByNama(String namaJenjang) {
        String idJenjang = null;
        try {
            String sql = "SELECT id_jenjang FROM jenjang WHERE nama_jenjang = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaJenjang);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idJenjang = rs.getString("id_jenjang");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idJenjang;
    }

    private List<String> getCurrentMapelByTutor(String idTutor) {
        List<String> mapel = new ArrayList<>();
        try {
            String sql = "select m.nama_mapel " +
                    "FROM mapel m " +
                    "JOIN biaya_tutor t ON m.id_mapel = t.id_mapel " +
                    "WHERE t.id_tutor = ? AND t.status_del = 0";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idTutor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mapel.add(rs.getString("nama_mapel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapel;
    }

    public String getIdMapelByNama(String namaMapel, String idJenjang) {
        String idMapel = null;
        try {
            System.out.println(idJenjang);
            String kodeJenjang = "";

            switch (idJenjang) {
                case "Kelas 1":
                    kodeJenjang = "J01";
                    break;
                case "Kelas 2":
                    kodeJenjang = "J02";
                    break;
                case "Kelas 3":
                    kodeJenjang = "J03";
                    break;
                case "Kelas 4":
                    kodeJenjang = "J04";
                    break;
                case "Kelas 5":
                    kodeJenjang = "J05";
                    break;
                case "Kelas 6":
                    kodeJenjang = "J06";
                    break;
                case "Kelas 7":
                    kodeJenjang = "J07";
                    break;
                case "Kelas 8":
                    kodeJenjang = "J08";
                    break;
                case "Kelas 9":
                    kodeJenjang = "J09";
                    break;
                case "Kelas 10":
                    kodeJenjang = "J10";
                    break;
                case "Kelas 11":
                    kodeJenjang = "J11";
                    break;
                case "Kelas 12":
                    kodeJenjang = "J12";
                    break;
                default:
                    System.out.println("Jenjang tidak dikenali.");
                    break;
            }
            String sql = "SELECT id_mapel FROM mapel WHERE nama_mapel = ? and id_jenjang = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaMapel);
            ps.setString(2, kodeJenjang);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println(rs.getString("id_mapel"));
                idMapel = rs.getString("id_mapel");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idMapel;
    }

    public List<String> getMapelByTutor(String idTutor) {
        List<String> mapelList = new ArrayList<>();
        String sql = "SELECT DISTINCT m.nama_mapel " +
                "FROM biaya_tutor b " +
                "JOIN mapel m ON b.id_mapel = m.id_mapel " +
                "WHERE b.id_tutor = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idTutor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mapelList.add(rs.getString("nama_mapel"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapelList;
    }

    public String getJenjangFromTutor(String idTutor) {
        String jenjang = "";
        try {
            String sql = "SELECT DISTINCT j.level " +
                    "FROM biaya_tutor b " +
                    "JOIN mapel m ON b.id_mapel = m.id_mapel " +
                    "JOIN jenjang j ON m.id_jenjang = j.id_jenjang " +
                    "WHERE b.id_tutor = ? AND b.status_del = 0 " +
                    "LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idTutor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jenjang = "Kelas " + rs.getString("level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jenjang;
    }

    public int getBiayaFromIdTutor(String id) {
        int biaya = 0;
        try {
            String sql = "select distinct biaya\n" +
                    "from biaya_tutor\n" +
                    "where id_tutor = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                biaya = rs.getInt("biaya");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return biaya;
    }

    public List<String> getTutorByMapel (String namaMapel, String idJenjang){
        String idMapel = getIdMapelByNama(namaMapel, idJenjang);
        ArrayList<String> list = new ArrayList<>();
        try {
            String sql = "select t.nama \n" +
                    "from tutor t\n" +
                    "join biaya_tutor b on t.id_tutor = b.id_tutor\n" +
                    "where b.id_mapel = ? order by t.id_tutor asc";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idMapel);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("nama"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<String> getRatingByMapel (String namaMapel, String idJenjang){
        String idMapel = getIdMapelByNama(namaMapel, idJenjang);
        ArrayList<String> list = new ArrayList<>();
        try {
            String sql = "select t.rating \n" +
                    "from tutor t\n" +
                    "join biaya_tutor b on t.id_tutor = b.id_tutor\n" +
                    "where b.id_mapel = ? order by t.id_tutor asc";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idMapel);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("rating"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public int getHargaByTutor(String namaTutor) {
        int harga = 0;
        try {
            String idTutor = "";
            String sqlTutor = "SELECT id_tutor FROM tutor WHERE nama = '" + namaTutor + "'";
            try (PreparedStatement psTutor = conn.prepareStatement(sqlTutor)) {
                ResultSet rs = psTutor.executeQuery();
                if (rs.next()) {
                    idTutor = rs.getString(1);
                }
            }
            String query = "SELECT biaya FROM biaya_tutor WHERE id_tutor = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, idTutor);
            ResultSet rs2 = stmt.executeQuery();
            if (rs2.next()) {
                harga = rs2.getInt("biaya");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return harga;
    }

    public String generateIdTransaksi() {
        String prefix = "TR" + String.valueOf(java.time.Year.now().getValue()).substring(2); // TR25
        String lastId = "";

        String query = "SELECT id_transaksi FROM transaksi WHERE id_transaksi LIKE ? ORDER BY id_transaksi DESC LIMIT 1";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, prefix + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                lastId = rs.getString("id_transaksi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int nextNumber = 1;
        if (lastId != null) {
            String numberPart = lastId.substring(4);
            nextNumber = Integer.parseInt(numberPart) + 1;
        }
        String nextId = prefix + String.format("%04d", nextNumber);
        return nextId;
    }

    public DefaultTableModel setGajiTutorTable(String bulan, String status){
        String[] column = {"Nama Tutor", "Bulan", "Jumlah Pertemuan", "Total Gaji", "Tanggal Bayar", "Status"};
        DefaultTableModel model = new DefaultTableModel(null,column);

        String sql = "SELECT \n" +
                "    t.nama, pt.bulan_digaji,\n" +
                "    COUNT(DISTINCT sdl.id_siswa) AS jumlah_siswa,\n" +
                "    bt.biaya * COUNT(DISTINCT sdl.id_siswa) AS total, " +
                "IF(pt.tanggal_bayar IS NULL, '', pt.tanggal_bayar), " +
                "IF(pt.status_bayar = 1, 'Terbayar', 'Belum Terbayar') \n" +
                "FROM pembayaran_tutor pt\n" +
                "JOIN tutor t ON pt.id_tutor = t.id_tutor\n" +
                "JOIN \n" +
                "    (\n" +
                "        SELECT id_tutor, MAX(biaya) AS biaya\n" +
                "        FROM biaya_tutor\n" +
                "        GROUP BY id_tutor\n" +
                "    ) bt ON pt.id_tutor = bt.id_tutor\n" +
                "LEFT JOIN detail_les dl ON dl.id_tutor = t.id_tutor\n" +
                "LEFT JOIN siswa_detail_les sdl ON dl.id_detail_les = sdl.id_detail_les\n";

        List<String> conditions = new ArrayList<>();
        if (!bulan.equals("All")) {
            switch (bulan) {
                case "Januari": bulan = "1"; break;
                case "Februari": bulan = "2"; break;
                case "Maret": bulan = "3"; break;
                case "April": bulan = "4"; break;
                case "Mei": bulan = "5"; break;
                case "Juni": bulan = "6"; break;
                case "Juli": bulan = "7"; break;
                case "Agustus": bulan = "8"; break;
                case "September": bulan = "9"; break;
                case "Oktober": bulan = "10"; break;
                case "November": bulan = "11"; break;
                case "Desember": bulan = "12"; break;
            }
            conditions.add("pt.bulan_digaji = " + bulan);
        }

        if (!status.equals("All")) {
            switch (status){
                case "Terbayar": status = "1"; break;
                case "Belum Terbayar": status = "0"; break;
            }
            conditions.add("pt.status_bayar = " + status);
        }

        if (!conditions.isEmpty()) {
            sql += "WHERE " + String.join(" AND ", conditions) + " ";
        }

        sql += "GROUP BY t.id_tutor, t.nama, bt.biaya, pt.bulan_digaji, pt.tanggal_bayar, pt.status_bayar " +
                "ORDER BY 1;";

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String bulanGaji = rs.getString(2);
                switch (bulanGaji) {
                    case "1": bulanGaji = "Januari";break;
                    case "2": bulanGaji = "Februari";break;
                    case "3": bulanGaji = "Maret";break;
                    case "4": bulanGaji = "April";break;
                    case "5": bulanGaji = "Mei";break;
                    case "6": bulanGaji = "Juni";break;
                    case "7": bulanGaji = "Juli";break;
                    case "8": bulanGaji = "Agustus";break;
                    case "9": bulanGaji = "September";break;
                    case "10": bulanGaji = "Oktober";break;
                    case "11": bulanGaji = "November";break;
                    case "12": bulanGaji = "Desember";break;
                }
                model.addRow(new Object[]{
                        rs.getString(1),
                        bulanGaji,
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    public Object[] getDetailGajiTutor(String tutorNama) {
        String sql = "SELECT \n" +
                "    t.id_tutor, \n" +
                "    t.nama, \n" +
                "    bt.biaya, \n" +
                "    COUNT(DISTINCT sdl.id_siswa) AS jumlah_siswa,\n" +
                "    bt.biaya * COUNT(DISTINCT sdl.id_siswa) AS total\n" +
                "FROM pembayaran_tutor pt\n" +
                "JOIN tutor t ON pt.id_tutor = t.id_tutor\n" +
                "JOIN \n" +
                "    (\n" +
                "        SELECT id_tutor, MAX(biaya) AS biaya\n" +
                "        FROM biaya_tutor\n" +
                "        GROUP BY id_tutor\n" +
                "    ) bt ON pt.id_tutor = bt.id_tutor\n" +
                "LEFT JOIN detail_les dl ON dl.id_tutor = t.id_tutor\n" +
                "LEFT JOIN siswa_detail_les sdl ON dl.id_detail_les = sdl.id_detail_les\n" +
                "WHERE t.nama = ?\n" +
                "GROUP BY t.id_tutor, t.nama, bt.biaya;\n";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tutorNama);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Object[]{
                        rs.getString("id_tutor"),
                        rs.getString("nama"),
                        rs.getInt("biaya"),
                        rs.getInt("jumlah_siswa"),
                        rs.getInt("total")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setStatusGajiTutor(String namaTutor){
        String idTutor = "";
        String sqlTutor = "SELECT id_tutor FROM tutor WHERE nama = '" + namaTutor + "'";
        try (PreparedStatement psTutor = conn.prepareStatement(sqlTutor)){
            ResultSet rs = psTutor.executeQuery();
            if (rs.next()){
                idTutor = rs.getString(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        LocalDate today = LocalDate.now();
        String sql = "UPDATE pembayaran_tutor SET status_bayar = 1, tanggal_bayar = ? WHERE id_tutor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, String.valueOf(today));
            ps.setString(2, idTutor);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void softDeleteTutor(String idTutor) {
        try {
            String sql = "update tutor set status_del = 1\n" +
                    "where id_tutor = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idTutor);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getNamaJenjang(){
        return namaJenjang;
    }

    public ArrayList<String> getNamaMapel() {
        return namaMapel;
    }



}
