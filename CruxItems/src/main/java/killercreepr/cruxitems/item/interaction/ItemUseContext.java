package killercreepr.cruxitems.item.interaction;

import killercreepr.crux.util.CruxItem;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//todo make a custom action instead of using Bukkit's Action enum
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
    @Nullable
    Entity getEntityClicked();
}
