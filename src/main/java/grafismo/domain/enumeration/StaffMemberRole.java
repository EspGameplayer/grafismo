package grafismo.domain.enumeration;

/**
 * The StaffMemberRole enumeration.
 */
public enum StaffMemberRole {
    DT("Entrenador"),
    DT2("Segundo entrenador"),
    PRESIDENT("Presidente"),
    BOARD_MEMBER("Miembro de la junta directiva"),
    MATCH_DELEGATE("Delegado de campo"),
    TEAM_DELEGATE("Delegado del equipo"),
    MEMBER("Miembro");

    private final String value;

    StaffMemberRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
