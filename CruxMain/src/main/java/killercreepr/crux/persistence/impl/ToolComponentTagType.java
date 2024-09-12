package killercreepr.crux.persistence.impl;

import killercreepr.crux.Crux;
import killercreepr.crux.block.predicate.BlockPredicate;
import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.tag.block.BlockTag;
import killercreepr.crux.item.ListToolComponent;
import killercreepr.crux.item.SimpleToolComponent;
import killercreepr.crux.item.ToolComponent;
import killercreepr.crux.persistence.CruxPersistence;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.crux.util.CruxTag;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ToolComponentTagType implements PersistentDataType<PersistentDataContainer, ToolComponent> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<ToolComponent> getComplexType() {
        return ToolComponent.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull ToolComponent complex, @NotNull PersistentDataAdapterContext context) {
        return null;
    }

    @Override
    public @NotNull ToolComponent fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext ctx) {
        List<ToolComponent> values = CruxTag.get(c, "values", CruxPersistence.LIST.TOOL_COMPONENT, null);
        if(values != null && !values.isEmpty()) return new ListToolComponent(values);
        BlockTag tag = CruxRegistries.BLOCK_TAG.get(CruxTag.get(c, "blocks", CruxPersistence.CRUX_KEY));
        Objects.requireNonNull(tag, "BlockTag " + CruxTag.get(c, "blocks", CruxPersistence.CRUX_KEY) + " not found!");
        return new SimpleToolComponent(
            BlockPredicate.fromTag(tag),
            CruxTag.get(c, "can_harvest", PersistentDataType.BOOLEAN, false),
            CruxTag.get(c, "speed", PersistentDataType.FLOAT, 1f)
        );
    }
}
