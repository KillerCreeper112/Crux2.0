package killercreepr.cruxitems.core.command.argument;

public final class CruxItemsArguments {
    private static final CruxPluginItemArgument PLUGIN_ITEM = new CruxPluginItemArgument();
    public static CruxPluginItemArgument pluginItem(){
        return PLUGIN_ITEM;
    }

    private static final EnchantmentArgument ENCHANTMENT = new EnchantmentArgument();
    public static EnchantmentArgument enchantment(){
        return ENCHANTMENT;
    }
    private static final EnchantmentLevelArgument ENCHANTMENT_LEVEL = new EnchantmentLevelArgument();
    public static EnchantmentLevelArgument enchantmentLevel(){
        return ENCHANTMENT_LEVEL;
    }
}
