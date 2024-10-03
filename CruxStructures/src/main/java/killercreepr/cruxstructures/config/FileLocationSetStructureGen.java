package killercreepr.cruxstructures.config;

import killercreepr.crux.loot.LootTable;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.config.handler.FileStructureHandlers;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.impl.LocationSetStructureGen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileLocationSetStructureGen extends PureYamlFileHandler<LocationSetStructureGen> {
    @Override
    public @Nullable LocationSetStructureGen deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        LootTable<StructureGenerator> generators = FileStructureHandlers.STRUCTURE_GENERATOR_LOOT_TABLE.deserializeFromFile(ctx, o.get("generators"));
        if(generators == null) return null;
        NumberProvider structureAmount = registry.deserializeFromFile(NumberProvider.class, o.get("structure_amount"));
        if(structureAmount==null) return null;
        NumberProvider chunkRange = registry.deserializeFromFile(NumberProvider.class, o.get("chunk_range"));
        NumberProvider chunkRangeX = registry.deserializeFromFile(NumberProvider.class, o.get("chunk_range_x"));
        NumberProvider chunkRangeZ = registry.deserializeFromFile(NumberProvider.class, o.get("chunk_range_z"));
        if(chunkRangeX == null) chunkRangeX = chunkRange;
        if(chunkRangeZ == null) chunkRangeZ = chunkRange;
        return new LocationSetStructureGen(generators, structureAmount, chunkRangeX, chunkRangeZ);
    }
}
