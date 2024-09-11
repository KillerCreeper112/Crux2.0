package killercreepr.cruxstructures.structure.module.standard;

import killercreepr.crux.Crux;
import killercreepr.crux.context.InputContext;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.LootTable;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.provider.StringTagProvider;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.module.StructureModule;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

public class StructureScatterModule implements StructureModule {
    protected final @NotNull LootTable<Key> structuresToScatter;
    protected final @NotNull NumberProvider scatterRangeX;
    protected final @NotNull NumberProvider scatterRangeY;
    protected final @NotNull NumberProvider scatterRangeZ;
    protected final @NotNull NumberProvider maxScatterAttempts;
    protected final boolean ignoreCenterStructure;

    public StructureScatterModule(@NotNull LootTable<Key> structuresToScatter, @NotNull NumberProvider scatterRangeX, @NotNull NumberProvider scatterRangeY, @NotNull NumberProvider scatterRangeZ, @NotNull NumberProvider maxScatterAttempts, boolean ignoreCenterStructure) {
        this.structuresToScatter = structuresToScatter;
        this.scatterRangeX = scatterRangeX;
        this.scatterRangeY = scatterRangeY;
        this.scatterRangeZ = scatterRangeZ;
        this.maxScatterAttempts = maxScatterAttempts;
        this.ignoreCenterStructure = ignoreCenterStructure;
    }

    @Override
    public void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation) {
        LootContext ctx = LootContext.builder()
            .location(at)
            .info(DataExchange.builder()
                .put("structure", structure)
                .put("rotation", rotation)
                .put("structure_module", this)
                .build())
            .build();

        List<Key> keys = structuresToScatter.populateLoot(ctx);
        Collection<Structure> structuresToScatter = new ArrayList<>();
        for(Key key : keys){
            Structure scatterStructure = StructureRegistries.STRUCTURES.get(key);
            if(scatterStructure == null){
                Crux.log(Level.WARNING, "No crux Structure found for scatter structure (" + key + ")");
                return;
            }
            structuresToScatter.add(scatterStructure);
        }
        StructureScatterer scatterer = new StructureScatterer(at, structuresToScatter, scatterRangeX, scatterRangeY, scatterRangeZ, maxScatterAttempts);
        if(!ignoreCenterStructure){
            scatterer.addPlacedStructure(structure, at, rotation);
        }
        scatterer.setInputContext(InputContext.simple(
            StringTagProvider.build(TagContainer.string(
                Tag.string("center_width_x", (args, context) -> structure.boundingBox().getWidthX() + ""),
                Tag.string("center_width_z", (args, context) -> structure.boundingBox().getWidthZ() + ""),
                Tag.string("center_x", (args, context) -> at.x() + ""),
                Tag.string("center_y", (args, context) -> at.y() + ""),
                Tag.string("center_z", (args, context) -> at.z() + "")
            ))
        ));
        scatterer.scatter();
    }
}
