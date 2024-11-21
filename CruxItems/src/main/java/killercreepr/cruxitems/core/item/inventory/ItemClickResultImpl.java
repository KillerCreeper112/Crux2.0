package killercreepr.cruxitems.core.item.inventory;

import killercreepr.cruxitems.api.item.inventory.ItemClickResult;
import org.jetbrains.annotations.Nullable;

public class ItemClickResultImpl implements ItemClickResult {
    public static Builder builder(){
        return new Builder();
    }

    protected final Boolean cancel;

    public ItemClickResultImpl(@Nullable Boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public @Nullable Boolean getCancelled() {
        return cancel;
    }

    public static final class Builder {
        private Boolean cancel;

        public Builder cancel(boolean cancel) {
            this.cancel = cancel;
            return this;
        }


        public ItemClickResultImpl build() {
            return new ItemClickResultImpl(cancel);
        }
    }
}
