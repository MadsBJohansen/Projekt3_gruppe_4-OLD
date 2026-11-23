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
            SELECT cm.car_model_id, cm.name, cm.model_year, cm.car_brand_id,
                   cb.name AS brand_name
            FROM car_model cm
            JOIN car_brand cb ON cm.car_brand_id = cb.car_brand_id
            ORDER BY cb.name, cm.name, cm.model_year
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new CarModel(
                rs.getInt("car_model_id"),
                rs.getString("name"),
                rs.getInt("model_year"),
                rs.getInt("car_brand_id"),
                rs.getString("brand_name")
        ));
    }
}
