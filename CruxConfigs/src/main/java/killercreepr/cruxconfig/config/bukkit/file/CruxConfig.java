package killercreepr.cruxconfig.config.bukkit.file;

import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxconfig.config.common.file.ICruxConfig;
import killercreepr.cruxconfig.config.common.file.IYamlCfg;
import killercreepr.cruxconfig.config.common.yaml.element.YamlArray;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlGeneric;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;

public class CruxConfig extends CruxFolder implements IYamlCfg<MemoryConfiguration>, DataFile {
    protected final YamlRegistry yamlRegistry;
    protected final FileConfiguration cfg;

    public CruxConfig(@NotNull Plugin plugin, @NotNull String path){
        this(plugin, path, CfgRegistries.YAML);
    }

    public CruxConfig(@NotNull File file){
        this(file, CfgRegistries.YAML);
    }

    public CruxConfig(@NotNull CruxConfig cfg){
        this(cfg.file(), CfgRegistries.YAML);
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

    public void reload() {
        if(!file.exists()) return;
        try{
            cfg.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public @NotNull YamlRegistry yamlRegistry() {
        return yamlRegistry;
    }

    protected void setDefaults(){}

    @Override
    public void set(@NotNull String path, @Nullable Object value){
        if(value==null){
            cfg.set(path, null);
            return;
        }
        setElement(path, yamlRegistry.serializeToYaml(value));
    }

    public void setElement(@NotNull String path, @NotNull YamlElement element){
        ConfigurationSection section = cfg.getRoot();
        if(section == null) throw new UnsupportedOperationException("Configuration has no root!");
        setElement(section, path, element);
    }

    public @NotNull Object serializeReady(@NotNull YamlElement element){
        if(element instanceof YamlGeneric r){
            return yamlRegistry.serializeToYaml(r.getAsObject()).getAsObject();
        }
        if(element instanceof YamlArray a){
            if(a.isEmpty()){
                return new ArrayList<>();
            }
            YamlElement first = a.get(0);
            if(first instanceof YamlGeneric){
                List<Object> list = new ArrayList<>();
                for(YamlElement e : a){
                    list.add(e.getAsObject());
                }
                return list;
            }
            List<Object> mapList = new ArrayList<>();
            for(YamlElement e : a){
                mapList.add(serializeReady(e));
            }
            return mapList;
        }

        if(element instanceof YamlObject a){
            Map<String, Object> map = new LinkedHashMap<>();
            a.forEach((key, value) -> map.put(key, serializeReady(value)));
            return map;
        }
        throw new IllegalArgumentException("no sir");
    }

    public void setElement(@NotNull ConfigurationSection section, @NotNull String path, @NotNull YamlElement element){
        if(true){
            section.set(path, serializeReady(element));
            return;
        }
        if(element instanceof YamlGeneric r){
            section.set(path, yamlRegistry.serializeToYaml(r.getAsObject()).getAsObject());
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
            List<Map<?, ?>> mapList = new ArrayList<>();

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

    public <T> @Nullable T deserialize(@NotNull Class<T> clazz, @NotNull String path){
        ConfigurationSection section = cfg.getRoot();
        if(section == null) throw new UnsupportedOperationException("Configuration has no root!");
        return deserialize(clazz, section, path);
    }

    public <T> @Nullable T deserialize(@NotNull Type clazz, @NotNull ConfigurationSection section, @NotNull String path){
        YamlElement yaml = getAsYamlObject(section, path);
        if(yaml==null) return null;
        return (T) yamlRegistry.deserializeFromYaml(clazz, yaml);
    }

    public @Nullable Object deserializeObject(@NotNull Type type, @NotNull String path){
        ConfigurationSection section = cfg.getRoot();
        if(section == null) throw new UnsupportedOperationException("Configuration has no root!");
        return deserialize(type, section, path);
    }

    public @Nullable Object deserializeObject(@NotNull Class<?> clazz, @NotNull ConfigurationSection section, @NotNull String path){
        YamlElement yaml = getAsYamlObject(section, path);
        if(yaml==null) return null;
        return yamlRegistry.deserializeFromYaml(clazz, yaml);
    }

    public @Nullable YamlObject getRootAsYamlObject(){
        ConfigurationSection root = cfg.getRoot();
        if(root == null) return null;
        YamlObject map = new YamlObject();
        for(String s : root.getKeys(false)){
            YamlElement found = getAsYamlObject(root, s);
            map.add(s, found);
        }
        return map;
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
        return yamlRegistry.serializeToYaml(object);
    }


    @Override
    public boolean contains(@NotNull String path) {
        return cfg.contains(path);
    }

    //Convenience
    public static @NotNull String addDot(@NotNull String s){
        return ICruxConfig.addDot(s);
    }

    public static @NotNull String removeDot(@NotNull String s){
        return ICruxConfig.removeDot(s);
    }

    @Override
    public @Nullable FileElement getRoot() {
        YamlObject root = getRootAsYamlObject();
        if(root==null) return null;
        return FileElement.fromYaml(root);
    }

    @Override
    public @Nullable FileElement getElement(@NotNull String path) {
        if(path.isBlank()) return getRoot();
        if(!(getRoot() instanceof FileObject o)) return null;
        FileObject previous = o;
        int index = 0;
        String[] split = path.split("\\" + pathSeparator);
        for(String s : split){
            index++;
            if(index == split.length){
                return previous.get(s);
            }
            if(!(previous.get(s) instanceof FileObject got)) return null;
            previous = got;
        }
        return null;
    }

    @Override
    public void serialize(@NotNull String path, @Nullable Object value) {
        set(path, value);
    }

    @Override
    public <T> @Nullable T deserialize(@NotNull String path, @NotNull Type type) {
        ConfigurationSection section = cfg.getRoot();
        if(section == null) throw new UnsupportedOperationException("Configuration has no root!");
        return deserialize(type, section, path);
    }

    @Override
    public @NotNull FileRegistry fileRegistry() {
        return yamlRegistry;
    }

    protected char pathSeparator = '.';
    @Override
    public char getPathSeparator() {
        return pathSeparator;
    }

    @Override
    public void setPathSeparator(char separator) {
        this.pathSeparator = separator;
        cfg.options().pathSeparator(separator);
    }

    @Override
    public void close() {
    }
}
