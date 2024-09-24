package killercreepr.cruxmenus.core.listener;

import killercreepr.cruxmenus.api.event.MenuCloseEvent;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.core.registries.Menus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.Plugin;

public class MenuListener implements Listener {
    protected final Plugin plugin;

    public MenuListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void inventoryDrag(InventoryDragEvent event){
        if(!(event.getWhoClicked() instanceof Player p)) return;
        Menu menu = Menus.getOpened(p);
        if(menu==null) return;
        event.setCancelled(true);
        menu.onDrag(event);
    }

    @EventHandler
    private void inventoryClick(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player p)) return;
        Menu menu = Menus.getOpened(p);
        if(menu == null) return;
        event.setCancelled(true);
        if(event.getClickedInventory() != null){
            //Click in our own inventory.
            if(event.getRawSlot() >= p.getOpenInventory().getTopInventory().getSize()){
                menu.onInvClick(event);
            }else{ //Click in the open menu.
                menu.onMenuClick(event);
            }
        }
    }

    @EventHandler
    private void inventoryClose(InventoryCloseEvent event){
        if(!(event.getPlayer() instanceof Player p)) return;
        Menu menu = Menus.getOpened(p);
        if(menu == null) return;
        if(menu.isRefreshing()) return;
        MenuCloseEvent closeEvent = menu.close(p);
        if(!closeEvent.isCancelled()) return;

        //open the menu back up silently.
        plugin.getServer().getScheduler().runTask(plugin, task -> p.openInventory(menu.getInventory()));
    }
}
