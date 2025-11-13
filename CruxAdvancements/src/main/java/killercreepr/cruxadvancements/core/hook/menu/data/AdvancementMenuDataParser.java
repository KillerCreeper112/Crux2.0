package killercreepr.cruxadvancements.core.hook.menu.data;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.core.Crux;
import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.core.registries.AdvancementRegistries;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.core.menu.data.SimpleItemDataParser;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdvancementMenuDataParser extends SimpleItemDataParser {
    public AdvancementMenuDataParser(@NotNull Key key) {
        super(key);
    }

    @Override
    public @NotNull DataExchange parse(@NotNull Entity p,
                                       @NotNull MenuContext ctx,
                                       @NotNull MenuItemHolder itemHolder,
                                       @NotNull DataExchange info) {
        DataExchange mergedInfo = ctx.getAllMergedInfo();
        CfgMenu menu = ctx.getMenu();
        MenuHolder holder = menu.getHolder();
        FormatSerializer format = holder.getRegistry().getFormat();

        DataExchange.Builder builder = DataExchange.builder();
        if(info.has("advancement_manager")){
            String key = format.deserializeString(info.getOrThrow("advancement_manager").toString(), ctx.getResolvers());
            CruxAdvancementManager<?> manager = AdvancementRegistries.ADVANCEMENT_MANAGERS.get(Crux.key(key));
            if(manager != null) builder.put(manager);
        }
        if(info.get("advancement_managers") instanceof List<?> list){
            list.forEach(obj ->{
                String key = format.deserializeString(obj + "", ctx.getResolvers());
                String[] split = key.split(" ");

                String prefix;
                Key keyKey = Crux.key(split[0]);
                if(split.length > 1){
                    prefix = split[1];
                }else prefix = null;

                CruxAdvancementManager<?> manager = AdvancementRegistries.ADVANCEMENT_MANAGERS.get(keyKey);
                if(manager != null){
                    if(prefix != null){
                        builder.put(prefix, manager);
                    }else builder.put(manager);
                }
            });
        }

        if(info.has("advancement")){
            String keyString = format.deserializeString(info.getOrThrow("advancement").toString(), ctx.getResolvers());
            Key key = Crux.key(keyString);
            CruxAdvancementManager<?> manager = builder.getOrDefault(
                CruxAdvancementManager.class, mergedInfo.get(CruxAdvancementManager.class)
            );
            if(manager != null){
                CruxAdvancement advancement = manager.getAdvancement(key);
                if(advancement != null) builder.put(advancement);
            }
        }
        return builder.build();
    }
}
