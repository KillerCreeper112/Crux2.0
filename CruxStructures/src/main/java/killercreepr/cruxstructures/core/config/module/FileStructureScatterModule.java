package killercreepr.cruxstructures.core.config.module;

import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.core.CruxStructuresModule;
import killercreepr.cruxstructures.api.location.LocationFinder;
import killercreepr.cruxstructures.core.structure.module.StructureScatterModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileStructureScatterModule extends PureYamlFileHandler<StructureScatterModule> {
    protected static final @NotNull FileSimpleLootTable<Key> fileSimpleLootTable = CruxStructuresModule.fileSimpleLootTable;
    @Override
    public @Nullable StructureScatterModule deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        LootTable<Key> structures = fileSimpleLootTable.deserializeFromFile(
            ctx, o.get("structures")
        );
        if(structures == null) return null;
        NumberProvider maxAttempts = registry.deserializeFromFile(NumberProvider.class, o.get("max_attempts"));
        if(maxAttempts == null) maxAttempts = NumberProvider.constant(16);

        NumberProvider xRange = registry.deserializeFromFile(NumberProvider.class, o.get("range_x"));
        if(xRange==null) return null;
        NumberProvider yRange = registry.deserializeFromFile(NumberProvider.class, o.get("range_y"));
        if(yRange == null) yRange = xRange;
        NumberProvider zRange = registry.deserializeFromFile(NumberProvider.class, o.get("range_z"));
        if(zRange == null) zRange = xRange;

        LocationFinder locationFinder = registry.deserializeFromFile(LocationFinder.class, o.get("location_finder"));
        if(locationFinder == null) locationFinder = new LocationFinder.Dummy();

        return new StructureScatterModule(
            structures,
            xRange, yRange, zRange,
            maxAttempts,
            locationFinder,
            o.getObject(Boolean.class, "ignore_center", false)
        );
    }
}
