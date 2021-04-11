package by.stepanov.navy.dao.impl;

import by.stepanov.navy.dao.BaseDao;
import by.stepanov.navy.dao.ShipDao;
import by.stepanov.navy.model.entity.Ship;
import by.stepanov.navy.model.enums.ShipStatusType;
import by.stepanov.navy.model.mapper.ShipMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class ShipDaoImpl extends BaseDao implements ShipDao {

    @Override
    public List<Ship> selectAllShips() {
        final String sql = "SELECT id, name, port_id, status, min_sailors, max_sailors FROM ships";
        System.out.println("i'm here!!!!!!!!!!!");
        return jdbcTemplate.query(sql, new ShipMapper());
    }

    @Override
    public List<Ship> selectShipsByStatus(ShipStatusType shipStatus) {
        if (shipStatus == null){
            return Collections.emptyList();
        }
        final String sql = "SELECT id, name, port_id, status, min_sailors, max_sailors FROM ships WHERE status='" + shipStatus.name() + "'";
        return jdbcTemplate.query(sql, new ShipMapper());
    }

    @Override
    public int selectShipsCountByPortId(long portId) {
        final String sql = String.format("SELECT COUNT(ID) FROM ships WHERE port_id=%s AND status='%s'", portId, ShipStatusType.PORT.name());
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (DataAccessException e){
            return 0;
        }
    }

    @Override
    public Ship selectShipById(long shipId) {
        final String sql = "SELECT id, name, port_id, status, min_sailors, max_sailors FROM ships WHERE id=" + shipId;
        try {
            return jdbcTemplate.queryForObject(sql, new ShipMapper());
        } catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public void insertShip(Ship ship) {
        if (ship == null || ship.getName() == null || ship.getPortId() == null || ship.getMinSailors() <= 0 || ship.getMaxSailors() < ship.getMinSailors()) {
            return;
        }
        final String sql = String.format("INSERT INTO ships (name, port_id, status, min_sailors, max_sailors) VALUES ('%s', %s, '%s', %s, %s)",
                ship.getName(), ship.getPortId(), ShipStatusType.PORT.name(), ship.getMinSailors(), ship.getMaxSailors());
        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteShipById(long shipId) {
        final String sql = "DELETE FROM ships WHERE id=" + shipId;
        jdbcTemplate.update(sql);
    }

    @Override
    public void updateShipStatusById(long shipId, ShipStatusType status) {
        if (status == null) {
            return;
        }
        final String sql = String.format("UPDATE ships SET status='%s' WHERE id=%s", status.name(), shipId);
        jdbcTemplate.update(sql);
    }

    @Override
    public void updateShipPortIdById(long shipId, Long portId) {
        final String sql = String.format("UPDATE ships SET port_id=%s WHERE id=%s", portId, shipId);
        jdbcTemplate.update(sql);
    }
}
