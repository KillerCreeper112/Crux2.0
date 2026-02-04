package killercreepr.crux.core.loot.conditions.location;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootCtxObjects;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.loot.CruxLootHelper;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.crux.core.util.CruxLoc;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LocationIsNearCondition extends BaseCondition {
    public final String location;
    public final double distance;
    public final double distanceSqr;
    public final boolean invert;

    public LocationIsNearCondition(@NotNull String target, String location, double distance, boolean invert) {
        super(target);
      this.location = location;
      this.distance = distance;
      this.distanceSqr = distance*distance;
      this.invert = invert;
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        var target = CruxLootHelper.parseLocation(context.info().get(this.target));
        if(target == null) return false;

        var loc = CruxLootHelper.parseLocation(context.info().get(this.location));
        if(loc == null) return false;

        if(!Objects.equals(target.getWorld(), loc.getWorld())) return false;

        if(invert){
            if(loc.distanceSquared(target) <= distanceSqr) return false;
        }else if(loc.distanceSquared(target) > distanceSqr) return false;

        return true;
    }
}
