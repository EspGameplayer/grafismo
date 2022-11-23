package grafismo.domain.enumeration;

/**
 * The ActionType enumeration.
 */
public enum ActionType {
    GOAL("Gol"),
    YC("Amonestación"),
    RC("Expulsión"),
    SUB("Sustitución"),
    PASS("Pase"),
    CORNER("Saque de esquina"),
    SHOT("Disparo"),
    FK("Lanzamiento de falta"),
    PK("Penalti"),
    CLEARANCE("Despeje"),
    TACKLE("Entrada"),
    FOUL("Falta cometida"),
    OFFSIDE("Fuera de juego"),
    PENALTY("Penalti cometido"),
    PERIOD_FINISH("Fin de periodo");

    private final String value;

    ActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
