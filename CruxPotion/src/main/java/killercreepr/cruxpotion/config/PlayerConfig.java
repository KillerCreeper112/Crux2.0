package killercreepr.cruxpotion.config;

import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxpotion.potions.ActivePotion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class PlayerConfig extends CruxConfig {
    public PlayerConfig(@NotNull UUID uuid) {
        super(PotionCore.inst(), "player/" + uuid);
    }

    public @NotNull Collection<ActivePotion> getPotions(){
        Collection<?> list = cfg.getList("potions");
        try{ return (Collection<ActivePotion>) list; }
        catch (Exception ignored){ ignored.printStackTrace(); }
        return new HashSet<>();
    }

    public PlayerConfig savePotions(@Nullable Collection<ActivePotion> potions){
        cfg.set("potions", potions == null ? null : new ArrayList<>(potions));
        return this;
    }
}
