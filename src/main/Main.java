
package main;

import java.io.IOException;
import java.sql.SQLException;
import main.controllers.Managers;
import static main.utils.DatabaseUtil.connect;


public class Main {
    
    public static void main(String[] args) throws SQLException, IOException {
        connect();
        Managers.initMM();
        Managers.getMM().addMovie("A00001");
        Managers.getMM().display(Managers.getMM().getList(), "Movie");
        
        
    }
}
