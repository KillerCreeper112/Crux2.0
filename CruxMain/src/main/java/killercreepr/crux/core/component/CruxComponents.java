package killercreepr.crux.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.serialization.PersistentDataSerializer;
import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.type.ComponentInputParsers;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.registries.CruxRegistries;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.UnaryOperator;

public class CruxComponents {
    public static void register(){}

    public static final DataComponentType<Boolean> INVULNERABLE = register("invulnerable", builder -> builder
        .persistTextParser(PersistTextParser.BOOLEAN.createInput(Crux.key("invulnerable"))));

    public static final DataComponentType<Float> HARDNESS = register("hardness", builder -> builder
        .persistTextParser(PersistTextParser.FLOAT.createInput(Crux.key("hardness"))));

    public static final DataComponentType<Float> SIZE = register("size", builder -> builder
        .persistTextParser(PersistTextParser.FLOAT.createInput(Crux.key("size"))));

    public static final DataComponentType<ItemStack> ITEM_DISPLAY = register("item_display", builder -> builder
        .persistent(PersistentDataSerializer.create("item_display", CruxPersistence.ITEM_STACK)));

    public static final DataComponentType<Boolean> UNBREAKABLE = register("unbreakable", builder -> builder
        .persistTextParser(PersistTextParser.BOOLEAN.createInput(Crux.key("unbreakable"))));

    public static final DataComponentType<ToolComponent> TOOL = register("tool", builder -> builder
        .persistTextParser(ComponentInputParsers.TOOL.createInput(Crux.key("tool"))));

    public static final DataComponentType<ItemLootTable> ITEM_LOOT_TABLE = register("item_loot_table", builder -> builder
        .persistTextParser(ComponentInputParsers.ITEM_LOOT_TABLE.createInput(Crux.key("item_loot_table"))));

    public static final DataComponentType<List<ItemLootTable>> ITEM_LOOT_TABLES = register("item_loot_tables", builder -> builder
        .persistTextParser(ComponentInputParsers.ITEM_LOOT_TABLE_LIST.createInput(Crux.key("item_loot_tables"))));

    public static final DataComponentType<Long> LOOT_GENERATED_TIME = register("loot_generated_time", builder -> builder
        .persistTextParser(PersistTextParser.LONG.createInput(Crux.key("loot_generated_time"))));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
