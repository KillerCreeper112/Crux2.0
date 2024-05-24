package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.menu.bukkit.holder.MenuItemHolder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DataExchangeValue extends ConfigValue<DataExchange> {
    public DataExchangeValue(@Nullable DataExchange defaultValue) {
        super(DataExchange.class, defaultValue);
    }

    public DataExchangeValue() {
        this(null);
    }

    @Override
    public @Nullable DataExchange get(@NotNull CruxConfig cfg, @NotNull String path) {
        ConfigurationSection section = cfg.config().getConfigurationSection(removeDot(path));
        if(section == null) return null;
        return get(cfg, path, section);
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable DataExchange object) {

    }

    public @Nullable DataExchange get(@NotNull CruxConfig config, @NotNull String path, @NotNull ConfigurationSection extraSection){
        FileConfiguration cfg = config.config();
        DataExchange.Builder builder = new DataExchange.Builder();
        for(String s : extraSection.getKeys(false)){
            switch (s.toLowerCase()){
                case "items" ->{
                    Map<String, MenuItemHolder> items = new HashMap<>();
                    ConfigurationSection subSection = cfg.getConfigurationSection(addDot(path) + s);
                    if(subSection == null) continue;
                    for(String ss : subSection.getKeys(false)){
                        MenuItemHolder menuItem = new MenuItemValue().get(config,addDot(path) + s + "." + ss, config);
                        if(menuItem == null) continue;
                        items.put(ss, menuItem);
                    }
                    if(!items.isEmpty()) builder.put(s.toLowerCase(), Holder.directObject(items));
                    continue;
                }
            }
            builder.put(s.toLowerCase(), Holder.directObject(cfg.get(addDot(path) + "." + s)));
        }
        return builder.build();
    }
}
