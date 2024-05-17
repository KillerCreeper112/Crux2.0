package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.ConfigValue;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MaterialValue extends ConfigValue<Material> {
    public MaterialValue(@Nullable Material defaultValue) {
        super(Material.class, defaultValue);
    }

    public MaterialValue() {
        this(null);
    }

    @Override
    public @Nullable Material get(@NotNull CruxConfig cfg, @NotNull String path) {
        String s = cfg.config().getString(removeDot(path));
        return s == null ? null : Material.getMaterial(s.toUpperCase());
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable Material object) {
        cfg.set(removeDot(path), object==null ? null : object.toString());
    }
}
