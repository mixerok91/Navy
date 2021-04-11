package by.stepanov.navy.model.entity;


import by.stepanov.navy.model.enums.SeamanRoleType;

public class Seaman {

    private long id;
    private String name;
    private Long shipId;
    private SeamanRoleType role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getShipId() {
        return shipId;
    }

    public void setShipId(Long shipId) {
        this.shipId = shipId;
    }

    public SeamanRoleType getRole() {
        return role;
    }

    public void setRole(SeamanRoleType role) {
        this.role = role;
    }
}
