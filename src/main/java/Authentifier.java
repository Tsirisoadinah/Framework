package src.main.java;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class Authentifier extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String nom = req.getParameter("nom");
        String motdepasse = req.getParameter("motdepasse");

        if (nom == "" && motdepasse == "") {
            res.sendRedirect("index.html");
        } else {
            HttpSession session = req.getSession();
            session.setAttribute("nom", nom);
            session.setAttribute("motdepasse", motdepasse);
            res.sendRedirect("authentifier");
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        HttpSession session = req.getSession();
        String nom = (String) session.getAttribute("nom");
        String motdepasse = (String) session.getAttribute("motdepasse");
        if (nom == "" && motdepasse == "") {
            out.println("Vous n'êtes pas authentifier");
        } else {
            out.println("Bonjour " + nom);
        }
    }
}
