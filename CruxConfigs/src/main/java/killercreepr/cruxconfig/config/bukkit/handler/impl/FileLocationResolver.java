package killercreepr.cruxconfig.config.bukkit.handler.impl;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.data.holder.LocationResolver;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.location.SimplePositionResolver;
import killercreepr.crux.core.util.CruxCollection;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.*;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@JsonSerializer(id = "location_resolver")
public class FileLocationResolver extends SimpleFileHandler<LocationResolver> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull LocationResolver vector) {
        if(!(vector instanceof SimplePositionResolver r)) return FileNull.INSTANCE;
        var reg = context.getRegistry();

        var o = new FileObject()
            .add("x", reg.serializeToFile(r.getX()))
            .add("y", reg.serializeToFile(r.getY()))
            .add("z", reg.serializeToFile(r.getZ()))
            ;
        if(r.getWorld() != null) o.addProperty("world", r.getWorld());
        return o;
    }

    @Override
    public @Nullable LocationResolver deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        FileRegistry registry = context.getRegistry();
        if(!(e instanceof FileObject o)){
            if(!(e instanceof FileArray)) return null;
            List<NumberProvider> list = registry.deserializeFromFile(new TypeToken<List<NumberProvider>>(){}.getType(), e);
            if(list == null) return new SimplePositionResolver(
                NumberProvider.zero(), NumberProvider.zero(), NumberProvider.zero(), null
            );
            return new SimplePositionResolver(
                CruxCollection.getOrDefault(list, 0, NumberProvider.zero()),
                CruxCollection.getOrDefault(list, 1, NumberProvider.zero()),
                CruxCollection.getOrDefault(list, 2, NumberProvider.zero()),
                null
            );
        }

        if(o.get("pos") instanceof FileArray){
            List<NumberProvider> list = registry.deserializeFromFile(new TypeToken<List<NumberProvider>>(){}.getType(), e);
            if(list == null) return new SimplePositionResolver(
                NumberProvider.zero(), NumberProvider.zero(), NumberProvider.zero(), null
            );
            return new SimplePositionResolver(
                CruxCollection.getOrDefault(list, 0, NumberProvider.zero()),
                CruxCollection.getOrDefault(list, 1, NumberProvider.zero()),
                CruxCollection.getOrDefault(list, 2, NumberProvider.zero()),
                registry.deserializeFromFile(String.class, o.get("world"))
            );
        }

        NumberProvider x = registry.deserializeFromFileOrDefault(NumberProvider.class, o.get("x"), NumberProvider.zero());
        NumberProvider y = registry.deserializeFromFileOrDefault(NumberProvider.class, o.get("y"), NumberProvider.zero());
        NumberProvider z = registry.deserializeFromFileOrDefault(NumberProvider.class, o.get("z"), NumberProvider.zero());
        return new SimplePositionResolver(
            x, y, z, registry.deserializeFromFile(String.class, o.get("world"))
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "location_resolver";
    }
}
