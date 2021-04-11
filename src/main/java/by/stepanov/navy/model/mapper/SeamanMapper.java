package by.stepanov.navy.model.mapper;

import by.stepanov.navy.model.entity.Seaman;
import by.stepanov.navy.model.enums.SeamanRoleType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeamanMapper implements RowMapper<Seaman> {
    @Override
    public Seaman mapRow(ResultSet resultSet, int i) throws SQLException {
        final Seaman seaman = new Seaman();
        seaman.setId(resultSet.getLong("id"));
        seaman.setName(resultSet.getString("name"));
        seaman.setRole(SeamanRoleType.getSeamanRoleType(resultSet.getString("role")));
        final Long shipId = resultSet.getLong("ship_id");

        if (!resultSet.wasNull()) {
            seaman.setShipId(shipId);
        }
        return seaman;
    }
}
