package ru.itis.servlets;

import ru.itis.models.Tour;
import ru.itis.models.User;
import ru.itis.repository.UsersRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/Profile")
public class UserProfileServlet extends HttpServlet {
    private UsersRepository usersRepository;

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "19707194";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tour_booking?useUnicode=true&characterEncoding=UTF-8";
    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            usersRepository = new UsersRepositoryJdbcImpl(connection);
        } catch (SQLException e) {
            throw new ServletException("Error initializing UsersRepository", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userIdString = req.getParameter("userId");

        if (userIdString != null && !userIdString.isEmpty()) {
            try {
                Long userId = Long.parseLong(userIdString);

                Optional<User> userOptional = usersRepository.findById(userId);

                if (userOptional.isPresent()) {
                    req.setAttribute("user", userOptional.get());

                    List<Tour> tours = usersRepository.findAllTours();
                    req.setAttribute("tours", tours);

                    req.getRequestDispatcher("/profile.jsp").forward(req, resp);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid userId format");
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter 'userId' is missing or empty.");
        }
    }

    @Override
    public void destroy() {
        try {
            usersRepository.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}