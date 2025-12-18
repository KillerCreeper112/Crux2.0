package killercreepr.crux.core.loot.conditions.location;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootCtxObjects;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.crux.core.util.CruxLoc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class LocationInRegionCondition extends BaseCondition {
    protected final Vector pos1;
    protected final Vector pos2;


    public LocationInRegionCondition(@NotNull String target, Vector pos1, Vector pos2) {
        super(target);
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public Location parseLocation(Object o, World world){
        if(o instanceof Location l) return l;
        if(o instanceof Entity e) return e.getLocation();
        if(o instanceof Block e) return e.getLocation();
        if(o instanceof CruxPosition e) return e.toLocation(world);
        return null;
    }

    public World parseWorld(LootContext ctx){
        var loc = ctx.get(LootCtxObjects.LOCATION);
        if(loc == null) return null;
        return loc.getWorld();
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        Location b = parseLocation(context.info().get(target), parseWorld(context));
        if(b == null) return false;
        return CruxLoc.inRegion(b, pos1, pos2);
    }
}
