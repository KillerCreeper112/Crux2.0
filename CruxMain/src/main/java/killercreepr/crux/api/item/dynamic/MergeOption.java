package killercreepr.crux.api.item.dynamic;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.data.SimpleKeyed;
import net.kyori.adventure.key.Key;

import java.util.Objects;

public interface MergeOption extends CruxKeyed {
    static MergeOption mergeOption(Key key){
        String x = key.asString();
        return switch (x){
            case "crux:ignore" -> IGNORE;
            case "crux:merge" -> MERGE;
            case "crux:overwrite" -> OVERWRITE;
            default -> null;
        };
    }

    MergeOption IGNORE = new Simple(Crux.key("ignore"));
    MergeOption MERGE = new Simple(Crux.key("merge"));
    MergeOption OVERWRITE = new Simple(Crux.key("overwrite"));

    class Simple extends SimpleKeyed implements MergeOption{
        public Simple(Key key) {
            super(key);
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof MergeOption option)) return false;
            return option.compare(this);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key);
        }

        @Override
        public String toString() {
            return "MergeOption.Simple{" +
                "key=" + key +
                '}';
        }
    }
}
