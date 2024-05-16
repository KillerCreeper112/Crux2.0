package killercreepr.crux.config.bukkit.file;

import killercreepr.crux.config.common.file.ICruxConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    @Override
    public boolean contains(@NotNull String path) {
        return cfg.contains(path);
    }
}
