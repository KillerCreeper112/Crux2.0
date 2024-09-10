package killercreepr.cruxstructures.config.module;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.loot.LootTable;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.module.standard.WallsModule;
import net.kyori.adventure.key.Key;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileWallsModule extends PureYamlFileHandler<WallsModule> {
    protected final @NotNull FileSimpleLootTable<Key> fileSimpleLootTable = new FileSimpleLootTable<>(Key.class);
    @Override
    public @Nullable WallsModule deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Map<BlockFace, WallsModule.Wall> walls = new HashMap<>();
        if(!(o.get("walls") instanceof FileObject aa)) return null;
        aa.forEach((key, value) ->{
            List<LootTable<Key>> lootTables = new ArrayList<>();
            if(!(value instanceof FileObject oo)) return;
            if(!(oo.get("structures") instanceof FileArray a)) return;
            a.forEach(ele ->{
                LootTable<Key> lootTable = fileSimpleLootTable.deserializeFromFile(ctx, ele);
                if(lootTable == null) return;
                lootTables.add(lootTable);
            });

            walls.put(BlockFace.valueOf(key.toUpperCase()), new WallsModule.Wall(lootTables,
                registry.deserializeFromFile(NumberProvider.class, oo.get("wall_spacing"))
            ));
        });

        Map<BlockFace, LootTable<Key>> corners = new HashMap<>();
        if(!(o.get("corners") instanceof FileObject oo)) return null;

        oo.forEach((key, value) ->{
            LootTable<Key> lootTable = fileSimpleLootTable.deserializeFromFile(ctx, value);
            if(lootTable == null) return;
            BlockFace face;
            try{
                face = BlockFace.valueOf(key.toUpperCase());
            }catch (IllegalArgumentException ignored){ return; }
            corners.put(face, lootTable);
        });


        Map<BlockFace, NumberProvider> wallSpacing = registry.deserializeFromFile(
            new TypeToken<Map<BlockFace, NumberProvider>>(){}.getType(), o.get("wall_spacing")
        );

        if(wallSpacing == null) wallSpacing = Map.of();

        WallsModule.WallRotationType rotationType = registry.deserializeFromFile(
            WallsModule.WallRotationType.class, o.get("rotation_type")
        );
        if(rotationType==null) rotationType = WallsModule.WallRotationType.STRUCTURE;

        return new WallsModule(
            walls,
            corners,
            registry.deserializeFromFile(NumberProvider.class, o.get("default_wall_spacing")),
            wallSpacing,
            rotationType,
            registry.deserializeFromFile(NumberProvider.class, o.get("wall_max_y_check"))
        );
    }
}
