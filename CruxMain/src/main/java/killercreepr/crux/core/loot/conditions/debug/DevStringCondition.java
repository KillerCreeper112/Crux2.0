package killercreepr.crux.core.loot.conditions.debug;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class DevStringCondition extends BaseCondition {
    protected final String match;
    protected final String eva;
    protected final Collection<String> targets;
    protected final boolean ignoreCase;
    public DevStringCondition(@NotNull String target, String match, String eva, Collection<String> targets, boolean ignoreCase) {
        super(target);
        this.match = match;
        this.eva = eva;
        this.targets = targets;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        var tags = TagContainer.string();

        if(targets == null || targets.isEmpty()){
            tags.hookAllWithPrefix(ctx.info());
        }else{
            for(String s : targets){
                Object object = ctx.info().get(s);
                if(object == null) continue;
                tags.hook(object, TagsPrefixBuilder.overwriteBase(s + "_"));
            }
        }

        String check = Crux.format().deserializeString(eva, tags);
        Bukkit.broadcastMessage("check BEFORE=" + eva);
        Bukkit.broadcastMessage("check =" + check);
        Bukkit.broadcastMessage("match=" + match);
        return true;
    }
}
