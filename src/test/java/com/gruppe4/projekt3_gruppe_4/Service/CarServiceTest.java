package com.gruppe4.projekt3_gruppe_4.Service;

import com.gruppe4.projekt3_gruppe_4.Model.Car;
import com.gruppe4.projekt3_gruppe_4.Repo.CarRepo;
import com.gruppe4.projekt3_gruppe_4.enums.CarStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepo carRepo;

    @InjectMocks
    private CarService carService;

    @Test
    void createCar_validInput_returnsGeneratedIdAndSetsAvailable() {
        // Arrange
        Car car = new Car(
                "TEST1234567890123",
                1,
                "Comfort",
                25000,
                new BigDecimal("250000.00"),
                new BigDecimal("120.0"),
                "ZZ98765",
                1
        );

        when(carRepo.saveCar(car)).thenReturn(42);

        // Act
        int carId = carService.createCar(car);

        // Assert
        assertEquals(42, carId);
        verify(carRepo).saveCar(car);
        verify(carRepo).updateCarStatus(42, CarStatus.AVAILABLE);
    }

    @Test
    void createCar_existingVin_throwsExceptionWithoutSaving() {
        // Arrange
        Car car = new Car(
                "TEST1234567890123",
                1,
                "Comfort",
                25000,
                new BigDecimal("250000.00"),
                new BigDecimal("120.0"),
                "ZZ98765",
                1
        );

        when(carRepo.existsByVin(car.getVin())).thenReturn(true);

        // Act and Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> carService.createCar(car)
        );

        verify(carRepo, never()).saveCar(any());
        verify(carRepo, never()).updateCarStatus(anyInt(), any());
    }

    @Test
    void createCar_existingLicensePlate_throwsExceptionWithoutSaving() {
        // Arrange
        Car car = new Car(
                "TEST1234567890123",
                1,
                "Comfort",
                25000,
                new BigDecimal("250000.00"),
                new BigDecimal("120.0"),
                "ZZ98765",
                1
        );

        when(carRepo.existsByLicensePlate(car.getLicensePlate()))
                .thenReturn(true);

        // Act and Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> carService.createCar(car)
        );

        verify(carRepo, never()).saveCar(any());
        verify(carRepo, never()).updateCarStatus(anyInt(), any());
    }

    @Test
    void createCar_blankVin_throwsExceptionWithoutCallingRepository() {
        // Arrange
        Car car = new Car(
                "   ",
                1,
                "Comfort",
                25000,
                new BigDecimal("250000.00"),
                new BigDecimal("120.0"),
                "ZZ98765",
                1
        );

        // Act and Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> carService.createCar(car)
        );

        verifyNoInteractions(carRepo);
    }

    @Test
    void createCar_blankLicensePlate_throwsExceptionWithoutCallingRepository() {
        // Arrange
        Car car = new Car(
                "TEST1234567890123",
                1,
                "Comfort",
                25000,
                new BigDecimal("250000.00"),
                new BigDecimal("120.0"),
                "   ",
                1
        );

        // Act and Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> carService.createCar(car)
        );

        verifyNoInteractions(carRepo);
    }
}