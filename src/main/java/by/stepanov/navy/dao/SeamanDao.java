package by.stepanov.navy.dao;

import by.stepanov.navy.model.entity.Seaman;
import by.stepanov.navy.model.enums.SeamanRoleType;

import java.util.List;

public interface SeamanDao {

    List<Seaman> selectAllSeamen();
    List<Seaman> selectSeamanByRole(SeamanRoleType roleType);
    List<Seaman> selectSeamanByShipId(long shipId);

    Seaman selectSeamanById(long id);
    void insertSeaman(Seaman seaman);
    void deleteSeaman(long id);

    void updateSeamanShipIdById(long seamanId, long shipId);
    void updateSeamanRoleById(long seamanId, SeamanRoleType roleType);
}
