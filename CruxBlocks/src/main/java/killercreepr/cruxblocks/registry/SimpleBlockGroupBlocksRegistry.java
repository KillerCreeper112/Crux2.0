package killercreepr.cruxblocks.registry;

import killercreepr.crux.core.registry.SimpleKeyedRegistry;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.component.CruxBlockGroupComponent;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SimpleBlockGroupBlocksRegistry<T extends CruxBlock> extends SimpleKeyedRegistry<T> implements BlockGroupBlocksRegistry<T>{
    protected final @NotNull CruxBlockGroup group;

    public SimpleBlockGroupBlocksRegistry(@NotNull CruxBlockGroup group) {
        this.group = group;
    }

    public SimpleBlockGroupBlocksRegistry(@NotNull Map<Key, T> map, @NotNull CruxBlockGroup group) {
        super(map);
        this.group = group;
    }

    @Override
    public <E extends T> @NotNull E register(@NotNull Key key, @NotNull E value) {
        group.getComponents().getAllOfType(CruxBlockGroupComponent.class).forEach(data -> data.onRegistered(value, group));
        return super.register(key, value);
    }
}
