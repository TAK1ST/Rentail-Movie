package main.utils;

import java.sql.*;
import main.utils.DatabaseUtil;

public class statisticsUtils {
  //Connect Database
    try (Connection connection = DatabaseUtil.getConnection())
    {
        String query1 = "SELECT movie_id, COUNT(*) AS rental_count FROM Rental GROUP BY movie_id ORDER BY rental_count DESC LIMIT 5";
        Statement stmt = connection.createStatement();
        ResultSet rs1 = stmt.executeQuery(query1);

    }
    
}
