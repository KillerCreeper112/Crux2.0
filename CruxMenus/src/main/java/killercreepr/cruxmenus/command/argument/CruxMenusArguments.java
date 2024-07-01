package killercreepr.cruxmenus.command.argument;

public final class CruxMenusArguments {
    private static CruxMenuHolderArgument MENU_HOLDER;
    public static CruxMenuHolderArgument menuHolder(){
        return MENU_HOLDER;
    }

    public static void setMenuHolder(CruxMenuHolderArgument menuHolder) {
        MENU_HOLDER = menuHolder;
    }
}
