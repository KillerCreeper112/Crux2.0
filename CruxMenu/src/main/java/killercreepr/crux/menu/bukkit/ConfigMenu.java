package killercreepr.crux.menu.bukkit;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.menu.bukkit.holder.MenuHolder;
import killercreepr.crux.tags.container.ObjectStringHookContainer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigMenu extends Menu{
    protected final MenuHolder holder;
    protected final DataExchange info;
    protected final ObjectStringHookContainer tags;

    protected final Map<Integer, MenuItem> items = new HashMap<>();
    public ConfigMenu(@NotNull MenuHolder holder, @NotNull DataExchange info){
        this(holder, info, null);
    }

    public ConfigMenu(@NotNull MenuHolder holder, @NotNull DataExchange info, @Nullable ObjectStringHookContainer tags){
        this.holder = holder;
        this.info = info;
        this.tags = new ObjectStringHookContainer(holder.getRegistry().getFormat().getTags());
        if(tags != null) this.tags.putAll(tags);
        reconstruct(Bukkit.createInventory(null, buildSize(), buildTitle()));
    }

    public @NotNull Component buildTitle(){
        if(holder.getTitle() == null) return Component.empty();
        return Crux.FORMAT.deserialize(info, null, holder.getTitle(), buildTags());
    }

    public int buildSize(){
        return (int) Double.parseDouble(holder.getRegistry().getFormat().setPlaceholders(
                holder.getSize(), buildTags()
        ));
    }

    public @NotNull ObjectStringHookContainer buildTags(){
        ObjectStringHookContainer tags = new ObjectStringHookContainer(holder.getRegistry().getFormat().getTags());
        return tags;
    }
    public @NotNull DataExchange info(){
        return info;
    }

    public ConfigMenu setItems(@NotNull MenuHolder holder){
        Player viewer = info.getObjectOrThrow("viewer", Player.class);
        MenuInfo menuInfo = new MenuInfo(this, info, tags);
        holder.getItems().items().forEach(menuItem -> {
            MenuItem i = menuItem.getDisplayItem(viewer, menuInfo);
            Optional<Integer> slot = i.getSlot();
            if(slot.isEmpty() || !i.canDisplay()) return;
            setItem(slot.get(), i, i.buildItem(viewer));
        });
        return this;
    }

    public ConfigMenu setItem(int slot, @Nullable MenuItem item, @Nullable ItemStack display){
        if(item == null){
            items.remove(slot);
            setItem(slot, null, (MenuClick) null);
            return this;
        }
        items.put(slot, item);
        setItem(slot, display, item::click);
        return this;
    }

    public @NotNull MenuHolder getHolder() {
        return holder;
    }

    public @NotNull ObjectStringHookContainer getTags() {
        return tags;
    }
}
