package com.example.kgarciaassignment7;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                user.setRole(UserRole.valueOf(rs.getString("role").toUpperCase()));
                return user;
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

                user.setRole(UserRole.valueOf(rs.getString("role").toUpperCase()));

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

    public static void updateItem(Item item) {

        String sql = """
             UPDATE Items
            SET itemName = ?,
                manufacturer = ?,
                itemPrice = ?,
                itemInventory = ?,
                itemType = ?,
                imageName = ?,
                imageContent = ?
            WHERE itemID = ?
            """;
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, item.getName());
            ps.setString(2, item.getManufacturer());
            ps.setDouble(3, item.getPrice());
            ps.setInt(4, item.getInventory());
            ps.setString(5, item.getType().name());

            if (item.getImage() != null) {
                ps.setString(6, item.getImage().getName());
                ps.setBytes(7, item.getImage().getContents());
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
                ps.setNull(7, java.sql.Types.BLOB);
            }

            ps.setInt(8, item.getId());

            int rowsUpdated = ps.executeUpdate();

            System.out.println("Rows updated: " + rowsUpdated);
            System.out.println("Updated image name: "
                    + (item.getImage() != null
                    ? item.getImage().getName()
                    : "NO IMAGE"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteItem(int id) {

        String sql = "DELETE FROM Items WHERE itemID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rowsDeleted = ps.executeUpdate();

            System.out.println("Rows deleted = " + rowsDeleted);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateInventory(
            int itemID,
            int inventory) {

        String sql = """
            UPDATE Items
            SET itemInventory = ?
            WHERE itemID = ?
            """;

        try (Connection con = getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, inventory);
            ps.setInt(2, itemID);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Item> filterItems(String type) {

        Map<Integer, Item> itemDB = new HashMap<>();

        String sql = "SELECT * FROM Items WHERE itemType = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, type);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Item item = new Item();

                item.setId(rs.getInt("itemID"));
                item.setName(rs.getString("itemName"));
                item.setManufacturer(rs.getString("manufacturer"));
                item.setPrice(rs.getDouble("itemPrice"));
                item.setInventory(rs.getInt("itemInventory"));

                String imageName = rs.getString("imageName");
                byte[] imageContent = rs.getBytes("imageContent");

                if (imageContent != null && imageContent.length > 0) {

                    Image image = new Image();

                    image.setName(imageName);
                    image.setContents(imageContent);

                    item.setImage(image);
                }

                item.setType(
                        ItemTitle.valueOf(rs.getString("itemType"))
                );

                itemDB.put(rs.getInt("itemID"), item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemDB;
    }

    public static Map<Integer, Item> searchItems(String search) {

        Map<Integer, Item> itemDB = new HashMap<>();

        String sql = """
            SELECT *
            FROM Items
            WHERE itemName LIKE ? COLLATE NOCASE
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // % allows partial searches
            ps.setString(1, "%" + search + "%");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Item item = new Item();

                    item.setId(rs.getInt("itemID"));
                    item.setName(rs.getString("itemName"));
                    item.setManufacturer(rs.getString("manufacturer"));
                    item.setPrice(rs.getDouble("itemPrice"));
                    item.setInventory(rs.getInt("itemInventory"));

                    // Get image
                    String imageName = rs.getString("imageName");
                    byte[] imageContent = rs.getBytes("imageContent");

                    if (imageContent != null && imageContent.length > 0) {

                        Image image = new Image();

                        image.setName(imageName);
                        image.setContents(imageContent);

                        item.setImage(image);
                    }

                    // Get item type
                    item.setType(
                            ItemTitle.valueOf(rs.getString("itemType"))
                    );

                    itemDB.put(item.getId(), item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to search items", e);
        }

        return itemDB;
    }

    public static List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        String sql = """
            SELECT userLogin,
                   password,
                   role,
                   createdBy,
                   CreatedAt,
                   archived,
                   name,
                   email
         
            FROM Users
            WHERE archived = 0
            ORDER BY userLogin
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                User user = new User();

                user.setUserLogin(rs.getString("userLogin"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));

                String role = rs.getString("role");

                if (role != null) {
                    user.setRole(
                            UserRole.valueOf(
                                    role.trim()
                                            .toUpperCase()
                                            .replace(" ", "_")
                            )
                    );
                }

                user.setCreatedBy(rs.getString("createdBy"));
                user.setCreatedAt(rs.getString("CreatedAt"));

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to retrieve users", e);
        }

        return users;
    }

    public static List<User> getUsersExceptSuperAdmin() {
        return List.of();
    }

    public static boolean addUser(User user) {

        String sql = """
            INSERT INTO Users
            (userLogin, email, password, role, createdBy, createdAt)
            VALUES (?, ?, ?, ?, ?, datetime('now', 'localtime'))
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUserLogin());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole().name());
            ps.setString(5, user.getCreatedBy());

            int rowsAdded = ps.executeUpdate();

            return rowsAdded > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateUser(String userLogin,
                                     String email,
                                     String password,
                                     UserRole role) {

        String sql = """
            UPDATE Users
            SET userLogin = ?,
                email = ?,
                password = ?,
                role = ?
            WHERE userLogin = ?
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userLogin);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role.name());
            ps.setString(5, userLogin);

            int rowsUpdated = ps.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUser(int userID) {

        String sql = """
            DELETE FROM Users
            WHERE userID = ?
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userID);

            int rowsDeleted = ps.executeUpdate();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUserByLogin(String userLogin) {

        String sql = """
            SELECT userLogin,
                   password,
                   role,
                   createdBy,
                   CreatedAt
            FROM Users
            WHERE userLogin = ?
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userLogin);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User user = new User();

                    user.setUserLogin(rs.getString("userLogin"));
                    user.setPassword(rs.getString("password"));

                    String role = rs.getString("role");

                    if (role != null) {
                        user.setRole(
                                UserRole.valueOf(
                                        role.trim()
                                                .toUpperCase()
                                                .replace(" ", "_")
                                )
                        );
                    }

                    user.setCreatedBy(rs.getString("createdBy"));
                    user.setCreatedAt(rs.getString("CreatedAt"));

                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to retrieve user", e);
        }

        return null;
    }

    public static boolean archiveUser(String userLogin) {

        String sql = """
            UPDATE Users
            SET archived = 1
            WHERE userLogin = ?
            """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userLogin);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to archive user", e);
        }
    }
}
