package com.example.kgarciaassignment7;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ItemDB {
    private static Connection getConnection() {
        try {
            InitialContext initialContext = new InitialContext();

            DataSource ds = (DataSource) initialContext.lookup(
                    "java:/comp/env/jdbc/KGarcia-Assignment8"
            );

            return ds.getConnection();

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<Integer, Item> getAllItems(){
        Map<Integer, Item> itemDB = new HashMap<>();

        // get connection, query the db, add items to the map
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Items")){
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Item a = new Item();

                a.setId(rs.getInt("itemID"));
                a.setName(rs.getString("itemName"));
                a.setManufacturer(rs.getString("manufacturer"));
                a.setPrice(rs.getDouble("itemPrice"));
                a.setInventory(rs.getInt("itemInventory"));
                a.setType(ItemTitle.valueOf(rs.getString("itemType") ));

                Image i = new Image();
                i.setName(rs.getString("imageName"));
                i.setContents(rs.getBytes("imageContent"));
                a.setImage(i);

                itemDB.put(rs.getInt("itemID"), a);
            }

        }
        catch (SQLException e){
            System.out.println(e);
        }

        return itemDB;
    }

    public static Item getItem(String itemID) {

        Item item = null;

        try (Connection con = getConnection();
             PreparedStatement ps =
                     con.prepareStatement(
                             "SELECT * FROM Items WHERE itemID=?"
                     )) {

            int id = Integer.parseInt(itemID);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                item = new Item();

                item.setId(rs.getInt("itemID"));
                item.setName(rs.getString("itemName"));
                item.setManufacturer(rs.getString("manufacturer"));
                item.setPrice(rs.getDouble("itemPrice"));
                item.setInventory(rs.getInt("itemInventory"));
                item.setType(
                        ItemTitle.valueOf(
                                rs.getString("itemType")
                        )
                );

                Image i = new Image();

                i.setName(rs.getString("imageName"));

                byte[] contents =
                        rs.getBytes("imageContent");

                if (contents != null) {
                    i.setContents(contents);
                }

                item.setImage(i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return item;
    }

    public static boolean addItem(Item item) {

        String sql = """
        INSERT INTO Items
        (itemID, itemName, itemType, manufacturer,
         itemPrice, itemInventory, imageName, imageContent)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, item.getId());
            ps.setString(2, item.getName());
            ps.setString(3, item.getType().name());
            ps.setString(4, item.getManufacturer());
            ps.setDouble(5, item.getPrice());
            ps.setInt(6, item.getInventory());

            if (item.getImage() != null) {
                ps.setString(7, item.getImage().getName());
                ps.setBytes(8, item.getImage().getContents());
            } else {
                ps.setNull(7, java.sql.Types.VARCHAR);
                ps.setNull(8, java.sql.Types.BLOB);
            }

            int rowsAdded = ps.executeUpdate();

            System.out.println("ROWS ADDED = " + rowsAdded);

            return rowsAdded > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static User getUser(int id) {

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM USERS WHERE itemID=?")) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserLogin(rs.getString("userLogin"));
                user.setPassword(rs.getString("password"));
            }
        }
        catch(SQLException e) {
            System.out.println(e);
        }


        return null;
    }

    public static User getUser(String userLogin, String password) {

        String sql = """
            SELECT *
            FROM Users
            WHERE userLogin = ?
            AND password = ?
            """;

        Connection con = getConnection();

        if (con == null) {
            System.out.println("Database connection failed.");
            return null;
        }

        try (con;
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userLogin);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();

                user.setUserLogin(rs.getString("userLogin"));
                user.setPassword(rs.getString("password"));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void addCookie(int id, String cookie) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO Cookies VALUES(?, ?)")) {

            ps.setInt(1, id);
            ps.setString(2, cookie);

            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void removeCookie(String cookie) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM Cookies WHERE cookie=?")) {
            ps.setString(1, cookie);

            ps.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }

    public static User getUserFromCookie(String cookie) {
        User user = null;
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT userID FROM Cookies WHERE cookie=?")) {

            ps.setString(1, cookie);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = getUser(rs.getInt("userID"));
            }
        }
        catch(SQLException e) {
            System.out.println(e);
        }

        return user;
    }
}
