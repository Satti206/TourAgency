package ru.itis.servlets;

import ru.itis.models.Tour;
import ru.itis.repository.ToursRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToursRepositoryJdbcImpl implements ToursRepository {
    private Connection connection;
    private static final String SQL_SELECT_ALL_TOURS = "SELECT * FROM tours";
    private static final String SQL_INSERT_INTO_TOURS = "INSERT INTO tours (name, description, price, direction, country, town, start_date, end_date)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_TOUR = "DELETE FROM tours WHERE id = ?";

    public ToursRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Tour> getAllTours() {
        List<Tour> tourList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_TOURS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Tour tour = mapResultSetToTour(resultSet);
                tourList.add(tour);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tourList;
    }

    @Override
    public void addTour(Tour tour) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_INTO_TOURS, Statement.RETURN_GENERATED_KEYS)) {
            setTourParameters(preparedStatement, tour);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating tour failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tour.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating tour failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeTour(Tour tour) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_TOUR)) {
            preparedStatement.setLong(1, tour.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Tour mapResultSetToTour(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        int price = resultSet.getInt("price");
        String direction = resultSet.getString("direction");
        String country = resultSet.getString("country");
        String town = resultSet.getString("town");
        String startDate = resultSet.getString("start_date");
        String endDate = resultSet.getString("end_date");

        return Tour.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .direction(direction)
                .country(country)
                .town(town)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    private void setTourParameters(PreparedStatement preparedStatement, Tour tour) throws SQLException {
        preparedStatement.setString(1, tour.getName());
        preparedStatement.setString(2, tour.getDescription());
        preparedStatement.setInt(3, tour.getPrice());
        preparedStatement.setString(4, tour.getDirection());
        preparedStatement.setString(5, tour.getCountry());
        preparedStatement.setString(6, tour.getTown());
        preparedStatement.setString(7, tour.getStartDate());
        preparedStatement.setString(8, tour.getEndDate());
    }
}

