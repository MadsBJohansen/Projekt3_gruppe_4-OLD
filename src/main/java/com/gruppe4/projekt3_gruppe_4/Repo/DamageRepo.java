package com.gruppe4.projekt3_gruppe_4.Repo;

import com.gruppe4.projekt3_gruppe_4.Model.Damage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DamageRepo {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Damage> damageRowMapper = (rs, rowNum) ->
            new Damage(rs.getInt("damage_id"),
                    rs.getInt("damage_report_id"),
                    rs.getString("description"),
                    rs.getBigDecimal("repair_cost"));

    public DamageRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveDamage(Damage damage) {
        String sql = "INSERT INTO damage(damage_report_id, description, repair_cost)" +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                damage.getDamageReportId(),
                damage.getDescription(),
                damage.getRepairCost());

    }
}
