package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.storage.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        if (PsqlStore.instOf().findUserByEmail(req.getParameter("name")) == null) {
            try {
                PsqlStore.instOf().createUser(
                        new User(
                                req.getParameter("name"),
                                req.getParameter("email"),
                                req.getParameter("password"))
                );
            } catch (SQLException e) {
                req.setAttribute("error", "Пользователь с таким email уже существует");
                req.getRequestDispatcher("reg.jsp").forward(req, resp);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }

}
