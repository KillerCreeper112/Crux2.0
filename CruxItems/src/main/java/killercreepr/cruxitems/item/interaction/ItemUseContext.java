package killercreepr.cruxitems.item.interaction;

import killercreepr.crux.util.CruxItem;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemUseContext {
    @NotNull
    Player getPlayer();
    @Nullable
    Block getBlockClicked();
    @NotNull
    CruxItem getItem();
    @NotNull
    Action getAction();
    @Nullable
    BlockFace getBlockFace();
    @NotNull
    EquipmentSlot getHand();
    @Nullable
    Location getInteractionPoint();
}
