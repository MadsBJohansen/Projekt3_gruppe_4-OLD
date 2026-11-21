package com.gruppe4.projekt3_gruppe_4.Service;

import com.gruppe4.projekt3_gruppe_4.enums.CarStatus;
import com.gruppe4.projekt3_gruppe_4.Model.Car;
import com.gruppe4.projekt3_gruppe_4.Repo.CarRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarService {
    private final CarRepo carRepo;

    public CarService(CarRepo carRepo) {
        this.carRepo = carRepo;
    }

    // Transactional: saves the car and its initial status in one transaction.
    // If either operation fails, both changes are rolled back.

    @Transactional
    public int createCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car must not be null");
        }

        if (car.getVin() == null || car.getVin().isBlank()) {
            throw new IllegalArgumentException("VIN must not be blank");
        }

        if (car.getLicensePlate() == null || car.getLicensePlate().isBlank()) {
            throw new IllegalArgumentException("License plate must not be blank");
        }

        if (car.getTrimLevel() == null || car.getTrimLevel().isBlank()) {
            throw new IllegalArgumentException("Trim level must not be blank");
        }

        if (car.getBasePrice() == null || car.getBasePrice().signum() <= 0) {
            throw new IllegalArgumentException("Base price must be greater than zero");
        }

        if (car.getCo2Emission() == null || car.getCo2Emission().signum() < 0) {
            throw new IllegalArgumentException("CO2 emission must be zero or greater");
        }

        if (car.getMileage() < 0) {
            throw new IllegalArgumentException("Mileage must be zero or greater");
        }

        if (car.getVin().length() != 17) {
            throw new IllegalArgumentException("VIN must contain exactly 17 characters");
        }

        if (car.getLicensePlate().length() > 10) {
            throw new IllegalArgumentException("License plate must not exceed 10 characters");
        }

        if (car.getCarModelId() <= 0) {
            throw new IllegalArgumentException("A car model must be selected");
        }

        if (car.getCurrentLocationId() <= 0) {
            throw new IllegalArgumentException("A location must be selected");
        }

        if (carRepo.existsByVin(car.getVin())) {
            throw new IllegalArgumentException("VIN already exists");
        }

        if (carRepo.existsByLicensePlate(car.getLicensePlate())) {
            throw new IllegalArgumentException("License plate already exists");
        }

        int carId = carRepo.saveCar(car);
        carRepo.updateCarStatus(carId, CarStatus.AVAILABLE);
        return carId;
    }

    public void changeStatus(int carId){

    }
}
