package com.gruppe4.projekt3_gruppe_4.Repo;

import com.gruppe4.projekt3_gruppe_4.Model.Location;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepo {
    private final JdbcTemplate jdbcTemplate;

    public LocationRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Location> findAllLocations() {
        return jdbcTemplate.query(
                "SELECT location_id, name FROM location ORDER BY name, location_id",
                (rs, rowNum) -> new Location(rs.getInt("location_id"), rs.getString("name")));
    }

    public boolean existsById(int locationId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM location WHERE location_id = ?", Integer.class, locationId);
        return count != null && count > 0;
    }
}
