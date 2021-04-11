package by.stepanov.navy.dao.impl;

import by.stepanov.navy.dao.BaseDao;
import by.stepanov.navy.dao.PortDao;
import by.stepanov.navy.model.entity.Port;
import by.stepanov.navy.model.mapper.PortMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PortDaoImpl extends BaseDao implements PortDao {

    @Override
    public List<Port> selectAllPorts() {
        final String sql = "SELECT id, name, capacity FROM ports";
        return jdbcTemplate.query(sql, new PortMapper());
    }

    @Override
    public Port selectPortById(long id) {
        final String sql = "SELECT id, name, capacity FROM ports WHERE id =" + id;
        try {
            return jdbcTemplate.queryForObject(sql, new PortMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }
}
