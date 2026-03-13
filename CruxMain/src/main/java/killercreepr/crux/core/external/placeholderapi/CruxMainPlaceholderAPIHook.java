package killercreepr.crux.core.external.placeholderapi;

import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;

public class CruxMainPlaceholderAPIHook {
  public static void onEnable(CruxPlugin plugin){
    try{
      new TagsExpansionHook("crux", Crux.format()).register();
      new FormatTicksHook().register();
      new FormatMillisecondsHook().register();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
