package com.example.kgarciaassignment7;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println("===== USER SERVLET doGet =====");
        System.out.println("Request URI: " + req.getRequestURI());
        System.out.println("Context Path: " + req.getContextPath());
        System.out.println("Action: " + req.getParameter("action"));
        System.out.println("User Login: " + req.getParameter("userLogin"));

            // rest of your code...

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User loggedInUser = (User) session.getAttribute("user");

        // Only ADMIN and SUPER_ADMIN can manage users
        if (loggedInUser.getRole() != UserRole.ADMIN &&
                loggedInUser.getRole() != UserRole.SUPER_ADMIN) {

            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = req.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {

            case "add":
                req.setAttribute("roles", UserRole.values());

                req.getRequestDispatcher(
                        "/WEB-INF/jsp/addUser.jsp"
                ).forward(req, resp);
                break;

            case "view":
                viewUser(req, resp);
                break;

            case "edit":

                System.out.println("ENTERED EDIT CASE");

                String userLogin = req.getParameter("userLogin");

                System.out.println("Editing user: " + userLogin);

                User editUser = ItemDB.getUserByLogin(userLogin);

                System.out.println("User returned from DB: " + editUser);

                if (editUser == null) {
                    resp.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "User not found"
                    );
                    return;
                }

                req.setAttribute("editUser", editUser);
                req.setAttribute("roles", UserRole.values());

                System.out.println("Forwarding to editUser.jsp");

                req.getRequestDispatcher(
                        "/WEB-INF/jsp/editUser.jsp"
                ).forward(req, resp);

                return;

            case "archive":
                archiveUser(req, resp);
                break;

            case "list":
            default:
                listUsers(req, resp);
                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User loggedInUser = (User) session.getAttribute("user");

        // Only ADMIN and SUPER_ADMIN can manage users
        if (loggedInUser.getRole() != UserRole.ADMIN &&
                loggedInUser.getRole() != UserRole.SUPER_ADMIN) {

            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = req.getParameter("action");

        if ("add".equals(action)) {

            addUser(req, resp, loggedInUser);

        } else if ("update".equals(action)) {

            updateUser(req, resp, loggedInUser);

        } else {

            resp.sendRedirect(
                    req.getContextPath() + "/users?action=list"
            );
        }
    }


    /*
     * LIST USERS
     */
    private void listUsers(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        List<User> users = ItemDB.getAllUsers();

        req.setAttribute("users", users);

        req.getRequestDispatcher(
                "/WEB-INF/jsp/manageUsers.jsp"
        ).forward(req, resp);
    }


    /*
     * SHOW ADD USER FORM
     */
    private void showAddUserForm(HttpServletRequest req,
                                 HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("siteTitle", "Add User");

        req.getRequestDispatcher("/WEB-INF/jsp/addUser.jsp")
                .forward(req, resp);
    }


    /*
     * ADD USER
     */
    private void addUser(HttpServletRequest req,
                         HttpServletResponse resp,
                         User loggedInUser)
            throws IOException {

        String userLogin = req.getParameter("userLogin");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String roleValue = req.getParameter("role");

        if (userLogin == null || userLogin.isBlank() ||
                password == null || password.isBlank() ||
                roleValue == null || roleValue.isBlank() ||
                email == null || email.isBlank()) {

            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Login, password, and role are required."
            );
            return;
        }

        UserRole role;

        try {
            role = UserRole.valueOf(roleValue);
        } catch (IllegalArgumentException e) {

            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid user role."
            );
            return;
        }

        // ADMIN cannot create SUPER_ADMIN accounts
        if (loggedInUser.getRole() == UserRole.ADMIN &&
                role == UserRole.SUPER_ADMIN) {

            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        User newUser = new User();

        newUser.setUserLogin(userLogin);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole(role);

        // Store who created this account
        newUser.setCreatedBy(loggedInUser.getUserLogin());

        ItemDB.addUser(newUser);

        resp.sendRedirect(
                req.getContextPath() + "/users?action=list"
        );
    }


    /*
     * SHOW EDIT USER FORM
     */
    private void showEditUserForm(HttpServletRequest req,
                                  HttpServletResponse resp,
                                  User loggedInUser)
            throws ServletException, IOException {

        int userID;

        try {
            userID = Integer.parseInt(req.getParameter("userID"));
        } catch (NumberFormatException e) {

            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid user ID."
            );
            return;
        }

        User editUser = ItemDB.getUser(userID);

        if (editUser == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // ADMIN cannot edit SUPER_ADMIN
        if (loggedInUser.getRole() == UserRole.ADMIN &&
                editUser.getRole() == UserRole.SUPER_ADMIN) {

            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        req.setAttribute("editUser", editUser);
        req.setAttribute("siteTitle", "Edit User");

        req.getRequestDispatcher("/WEB-INF/jsp/editUser.jsp")
                .forward(req, resp);
    }


    /*
     * UPDATE USER
     */
    private void updateUser(HttpServletRequest req,
                            HttpServletResponse resp,
                            User loggedInUser)
            throws IOException {

        // Login currently stored in the database
        String originalUserLogin =
                req.getParameter("originalUserLogin");

        // New values from the form
        String userLogin =
                req.getParameter("userLogin");

        String email =
                req.getParameter("email");

        String password =
                req.getParameter("password");

        String roleValue =
                req.getParameter("role");


        // Validate original login
        if (originalUserLogin == null ||
                originalUserLogin.isBlank()) {

            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid original user login."
            );
            return;
        }


        // Find existing user
        User existingUser =
                ItemDB.getUserByLogin(originalUserLogin);

        if (existingUser == null) {

            resp.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "User not found."
            );
            return;
        }


        // ADMIN cannot edit SUPER_ADMIN
        if (loggedInUser.getRole() == UserRole.ADMIN &&
                existingUser.getRole() == UserRole.SUPER_ADMIN) {

            resp.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Admins cannot edit Super Admin users."
            );
            return;
        }


        // Validate form fields
        if (userLogin == null || userLogin.isBlank() ||
                email == null || email.isBlank() ||
                password == null || password.isBlank()) {

            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "All user fields are required."
            );
            return;
        }


        // Convert role
        UserRole newRole;

        try {

            newRole = UserRole.valueOf(
                    roleValue.trim().toUpperCase()
            );

        } catch (IllegalArgumentException |
                 NullPointerException e) {

            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid user role."
            );
            return;
        }


        // ADMIN cannot promote someone to SUPER_ADMIN
        if (loggedInUser.getRole() == UserRole.ADMIN &&
                newRole == UserRole.SUPER_ADMIN) {

            resp.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Admins cannot assign the Super Admin role."
            );
            return;
        }


        // Update database
        ItemDB.updateUser(
                userLogin,
                email,
                password,
                newRole
        );


        // Success message
        req.getSession().setAttribute(
                "successMessage",
                "The user changes were saved successfully."
        );


        // Return to Manage Users
        resp.sendRedirect(
                req.getContextPath() + "/users?action=list"
        );
    }


    /*
     * DELETE USER
     */
    private void deleteUser(HttpServletRequest req,
                            HttpServletResponse resp,
                            User loggedInUser)
            throws IOException {

        int userID;

        try {
            userID = Integer.parseInt(req.getParameter("userID"));
        } catch (NumberFormatException e) {

            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid user ID."
            );
            return;
        }

        User userToDelete = ItemDB.getUser(userID);

        if (userToDelete == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // ADMIN cannot delete SUPER_ADMIN
        if (loggedInUser.getRole() == UserRole.ADMIN &&
                userToDelete.getRole() == UserRole.SUPER_ADMIN) {

            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Prevent someone from deleting their own account
        if (userToDelete.getUserLogin()
                .equals(loggedInUser.getUserLogin())) {

            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "You cannot delete your own account."
            );
            return;
        }

        ItemDB.deleteUser(userID);

        resp.sendRedirect(
                req.getContextPath() + "/users?action=list"
        );
    }


    /*
     * ARCHIVE USER
     */
    private void archiveUser(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException {

        String userLogin = req.getParameter("userLogin");

        if (userLogin == null || userLogin.isBlank()) {
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Missing userLogin"
            );
            return;
        }

        boolean archived = ItemDB.archiveUser(userLogin);

        if (!archived) {
            resp.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Unable to archive user"
            );
            return;
        }

        resp.sendRedirect(
                req.getContextPath() + "/users?action=list"
        );
    }

    private void viewUser(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        String userLogin = req.getParameter("userLogin");

        if (userLogin == null || userLogin.isBlank()) {

            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Missing user login"
            );

            return;
        }

        User employee = ItemDB.getUserByLogin(userLogin);

        if (employee == null) {

            resp.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Employee not found"
            );

            return;
        }

        req.setAttribute("employee", employee);

        req.getRequestDispatcher(
                "/WEB-INF/jsp/employeeProfile.jsp"
        ).forward(req, resp);
    }

    private void showAddUser(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("roles", UserRole.values());

        req.getRequestDispatcher(
                "/WEB-INF/jsp/employeeProfileForm.jsp"
        ).forward(req, resp);
    }
}