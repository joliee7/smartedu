package Model;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiModel {
    private Connection conn;
    public TransaksiModel(Connection conn) {
        this.conn = conn;
    }

    public void addTransaksi(String idTrans, String idSiswa, String idAdmin, Date tgl, int nominal, String metode,
                             Date periodMulai, Date periodAkhir){
        try {
            String sql = "insert into transaksi (id_transaksi, id_siswa, id_admin, tanggal, nominal, metode_bayar, periode_mulai, periode_akhir, status_del)\n" +
                    "values  (?,?,?,?,?,?,?,?,0)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idTrans);
            ps.setString(2, idSiswa);
            ps.setString(3, idAdmin);
            ps.setDate(4, tgl);
            ps.setInt(5, nominal);
            ps.setString(6, metode);
            ps.setDate(7, periodMulai);
            ps.setDate(8, periodAkhir);
            ps.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Date getPeriodAkhirByTransaksi(String idSiswa) {
        Date tanggal = null;
        try {
            String sql = "select t.periode_akhir from siswa s\n" +
                    "join transaksi t on s.id_siswa = t.id_siswa\n" +
                    "where s.id_siswa = ? \n" +
                    "order by t.periode_akhir desc limit 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idSiswa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tanggal = rs.getDate("periode_akhir");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tanggal;
    }

    public void addDetailTransaksi(String idDetail, String idTrans, ArrayList<String> mapelList, ArrayList<String> tutorList,
                                   ArrayList<Integer> durasiList, ArrayList<Integer> hargaSesiList, int jumlahSesi, int subtotal) {
        for (int i = 0; i < mapelList.size(); i++) {
            String detailId = generateIdDetailTrans();
            String mapel = mapelList.get(i);
            String tutor = tutorList.get(i);
            int durasi = durasiList.get(i);
            int hargaSesi = hargaSesiList.get(i);

            String idMapel = getIdMapelByNama(mapel);
            String idTutor = getIDTutorByNama(tutor);
            try {
                String sql = "insert into detail_transaksi(id_detailtrans, id_transaksi, id_mapel, id_tutor, durasi_sesi, harga_per_sesi, jumlah_sesi, subtotal, status_del)\n" +
                        "values (?,?,?,?,?,?,?,?,0);";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, detailId);
                ps.setString(2, idTrans);
                ps.setString(3, idMapel);
                ps.setString(4, idTutor);
                ps.setInt(5, durasi);
                ps.setInt(6, hargaSesi);
                ps.setInt(7, jumlahSesi);
                ps.setInt(8, subtotal);
                ps.execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
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

    public DefaultTableModel setRegistMapelTable(
            String mapel1, String tutor1, String durasi1,
            String mapel2, String tutor2, String durasi2,
            String mapel3, String tutor3, String durasi3
    ) {
        String[] column = {"No", "Mata Pelajaran", "Tutor", "Durasi", "Harga per Sesi", "Jumlah Sesi", "Total Harga"};
        DefaultTableModel modelTable = new DefaultTableModel(column, 0);

        int no = 1;
        int jumlahSesi = 8;

        if (mapel1 != null && !mapel1.isEmpty()) {
            String angka = durasi1.substring(0, durasi1.indexOf(" "));
            int durasi = Integer.parseInt(angka);
            System.out.println(durasi);

            int harga1 = (durasi / 30) * getHargaByTutor(tutor1);
            int total1 = harga1 * jumlahSesi;

            String sql1 = "select pt.nominal, t.id_tutor from pembayaran_tutor pt, tutor t where t.nama = ? AND t.id_tutor = pt.id_tutor";
            try (PreparedStatement ps = conn.prepareStatement(sql1)){
                ps.setString(1, tutor1);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    String nomimalNow = rs.getString(1);
                    String id_tutorSelected = rs.getString(2);
                    int tambahNominal = harga1*8*6/10;
                    int updateNom = Integer.parseInt(nomimalNow) + tambahNominal;
                    String updateNomStr = String.valueOf(updateNom);
                    String sql2 = "update pembayaran_tutor set nominal = ? where id_tutor = ?";
                    try (PreparedStatement ps2 = conn.prepareStatement(sql2)){
                        ps2.setString(1,updateNomStr);
                        ps2.setString(2,id_tutorSelected);
                        ps2.executeUpdate();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            modelTable.addRow(new Object[]{no++, mapel1, tutor1, durasi1, "Rp. " + harga1, jumlahSesi, "Rp. " + total1});
        }

        if (mapel2 != null && !mapel2.isEmpty()) {
            String angka = durasi2.substring(0, durasi2.indexOf(" "));
            int durasi = Integer.parseInt(angka);
            int harga2 = (durasi / 30) * getHargaByTutor(tutor2);
            int total2 = harga2 * jumlahSesi;
            modelTable.addRow(new Object[]{no++, mapel2, tutor2, durasi2, "Rp. " + harga2, jumlahSesi, "Rp. " + total2});
        }

        if (mapel3 != null && !mapel3.isEmpty()) {
            String angka = durasi3.substring(0, durasi3.indexOf(" "));
            int durasi = Integer.parseInt(angka);
            int harga3 = (durasi / 30) * getHargaByTutor(tutor3);
            int total3 = harga3 * jumlahSesi;
            modelTable.addRow(new Object[]{no++, mapel3, tutor3, durasi3, "Rp. " + harga3, jumlahSesi, "Rp. " + total3});
        }
        return modelTable;
    }

    public DefaultTableModel setPembayaranSiswaTable(String bulan, String metodeBayar){
        String[] column = {"Tanggal Transaksi", "ID Siswa", "Nama", "Total Bayar", "Metode Pembayaran", "Periode Mulai", "Periode Akhir"};
        DefaultTableModel model = new DefaultTableModel(null, column);

        String sql = "SELECT t.tanggal, t.id_siswa, s.nama, t.nominal, t.metode_bayar, t.periode_mulai, t.periode_akhir " +
                "FROM transaksi t JOIN siswa s ON t.id_siswa = s.id_siswa ";

        List<String> conditions = new ArrayList<>();

        if (!bulan.equalsIgnoreCase("All")) {
            String bulanAngka = switch (bulan) {
                case "Januari" -> "01";
                case "Februari" -> "02";
                case "Maret" -> "03";
                case "April" -> "04";
                case "Mei" -> "05";
                case "Juni" -> "06";
                case "Juli" -> "07";
                case "Agustus" -> "08";
                case "September" -> "09";
                case "Oktober" -> "10";
                case "November" -> "11";
                case "Desember" -> "12";
                default -> null;
            };

            if (bulanAngka != null) {
                conditions.add("MONTH(t.tanggal) = " + bulanAngka);
            }
        }

        if (!metodeBayar.equalsIgnoreCase("All")) {
            conditions.add("t.metode_bayar = '" + metodeBayar + "'");
        }

        if (!conditions.isEmpty()) {
            sql += "WHERE " + String.join(" AND ", conditions) + " ";
        }

        sql += "ORDER BY t.id_siswa";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    public String generateIdDetailTrans() {
        String tahun = String.valueOf(java.time.Year.now().getValue()).substring(2); // "25"
        String prefix = "DT" + tahun;
        String lastId = null;
        try {
            String query = "SELECT id_detailtrans FROM detail_transaksi WHERE id_detailtrans LIKE ? ORDER BY id_detailtrans DESC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, prefix + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                lastId = rs.getString("id_detailtrans");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int nomorUrut = 1;
        if (lastId != null) {
            String nomorStr = lastId.substring(4);
            nomorUrut = Integer.parseInt(nomorStr) + 1;
        }
        return String.format("%s%04d", prefix, nomorUrut);
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

    public String getIDTutorByNama(String tutor) {
        String idTutor = "";
        try {
            String sql = "select id_tutor from tutor where nama = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tutor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idTutor = rs.getString("id_tutor");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return idTutor;
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

    public List<String[]> getDetailLesByStudent(String idSiswa) {
        List<String[]> detailLes = new ArrayList<>();
        try {
            String query = "SELECT m.nama_mapel, t.nama, d.durasi_sesi\n" +
                    "FROM detail_transaksi d\n" +
                    "join mapel m on d.id_mapel = m.id_mapel\n" +
                    "join tutor t on d.id_tutor = t.id_tutor\n" +
                    "join transaksi r on d.id_transaksi = r.id_transaksi\n" +
                    "WHERE r.id_siswa = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idSiswa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String mapel = rs.getString("nama_mapel");
                String tutor = rs.getString("nama");
                String durasi = rs.getString("durasi_sesi") + " Menit";
                detailLes.add(new String[]{mapel, tutor, durasi});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detailLes;
    }



}
