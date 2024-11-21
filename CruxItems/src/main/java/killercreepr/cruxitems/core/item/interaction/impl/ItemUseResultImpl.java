package killercreepr.cruxitems.core.item.interaction.impl;

import killercreepr.cruxitems.api.item.interaction.ItemUseResult;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ItemUseResultImpl implements ItemUseResult {
    public static Builder builder(){
        return new Builder();
    }

    protected final Boolean cancel;
    protected final @Nullable Event.Result useItemInHand;
    protected final @Nullable Event.Result useInteractedBlock;
    protected final boolean successful;

    public ItemUseResultImpl(@Nullable Boolean cancel, @Nullable Event.Result useItemInHand, @Nullable Event.Result useInteractedBlock, boolean successful) {
        this.cancel = cancel;
        this.useItemInHand = useItemInHand;
        this.useInteractedBlock = useInteractedBlock;
        this.successful = successful;
    }

    @Override
    public @Nullable Boolean getCancelled() {
        return cancel;
    }

    @Override
    public @Nullable Event.Result getUseItemInHand() {
        return useItemInHand;
    }

    @Override
    public @Nullable Event.Result getUseInteractedBlock() {
        return useInteractedBlock;
    }

    @Override
    public boolean successful() {
        return successful;
    }


    public static final class Builder {
        private Boolean cancel;
        private Event.Result useItemInHand;
        private Event.Result useInteractedBlock;
        private boolean successful;

        public Builder cancel(boolean cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder successful(boolean successful) {
            this.successful = successful;
            return this;
        }

        public Builder useItemInHand(Event.Result useItemInHand) {
            this.useItemInHand = useItemInHand;
            return this;
        }

        public Builder useInteractedBlock(Event.Result useInteractedBlock) {
            this.useInteractedBlock = useInteractedBlock;
            return this;
        }

        public ItemUseResultImpl build() {
            return new ItemUseResultImpl(cancel, useItemInHand, useInteractedBlock, successful);
        }
    }
}
