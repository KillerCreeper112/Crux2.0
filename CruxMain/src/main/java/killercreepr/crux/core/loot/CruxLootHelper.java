package killercreepr.crux.core.loot;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.data.WorldLocation;
import killercreepr.crux.core.Crux;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

import java.lang.ref.Reference;

public class CruxLootHelper {
  //todo open this up to make it data driven
  public static Entity parseEntity(Object o){
    if(o instanceof Entity e) return e;
    if(o instanceof Holder<?> holder){
      var e = holder.value();
      if(e instanceof Entity ee) return ee;
    }
    if(o instanceof Reference<?> holder){
      var e = holder.get();
      if(e instanceof Entity ee) return ee;
    }
    return null;
  }
  public static Location parseLocation(Object o){
    if(o instanceof Location e) return e;
    if(o instanceof WorldLocation e) return e.toLocation();
    if(o instanceof Block e) return e.getLocation();
    if(o instanceof BlockState e) return e.getLocation();
    Entity e = parseEntity(o);
    if(e != null) return e.getLocation();
    return null;
  }
}
