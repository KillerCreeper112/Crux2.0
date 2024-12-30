package killercreepr.cruxadvancements.core.config.handler.crazy;

import io.leangen.geantyref.TypeToken;
import killercreepr.crux.core.util.CruxObjects;
import killercreepr.cruxadvancements.api.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.api.advancement.flag.CruxAdvancementFlag;
import killercreepr.cruxadvancements.api.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.api.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxadvancements.core.config.CruxAdvanceCfgData;
import killercreepr.cruxadvancements.core.config.CruxConfigHook;
import killercreepr.cruxadvancements.core.config.handler.FileAdvancementObjective;
import killercreepr.cruxadvancements.crazy.advancement.CrazyAdvancement;
import killercreepr.cruxadvancements.crazy.advancement.CrazyAdvancementDisplay;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FileCrazyAdvancement implements FileObjectHandler<CrazyAdvancement> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CrazyAdvancement object) {
        FileRegistry registry = ctx.getRegistry();
        FileObject o = new FileObject();
        o.add("key", registry.serializeToFile(object.key()));
        if(object.parent() != null) o.add("parent", registry.serializeToFile(object.parent()));
        o.add("criteria", registry.serializeToFile(object.getCriteria()));
        o.add("display", registry.serializeToFile(object.getDisplay()));
        o.add("flags", registry.serializeToFile(object.getFlags()));
        return o;
    }

    @Override
    public @Nullable CrazyAdvancement deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, o.get("key"));
        if(key == null) return null;
        return deserializeFromFile(ctx, e, key);
    }
    public @Nullable CrazyAdvancement deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key){
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key parentKey = registry.deserializeFromFile(Key.class, o.get("parent"));
        CruxCriteria criteria = registry.deserializeFromFile(CruxCriteria.class, o.get("criteria"));
        CrazyAdvancementDisplay display = registry.deserializeFromFile(CrazyAdvancementDisplay.class, o.get("display"));

        Collection<CruxAdvancementFlag> flags = registry.deserializeFromFile(new TypeToken<Collection<CruxAdvancementFlag>>(){}.getType(), o.get("flags"));

        CruxAdvanceReward reward = registry.deserializeFromFile(CruxAdvanceReward.class, o.get("reward"));
        if(CruxObjects.checkNull(key, criteria, display)) return null;

        CruxAdvancementFlag[] flagsParsed;
        if(flags == null) flagsParsed = CruxAdvancementFlag.TOAST_AND_MESSAGE;
        else flagsParsed = flags.toArray(new CruxAdvancementFlag[0]);

        Map<String, AdvancementObjective> objectives = new HashMap<>();
        if(o.get("objectives") instanceof FileObject oo){
            oo.forEach((objectiveKey, value) ->{
                AdvancementObjective objective = CruxAdvanceCfgData.fileAdvancementObjective().deserializeFromFile(ctx, value, objectiveKey);
                if(objective != null) objective = registry.getParsedObjectRegistry().parse(value, ctx, objective);
                if(objective==null) return;
                objectives.put(objectiveKey, objective);
            });
        }

        Integer updateAdvancementPeriod = o.getObject(Integer.class, "update_advancement_period");
        if(updateAdvancementPeriod == null) updateAdvancementPeriod = 0;

        return new CrazyAdvancement(
            key, parentKey, display, criteria, reward, flagsParsed, objectives, updateAdvancementPeriod
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crazy_advancement";
    }
}
