package killercreepr.cruxpotions.command.argument;

public final class CruxPotionArguments {
    private static final CruxPotionArgument CRUX_POTION = new CruxPotionArgument();
    public static CruxPotionArgument cruxPotion(){
        return CRUX_POTION;
    }

    private static final BlockInflictorArgument BLOCK_INFLICTOR = new BlockInflictorArgument();
    public static BlockInflictorArgument blockInflictor(){
        return BLOCK_INFLICTOR;
    }

    private static final EntityInflictorArgument ENTITY_INFLICTOR = new EntityInflictorArgument();
    public static EntityInflictorArgument entityInflictor(){
        return ENTITY_INFLICTOR;
    }
}
