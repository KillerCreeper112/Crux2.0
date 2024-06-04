package killercreepr.cruxattributes.command.argument;

public final class CruxAttributeArguments {
    private static final CruxAttributeArgument CRUX_ATTRIBUTE = new CruxAttributeArgument();
    public static CruxAttributeArgument cruxAttribute(){
        return CRUX_ATTRIBUTE;
    }

    private static final CruxSlotArgument CRUX_SLOT = new CruxSlotArgument();
    public static CruxSlotArgument cruxSlot(){
        return CRUX_SLOT;
    }

    private static final CruxAttributeOperationArgument CRUX_ATTRIBUTE_OPERATION = new CruxAttributeOperationArgument();
    public static CruxAttributeOperationArgument cruxAttributeOperation(){
        return CRUX_ATTRIBUTE_OPERATION;
    }
}
