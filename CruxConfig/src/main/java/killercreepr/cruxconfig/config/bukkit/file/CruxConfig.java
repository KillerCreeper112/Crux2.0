package killercreepr.cruxconfig.config.bukkit.file;

import killercreepr.cruxconfig.config.common.file.ICruxConfig;
import killercreepr.cruxconfig.config.common.yaml.element.*;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import killercreepr.cruxconfig.config.registry.DefaultYamlRegistry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CruxConfig extends CruxFolder implements ICruxConfig<FileConfiguration> {
    protected final YamlRegistry yamlRegistry;
    protected final FileConfiguration cfg;

    public CruxConfig(@NotNull Plugin plugin, @NotNull String path){
        this(plugin, path, DefaultYamlRegistry.REGISTRY);
    }

    public CruxConfig(@NotNull File file){
        this(file, DefaultYamlRegistry.REGISTRY);
    }

    public CruxConfig(@NotNull CruxConfig cfg){
        this(cfg.file(), DefaultYamlRegistry.REGISTRY);
    }

    public CruxConfig(@NotNull Plugin plugin, @NotNull String path, @NotNull YamlRegistry registry){
        super(plugin, path + ".yml");
        cfg = YamlConfiguration.loadConfiguration(file);
        this.yamlRegistry = registry;
    }

    public CruxConfig(@NotNull File file, @NotNull YamlRegistry registry){
        super(file);
        cfg = YamlConfiguration.loadConfiguration(file);
        this.yamlRegistry = registry;
    }

    public CruxConfig(@NotNull CruxConfig cfg, @NotNull YamlRegistry registry){
        this(cfg.file(), registry);
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

    @Override
    public @NotNull YamlRegistry yamlRegistry() {
        return yamlRegistry;
    }

    protected void setDefaults(){}

    @Override
    public void set(@NotNull String path, @Nullable Object value){
        value = YamlElement.toSerializable(value);
        if(value instanceof YamlElement e){
            setElement(cfg.getRoot(), path, e);
            return;
        }
        cfg.set(path, value);
    }

    public void setElement(@NotNull ConfigurationSection section, @NotNull String path, @NotNull YamlElement element){
        if(element instanceof YamlGeneric r){
            section.set(path, yamlRegistry.serializeObject(r.getAsObject()));
            return;
        }
        if(element instanceof YamlArray a){
            if(a.isEmpty()){
                section.set(path, new ArrayList<>());
                return;
            }
            YamlElement first = a.get(0);
            if(first instanceof YamlGeneric){
                List<Object> list = new ArrayList<>();
                for(YamlElement e : a){
                    list.add(e.getAsObject());
                }
                section.set(path, list);
                return;
            }
            int index = -1;
            for(YamlElement e : a){
                index++;
                setElement(section, addDot(path) + index, e);
            }
            return;
        }

        if(element instanceof YamlObject a){
            a.forEach((key, value) -> setElement(section, addDot(path) + key, value));
            return;
        }
        throw new UnsupportedOperationException("Cannot serialize YamlElement! (" + element.getClass().getSimpleName() + ")");
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

    public @Nullable YamlElement getAsYamlObject(@NotNull String path){
        ConfigurationSection root = cfg.getRoot();
        if(root == null) return new YamlObject();
        return getAsYamlObject(root, path);
    }

    public @Nullable YamlElement getAsYamlObject(@NotNull ConfigurationSection section, @NotNull String path){
        Object object = section.get(path);
        if(object == null) return null;
        if(object instanceof ConfigurationSection sec){
            YamlObject map = new YamlObject();
            for(String s : sec.getKeys(false)){
                YamlElement found = getAsYamlObject(sec, s);
                map.add(s, found);
            }
            return map;
        }
        return YamlElement.fromObject(object);
    }


    @Override
    public boolean contains(@NotNull String path) {
        return cfg.contains(path);
    }

    public static @NotNull String addDot(@NotNull String s){
        return ICruxConfig.addDot(s);
    }

    public static @NotNull String removeDot(@NotNull String s){
        return ICruxConfig.removeDot(s);
    }
}
