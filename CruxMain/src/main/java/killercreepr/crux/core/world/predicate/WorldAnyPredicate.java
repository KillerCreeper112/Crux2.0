package killercreepr.crux.core.world.predicate;

import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.world.predicate.WorldPredicate;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WorldAnyPredicate implements WorldPredicate, StringListEncodeComponent {
    protected final @NotNull Collection<WorldPredicate> children;
    public WorldAnyPredicate(@NotNull Collection<WorldPredicate> children) {
        this.children = children;
    }

    @Override
    public boolean test(@NotNull World block) {
        for(WorldPredicate predicate : children){
            if(predicate.test(block)) return true;
        }
        return false;
    }

    @Override
    public @NotNull List<String> encodeToParser() {
        List<String> list = new ArrayList<>();
        for(var predicate : children){
            if(!(predicate instanceof StringListEncodeComponent cc)) continue;
            list.addAll(cc.encodeToParser());
        }
        return list;
    }
}
