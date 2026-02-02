package killercreepr.crux.core.loot;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.Crux;
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
}
