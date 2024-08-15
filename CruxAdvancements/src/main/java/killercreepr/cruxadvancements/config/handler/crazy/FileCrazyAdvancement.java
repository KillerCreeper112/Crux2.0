package killercreepr.cruxadvancements.config.handler.crazy;

import eu.endercentral.crazy_advancements.advancement.AdvancementFlag;
import io.leangen.geantyref.TypeToken;
import killercreepr.crux.util.CruxObjects;
import killercreepr.cruxadvancements.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxadvancements.config.handler.FileAdvancementObjective;
import killercreepr.cruxadvancements.crazy.CrazyAdvancement;
import killercreepr.cruxadvancements.crazy.CrazyAdvancementDisplay;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
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
        Key parentKey = registry.deserializeFromFile(Key.class, o.get("parent"));
        CruxCriteria criteria = registry.deserializeFromFile(CruxCriteria.class, o.get("criteria"));
        CrazyAdvancementDisplay display = registry.deserializeFromFile(CrazyAdvancementDisplay.class, o.get("display"));

        Collection<AdvancementFlag> flags = registry.deserializeFromFile(new TypeToken<Collection<AdvancementFlag>>(){}.getType(), o.get("flags"));

        CruxAdvanceReward reward = registry.deserializeFromFile(CruxAdvanceReward.class, o.get("reward"));
        if(CruxObjects.checkNull(key, criteria, display)) return null;

        AdvancementFlag[] flagsParsed;
        if(flags == null) flagsParsed = AdvancementFlag.TOAST_AND_MESSAGE;
        else flagsParsed = flags.toArray(new AdvancementFlag[0]);

        Map<String, AdvancementObjective> objectives = new HashMap<>();
        if(o.get("objectives") instanceof FileObject oo){
            oo.forEach((objectiveKey, value) ->{
                AdvancementObjective objective = FileAdvancementObjective.deserializeFromFile(ctx, value, objectiveKey);
                if(objective==null) return;
                objectives.put(objectiveKey, objective);
            });
        }

        return new CrazyAdvancement(
            key, parentKey, display, criteria, reward, flagsParsed, objectives

        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crazy_advancement";
    }
}
