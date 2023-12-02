package ru.itis.servlets;

import ru.itis.TourManager;
import ru.itis.models.Tour;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/Admin")
public class AdminServlet extends HttpServlet {

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "19707194";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tour_booking?useUnicode=true&characterEncoding=UTF-8";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");

            String action = request.getParameter("action");

            if ("addTour".equals(action)) {
                String tourName = request.getParameter("tourName");
                String tourDescription = request.getParameter("tourDescription");
                int tourPrice = Integer.parseInt(request.getParameter("tourPrice"));
                String tourDirection = request.getParameter("tourDirection");
                String tourCountry = request.getParameter("tourCountry");
                String tourTown = request.getParameter("tourTown");
                String tourStartDate = request.getParameter("tourStartDate");
                String tourEndDate = request.getParameter("tourEndDate");

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                        String sql = "INSERT INTO tours (name, description, price, direction, country, town, start_date, end_date)" +
                                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, tourName);
                            statement.setString(2, tourDescription);
                            statement.setInt(3, tourPrice);
                            statement.setString(4, tourDirection);
                            statement.setString(5, tourCountry);
                            statement.setString(6, tourTown);
                            statement.setString(7, tourStartDate);
                            statement.setString(8, tourEndDate);

                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                System.out.println("Tour added successfully!");

                                Tour tour = Tour.builder()
                                        .name(tourName)
                                        .description(tourDescription)
                                        .price(tourPrice)
                                        .direction(tourDirection)
                                        .country(tourCountry)
                                        .town(tourTown)
                                        .startDate(tourStartDate)
                                        .endDate(tourEndDate)
                                        .build();
                                TourManager.addTour(tour);

                            } else {
                                System.out.println("Failed to add tour.");
                            }
                        }
                    }

                    List<Tour> updatedTours = TourManager.getTours();
                    request.getSession().setAttribute("tours", TourManager.getTours());
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            } else if ("removeTour".equals(action)) {
                int tourIdToRemove = Integer.parseInt(request.getParameter("tourIdToRemove"));
                TourManager.removeTour(tourIdToRemove);

                List<Tour> updatedTours = TourManager.getTours();

                request.getSession().setAttribute("tours", updatedTours);
            }

            response.sendRedirect("admin.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
