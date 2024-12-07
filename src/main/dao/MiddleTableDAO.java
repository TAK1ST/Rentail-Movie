
package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.config.Database;


public class MiddleTableDAO { 
    
    public static boolean addDataToMidTable(String middleTableName, String mainId, String mainAttr, String subIds, String subAttr) {
        String sql = "INSERT INTO " + middleTableName +" (" + subAttr + ", " + mainAttr + ") VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); 
            PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String subId : subIds.split(", ")) {
                ps.setString(1, subId.trim());
                ps.setString(2, mainId);
                ps.addBatch();  
            }
            ps.executeBatch(); 
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static String getSubIdsByMainId(String middleTableName, String mainId, String mainAttr, String subAttr) {
        String sql = "SELECT " + subAttr + " FROM " + middleTableName + " WHERE " + mainAttr + " = ?";
        StringBuilder subIds = new StringBuilder();
        try (Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, mainId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (subIds.length() > 0) {
                    subIds.append(", ");
                }
                subIds.append(rs.getString(subAttr));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subIds.toString();
    }
    
}
