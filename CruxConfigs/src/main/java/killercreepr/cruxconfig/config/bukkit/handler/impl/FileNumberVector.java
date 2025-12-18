package killercreepr.cruxconfig.config.bukkit.handler.impl;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.api.valueproviders.vector.NumberVector;
import killercreepr.crux.core.util.CruxCollection;
import killercreepr.crux.core.util.CruxObjects;
import killercreepr.crux.core.valueproviders.vector.SimpleNumberVector;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@JsonSerializer(id = "number_vector")
public class FileNumberVector extends SimpleFileHandler<NumberVector> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull NumberVector object) {
        FileRegistry registry = context.getRegistry();
        return new FileObject()
            .add("x", registry.serializeToFile(object.x()))
            .add("y", registry.serializeToFile(object.y()))
            .add("z", registry.serializeToFile(object.z()))
            ;
        //throw new UnsupportedOperationException("Oof");
    }

    @Override
    public @Nullable NumberVector deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        FileRegistry registry = context.getRegistry();
        if(!(e instanceof FileObject o)){
            List<NumberProvider> list = registry.deserializeFromFile(new TypeToken<List<NumberProvider>>(){}.getType(), e);
            if(list == null) return null;
            return new SimpleNumberVector(
                CruxCollection.getOrDefault(list, 0, NumberProvider.zero()),
                CruxCollection.getOrDefault(list, 1, NumberProvider.zero()),
                CruxCollection.getOrDefault(list, 2, NumberProvider.zero())
            );
        }
        NumberProvider x = registry.deserializeFromFile(NumberProvider.class, o.get("x"));
        NumberProvider y = registry.deserializeFromFile(NumberProvider.class, o.get("y"));
        NumberProvider z = registry.deserializeFromFile(NumberProvider.class, o.get("z"));
        if(CruxObjects.checkNull(x, y, z)) return null;
        return new SimpleNumberVector(x, y, z);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "number_vector";
    }
}
