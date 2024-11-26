package main;

import java.io.IOException;
import java.sql.SQLException;
import static main.utils.DatabaseUtil.connect;

public class Main {

    public static void main(String[] args) throws SQLException, IOException {
        connect();

        App.run();
    }
}
