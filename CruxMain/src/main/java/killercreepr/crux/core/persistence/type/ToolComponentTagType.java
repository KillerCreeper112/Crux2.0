package killercreepr.crux.core.persistence.type;

import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.util.CruxTag;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        if(!(complex instanceof ToolComponent.Simple s)) throw new IllegalArgumentException(complex.getClass().getName() + " not supported");
        PersistentDataContainer c = context.newPersistentDataContainer();
        CruxTag.set(c, "default_mining_speed", FLOAT, s.getDefaultMiningSpeed() == 1f ? null : s.getDefaultMiningSpeed());
        CruxTag.set(c, "rules", CruxPersistence.LIST.TOOL_COMPONENT_RULE, s.getRules());
        return c;
    }

    @Override
    public @NotNull ToolComponent fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext ctx) {
        float defaultMiningSpeed = CruxTag.get(c, "default_mining_speed", PersistentDataType.FLOAT, 1f);
        List<ToolComponent.Rule> rules = CruxTag.get(c, "rules", CruxPersistence.LIST.TOOL_COMPONENT_RULE, null);
        return new ToolComponent.Simple(
            defaultMiningSpeed, rules
        );
    }
}
