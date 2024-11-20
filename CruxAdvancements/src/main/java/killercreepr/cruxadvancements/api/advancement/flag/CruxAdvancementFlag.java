package killercreepr.cruxadvancements.api.advancement.flag;

public enum CruxAdvancementFlag {
    SHOW_TOAST,
    DISPLAY_MESSAGE,
    SEND_WITH_HIDDEN_BOOLEAN,
    GLOBAL;
    public static final CruxAdvancementFlag[] TOAST_AND_MESSAGE = new CruxAdvancementFlag[]{SHOW_TOAST, DISPLAY_MESSAGE};
}
