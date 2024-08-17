package killercreepr.cruxitems.item.interaction.impl;

import killercreepr.crux.util.CruxItem;
import killercreepr.cruxitems.item.interaction.ItemUseContext;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUseContextImpl implements ItemUseContext {
    public static Builder builder(){
        return new Builder();
    }

    protected final @NotNull Player player;
    protected final @NotNull CruxItem item;
    protected final @NotNull Action action;
    protected final @NotNull EquipmentSlot hand;
    protected final @Nullable Block blockClicked;
    protected final @Nullable BlockFace blockFace;
    protected final @Nullable Location interactionPoint;

    public ItemUseContextImpl(@NotNull Player player, @NotNull CruxItem item, @NotNull Action action, @NotNull EquipmentSlot hand, @Nullable Block blockClicked, @Nullable BlockFace blockFace, @Nullable Location interactionPoint) {
        this.player = player;
        this.item = item;
        this.action = action;
        this.hand = hand;
        this.blockClicked = blockClicked;
        this.blockFace = blockFace;
        this.interactionPoint = interactionPoint;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public @Nullable Block getBlockClicked() {
        return blockClicked;
    }

    @Override
    public @NotNull CruxItem getItem() {
        return item;
    }

    @Override
    public @NotNull Action getAction() {
        return action;
    }

    @Override
    public @Nullable BlockFace getBlockFace() {
        return blockFace;
    }

    @Override
    public @NotNull EquipmentSlot getHand() {
        return hand;
    }

    @Override
    public @Nullable Location getInteractionPoint() {
        return interactionPoint;
    }


    public static final class Builder {
        private @NotNull Player player;
        private @NotNull CruxItem item;
        private @NotNull Action action;
        private @NotNull EquipmentSlot hand;
        private @Nullable Block blockClicked;
        private @Nullable BlockFace blockFace;
        private @Nullable Location interactionPoint;

        public Builder player(Player player) {
            this.player = player;
            return this;
        }

        public Builder item(CruxItem item) {
            this.item = item;
            return this;
        }

        public Builder action(Action action) {
            this.action = action;
            return this;
        }

        public Builder hand(EquipmentSlot hand) {
            this.hand = hand;
            return this;
        }

        public Builder blockClicked(Block blockClicked) {
            this.blockClicked = blockClicked;
            return this;
        }

        public Builder blockFace(BlockFace blockFace) {
            this.blockFace = blockFace;
            return this;
        }

        public Builder clickedPosition(Location interactionPoint) {
            this.interactionPoint = interactionPoint;
            return this;
        }

        public ItemUseContextImpl build() {
            return new ItemUseContextImpl(player, item, action, hand, blockClicked, blockFace, interactionPoint);
        }
    }
}
