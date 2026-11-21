package com.gruppe4.projekt3_gruppe_4.Repo;

import com.gruppe4.projekt3_gruppe_4.Model.CarModel;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CarModelRepo {

    private final JdbcTemplate jdbcTemplate;

    public CarModelRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CarModel> findAllCarModels() {
        String sql = """
            SELECT car_model_id, name, model_year, car_brand_id
            FROM car_model
            ORDER BY name, model_year
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new CarModel(
                rs.getInt("car_model_id"),
                rs.getString("name"),
                rs.getInt("model_year"),
                rs.getInt("car_brand_id")
        ));
    }
}