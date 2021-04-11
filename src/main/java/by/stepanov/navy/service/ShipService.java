package by.stepanov.navy.service;

import by.stepanov.navy.model.entity.Ship;
import by.stepanov.navy.model.entity.ShipStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShipService {
    ResponseEntity<List<Ship>> readAllShips(String status);
    ResponseEntity<String> createShip(Ship ship);
    ResponseEntity<String> deleteShip(long id);
    ResponseEntity<ShipStatus> readShipStatus(long id);
    ResponseEntity<ShipStatus> updateShipStatus(long id, Long portId, ShipStatus shipStatus);
}
