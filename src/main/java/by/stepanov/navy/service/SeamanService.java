package by.stepanov.navy.service;

import by.stepanov.navy.model.entity.Seaman;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SeamanService {

    ResponseEntity<List<Seaman>> reaAllSeamen(String role, Long shipId);

    ResponseEntity<Seaman> readSeaman(long seaman);
    ResponseEntity<String> createSeaman(Seaman seaman);
    ResponseEntity<String> deleteSeaman(long seamanId);

    ResponseEntity<String> updateSeamanRole(long seamanId, String seamanRole);
    ResponseEntity<String> updateSeamanShip(long seamanId, Long shipId);


}

