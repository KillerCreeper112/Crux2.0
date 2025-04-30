package killercreepr.crux.core.entity.predicate;

import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityAllPredicate implements EntityPredicate, StringListEncodeComponent {
    protected final @NotNull Collection<EntityPredicate> children;
    public EntityAllPredicate(@NotNull Collection<EntityPredicate> children) {
        this.children = children;
    }

    @Override
    public boolean test(@NotNull Entity block) {
        for(EntityPredicate predicate : children){
            if(!predicate.test(block)) return false;
        }
        return true;
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
