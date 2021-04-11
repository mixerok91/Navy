package by.stepanov.navy.dao;

import by.stepanov.navy.model.entity.Ship;
import by.stepanov.navy.model.enums.ShipStatusType;

import java.util.List;

public interface ShipDao {

    List<Ship> selectAllShips();
    List<Ship> selectShipsByStatus(ShipStatusType shipStatus);

    int selectShipsCountByPortId(long portId);

    Ship selectShipById(long shipId);
    void insertShip(Ship ship);
    void deleteShipById(long shipId);

    void updateShipStatusById(long shipId, ShipStatusType status);
    void updateShipPortIdById(long shipId, Long portId);
}
