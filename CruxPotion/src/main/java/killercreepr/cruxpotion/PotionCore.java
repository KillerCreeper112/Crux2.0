package killercreepr.cruxpotion;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.util.CruxEntity;
import killercreepr.crux.util.CruxItem;
import killercreepr.crux.util.CruxTag;
import killercreepr.cruxpotion.data.PotionHolder;
import killercreepr.cruxpotion.persistence.CustomPotionHolder;
import killercreepr.cruxpotion.persistence.PotionPersistTags;
import killercreepr.cruxpotion.potions.CustomPotion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class PotionCore {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final int startIndex = 0;
        String subAction = args.length > startIndex ? args[startIndex].toLowerCase() : "";
        Collection<Entity> list = new HashSet<>();
        String eValue = args.length > startIndex+1 ? args[startIndex+1].toLowerCase() : "";
        switch (eValue){
            case "@all" ->{
                if(sender instanceof Player p) list.addAll(p.getWorld().getEntities());
                else{
                    for(World w : Bukkit.getWorlds()){
                        list.addAll(w.getEntities());
                    }
                }
            }
            case "@players" -> list.addAll(Bukkit.getOnlinePlayers());
            case "@animals" ->{
                if(sender instanceof Player p){
                    list.addAll(p.getWorld().getEntitiesByClass(Animals.class));
                } else{
                    for(World w : Bukkit.getWorlds()){
                        list.addAll(w.getEntitiesByClass(Animals.class));
                    }
                }
            }
            case "@monsters" ->{
                if(sender instanceof Player p){
                    list.addAll(p.getWorld().getEntitiesByClass(Monster.class));
                } else{
                    for(World w : Bukkit.getWorlds()){
                        list.addAll(w.getEntitiesByClass(Monster.class));
                    }
                }
            }
            case "@selected" ->{
                if(sender instanceof Player p){
                    Entity e = p.getTargetEntity(10);
                    if(e != null) list.add(e);
                }
            }
            default ->{
                if(eValue.startsWith("@type=")){
                    EntityType t;
                    try{
                        t = EntityType.valueOf(eValue.replaceFirst("@type=", "").toUpperCase());
                    }catch (IllegalArgumentException ex){
                        sender.sendPlainMessage("Entity type not found.");
                        return true;
                    }
                    if(sender instanceof Player p){
                        for(Entity e : p.getWorld().getEntities()){
                            if(e.getType() == t) list.add(e);
                        }
                    } else{
                        for(World w : Bukkit.getWorlds()){
                            for(Entity e : w.getEntities()){
                                if(e.getType() == t) list.add(e);
                            }
                        }
                    }
                }else{
                    Entity e = CruxEntity.getEntity(eValue);
                    if(e != null) list.add(e);
                }
            }
        }
        if(list.isEmpty()){
            sender.sendPlainMessage("No entities found.");
            return true;
        }
        String pValue = args.length > startIndex+2 ? args[startIndex+2].toLowerCase() : "";
        CustomPotion potion = null;//todo CustomPotion.REGISTRY.get(key(pValue.isBlank() ? "a" : pValue));
        boolean item = args[args.length-1].equalsIgnoreCase("-item");
        switch (subAction){
            case "give" ->{
                if(potion == null){
                    sender.sendPlainMessage("Potion not found.");
                    return true;
                }
                int duration;
                if(args.length > startIndex+3){
                    try{
                        duration = (int) (Float.parseFloat(args[startIndex+3])*20D);
                    }catch (IllegalArgumentException e){
                        if(args[startIndex+3].equalsIgnoreCase("infinite")) duration = -1;
                        else{
                            sender.sendPlainMessage("Invalid duration input.");
                            return true;
                        }
                    }
                }else duration = 600;

                int amplifier = 0;
                if(args.length > startIndex+4){
                    try{
                        amplifier = Integer.parseInt(args[startIndex+4]);
                    }catch (IllegalArgumentException e){
                        sender.sendPlainMessage("Invalid amplifier input.");
                        return true;
                    }
                }
                int added = 0;
                for(Entity e : list){
                    if(item){
                        if(!(e instanceof LivingEntity lE) || lE.getEquipment() == null) continue;
                        ItemStack main = lE.getEquipment().getItemInMainHand();
                        if(CruxItem.isEmpty(main)) continue;
                        Collection<CustomPotionHolder> holders = PotionPersistTags.STORED_POTIONS.get(main, new HashSet<>());
                        holders.add(new CustomPotionHolder(potion.create(e, duration, amplifier)));
                        PotionPersistTags.STORED_POTIONS.set(main, holders);
                        lE.getEquipment().setItemInMainHand(main, true);
                        added++;
                        continue;
                    }
                    if(!canApplyPotion(e)) continue;
                    EntityMemory memory = EntityMemory.getOrCreate(e);
                    if(!((memory.getHolder(PotionHolder.KEY)) instanceof  PotionHolder data)) continue;
                    if(data.addPotion(potion.create(e, duration, amplifier))) added++;
                }
                sender.sendPlainMessage("Added potion from " + added + " entities with duration of " + duration + " ticks and amplifier of " + amplifier + ".");
            }
            case "clear" ->{
                int removed = 0;
                if(potion == null){
                    for(Entity d : list){
                        if(item){
                            if(!(d instanceof LivingEntity lE) || lE.getEquipment() == null) continue;
                            ItemStack main = lE.getEquipment().getItemInMainHand();
                            if(CruxItem.isEmpty(main)) continue;
                            lE.getEquipment().setItemInMainHand(PotionPersistTags.STORED_POTIONS.remove(main), true);
                            removed++;
                            continue;
                        }
                        EntityMemory memory = EntityMemory.get(d);
                        if(memory != null && memory.getHolder(PotionHolder.KEY) instanceof PotionHolder data){
                            data.clearPotions();
                            removed++;
                        }
                    }
                    sender.sendPlainMessage("Cleared potions from " + removed + " entities.");
                }else{
                    for(Entity d : list){
                        if(item){
                            if(!(d instanceof LivingEntity lE) || lE.getEquipment() == null) continue;
                            ItemStack main = lE.getEquipment().getItemInMainHand();
                            if(CruxItem.isEmpty(main)) continue;
                            Collection<CustomPotionHolder> holders = PotionPersistTags.STORED_POTIONS.get(main, new HashSet<>());
                            holders.removeIf(x -> x.getPotion().getKey().equals(potion.getKey()));
                            if(holders.isEmpty()) PotionPersistTags.STORED_POTIONS.remove(main);
                            else PotionPersistTags.STORED_POTIONS.set(main, holders);
                            lE.getEquipment().setItemInMainHand(main, true);
                            continue;
                        }
                        EntityMemory memory = EntityMemory.get(d);
                        if(memory != null && memory.getHolder(PotionHolder.KEY) instanceof PotionHolder data){
                            if(data.removePotion(potion)) removed++;
                        }
                    }
                    sender.sendPlainMessage("Removed potion " + potion.getKey() + " from " + removed + " entities.");
                }
            }
            case "reload" ->{
                sender.sendPlainMessage("Reloading configs...");
                //todo reloadConfigs();
            }
            default -> sender.sendPlainMessage("Sub action not found.");
        }
        return true;
    }

    public static boolean canApplyPotion(@NotNull Entity e){
        if(CruxTag.has(e, "disable_potions") || !(e instanceof LivingEntity)) return false;
        return switch (e.getType()){
            case ARMOR_STAND, UNKNOWN-> false;
            default -> true;
        };
    }

    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        switch (args.length){
            case 1 ->{
                list.add("reload");
                list.add("give");
                list.add("clear");
            }
            case 2 ->{
                if(args[1].startsWith("@type=")){
                    for(EntityType t : EntityType.values()){
                        if(!t.isAlive()) continue;
                        switch (t){
                            case ARMOR_STAND, UNKNOWN ->{ continue; }
                        }
                        list.add("@type=" + t.toString().toLowerCase());
                    }
                    return list;
                }
                for(Player p : Bukkit.getOnlinePlayers()){
                    list.add(p.getName());
                }
                list.add("@all");
                list.add("@mobs");
                list.add("@animals");
                list.add("@monsters");
                list.add("@players");
                list.add("@selected");
                list.add("@type=");
            }
            case 3 ->{
                if(args[0].equalsIgnoreCase("clear")) list.add("-item");
                //todo CustomPotion.REGISTRY.forEach(k -> list.add(getKeyAsStringWithoutPlugin(k.getKey())));
            }
            case 4 ->{
                if(args[0].equalsIgnoreCase("clear")){
                    list.add("-item");
                    return list;
                }
                list.add("infinite");
            }
            case 5 -> list.add("<amplifier>");
        }
        return list;
    }
}
