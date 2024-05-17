package killercreepr.cruxconfig.config.bukkit.file;

import killercreepr.cruxconfig.config.common.file.ICruxConfig;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CruxConfig extends CruxFolder implements ICruxConfig<FileConfiguration> {
    protected final FileConfiguration cfg;
    public CruxConfig(@NotNull Plugin plugin, @NotNull String path){
        super(plugin, path + ".yml");
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public CruxConfig(@NotNull File file){
        super(file);
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public CruxConfig(@NotNull CruxConfig cfg){
        this(cfg.file());
    }

    @Override
    public final @NotNull FileConfiguration config(){
        return cfg;
    }

    public void createDefaultAndRegister(){
        this.createDefault();
        this.register();
    }

    @Override
    public void register(){}

    @Override
    public final boolean save(){
        try{
            cfg.save(file);
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public final boolean createDefault(){
        if(file.exists()) return false;
        setDefaults();
        return save();
    }

    protected void setDefaults(){}

    @Override
    public void set(@NotNull String path, @Nullable Object value){
        if(value instanceof YamlElement e){
            if(e.map().size() == 1){
                Object d = e.map().get(null);
                if(d != null){
                    set(path, d);
                    return;
                }
            }
            return;
        }
        cfg.set(path, value);
    }
    @Override
    public void set(@NotNull String path, @Nullable Object value, @NotNull String @Nullable... comments){
        cfg.set(path, value);
        setComments(path, comments);
    }
    @Override
    public void set(@NotNull String path, @Nullable Object value, @Nullable List<String> comments){
        cfg.set(path, value);
        cfg.setComments(path, comments);
    }
    @Override
    public void setComments(@NotNull String path, @NotNull String @Nullable... comments){
        cfg.setComments(path, comments == null || comments.length < 1 ? null : List.of(comments));
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return cfg.get(path);
    }

    public @NotNull Map<String, Object> getAsMap(@NotNull String path){
        ConfigurationSection root = cfg.getRoot();
        if(root == null) return new HashMap<>();
        return getAsMap(root, path);
    }

    public @NotNull Map<String, Object> getAsMap(@NotNull ConfigurationSection section, @NotNull String path){
        Map<String, Object> map = new HashMap<>();
        Object object = section.get(path);
        if(object == null) return map;
        if(object instanceof MemorySection sec){
            for(String s : sec.getKeys(false)){
                Map<String, Object> found = getAsMap(sec, s);
                Object d = found.size() == 1 ? found.get(null) : found;
                if(d==null) d = found;
                map.put(s, d);
            }
            return map;
        }
        map.put(null, object);
        return map;
    }


    @Override
    public boolean contains(@NotNull String path) {
        return cfg.contains(path);
    }
}
