import View.*;
import Model.*;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql. *;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/smartedu";
        String user = "root";
        String password = "root";

        try{
            Connection conn = DriverManager.getConnection(url,user,password);
            AdminModel adminModel = new AdminModel(conn);
            LoginFrame frame = new LoginFrame(adminModel);
            frame.setVisible(true);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}

//KELOMPOK 2
//ANGGOTA KELOMPOK:
//0706022410004 CECILIA AGUSTA LEO
//0706022410006 JOCELYN JOLIE
//0706022410008 PUTU DIAHLOKA MAHAPUTRI
//0706022210065 MALVIN JAPNANTO
//0706022410049 JEFFERSON WIJAYA


