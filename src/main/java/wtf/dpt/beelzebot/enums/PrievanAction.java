package wtf.dpt.beelzebot.enums;

public enum PrievanAction {

    JOIN("joined"),
    LEFT("left");

    public final String label;

    private PrievanAction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
