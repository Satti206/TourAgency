package ru.itis;

import ru.itis.models.Tour;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TourManager {
    private static List<Tour> tours = new ArrayList<>();
    private static String DB_URL = "jdbc:mysql://localhost:3306/tour_booking?useUnicode=true&characterEncoding=UTF-8";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "19707194";

    public static List<Tour> getTours() {
        return tours;
    }

    public static void addTour(Tour tour) {
        tours.add(tour);
    }

    public static void removeTour(int tourId) {
        Long idToRemove = (long) tourId;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "DELETE FROM tours WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setLong(1, idToRemove);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Tour removed from the database successfully!");
                    } else {
                        System.out.println("Tour not found in the database.");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        tours.removeIf(tour -> {
            Long id = tour.getId();
            return id != null && id.equals(idToRemove);
        });
    }
}

