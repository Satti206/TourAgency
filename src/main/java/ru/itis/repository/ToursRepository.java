package ru.itis.repository;

import ru.itis.models.Tour;

import java.util.List;

public interface ToursRepository {
    List<Tour> getAllTours();
    void addTour(Tour tour);
    void removeTour(Tour tour);
}
