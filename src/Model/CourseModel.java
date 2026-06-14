package Model;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CourseModel {
    private Connection conn;
    private String[] idJenjang = {"J01", "J02", "J03", "J04", "J05", "J06", "J07", "J08", "J09", "J10", "J11", "J12"};
    private String[] namaJenjang = {"Kelas 1", "Kelas 2", "Kelas 3", "Kelas 4", "Kelas 5", "Kelas 6", "Kelas 7", "Kelas 8", "Kelas 9", "Kelas 10", "Kelas 11", "Kelas 12"};
    private ArrayList<String> namaMapel = new ArrayList<>();
    public CourseModel(Connection conn) {
        this.conn = conn;
    }

    public boolean checkExists(String jenjang, String namaMapel, String tipe){
        boolean check = true;
        String jenjangID = "SELECT ";
        for (int i = 0; i < namaJenjang.length; i++) {
            if (jenjang.equals(namaJenjang[i])) {
                jenjangID = idJenjang[i];
                break;
            }
        }
        String autoMapelID = autoIDMapel(jenjangID, namaMapel, tipe);
        String sql = "SELECT id_mapel FROM mapel";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                if (rs.getString(1).equalsIgnoreCase(autoMapelID)){
                    check = false;
                    break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return check;
    }

    public void addCourse(String jenjang, String namaMapel, String tipe) throws SQLException {
        String sql = "INSERT INTO mapel VALUES (?,?,?,?)";
        String jenjangID = "";
        for (int i = 0; i < namaJenjang.length; i++) {
            if (jenjang.equals(namaJenjang[i])) {
                jenjangID = idJenjang[i];
                break;
            }
        }
        String autoMapelID = autoIDMapel(jenjangID, namaMapel, tipe);
        System.out.println(autoMapelID);
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, autoMapelID);
            ps.setString(2, jenjangID);
            ps.setString(3, namaMapel);
            ps.setString(4, "0");
            ps.executeUpdate();
        }
    }

    public String autoIDMapel(String jenjangID, String namaMapel, String tipe){
        String autoIDMapel = "";
        String mapelUpper = namaMapel.toUpperCase();
        autoIDMapel = jenjangID + mapelUpper.substring(0,3);
        if (tipe.equals("Nasional")){
            autoIDMapel += "01";
        }else {
            autoIDMapel += "02";
        }
        return autoIDMapel;
    }

    public void setNamaMapelByID(String IDjenjang) {
        try {
            String sql = "select distinct nama_mapel from mapel where id_jenjang = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, IDjenjang);
            ResultSet rs = ps.executeQuery();
            namaMapel.clear();
            while (rs.next()) {
                namaMapel.add(rs.getString("nama_mapel"));
                System.out.println(rs.getString("nama_mapel"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setNamaMapel(String jenjang) {
        int index = 0;
        for (int i = 0; i < namaJenjang.length; i++) {
            if (jenjang.equals(namaJenjang[i])) {
                index = i;
                break;
            }
        }
        String jenjangID = idJenjang[index];
        System.out.println(jenjangID);
        try {
            String sql = "select distinct nama_mapel from mapel where id_jenjang = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, jenjangID);
            ResultSet rs = ps.executeQuery();
            namaMapel.clear();
            while (rs.next()) {
                namaMapel.add(rs.getString("nama_mapel"));
                System.out.println(rs.getString("nama_mapel"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DefaultTableModel setTableCourse(String jenjang) throws SQLException {
        String[] column = {"ID Mata Pelajaran", "Jenjang", "Nama Mata Pelajaran"};
        DefaultTableModel model = new DefaultTableModel(column, 0);
        if (jenjang.equalsIgnoreCase("All")){
            String sql = "select id_mapel, id_jenjang, nama_mapel " +
                    "from mapel m order by 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int index2 = 0;
                for (int i = 0; i < idJenjang.length; i++) {
                    if (rs.getString("id_jenjang").equals(idJenjang[i])) {
                        index2 = i;
                        break;
                    }
                }
                String jenjangnama = namaJenjang[index2];
                model.addRow(new Object[]{
                        rs.getString("id_mapel"),
                        jenjangnama,
                        rs.getString("nama_mapel"),
                });
            }
        }else {
            int index = 0;
            for (int i = 0; i < namaJenjang.length; i++) {
                if (jenjang.equals(namaJenjang[i])) {
                    index = i;
                    break;
                }
            }
            String jenjangID = idJenjang[index];
            System.out.println(jenjangID);
            try {
                String sql = "select id_mapel, id_jenjang, nama_mapel " +
                        "from mapel m where id_jenjang = ? order by 1";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, jenjangID);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    for (int i = 0; i < idJenjang.length; i++) {
                        if (rs.getString("id_jenjang").equals(idJenjang[i])) {
                            index = i;
                            break;
                        }
                    }
                    String jenjangnama = namaJenjang[index];
                    model.addRow(new Object[]{
                            rs.getString("id_mapel"),
                            jenjangnama,
                            rs.getString("nama_mapel"),
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return model;
    }

    public DefaultTableModel setMateriTable(String id) {
        String[] column = {"ID Materi", "Nama Materi"};
        DefaultTableModel model = new DefaultTableModel(column, 0);
        try {
            String sql = "select id_materi, nama_materi from materi where id_mapel = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("id_materi"),
                        rs.getString("nama_materi")
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return model;
    }

    public String[] getNamaJenjang(){
        return namaJenjang;
    }


    public ArrayList<String> getNamaMapel() {
        return namaMapel;
    }
}
