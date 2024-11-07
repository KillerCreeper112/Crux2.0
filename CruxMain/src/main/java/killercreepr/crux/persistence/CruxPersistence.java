package killercreepr.crux.persistence;

import killercreepr.crux.loot.item.ItemLootTable;
import killercreepr.crux.persistence.impl.*;
import killercreepr.crux.registries.CruxRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CruxPersistence {
    public static final UUIDTagType UUID = new UUIDTagType();
    public static final PotionEffectTagType POTION_EFFECT = new PotionEffectTagType();
    public static final CruxKeyTagType CRUX_KEY = new CruxKeyTagType();
    public static final ItemStackTagType ITEM_STACK = new ItemStackTagType();
    public static final LocationTagType LOCATION = new LocationTagType();
    public static final VectorTagType VECTOR = new VectorTagType();
    public static final BlockPosTagType BLOCK_POS = new BlockPosTagType();
    public static final ToolComponentTagType TOOL_COMPONENT = new ToolComponentTagType();
    public static final ToolComponentRuleTagType TOOL_COMPONENT_RULE = new ToolComponentRuleTagType();
    public static final AbstractKeyedTagType<ItemLootTable> ITEM_LOOT_TABLE = new AbstractKeyedTagType<>() {
        @Override
        public @NotNull ItemLootTable fromKey(@NotNull Key key) {
            return Objects.requireNonNull((ItemLootTable) CruxRegistries.ITEM_LOOT_TABLE.get(key), "Invalid item loot table key " + key + "!");
        }

        @Override
        public @NotNull Class<ItemLootTable> getComplexType() {
            return ItemLootTable.class;
        }
    };

    public static final CruxListPersistence LIST = new CruxListPersistence();
}
