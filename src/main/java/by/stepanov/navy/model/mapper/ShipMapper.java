package by.stepanov.navy.model.mapper;

import by.stepanov.navy.model.entity.Ship;
import by.stepanov.navy.model.enums.ShipStatusType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShipMapper implements RowMapper<Ship> {
    @Override
    public Ship mapRow(ResultSet resultSet, int i) throws SQLException {
        final Ship ship = new Ship();
        ship.setId(resultSet.getLong("id"));
        ship.setName(resultSet.getString("name"));
        ship.setStatus(ShipStatusType.getShipStatusType(resultSet.getString("status")));
        ship.setMinSailors(resultSet.getInt("min_sailors"));
        ship.setMaxSailors(resultSet.getInt("max_sailors"));
        final Long portId = resultSet.getLong("port_id");

        if (!resultSet.wasNull()){
            ship.setPortId(portId);
        }
        return ship;
    }
}
