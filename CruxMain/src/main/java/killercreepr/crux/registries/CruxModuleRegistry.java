package killercreepr.crux.registries;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registry.SimpleMappedRegistry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class CruxModuleRegistry extends SimpleMappedRegistry<String, CruxModule> {
    @Override
    public @NotNull CruxModule register(@NotNull CruxModule object) {
        return register(object.name(), object);
    }

    @Override
    public boolean unregister(@NotNull CruxModule object) {
        return remove(object.name()) != null;
    }

    /**
     * Using this method requires the module class to have a public static String NAMESPACE variables
     * that it uses for its name/ID.
     */
    public <T extends CruxModule> @Nullable T getModule(@NotNull Class<T> clazz){
        try{
            Field staticKeyField = clazz.getDeclaredField("NAMESPACE");
            String key = (String) staticKeyField.get(null);
            try{
                return get(clazz, key);
            }catch (NullPointerException | ClassCastException ignored){ return null; }
        }catch (NoSuchFieldException | IllegalAccessException ignored){
            throw new UnsupportedOperationException(clazz.getSimpleName() + " does not have a public static String NAMESPACE variable!");
        }
    }

    public <T extends CruxModule> @NotNull T getModuleOrThrow(@NotNull Class<T> clazz){
        T found = getModule(clazz);
        if(found==null) throw new RuntimeException(clazz.getSimpleName() + " has not been registered as a module!");
        return found;
    }

    /**
     * Registers and enabled only the provided modules.
     */
    public CruxModuleRegistry registerAndEnable(@NotNull CruxPlugin plugin, @NotNull CruxModule... modules){
        for(CruxModule m : modules){
            register(m);
        }
        for(CruxModule m : modules){
            m.onEnable(plugin);
        }
        return this;
    }

    public CruxModuleRegistry register(@NotNull CruxModule... modules){
        for(CruxModule m : modules){
            register(m);
        }
        return this;
    }

    public CruxModuleRegistry enable(@NotNull CruxPlugin plugin){
        for(CruxModule m : this){
            m.onEnable(plugin);
        }
        return this;
    }

    public CruxModuleRegistry unregisterAll(@NotNull CruxPlugin plugin){
        for(CruxModule m : this){
            m.onDisable(plugin);
        }
        clear();
        return this;
    }

    public CruxModuleRegistry reload(@NotNull CruxPlugin plugin){
        for(CruxModule m : this){
            m.reload(plugin);
        }
        return this;
    }
}
