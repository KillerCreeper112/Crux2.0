package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class SelectStringCondition extends BaseCondition {
    protected final String match;
    protected final String eva;
    protected final Collection<String> targets;
    protected final boolean ignoreCase;
    protected final boolean startsWith;
    public SelectStringCondition(@NotNull String target, String match, String eva, Collection<String> targets, boolean ignoreCase, boolean startsWith) {
        super(target);
        this.match = match;
        this.eva = eva;
        this.targets = targets;
        this.ignoreCase = ignoreCase;
        this.startsWith = startsWith;
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
        if(startsWith){
            if(ignoreCase){
                if(!check.toLowerCase().startsWith(match.toLowerCase())) return false;
            }else if(!check.startsWith(match)) return false;
            return true;
        }

        if(ignoreCase){
            if(!match.equalsIgnoreCase(check)) return false;
        }else if(!match.equals(check)) return false;
        return true;
    }
}
