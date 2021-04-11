package by.stepanov.navy.model.entity;

import by.stepanov.navy.model.enums.ShipStatusType;


public class Ship {

    private Long id;
    private String name;
    private Long portId;
    private ShipStatusType status;
    private int minSailors;
    private int maxSailors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public ShipStatusType getStatus() {
        return status;
    }

    public void setStatus(ShipStatusType status) {
        this.status = status;
    }

    public int getMinSailors() {
        return minSailors;
    }

    public void setMinSailors(int minSailors) {
        this.minSailors = minSailors;
    }

    public int getMaxSailors() {
        return maxSailors;
    }

    public void setMaxSailors(int maxSailors) {
        this.maxSailors = maxSailors;
    }
}
