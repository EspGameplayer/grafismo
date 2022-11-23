package grafismo.domain.enumeration;

/**
 * The Foot enumeration.
 */
public enum Foot {
    R("Derecha"),
    L("Izquierda");

    private final String value;

    Foot(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
