package killercreepr.cruxblocks.core.command.argument;

public final class CruxBlocksArguments {
    private static final CruxBlockArgument CRUX_BLOCK = new CruxBlockArgument();
    public static CruxBlockArgument cruxBlock(){
        return CRUX_BLOCK;
    }

    private static final CruxBlockGroupArgument CRUX_BLOCK_GROUP = new CruxBlockGroupArgument();
    public static CruxBlockGroupArgument cruxBlockGroup(){
        return CRUX_BLOCK_GROUP;
    }
}
