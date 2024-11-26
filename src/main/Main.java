
package main;

import java.io.IOException;
import java.sql.SQLException;
import main.services.Managers;
import static main.utils.DatabaseUtil.connect;


public class Main {
    
    public static void main(String[] args) throws SQLException, IOException {
        connect();
        Managers.initMS();
        Managers.getMS().addMovie("A00001");
        Managers.getMS().display(Managers.getMS().getList(), "Movie");
        
        
    }
}
