package Model;

import javax.swing.table.DefaultTableModel;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomModel {
    private Connection conn;
    public RoomModel(Connection conn) {
        this.conn = conn;
    }

    public Boolean checkExists(String namaRuangan){
        String sql = "SELECT nama_ruangan FROM ruangan";
        boolean check = true;
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                if (rs.getString("nama_ruangan").equalsIgnoreCase(namaRuangan)){
                    check = false;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }

    public void addRoom(String namaRuangan, String kapasitas) throws SQLException {
        String sql = "INSERT INTO ruangan VALUES (?,?,?,?)";
        String autoRoomID = autoIDRoom();
        System.out.println(autoRoomID);
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, autoRoomID);
            ps.setString(2, namaRuangan);
            ps.setString(3, kapasitas);
            ps.setString(4, "0");
            ps.executeUpdate();
        }
    }

    public String autoIDRoom(){
        String autoIDRoom = "R";
        String sql = "SELECT id_ruangan FROM ruangan ORDER BY id_ruangan DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                String lastRoomID = rs.getString(1);
                String substrID = lastRoomID.substring(1,3);
                int autoID = Integer.parseInt(substrID) + 1;
                String id = String.valueOf(autoID);
                autoIDRoom += id;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return autoIDRoom;
    }

    public DefaultTableModel setTableRoom () {
        String[] column = {"ID Ruangan", "Nama Ruangan", "Kapasitas"};
        DefaultTableModel model = new DefaultTableModel(column, 0);
        try {
            String sql = "select id_ruangan, nama_ruangan, kapasitas " +
                    "from ruangan where status_del = 0 order by 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return model;
    }

    public void deleteRoom(String idRoom){
        String sql = "UPDATE ruangan SET status_del = 1 WHERE id_ruangan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, idRoom);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void editRoom(String idRoom, String namaRuangan, int kapasitas){
        String sql = "UPDATE ruangan set nama_ruangan = ?, kapasitas = ? where id_ruangan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, namaRuangan);
            ps.setInt(2, kapasitas);
            ps.setString(3, idRoom);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
