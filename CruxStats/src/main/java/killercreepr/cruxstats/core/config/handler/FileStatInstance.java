package killercreepr.cruxstats.core.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.api.stat.CruxStatInstance;
import killercreepr.cruxstats.api.stat.CruxStatModifier;
import killercreepr.cruxstats.core.registries.CruxStatRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.logging.Level;


public class FileStatInstance implements FileObjectHandler<CruxStatInstance> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CruxStatInstance o) {
        FileRegistry reg = ctx.getRegistry();
        return new FileObject()
            .add("stat", reg.serializeToFile(o.getStat().key()))
            .add("modifiers", reg.serializeToFile(o.getModifiers()))
            ;
    }

    @Override
    public @Nullable CruxStatInstance deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry reg = ctx.getRegistry();
        Key statKey = reg.deserializeFromFile(Key.class, o.get("stat"));
        CruxStat stat = CruxStatRegistries.STAT.get(statKey);
        if(stat == null){
            Crux.log(Level.WARNING, "No stat of " + statKey + " found!");
            return null;
        }
        CruxStatInstance in = stat.createNewInstance();
        if(in == null) return null;
        Collection<CruxStatModifier> modifiers = reg.deserializeFromFile(new TypeToken<Collection<CruxStatModifier>>(){}.getType(), o.get("modifiers"));
        if(modifiers != null) modifiers.forEach(in::addModifier);
        return in;
    }
}
