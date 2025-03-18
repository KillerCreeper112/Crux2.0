package killercreepr.crux.core.key.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.key.tag.KeyPredicate;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeyAnyPredicate implements KeyPredicate, StringListEncodeComponent {
    protected final @NotNull Collection<KeyPredicate> children;
    public KeyAnyPredicate(@NotNull Collection<KeyPredicate> children) {
        this.children = children;
    }

    @Override
    public boolean test(@NotNull Key block) {
        for(KeyPredicate predicate : children){
            if(predicate.test(block)) return true;
        }
        return false;
    }



    @Override
    public @NotNull List<String> encodeToParser() {
        List<String> list = new ArrayList<>();
        for(KeyPredicate predicate : children){
            if(!(predicate instanceof StringListEncodeComponent cc)) continue;
            list.addAll(cc.encodeToParser());
        }
        return list;
    }
}
