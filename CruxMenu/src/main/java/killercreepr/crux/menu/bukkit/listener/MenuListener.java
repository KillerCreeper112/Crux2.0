package killercreepr.crux.menu.bukkit.listener;

import killercreepr.crux.menu.bukkit.Menu;
import killercreepr.crux.menu.bukkit.api.events.menu.MenuCloseEvent;
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
        Menu menu = Menu.menu(p);
        if(menu==null) return;
        event.setCancelled(true);
        if(menu.getGeneralDragAction() != null) menu.getGeneralDragAction().drag(p, event);
    }

    @EventHandler
    private void inventoryClick(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player p)) return;
        Menu menu = Menu.menu(p);
        if(menu == null) return;
        event.setCancelled(true);
        if(event.getClickedInventory() != null){
            //Click in our own inventory.
            if(event.getRawSlot() >= p.getOpenInventory().getTopInventory().getSize()){
                if(menu.getGeneralInvClickAction() != null) menu.getGeneralInvClickAction().click(p, event);
            }else if(menu.getGeneralClickAction() != null){ //Click in the open menu.
                menu.getGeneralClickAction().click(p, event);
            }
        }

        Menu.MenuClick menuClick = menu.getAction(event.getRawSlot());
        if(menuClick != null) menuClick.click(p, event);
    }

    @EventHandler
    private void inventoryClose(InventoryCloseEvent event){
        if(!(event.getPlayer() instanceof Player p)) return;
        Menu menu = Menu.menu(p);
        if(menu == null) return;
        MenuCloseEvent closeEvent = menu.close(p);
        if(!closeEvent.isCancelled()) return;

        //open the menu back up silently.
        plugin.getServer().getScheduler().runTask(plugin, task -> p.openInventory(menu.getInventory()));
    }
}
