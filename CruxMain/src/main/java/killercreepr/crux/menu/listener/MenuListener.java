package killercreepr.crux.menu.listener;

import killerceepr.crux.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MenuListener implements Listener {
    @EventHandler
    private void inventoryDrag(InventoryDragEvent event){
        Player p = (Player) event.getWhoClicked();
        Menu.getMenu(p).ifPresent(menu ->{
            event.setCancelled(true);
            if(menu.getGeneralDragAction() != null) menu.getGeneralDragAction().drag(p, event);
        });
    }

    @EventHandler
    private void inventoryClick(InventoryClickEvent event){
        Player p = (Player) event.getWhoClicked();
        Menu menu = Menu.getMenu(p).orElse(null);
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
        Player p = (Player) event.getPlayer();
        Menu.getMenu(p).ifPresent(menu -> menu.close(p));
    }
}
