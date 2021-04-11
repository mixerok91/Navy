package by.stepanov.navy.model.enums;

public enum SeamanRoleType {
    SAILOR, CAPTAIN;

    public static SeamanRoleType getSeamanRoleType(String seamanRole) {
        for (SeamanRoleType type : SeamanRoleType.values()) {
            if (type.name().equals(seamanRole)){
                return type;
            }
        }
        return null;
    }
}
