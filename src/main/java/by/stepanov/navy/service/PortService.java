package by.stepanov.navy.service;

import by.stepanov.navy.model.entity.Port;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PortService {

    ResponseEntity<List<Port>> readAllPorts();
    ResponseEntity<String> readPortCapacityInfo(long portId);
}
