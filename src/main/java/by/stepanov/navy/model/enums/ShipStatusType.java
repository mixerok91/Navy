package by.stepanov.navy.model.enums;

public enum ShipStatusType {
    PORT, SEA;

    public static ShipStatusType getShipStatusType(String statusInString) {
        for (ShipStatusType type : ShipStatusType.values()) {
            if (type.name().equals(statusInString)){
                return type;
            }
        }
        return null;
    }
}
