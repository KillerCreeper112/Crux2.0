package killercreepr.cruxadvancements.core.util;

import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AdvancementMsg {
    public static AdvancementMsg msg(){
        return new AdvancementMsg();
    }

    public static AdvancementMsg msg(ItemStack icon, String title, String description){
        return new AdvancementMsg().icon(icon).title(title).description(description);
    }

    public static AdvancementMsg msg(ItemStack icon){
        return new AdvancementMsg().icon(icon);
    }

    protected ItemStack icon;
    protected String title = "";
    protected String description = "";
    protected AdvanceFrame frame = AdvanceFrame.TASK;

    public ItemStack getIcon() {
        return icon;
    }

    public AdvancementMsg icon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AdvancementMsg title(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AdvancementMsg description(String description) {
        this.description = description;
        return this;
    }

    public AdvanceFrame getFrame() {
        return frame;
    }

    public AdvancementMsg frame(AdvanceFrame frame) {
        this.frame = frame;
        return this;
    }

    private Advancement buildAdvancement(){
        return new Advancement(
            new NameKey("message", UUID.randomUUID().toString()),
            new AdvancementDisplay(icon , title, description, AdvancementDisplay.AdvancementFrame.valueOf(frame.toString()),
                AdvancementVisibility.ALWAYS)
        );
    }

    public AdvancementMsg displayToast(Player p){
        buildAdvancement().displayToast(p);
        return this;
    }

    public AdvancementMsg displayMessageToEverybody(Player p){
        buildAdvancement().displayMessageToEverybody(p);
        return this;
    }
}
