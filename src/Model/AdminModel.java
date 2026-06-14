package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminModel {
    private Connection conn;
    private String id_admin;
    public String idSIswa;

    public AdminModel(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public String getId_admin() {
        return id_admin;
    }

    public void setId_admin(String id_admin) {
        this.id_admin = id_admin;
    }

    public Boolean checkAdmin(String email, String password) {
        Boolean check = false;
        try {
            String sql = "select id_admin, password, email from admin";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String admin_email = rs.getString("email");
                String id_admin = rs.getString("id_admin");
                String admin_password = rs.getString("password");

                if (email.equals(admin_email) && password.equals(admin_password)) {
                    check = true;
                    setId_admin(id_admin);
                    System.out.println(id_admin);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    public String getAdminName() {
        String name = "";
        try {
            String sql = "select nama from admin where id_admin = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id_admin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString("nama");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public String[] getDataAdmin(String idAdmin) {
        String[] data = new String[4];
        try {
            String sql = "select id_admin, nama, email, password from admin where id_admin = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idAdmin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data[0] = rs.getString("id_admin");
                data[1] = rs.getString("nama");
                data[2] = rs.getString("email");
                data[3] = rs.getString("password");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public void updateProfilAdmin (String idAdmin, String nama, String email, String pass) {
        try {
            String sql = "update admin set nama = ?, email  = ?, password = ?\n" +
                    "where id_admin = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setString(2, email);
            ps.setString(3, pass);
            ps.setString(4, idAdmin);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setIDSiswa(String id){
        this.idSIswa = id;
    }


    public String getIdSIswa(){
        return idSIswa;}
}
