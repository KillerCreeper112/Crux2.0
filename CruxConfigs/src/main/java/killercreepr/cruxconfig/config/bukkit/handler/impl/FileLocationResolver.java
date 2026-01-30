package killercreepr.cruxconfig.config.bukkit.handler.impl;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.data.holder.LocationResolver;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.location.SimpleLocationResolver;
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
        if(vector instanceof SimplePositionResolver r){
            var reg = context.getRegistry();

            var o = new FileObject()
              .add("x", reg.serializeToFile(r.getX()))
              .add("y", reg.serializeToFile(r.getY()))
              .add("z", reg.serializeToFile(r.getZ()))
              ;
            if(r.getWorld() != null) o.addProperty("world", r.getWorld());
            return o;
        }
        if(vector instanceof SimpleLocationResolver r){
            var reg = context.getRegistry();

            var o = new FileObject()
              .add("x", reg.serializeToFile(r.getX()))
              .add("y", reg.serializeToFile(r.getY()))
              .add("z", reg.serializeToFile(r.getZ()))
              .add("yaw", reg.serializeToFile(r.getYaw()))
              .add("pitch", reg.serializeToFile(r.getPitch()))
              ;
            if(r.getWorld() != null) o.addProperty("world", r.getWorld());
            return o;
        }
        return FileNull.INSTANCE;
    }

    @Override
    public @Nullable LocationResolver deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        FileRegistry registry = context.getRegistry();
        if(!(e instanceof FileObject o)){
            if(!(e instanceof FileArray a)) return null;
            if(a.isEmpty()) return new SimplePositionResolver(
              NumberProvider.zero(), NumberProvider.zero(), NumberProvider.zero(), null
            );

            if(a.size() == 6){
                return new SimpleLocationResolver(
                  registry.deserializeFromFileOrDefault(NumberProvider.class, a.get(0), context, NumberProvider.zero()),
                  registry.deserializeFromFileOrDefault(NumberProvider.class, a.get(1), context, NumberProvider.zero()),
                  registry.deserializeFromFileOrDefault(NumberProvider.class, a.get(2), context, NumberProvider.zero()),
                  registry.deserializeFromFileOrDefault(NumberProvider.class, a.get(3), context, NumberProvider.zero()),
                  registry.deserializeFromFileOrDefault(NumberProvider.class, a.get(4), context, NumberProvider.zero()),
                  registry.deserializeFromFile(String.class, a.get(5), context)
                );
            }
            if(a.size() == 5){
                return new SimpleLocationResolver(
                  registry.deserializeFromFileOrDefault(NumberProvider.class, a.get(0), context, NumberProvider.zero()),
                  registry.deserializeFromFileOrDefault(NumberProvider.class, a.get(1), context, NumberProvider.zero()),
                  registry.deserializeFromFileOrDefault(NumberProvider.class, a.get(2), context, NumberProvider.zero()),
                  registry.deserializeFromFileOrDefault(NumberProvider.class, a.get(3), context, NumberProvider.zero()),
                  registry.deserializeFromFileOrDefault(NumberProvider.class, a.get(4), context, NumberProvider.zero()),
                  null
                );
            }

            int index = -1;
            var list = new ArrayList<NumberProvider>();
            for (FileElement aEle : a.asList()) {
                index++;
                if(index > 2) break;
                var value = registry.deserializeFromFile(NumberProvider.class, aEle, context);
                list.add(value);
            }
            return new SimplePositionResolver(
                CruxCollection.getOrDefaultNonNull(list, 0, NumberProvider.zero()),
                CruxCollection.getOrDefaultNonNull(list, 1, NumberProvider.zero()),
                CruxCollection.getOrDefaultNonNull(list, 2, NumberProvider.zero()),
                index == 3 ? registry.deserializeFromFile(String.class, a.get(index)) : null
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
        NumberProvider yaw = registry.deserializeFromFile(NumberProvider.class, o.get("yaw"));
        NumberProvider pitch = registry.deserializeFromFile(NumberProvider.class, o.get("pitch"));
        if(yaw != null || pitch != null){
            return new SimpleLocationResolver(
              x, y, z, yaw == null ? NumberProvider.zero() : yaw,
              pitch == null ? NumberProvider.zero() : pitch,
              registry.deserializeFromFile(String.class, o.get("world"))
            );
        }
        return new SimplePositionResolver(
            x, y, z, registry.deserializeFromFile(String.class, o.get("world"))
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "location_resolver";
    }
}
