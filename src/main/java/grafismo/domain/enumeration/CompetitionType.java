package grafismo.domain.enumeration;

/**
 * The CompetitionType enumeration.
 */
public enum CompetitionType {
    LEAGUE("Liga"),
    CUP("Copa");

    private final String value;

    CompetitionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
