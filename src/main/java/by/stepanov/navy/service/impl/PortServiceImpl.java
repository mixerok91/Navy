package by.stepanov.navy.service.impl;

import by.stepanov.navy.dao.PortDao;
import by.stepanov.navy.dao.ShipDao;
import by.stepanov.navy.model.entity.Port;
import by.stepanov.navy.service.PortService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortServiceImpl implements PortService {

    private final PortDao portDao;
    private final ShipDao shipDao;

    @Autowired
    public PortServiceImpl(PortDao portDao, ShipDao shipDao) {
        this.portDao = portDao;
        this.shipDao = shipDao;
    }

    @Override
    public ResponseEntity<List<Port>> readAllPorts() {
        return ResponseEntity.ok(portDao.selectAllPorts());
    }

    @Override
    public ResponseEntity<String> readPortCapacityInfo(long portId) {
        final Port port = portDao.selectPortById(portId);
        if (port == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        final int shipsInPortCount = shipDao.selectShipsCountByPortId(portId);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", port.getCapacity());
        jsonObject.put("used", shipsInPortCount);
        jsonObject.put("free", port.getCapacity() - shipsInPortCount);
        return ResponseEntity.ok(jsonObject.toString());
    }
}
