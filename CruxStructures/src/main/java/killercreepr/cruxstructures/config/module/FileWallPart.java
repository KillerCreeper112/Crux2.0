package killercreepr.cruxstructures.config.module;

import killercreepr.crux.loot.LootTable;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.module.standard.WallsModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileWallPart extends PureYamlFileHandler<WallsModule.WallPart> {
    @Override
    public @Nullable WallsModule.WallPart deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            LootTable<Key> lootTable = new FileSimpleLootTable<>(Key.class).deserializeFromFile(ctx, e);
            if(lootTable == null) return null;
            return new WallsModule.WallPart(lootTable, null, null);
        }
        FileRegistry registry = ctx.getRegistry();
        FileElement element = o.get("structure");
        if(element == null) return null;

        LootTable<Key> lootTable = new FileSimpleLootTable<>(Key.class).deserializeFromFile(ctx, element);
        if(lootTable == null) return null;
        return new WallsModule.WallPart(
            lootTable,
            registry.deserializeFromFile(NumberProvider.class, o.get("spacing")),
            registry.deserializeFromFile(NumberProvider.class, o.get("offset"))
        );
    }
}
