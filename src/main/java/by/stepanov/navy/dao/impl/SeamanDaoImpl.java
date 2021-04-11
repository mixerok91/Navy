package by.stepanov.navy.dao.impl;

import by.stepanov.navy.dao.BaseDao;
import by.stepanov.navy.dao.SeamanDao;
import by.stepanov.navy.model.entity.Seaman;
import by.stepanov.navy.model.enums.SeamanRoleType;
import by.stepanov.navy.model.mapper.SeamanMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class SeamanDaoImpl extends BaseDao implements SeamanDao {

    @Override
    public List<Seaman> selectAllSeamen() {
        final String sql = "SELECT id, name, role, ship_id FROM seamen";
        return jdbcTemplate.query(sql, new SeamanMapper());
    }

    @Override
    public List<Seaman> selectSeamanByRole(SeamanRoleType roleType) {
        if (roleType == null){
            return Collections.emptyList();
        }
        if (SeamanRoleType.getSeamanRoleType(roleType.toString()) == null) {
            return Collections.emptyList();
        }
        final String sql = "SELECT id, name, role, ship_id FROM seamen WHERE role='" + roleType.name() + "'";
        return jdbcTemplate.query(sql, new SeamanMapper());
    }

    @Override
    public List<Seaman> selectSeamanByShipId(long shipId) {
        if (shipId == 0){
            return Collections.emptyList();
        }
        final String sql = "SELECT id, name, role, ship_id FROM seamen WHERE ship_id=" + shipId;
        return jdbcTemplate.query(sql, new SeamanMapper());
    }

    @Override
    public Seaman selectSeamanById(long id) {
        final String sql = "SELECT id, name, role, ship_id FROM seamen WHERE id=" + id;
        try {
            return jdbcTemplate.queryForObject(sql, new SeamanMapper());
        } catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public void insertSeaman(Seaman seaman) {
        if (seaman == null || seaman.getName() == null || seaman.getRole() == null) {
            return;
        }
        final String sql = String.format("INSERT INTO seamen (name, role) VALUES ('%s', '%s')",
                seaman.getName(), seaman.getRole().name());
        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteSeaman(long id) {
        final String sql = "DELETE FROM seamen WHERE id=" + id;
        jdbcTemplate.update(sql);
    }

    @Override
    public void updateSeamanShipIdById(long seamanId, long shipId) {
        if (shipId <= 0) {
            return;
        }
        final String sql = String.format("UPDATE seamen SET ship_id=%s WHERE id=%s", shipId, seamanId);
        jdbcTemplate.update(sql);
    }

    @Override
    public void updateSeamanRoleById(long seamanId, SeamanRoleType roleType) {
        if (SeamanRoleType.getSeamanRoleType(roleType.toString()) == null) {
            return;
        }
        final String sql = String.format("UPDATE seamen SET role='%s' WHERE id=%s", roleType.name(), seamanId);
        jdbcTemplate.update(sql);
    }
}
