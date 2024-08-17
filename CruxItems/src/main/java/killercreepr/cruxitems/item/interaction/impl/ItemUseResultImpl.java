package killercreepr.cruxitems.item.interaction.impl;

import killercreepr.cruxitems.item.interaction.ItemUseResult;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ItemUseResultImpl implements ItemUseResult {
    public static Builder builder(){
        return new Builder();
    }

    protected final Boolean cancel;
    protected final @Nullable Event.Result useItemInHand;
    protected final @Nullable Event.Result useInteractedBlock;

    public ItemUseResultImpl(@Nullable Boolean cancel, @Nullable Event.Result useItemInHand, @Nullable Event.Result useInteractedBlock) {
        this.cancel = cancel;
        this.useItemInHand = useItemInHand;
        this.useInteractedBlock = useInteractedBlock;
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


    public static final class Builder {
        private Boolean cancel;
        private Event.Result useItemInHand;
        private Event.Result useInteractedBlock;

        public Builder cancel(boolean cancel) {
            this.cancel = cancel;
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
            return new ItemUseResultImpl(cancel, useItemInHand, useInteractedBlock);
        }
    }
}
