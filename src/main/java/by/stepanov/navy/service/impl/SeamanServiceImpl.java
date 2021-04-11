package by.stepanov.navy.service.impl;

import by.stepanov.navy.dao.SeamanDao;
import by.stepanov.navy.dao.ShipDao;
import by.stepanov.navy.model.entity.Seaman;
import by.stepanov.navy.model.entity.Ship;
import by.stepanov.navy.model.enums.SeamanRoleType;
import by.stepanov.navy.model.enums.ShipStatusType;
import by.stepanov.navy.service.SeamanService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeamanServiceImpl implements SeamanService {

    private final ShipDao shipDao;
    private final SeamanDao seamanDao;

    @Autowired
    public SeamanServiceImpl(ShipDao shipDao, SeamanDao seamanDao) {
        this.shipDao = shipDao;
        this.seamanDao = seamanDao;
    }


    @Override
    public ResponseEntity<List<Seaman>> reaAllSeamen(String role, Long shipId) {
        if (role == null && shipId == null) {
            return ResponseEntity.ok(seamanDao.selectAllSeamen());
        }
        if (shipId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (role == null) {
            return ResponseEntity.ok(seamanDao.selectSeamanByShipId(shipId));
        }
        SeamanRoleType seamanRole = SeamanRoleType.getSeamanRoleType(role);
        if (seamanRole == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(seamanDao.selectSeamanByRole(seamanRole));
    }

    @Override
    public ResponseEntity<Seaman> readSeaman(long seamanId) {
        if (seamanId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(seamanDao.selectSeamanById(seamanId));
    }

    @Override
    public ResponseEntity<String> createSeaman(Seaman seaman) {
        if (seaman == null || seaman.getName() == null || seaman.getRole() == null || seaman.getShipId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        seamanDao.insertSeaman(seaman);
        final Optional<Seaman> lastInsertSeaman = seamanDao.selectAllSeamen().stream().max((s1, s2) -> (int) (s1.getId() - s2.getId()));
        if (lastInsertSeaman.isPresent()) {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", lastInsertSeaman.get().getId());
            return ResponseEntity.ok(jsonObject.toString());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<String> deleteSeaman(long seamanId) {
        final Seaman seaman = seamanDao.selectSeamanById(seamanId);
        if (seaman == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (seaman.getShipId() == null) {
            seamanDao.deleteSeaman(seamanId);
            return ResponseEntity.ok("");
        }
        final Ship ship = shipDao.selectShipById(seaman.getShipId());
        if (ship == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (ship.getStatus().equals(ShipStatusType.SEA)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        seamanDao.deleteSeaman(seamanId);
        return ResponseEntity.ok("");
    }

    @Override
    public ResponseEntity<String> updateSeamanRole(long seamanId, String seamanRole) {

        final Seaman seaman = seamanDao.selectSeamanById(seamanId);
        if (seaman == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        SeamanRoleType seamanRoleType = SeamanRoleType.getSeamanRoleType(seamanRole);
        if (seamanRoleType == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (seaman.getShipId() == null) {
            seamanDao.updateSeamanRoleById(seamanId, seamanRoleType);
            return ResponseEntity.ok(seamanRoleType.name());
        }

        Ship ship = shipDao.selectShipById(seaman.getShipId());
        if (ship.getStatus().equals(ShipStatusType.PORT)){
            if (seamanRoleType.equals(SeamanRoleType.SAILOR)){
                seamanDao.updateSeamanRoleById(seamanId, seamanRoleType);
                return ResponseEntity.ok(seamanRoleType.name());
            } else {
                List<Seaman> seamen = seamanDao.selectSeamanByRole(SeamanRoleType.CAPTAIN);
                boolean shipHasCaptain = seamen.stream().anyMatch(seaman1 -> seaman1.getShipId().equals(ship.getId()));
                if (!shipHasCaptain) {
                    seamanDao.updateSeamanRoleById(seamanId, seamanRoleType);
                    return ResponseEntity.ok(seamanRoleType.name());
                } else {
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @Override
    public ResponseEntity<String> updateSeamanShip(long seamanId, Long shipId) {

        final Seaman seaman = seamanDao.selectSeamanById(seamanId);
        if (seaman == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //Check on seaman status (in the sea or not)
        if (seaman.getShipId() != null && shipDao.selectShipById(seaman.getShipId()).getStatus().equals(ShipStatusType.SEA)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        final Ship targetShip = shipDao.selectShipById(shipId);
        if (targetShip == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //Check on ship in the sea
        if (targetShip.getStatus().equals(ShipStatusType.SEA)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        final List<Seaman> seamanOnShip = seamanDao.selectSeamanByShipId(shipId);

        //Check on team max count on ship
        if (targetShip.getMaxSailors() <= seamanOnShip.size()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        //check on captain on ship
        if (seaman.getRole().equals(SeamanRoleType.CAPTAIN) && seamanOnShip.stream().anyMatch(seaman1 -> seaman1.getRole().equals(SeamanRoleType.CAPTAIN))) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        seamanDao.updateSeamanShipIdById(seamanId, shipId);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("shipId", shipId);
        return ResponseEntity.ok(jsonObject.toString());
    }

}
