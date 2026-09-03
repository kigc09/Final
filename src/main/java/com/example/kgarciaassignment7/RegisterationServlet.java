package com.example.kgarciaassignment7;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@WebServlet("/item")
@MultipartConfig
public class RegisterationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");

        String action = req.getParameter("action");

        if (action == null) {
            action = "list";
        }



        switch (action) {
            case "register" -> registerForm(req, resp);
            case "view" -> viewItem(req, resp);
            case "downloadImage" -> downloadImage(req, resp);
            case "modify" -> modifyForm(req, resp);
            case "delete" -> deleteItem(req, resp);
            case "filter" -> filterItems(req, resp);
            case "search" -> searchItems(req, resp);
            default -> listItems(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "register" -> registerItem(req, resp);
            case "modify" -> modifyItem(req, resp);
            case "inventory" -> updateInventory(req, resp);
            default -> resp.sendRedirect(
                    req.getContextPath() + "/item?action=list"
            );
        }
    }

    private void registerForm(HttpServletRequest req,
                              HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("siteTitle", "Add an Item");

        req.getRequestDispatcher("/WEB-INF/jsp/registrationForm.jsp")
                .forward(req, resp);
    }

    private void registerItem(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {

            int id = Integer.parseInt(req.getParameter("itemID"));

            String name = req.getParameter("itemName");

            String manufacturer = req.getParameter("manufacturer");

            double price = Double.parseDouble(req.getParameter("itemPrice"));

            int inventory = Integer.parseInt(req.getParameter("itemInventory"));

            String type = req.getParameter("itemType");

            System.out.println("=== REGISTER ITEM DEBUG ===");
            System.out.println("id = " + req.getParameter("itemID"));
            System.out.println("name = " + req.getParameter("itemName"));
            System.out.println("manufacturer = " + req.getParameter("manufacturer"));
            System.out.println("price = " + req.getParameter("itemPrice"));
            System.out.println("inventory = " + req.getParameter("itemInventory"));
            System.out.println("type = " + req.getParameter("itemType"));

            Item newItem = new Item(
                    id,
                    name,
                    manufacturer,
                    price,
                    inventory,
                    ItemTitle.valueOf(type)
            );

            Part imageFile = req.getPart("imageFile");

            if (imageFile != null && imageFile.getSize() > 0) {

                Image image = processImage(imageFile);

                if (image != null) {

                    String originalName = image.getName();

                    int dotLocation = originalName.lastIndexOf('.');

                    if (dotLocation >= 0) {

                        String extension =
                                originalName.substring(dotLocation);

                        image.setName(
                                newItem.getName()
                                        + "-Image"
                                        + extension
                        );
                    }

                    newItem.setImage(image);
                }
            }

            System.out.println("Calling ItemDB.addItem()");

            boolean added = ItemDB.addItem(newItem);

            if (added) {
                resp.sendRedirect(
                        req.getContextPath() + "/item"
                );
                return;
            }

            System.out.println("Item added = " + added);

            req.setAttribute(
                    "error",
                    "Item could not be added."
            );

            req.getRequestDispatcher(
                    "/WEB-INF/jsp/registrationForm.jsp"
            ).forward(req, resp);



        } catch (NumberFormatException e) {

            req.setAttribute(
                    "error",
                    "ID, price, and inventory must contain valid numbers."
            );

            req.getRequestDispatcher(
                    "/WEB-INF/jsp/registrationForm.jsp"
            ).forward(req, resp);

        } catch (IllegalArgumentException e) {

            req.setAttribute(
                    "error",
                    "Invalid item type."
            );

            req.getRequestDispatcher(
                    "/WEB-INF/jsp/registrationForm.jsp"
            ).forward(req, resp);
        }
    }

    private void listItems(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute(
                "siteTitle",
                "List of Items"
        );

        req.setAttribute(
                "itemDB",
                ItemDB.getAllItems()
        );

        req.setAttribute(
                "itemTypes",
                ItemTitle.values()
        );

        req.getRequestDispatcher(
                "/WEB-INF/jsp/listItems.jsp"
        ).forward(req, resp);
    }

    private void viewItem(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParameter = req.getParameter("id");

        if (idParameter == null || idParameter.isEmpty()) {

            resp.sendRedirect(
                    req.getContextPath() + "/item?action=list"
            );

            return;
        }

        try {

            int id = Integer.parseInt(idParameter);

            Item current = ItemDB.getItem(String.valueOf(id));

            if (current == null) {

                resp.sendRedirect(
                        req.getContextPath() + "/item?action=list"
                );

                return;
            }

            req.setAttribute(
                    "id",
                    id
            );

            req.setAttribute(
                    "current",
                    current
            );

            req.setAttribute(
                    "siteTitle",
                    current.getName()
            );

            req.getRequestDispatcher(
                    "/WEB-INF/jsp/viewItems.jsp"
            ).forward(req, resp);

        } catch (NumberFormatException e) {

            resp.sendRedirect(
                    req.getContextPath() + "/item?action=list"
            );
        }
    }

    private void downloadImage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParameter = req.getParameter("id");

        if (idParameter == null || idParameter.isEmpty()) {

            resp.sendRedirect(
                    req.getContextPath() + "/item?action=list"
            );

            return;
        }

        try {

            int id = Integer.parseInt(idParameter);

            Item current = ItemDB.getItem(String.valueOf(id));

            if (current == null || current.getImage() == null) {

                resp.sendRedirect(
                        req.getContextPath() + "/item?action=list"
                );

                return;
            }

            Image image = current.getImage();

            resp.setHeader(
                    "Content-Disposition",
                    "attachment; filename=\"" + image.getName() + "\""
            );

            resp.setContentType(
                    "application/octet-stream"
            );

            ServletOutputStream out =
                    resp.getOutputStream();

            out.write(
                    image.getContents()
            );

            out.flush();

        } catch (NumberFormatException e) {

            resp.sendRedirect(
                    req.getContextPath() + "/item?action=list"
            );
        }
    }

    private Image processImage(Part file)
            throws IOException {

        try (InputStream in = file.getInputStream();
             ByteArrayOutputStream out =
                     new ByteArrayOutputStream()) {

            byte[] bytes = new byte[4096];

            int read;

            while ((read = in.read(bytes)) != -1) {
                out.write(
                        bytes,
                        0,
                        read
                );
            }

            Image image = new Image();

            image.setName(
                    file.getSubmittedFileName()
            );

            image.setContents(
                    out.toByteArray()
            );

            return image;
        }
    }

    private void modifyForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParameter = req.getParameter("id");

        if (idParameter == null || idParameter.isEmpty()) {
            resp.sendRedirect(
                    req.getContextPath() + "/item?action=list"
            );
            return;
        }

        try {
            int id = Integer.parseInt(idParameter);

            Item current = ItemDB.getItem(String.valueOf(id));

            if (current == null) {
                resp.sendRedirect(
                        req.getContextPath() + "/item?action=list"
                );
                return;
            }

            req.setAttribute("current", current);
            req.setAttribute("siteTitle", "Modify " + current.getName());

            req.getRequestDispatcher(
                    "/WEB-INF/jsp/modifyItem.jsp"
            ).forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(
                    req.getContextPath() + "/item?action=list"
            );
        }
    }

    private void modifyItem(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("itemID"));
        String name = req.getParameter("itemName");
        String manufacturer = req.getParameter("manufacturer");
        double price = Double.parseDouble(req.getParameter("itemPrice"));
        int inventory = Integer.parseInt(req.getParameter("itemInventory"));
        ItemTitle type = ItemTitle.valueOf(req.getParameter("itemType"));

        // Get the existing item
        Item existingItem = ItemDB.getItem(String.valueOf(id));

        Item item = new Item(
                id,
                name,
                manufacturer,
                price,
                inventory,
                type
        );

        Part imageFile = req.getPart("imageFile");

        System.out.println("Image size: "
                + (imageFile != null ? imageFile.getSize() : "NULL"));

        if (imageFile != null && imageFile.getSize() > 0) {

            Image image = processImage(imageFile);

            if (image != null) {

                String originalName = image.getName();
                int dotLocation = originalName.lastIndexOf('.');

                if (dotLocation >= 0) {
                    String extension = originalName.substring(dotLocation);

                    image.setName(
                            item.getName() + "-Image" + extension
                    );
                }

                item.setImage(image);

                System.out.println(
                        "NEW IMAGE: " + image.getName()
                );
            }

        } else if (existingItem != null) {

            // No new image selected, keep existing image
            item.setImage(existingItem.getImage());

            System.out.println("Keeping existing image");
        }

        ItemDB.updateItem(item);

        req.getSession().setAttribute(
                "updateMessage",
                "Item updated successfully."
        );

        resp.sendRedirect(
                req.getContextPath() + "/item?action=view&id=" + id
        );
    }

    private boolean isManagerOrAdmin(HttpServletRequest req) {

        HttpSession session = req.getSession(false);

        if (session == null) {
            return false;
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return false;
        }

        return user.getRole() == UserRole.MANAGER
                || user.getRole() == UserRole.ADMIN;
    }

    private void deleteItem(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idParameter = req.getParameter("id");

        if (idParameter == null || idParameter.isEmpty()) {
            resp.sendRedirect(
                    req.getContextPath() + "/item?action=list"
            );
            return;
        }

        try {
            int id = Integer.parseInt(idParameter);

            ItemDB.deleteItem(id);

            req.getSession().setAttribute(
                    "deleteMessage",
                    "Item deleted successfully."
            );

            resp.sendRedirect(
                    req.getContextPath() + "/item?action=list"
            );

        } catch (NumberFormatException e) {
            resp.sendRedirect(
                    req.getContextPath() + "/item?action=list"
            );
        }
    }

    private void updateInventory(HttpServletRequest req,
                                 HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int id = Integer.parseInt(req.getParameter("itemID"));
        int inventory = Integer.parseInt(req.getParameter("itemInventory"));

        ItemDB.updateInventory(id, inventory);

        resp.sendRedirect(
                req.getContextPath() + "/item?action=view&id=" + id
        );
    }

    private void filterItems(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String type = req.getParameter("itemType");

        Map<Integer, Item> itemDB;

        if (type != null && !type.isBlank()) {
            itemDB = ItemDB.filterItems(type);
        } else {
            itemDB = ItemDB.getAllItems();
        }

        req.setAttribute("siteTitle", "List of Items");
        req.setAttribute("itemDB", itemDB);
        req.setAttribute("itemTypes", ItemTitle.values());

        req.getRequestDispatcher("/WEB-INF/jsp/listItems.jsp")
                .forward(req, resp);
    }

    private void searchItems(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String search = req.getParameter("search");

        Map<Integer, Item> itemDB;

        if (search != null && !search.isBlank()) {
            itemDB = ItemDB.searchItems(search);
        } else {
            itemDB = ItemDB.getAllItems();
        }

        req.setAttribute("itemDB", itemDB);
        req.setAttribute("itemTypes", ItemTitle.values());

        req.getRequestDispatcher("/WEB-INF/jsp/listItems.jsp")
                .forward(req, resp);
    }
}