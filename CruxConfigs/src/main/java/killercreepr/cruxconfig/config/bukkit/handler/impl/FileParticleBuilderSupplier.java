package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.data.ParticleBuilderSupplier;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.api.valueproviders.vector.NumberVector;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.util.CruxColor;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "particle_builder_supplier")
public class FileParticleBuilderSupplier extends SimpleFileHandler<ParticleBuilderSupplier> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull ParticleBuilderSupplier object) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public @Nullable ParticleBuilderSupplier deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        Double extra = context.getRegistry().deserializeFromFile(Double.class, o.get("extra"));
        NumberProvider count = context.getRegistry().deserializeFromFile(NumberProvider.class, o.get("count"));
        NumberVector offset = context.getRegistry().deserializeFromFile(NumberVector.class, o.get("offset"));
        if(extra == null) extra = 0D;
        if(offset == null) NumberVector.vector(0,0,0);
        if(count == null) NumberProvider.constant(1);
        Holder<Object> data = null;
        if(o.has("item")){
            Key itemKey = context.getRegistry().deserializeFromFile(Key.class, o.get("item"));
            if(itemKey != null){
                data = () -> Crux.handlers().item().getItem(itemKey).value();
            }
        }else if(o.has("block")){
            Material material = context.getRegistry().deserializeFromFile(Material.class, o.get("block"));
            if(material != null){
                data = material::createBlockData;
            }
        }else if(o.has("from_color")){
            Color fromColor = context.getRegistry().deserializeFromFile(Color.class, o.get("from_color"));
            Color toColor = context.getRegistry().deserializeFromFile(Color.class, o.get("to_color"));
            if(toColor == null) toColor = fromColor;
            Float size = context.getRegistry().deserializeFromFile(Float.class, o.get("size"));
            if(size == null) size = 1f;
            data = Holder.directObject(new Particle.DustTransition(
                fromColor, toColor, size
            ));
        }else if(o.has("color")){
            Color color = context.getRegistry().deserializeFromFile(Color.class, o.get("color"));
            Float size = context.getRegistry().deserializeFromFile(Float.class, o.get("size"));
            if(size == null) size = 1f;
            data = Holder.directObject(new Particle.DustOptions(color, size));
        }
        return ParticleBuilderSupplier.builder()
            .particle(context.getRegistry().deserializeFromFile(Particle.class, o.get("particle")))
            .offset(offset)
            .extra(extra)
            .count(count)
            .data(data)
            .build();
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "particle_builder_supplier";
    }
}
