package killercreepr.cruxstructures.api.world.module;

import killercreepr.crux.api.data.Reloadable;
import killercreepr.crux.api.data.tick.Ticked;
import killercreepr.crux.api.data.world.WorldChunkStorage;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxstructures.api.structure.ActiveStructure;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.api.structure.generation.StructureGenerator;
import killercreepr.cruxworlds.api.world.module.WorldModule;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface StructureWorldModule extends WorldModule, Ticked, Reloadable {
    boolean isActive(StoredStructure stored);
    ActiveStructure getActive(StoredStructure stored);

    List<StructureGenerator> getStructureGenerators();

    WorldChunkStorage<StoredStructure> getStoredStructures();

    WorldChunkStorage<ActiveStructure> getActiveStructures();

    void addStoredStructure(StoredStructure stored, long chunkKey);

    void addStoredStructureSilently(StoredStructure stored, long chunkKey);

    void addStoredStructure(StoredStructure stored, Chunk chunk);

    <T extends ActiveStructure> @Nullable T getFirstActiveAt(@NotNull Class<T> type, @NotNull CruxPosition block);

    <T extends ActiveStructure> @Nullable T getFirstActiveAt(@NotNull Class<T> type, @NotNull CruxPosition block, @Nullable Predicate<T> filter);
    <T extends ActiveStructure> @NotNull Collection<T> getActiveAt(@NotNull Class<T> type, @NotNull CruxPosition block, @Nullable Predicate<T> filter);


    <T extends ActiveStructure> @Nullable T getFirstActiveAt(@NotNull Class<T> type, @NotNull Block block);

    <T extends ActiveStructure> @Nullable T getFirstActiveAt(@NotNull Class<T> type, @NotNull Block block, @Nullable Predicate<T> filter);

    @NotNull Collection<ActiveStructure> getActiveAt(@NotNull Block block);

    @NotNull Collection<ActiveStructure> getActiveAt(@NotNull Block block, @Nullable Predicate<ActiveStructure> filter);

    <T extends ActiveStructure> @NotNull Collection<T> getActiveAt(@NotNull Class<T> type, @NotNull Block block);

    <T extends ActiveStructure> @NotNull Collection<T> getActiveAt(@NotNull Class<T> type, @NotNull Block block, @Nullable Predicate<T> filter);

    @NotNull Collection<StoredStructure> getStoredAt(@NotNull CruxPosition block);

    @NotNull Collection<StoredStructure> getStoredAt(@NotNull CruxPosition block, @Nullable Predicate<StoredStructure> filter);

    <T extends StoredStructure> @NotNull Collection<T> getStoredAt(@NotNull Class<T> type, @NotNull CruxPosition block);

    <T extends StoredStructure> @NotNull Collection<T> getStoredAt(@NotNull Class<T> type, @NotNull CruxPosition block, @Nullable Predicate<T> filter);

    <T extends StoredStructure> @Nullable T getFirstStoredAt(@NotNull Class<T> type, @NotNull CruxPosition block);

    <T extends StoredStructure> @Nullable T getFirstStoredAt(@NotNull Class<T> type, @NotNull CruxPosition block, @Nullable Predicate<T> filter);
    //

    @NotNull Collection<StoredStructure> getStoredAt(@NotNull Block block);

    @NotNull Collection<StoredStructure> getStoredAt(@NotNull Block block, @Nullable Predicate<StoredStructure> filter);

    <T extends StoredStructure> @NotNull Collection<T> getStoredAt(@NotNull Class<T> type, @NotNull Block block);

    <T extends StoredStructure> @NotNull Collection<T> getStoredAt(@NotNull Class<T> type, @NotNull Block block, @Nullable Predicate<T> filter);

    <T extends StoredStructure> @Nullable T getFirstStoredAt(@NotNull Class<T> type, @NotNull Block block);

    <T extends StoredStructure> @Nullable T getFirstStoredAt(@NotNull Class<T> type, @NotNull Block block, @Nullable Predicate<T> filter);

    Collection<StoredStructure> getStored(@Nullable Predicate<StoredStructure> filter);

    <T extends StoredStructure> Collection<T> getStored(@NotNull Class<T> type, @Nullable Predicate<T> filter);

    Collection<ActiveStructure> getActive(@Nullable Predicate<ActiveStructure> filter);

    <T extends ActiveStructure> Collection<T> getActive(@NotNull Class<T> type, @Nullable Predicate<T> filter);
}
