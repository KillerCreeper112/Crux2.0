package killercreepr.cruxstructures.structure.module.standard;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.LootTable;
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
    protected final @NotNull NumberProvider scatterRange;
    protected final @NotNull NumberProvider scatterRangeY;
    protected final @NotNull NumberProvider maxScatterAttempts;

    public StructureScatterModule(@NotNull LootTable<Key> structuresToScatter, @NotNull NumberProvider scatterRange, @NotNull NumberProvider scatterRangeY, @NotNull NumberProvider maxScatterAttempts) {
        this.structuresToScatter = structuresToScatter;
        this.scatterRange = scatterRange;
        this.scatterRangeY = scatterRangeY;
        this.maxScatterAttempts = maxScatterAttempts;
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
        new StructureScatterer(at, structuresToScatter, scatterRange, scatterRangeY, maxScatterAttempts)
            .scatter();
    }
}
