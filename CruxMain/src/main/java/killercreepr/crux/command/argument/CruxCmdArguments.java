package killercreepr.crux.command.argument;

public class CruxCmdArguments {
    protected static final CruxKeyArgument CRUX_KEY = new CruxKeyArgument();
    public static CruxKeyArgument cruxKey(){ return CRUX_KEY; }

    protected static final ColorArgument COLOR = new ColorArgument();
    public static ColorArgument color(){ return COLOR; }

    protected static final ItemFlagArgument ITEM_FLAG = new ItemFlagArgument();
    public static ItemFlagArgument itemFlag(){ return ITEM_FLAG; }
}
