package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.core.math.BlockPos;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@JsonSerializer(id = "block_pos")
public class FileBlockPos extends SimpleFileHandler<BlockPos> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull BlockPos vector) {
        return new FileObject()
            .addProperty("x", vector.blockX())
            .addProperty("y", vector.blockY())
            .addProperty("z", vector.blockZ())
            ;
    }

    @Override
    public @Nullable BlockPos deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            if(e instanceof FileArray a){
                List<Number> list = new ArrayList<>();
                a.forEach(ele ->{
                    FileGeneric generic = ele.getAsFilePrimitive();
                    if(generic.isString()) list.add(CruxMath.evaluate(generic.getAsString()));
                    else list.add(generic.getAsNumber());
                });
                if(list.isEmpty()) return BlockPos.at(0,0,0);
                return new BlockPos(
                    list.get(0).intValue(),
                    list.size() > 1 ? list.get(1).intValue() : 0,
                    list.size() > 2 ? list.get(2).intValue() : 0
                );
            }
            return null;
        }
        FileRegistry registry = context.getRegistry();
        Number x = registry.deserializeFromFile(Number.class, o.get("x"));
        Number y = registry.deserializeFromFile(Number.class, o.get("y"));
        Number z = registry.deserializeFromFile(Number.class, o.get("z"));
        return new BlockPos(x == null ? 0 : x.intValue(), y == null ? 0 : y.intValue(), z == null ? 0 : z.intValue());
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "block_pos";
    }
}
