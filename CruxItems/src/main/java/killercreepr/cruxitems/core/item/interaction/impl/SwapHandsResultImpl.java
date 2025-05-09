package killercreepr.cruxitems.core.item.interaction.impl;

import killercreepr.cruxitems.api.item.interaction.SwapHandsResult;
import org.jetbrains.annotations.Nullable;

public class SwapHandsResultImpl implements SwapHandsResult {
    public static Builder builder(){
        return new Builder();
    }

    protected final Boolean cancel;
    protected final boolean successful;

    public SwapHandsResultImpl(@Nullable Boolean cancel, boolean successful) {
        this.cancel = cancel;
        this.successful = successful;
    }

    @Override
    public @Nullable Boolean getCancelled() {
        return cancel;
    }

    @Override
    public boolean successful() {
        return successful;
    }


    public static final class Builder {
        private Boolean cancel;
        private boolean successful;

        public Builder cancel(boolean cancel) {
            this.cancel = cancel;
            return this;
        }
        public Builder successful(boolean successful) {
            this.successful = successful;
            return this;
        }

        public SwapHandsResultImpl build() {
            return new SwapHandsResultImpl(cancel, successful);
        }
    }
}
