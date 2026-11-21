package com.gruppe4.projekt3_gruppe_4.repo;

import com.gruppe4.projekt3_gruppe_4.Repo.CarModelRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Import(CarModelRepo.class)
class CarModelRepoTest {

    @Autowired
    private CarModelRepo carModelRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void findAllCarModels_returnsInsertedModel() {
        // Arrange
        jdbcTemplate.update(
                "INSERT INTO car_brand (name) VALUES (?)",
                "RepositoryTestBrand"
        );

        Integer brandId = jdbcTemplate.queryForObject(
                "SELECT car_brand_id FROM car_brand WHERE name = ?",
                Integer.class,
                "RepositoryTestBrand"
        );

        assertNotNull(brandId, "Brand ID should not be null after insertion");

        jdbcTemplate.update(
                """
                INSERT INTO car_model (name, model_year, car_brand_id)
                VALUES (?, ?, ?)
                """,
                "RepositoryTestModel", 2024, brandId
        );

        Integer modelId = jdbcTemplate.queryForObject(
                """
                SELECT car_model_id FROM car_model
                WHERE name = ? AND car_brand_id = ?
                """,
                Integer.class,
                "RepositoryTestModel", brandId
        );
        assertNotNull(modelId, "Model ID should not be null after insertion");
        // Act
        var models = carModelRepo.findAllCarModels();

        // Assert
        var model = models.stream()
                .filter(item -> item.getCarModelId() == modelId)
                .findFirst()
                .orElseThrow();

        assertEquals("RepositoryTestModel", model.getName());
        assertEquals(2024, model.getModelYear());

        assertEquals(brandId.intValue(), model.getCarBrandId());
    }

}