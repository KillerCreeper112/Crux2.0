package killercreepr.crux.data.communication;

import killercreepr.crux.Crux;
import killercreepr.crux.tags.container.MergedTagContainer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MsgContainer implements Communicator {
    protected final List<String> chat;
    protected final String actionBar;
    protected final CreateTitle title;
    protected final CreateSound sound;
    protected boolean broadcast;
    public MsgContainer(@Nullable List<String> chat, @Nullable String actionBar, @Nullable CreateTitle title, @Nullable CreateSound sound) {
        this.chat = chat;
        this.actionBar = actionBar;
        this.title = title;
        this.sound = sound;
    }

    @Override
    public String toString() {
        return "MsgContainer{chat=" + chat + ", actionBar=" + actionBar + ", title=" + title + ", sound=" + sound + ", broadcast=" + broadcast + "}";
    }

    public MsgContainer(@Nullable String chat, @Nullable String actionBar, @Nullable CreateTitle title, @Nullable CreateSound sound){
        this(chat == null ? null : List.of(chat), actionBar, title, sound);
    }
    public MsgContainer(@Nullable List<String> chat, @Nullable String actionBar, @Nullable CreateTitle title) {
        this(chat, actionBar, title, null);
    }
    public MsgContainer(@Nullable String chat, @Nullable String actionBar, @Nullable CreateTitle title) {
        this(chat == null ? null : List.of(chat), actionBar, title, null);
    }

    public MsgContainer(@Nullable List<String> chat, @Nullable String actionBar) {
        this(chat, actionBar, null);
    }
    public MsgContainer(@Nullable String chat, @Nullable String actionBar) {
        this(chat == null ? null : List.of(chat), actionBar, null);
    }
    public MsgContainer(@Nullable List<String> chat) {
        this(chat, null);
    }
    public MsgContainer(@Nullable String chat) {
        this(chat == null ? null : List.of(chat), null);
    }

    @Override
    public MsgContainer use(@NotNull Audience a, @Nullable OfflinePlayer placeholders, @Nullable MergedTagContainer tags){
        if(isBroadcast()) return broadcast(tags);
        if(a instanceof Player p) return use(p, placeholders, tags);
        if(chat != null){
            for(String s : chat){
                a.sendMessage(deserialize(placeholders, s, tags));
            }
        }
        return this;
    }

    public MsgContainer use(@NotNull Player p, @Nullable OfflinePlayer placeholders, @Nullable MergedTagContainer tags){
        return use(p, placeholders, true, tags);
    }

    public MsgContainer use(@NotNull Player p, @Nullable OfflinePlayer placeholders, boolean broadcastCheck, @Nullable MergedTagContainer tags){
        if(broadcastCheck && broadcast){
            return broadcast(tags);
        }
        if(chat != null){
            for(String s : chat){
                p.sendMessage(deserialize(placeholders, s, tags));
            }
        }
        if(actionBar != null) p.sendActionBar(deserialize(placeholders, actionBar, tags));
        if(title != null) title.use(p, placeholders, tags);
        if(sound != null) sound.playFor(p);
        return this;
    }

    @Override
    public MsgContainer broadcast(@Nullable MergedTagContainer tags){
        for(Player p : Bukkit.getOnlinePlayers()){
            use(p, null, false, tags);
        }
        return this;
    }

    @Override
    public Communicator playAt(@NotNull Location at) {
        if(sound != null) sound.playAt(at);
        return this;
    }

    @Override
    public Communicator playAt(@NotNull Entity at) {
        if(sound != null) sound.playAt(at);
        return this;
    }

    protected @NotNull Component deserialize(@Nullable OfflinePlayer viewer, @Nullable String input, @Nullable MergedTagContainer tags){
        if(input == null) return Component.empty();
        return Crux.format().deserialize(input, MergedTagContainer.mergeHook(tags, viewer));
    }

    public @Nullable List<String> getChat() {
        return chat;
    }

    public @Nullable  String getActionBar() {
        return actionBar;
    }

    public @Nullable CreateTitle getTitle() {
        return title;
    }

    public @Nullable CreateSound getSound() {
        return sound;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public MsgContainer setBroadcast(boolean broadcast) {
        this.broadcast = broadcast; return this;
    }

    public boolean isEmpty(){
        return chat == null && actionBar == null && title == null && sound == null;
    }

    public static class Builder implements Communicator.Builder{
        protected List<String> chat;
        protected String actionBar;
        protected CreateTitle title;
        protected CreateSound sound;
        protected boolean broadcast;

        public @NotNull MsgContainer build(){
            return new MsgContainer(chat, actionBar, title, sound).setBroadcast(broadcast);
        }

        public Builder chat(String chat) {
            this.chat = chat == null ? null : List.of(chat); return this;
        }

        public Builder broadcast(boolean broadcast) {
            this.broadcast = broadcast;
            return this;
        }

        public Builder chat(List<String> chat) {
            this.chat = chat; return this;
        }

        public Builder actionBar(String actionBar) {
            this.actionBar = actionBar; return this;
        }

        public Builder title(CreateTitle title) {
            this.title = title; return this;
        }

        public Builder sound(CreateSound sound) {
            this.sound = sound; return this;
        }
    }
}
