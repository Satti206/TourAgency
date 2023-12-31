package ru.itis.servlets;

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

@WebServlet("/Register")
public class RegistrationServlet extends HttpServlet {
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "19707194";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tour_booking?useUnicode=true&characterEncoding=UTF-8";


    private UsersRepository usersRepository;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            usersRepository = new UsersRepositoryJdbcImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/jsp/registration.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String lastname = request.getParameter("lastname");
        String role = request.getParameter("role");

        User newUser = User.builder()
                .email(email)
                .password(password)
                .name(name)
                .lastname(lastname)
                .role(role)
                .build();

        usersRepository.save(newUser);
        response.sendRedirect("Login");
    }
    @Override
    public void destroy() {
        try {
            if (usersRepository != null) {
                usersRepository.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
