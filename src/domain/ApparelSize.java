package domain;

public enum ApparelSize {
    SMALL("S"),
    MEDIUM("M"),
    LARGE("L"),
    EXTRA_LARGE("XL");

    private final String label;


    ApparelSize(String label) {
        this.label = label;

    }

    public String getLabel() {
        return label;
    }

}