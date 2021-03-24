package wtf.dpt.beelzebot.enums;

public enum PrievanAction {

    JOIN("joined"),
    LEFT("left"),
    SWITCH("switched to");

    public final String label;

    private PrievanAction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
