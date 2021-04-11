package by.stepanov.navy.model.mapper;

import by.stepanov.navy.model.entity.Port;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PortMapper implements RowMapper<Port> {
    @Override
    public Port mapRow(ResultSet resultSet, int i) throws SQLException {
        final Port port = new Port();
        port.setId(resultSet.getLong("id"));
        port.setCapacity(resultSet.getInt("capacity"));
        port.setName(resultSet.getString("name"));
        return port;
    }
}
