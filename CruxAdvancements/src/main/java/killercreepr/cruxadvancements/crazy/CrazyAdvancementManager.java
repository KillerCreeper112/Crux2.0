package killercreepr.cruxadvancements.crazy;

import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import killercreepr.cruxadvancements.manager.SimpleAdvancementManager;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class CrazyAdvancementManager<T extends CrazyAdvancement> extends SimpleAdvancementManager<T> {
    protected final @NotNull AdvancementManager crazyManager;
    protected final @NotNull Map<Key, Advancement> crazyAdvancements = new HashMap<>();
    public CrazyAdvancementManager(@NotNull Key key, @NotNull AdvancementManager crazyManager) {
        super(key);
        this.crazyManager = crazyManager;
    }

    public @NotNull AdvancementManager getCrazyManager() {
        return crazyManager;
    }

    public @NotNull Key toKey(@NotNull NameKey key){
        return Key.key(key.getNamespace(), key.getKey());
    }

    public @NotNull NameKey toNameKey(@NotNull Key key){
        return new NameKey(key.namespace(), key.value());
    }

    public @NotNull Advancement getOrCreateCrazyAdvancement(@NotNull Key key){
        return getOrCreateCrazyAdvancement(
            Objects.requireNonNull(getAdvancement(key), "CrazyAdvancement, " + key + " does not exist!")
        );
    }

    public @NotNull Advancement getOrCreateCrazyAdvancement(@NotNull T crux){
        Advancement a = crazyAdvancements.get(crux.key());
        if(a != null) return a;
        Key parentCrux = crux.parent();
        Advancement parent = parentCrux == null ? null : getOrCreateCrazyAdvancement(
            Objects.requireNonNull(getAdvancement(parentCrux),
                crux.key() + " does not have its parent registered! (" + parentCrux + ")")
        );
        a = new Advancement(parent, toNameKey(crux.key()), crux.getDisplay().toCrazy(this), crux.getFlags());
        crazyAdvancements.put(crux.key(), a);
        return a;
    }

    @Override
    public void registerAdvancement(@NotNull T a) {
        super.registerAdvancement(a);
        Advancement crazy = getOrCreateCrazyAdvancement(a);
        crazyManager.addAdvancement(crazy);
    }

    @Override
    public void unregisterAdvancement(@NotNull T a) {
        super.unregisterAdvancement(a);
        Advancement crazy = getOrCreateCrazyAdvancement(a);
        crazyManager.addAdvancement(crazy);
    }
}
