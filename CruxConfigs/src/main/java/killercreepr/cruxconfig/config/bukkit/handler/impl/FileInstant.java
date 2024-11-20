package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.core.util.CruxObjects;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.DateTimeException;
import java.time.Instant;

public class FileInstant implements FileObjectHandler<Instant> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull Instant object) {
        return new FileObject()
            .addProperty("epoch_second", object.getEpochSecond())
            .addProperty("nano", object.getNano())
            ;
    }

    @Override
    public @Nullable Instant deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            if(e instanceof FilePrimitive p){
                try{
                    return Instant.parse(p.getAsString());
                }catch (DateTimeException ignored){}
            }
            return null;
        }
        Long epochSecond = o.getObject(Long.class, "epoch_second");
        Integer nano = o.getObject(Integer.class, "nano");
        if(CruxObjects.checkNull(epochSecond, nano)) return null;
        return Instant.ofEpochSecond(epochSecond, nano);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "instant";
    }
}
