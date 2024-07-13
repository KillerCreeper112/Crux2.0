package killercreepr.cruxstructures.manager;

import killercreepr.crux.data.world.BlockPosed;
import killercreepr.crux.data.world.WorldChunkStorage;
import killercreepr.crux.data.world.standard.MultiVerseBlockPosedStorage;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class StructureStorage<T extends BlockPosed & Keyed> extends MultiVerseBlockPosedStorage<T> {
    public StructureStorage(@NotNull Map<UUID, WorldChunkStorage<T>> data) {
        super(data);
    }
}
