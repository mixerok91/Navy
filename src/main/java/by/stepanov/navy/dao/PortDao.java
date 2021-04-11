package by.stepanov.navy.dao;

import by.stepanov.navy.model.entity.Port;

import java.util.List;

public interface PortDao {

    List<Port> selectAllPorts();
    Port selectPortById(long id);
}
