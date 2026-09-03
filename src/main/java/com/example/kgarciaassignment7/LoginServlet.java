package com.example.kgarciaassignment7;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // check if logged in
        HttpSession session = req.getSession();

        if (req.getParameter("logout") != null) {
            session.invalidate();
            for (Cookie c : req.getCookies()) {
                if (c.getName().equals("userIDCookie")) {
                    c.setMaxAge(0);
                    c.setPath("/");
                    resp.addCookie(c);
                    ItemDB.removeCookie(c.getValue());
                    break;
                }
            }
            resp.sendRedirect("login");
            return;
        }

        req.setAttribute("siteTitle", "User Login");
        req.getRequestDispatcher("WEB-INF/jsp/loginForm.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        if (session.getAttribute("user") != null) {
            resp.sendRedirect("item?action=register");
            return;
        }

        String userLogin = req.getParameter("userLogin");
        String password = req.getParameter("password");

        User user = ItemDB.getUser(userLogin, password);

        if (user != null) {

            session.setAttribute("user", user);

            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;

        } else {

            req.setAttribute("loginError", "Invalid username or password.");

            req.getRequestDispatcher("/WEB-INF/jsp/loginForm.jsp")
                    .forward(req, resp);
        }

        System.out.println("=== LOGIN DEBUG ===");
        System.out.println("userLogin: " + userLogin);
        System.out.println("password: " + password);

        User foundUser = ItemDB.getUser(userLogin, password);

        System.out.println("foundUser: " + foundUser);

        if (userLogin == null || userLogin.isBlank() || password == null || password.isBlank() || foundUser == null) {
            req.setAttribute("loginFailed", true);
            req.getRequestDispatcher("/WEB-INF/jsp/loginForm.jsp")
                    .forward(req, resp);
            return;
        }

        session.setAttribute("user", foundUser);

        req.changeSessionId();

        Cookie c = new Cookie("userIDCookie", session.getId());
        c.setMaxAge(60 * 60 * 24 * 30);
        c.setPath("/");
        resp.addCookie(c);

        resp.sendRedirect("item?action=register");
    }
}
