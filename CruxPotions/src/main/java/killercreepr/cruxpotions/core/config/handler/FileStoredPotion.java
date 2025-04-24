package killercreepr.cruxpotions.core.config.handler;

import killercreepr.crux.api.component.parser.DataComponentDecoder;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxpotions.api.potion.CruxPotion;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.core.registries.CruxPotionRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileStoredPotion implements FileObjectHandler<StoredPotion> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull StoredPotion potion) {
        var reg = ctx.getRegistry();
        FileObject o = new FileObject();
        o.add("potion", reg.serializeToFile(potion.getPotion().key()));
        o.addProperty("duration", potion.getDuration());
        o.addProperty("amplifier", potion.getAmplifier());
        var data = potion.serializeDataToFile(ctx);
        if(data != null) o.add("data", data);
        return o;
    }

    @Override
    public @Nullable StoredPotion deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        var reg = ctx.getRegistry();
        var key = reg.deserializeFromFile(Key.class, o.get("potion"));
        if(key == null) return null;
        CruxPotion potion = CruxPotionRegistries.POTION.get(key);
        if(potion == null) return null;
        Integer duration = o.getObject(Integer.class, "duration");
        Integer amplifier = o.getObject(Integer.class, "amplifier");
        if(duration == null || amplifier == null) return null;
        if(o.get("data") instanceof FileObject data){
            return potion.deserializeFromFile(duration, amplifier, data);
        }
        return potion.deserializeFromFile(duration, amplifier, null);
    }

    public Object parseObject(Object object){
        if(object instanceof FileArray a){
            List<Object> list = new ArrayList<>();
            a.forEach(ee -> list.add(parseObject(ee)));
            return list;
        }
        if(object instanceof FileObject o){
            Map<String, Object> map = new HashMap<>();
            o.asMap().forEach((id, ee) ->{
                map.put(id, parseObject(ee));
            });
            return map;
        }
        if(object instanceof FileElement e) return e.getAsObject();
        return object;
    }
}
