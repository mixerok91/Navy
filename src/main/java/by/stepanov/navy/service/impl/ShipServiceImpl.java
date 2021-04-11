package by.stepanov.navy.service.impl;

import by.stepanov.navy.dao.PortDao;
import by.stepanov.navy.dao.SeamanDao;
import by.stepanov.navy.dao.ShipDao;
import by.stepanov.navy.model.entity.Port;
import by.stepanov.navy.model.entity.Seaman;
import by.stepanov.navy.model.entity.Ship;
import by.stepanov.navy.model.entity.ShipStatus;
import by.stepanov.navy.model.enums.SeamanRoleType;
import by.stepanov.navy.model.enums.ShipStatusType;
import by.stepanov.navy.service.ShipService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipServiceImpl implements ShipService {

    private final ShipDao shipDao;
    private final PortDao portDao;
    private final SeamanDao seamanDao;

    @Autowired
    public ShipServiceImpl(ShipDao shipDao, PortDao portDao, SeamanDao seamanDao) {
        this.shipDao = shipDao;
        this.portDao = portDao;
        this.seamanDao = seamanDao;
    }

    @Override
    public ResponseEntity<List<Ship>> readAllShips(String status) {
        if (status == null) {
            return ResponseEntity.ok(shipDao.selectAllShips());
        }
        ShipStatusType shipStatusType = ShipStatusType.getShipStatusType(status);
        if (shipStatusType == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(shipDao.selectShipsByStatus(shipStatusType));
    }

    @Override
    public ResponseEntity<String> createShip(Ship ship) {
        if (ship == null || ship.getName() == null || ship.getPortId() == null || ship.getMinSailors() <= 0 || ship.getMaxSailors() < ship.getMaxSailors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        final Port port = portDao.selectPortById(ship.getPortId());

        if (port == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final int shipsInPortCount = shipDao.selectShipsCountByPortId(ship.getPortId());

        if (shipsInPortCount < port.getCapacity()) {
            shipDao.insertShip(ship);
            final Optional<Ship> lastInsertShip = shipDao.selectAllShips().stream().max((s1, s2) -> (int)(s1.getId() - s2.getId()));
            if (lastInsertShip.isPresent()) {
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", lastInsertShip.get().getId());
                return ResponseEntity.ok(jsonObject.toString());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @Override
    public ResponseEntity<String> deleteShip(long id) {
        final Ship ship = shipDao.selectShipById(id);
        if (ship == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (ship.getStatus().equals(ShipStatusType.SEA)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        shipDao.deleteShipById(id);
        return ResponseEntity.ok("");
    }

    @Override
    public ResponseEntity<ShipStatus> readShipStatus(long id) {
        final Ship ship = shipDao.selectShipById(id);
        if (ship == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        final ShipStatus shipStatus = new ShipStatus();
        shipStatus.setStatusName(ship.getStatus().name());
        return ResponseEntity.ok(shipStatus);
    }

    @Override
    public ResponseEntity<ShipStatus> updateShipStatus(long id, Long portId, ShipStatus shipStatus) {
        final Ship ship = shipDao.selectShipById(id);
        if (ship == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ShipStatusType shipStatusType = ShipStatusType.getShipStatusType(shipStatus.getStatusName());
        if (shipStatusType == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<Seaman> seamenOnShip = seamanDao.selectSeamanByShipId(ship.getId());
        switch (shipStatusType) {
            case SEA:
                if (ship.getStatus() != ShipStatusType.SEA) {
                    if (ship.getMinSailors() > seamenOnShip.size() || ship.getMaxSailors() < seamenOnShip.size()) {
                        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
                    }
                    if (seamenOnShip.stream().noneMatch(seaman -> seaman.getRole().equals(SeamanRoleType.CAPTAIN))) {
                        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
                    }
                    ship.setStatus(ShipStatusType.SEA);
                    shipDao.updateShipStatusById(id, shipStatusType);
                    shipDao.updateShipPortIdById(id, null);
                }
                break;
            case PORT:
                if (ship.getStatus() != ShipStatusType.PORT) {
                    if (portId == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }

                    final Port port = portDao.selectPortById(portId);
                    if (port == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }

                    final int shipsInPortCount = shipDao.selectShipsCountByPortId(portId);
                    if (shipsInPortCount < port.getCapacity()) {
                        ship.setStatus(ShipStatusType.PORT);
                        shipDao.updateShipStatusById(id, shipStatusType);
                        shipDao.updateShipPortIdById(id, portId);
                    } else {
                        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
                    }
                }
                break;
            default:
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        ShipStatus newShipStatus = new ShipStatus();
        newShipStatus.setStatusName(ship.getStatus().name());
        return ResponseEntity.ok(newShipStatus);
    }
}
