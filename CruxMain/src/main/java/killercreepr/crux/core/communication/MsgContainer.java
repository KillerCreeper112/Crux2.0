package killercreepr.crux.core.communication;

import killercreepr.crux.api.communication.boss.CreateBossBar;
import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.communication.CreateTitle;
import killercreepr.crux.api.text.context.TextParserContext;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    protected final CreateBossBar bossBar;
    protected boolean broadcast;
    public MsgContainer(@Nullable List<String> chat, @Nullable String actionBar, @Nullable CreateTitle title, @Nullable CreateSound sound, @Nullable CreateBossBar bossBar) {
        this.chat = chat;
        this.actionBar = actionBar;
        this.title = title;
        this.sound = sound;
        this.bossBar = bossBar;
    }

    @Deprecated(forRemoval = true)
    public MsgContainer(@Nullable List<String> chat, @Nullable String actionBar, @Nullable CreateTitle title, @Nullable CreateSound sound) {
        this(chat, actionBar, title, sound, null);
    }

    @Deprecated(forRemoval = true)
    public MsgContainer(@Nullable List<String> chat, @Nullable String actionBar, @Nullable CreateTitle title) {
        this(chat, actionBar, title, null);
    }

    @Deprecated(forRemoval = true)
    public MsgContainer(@Nullable List<String> chat, @Nullable String actionBar) {
        this(chat, actionBar, null);
    }

    @Deprecated(forRemoval = true)
    public MsgContainer(@Nullable List<String> chat) {
        this(chat, null);
    }

    @Deprecated(forRemoval = true)
    public MsgContainer(@Nullable String chat, @Nullable String actionBar, @Nullable CreateTitle title, @Nullable CreateSound sound) {
        this(chat == null ? null : List.of(chat), actionBar, title, sound, null);
    }

    @Deprecated(forRemoval = true)
    public MsgContainer(@Nullable String chat, @Nullable String actionBar, @Nullable CreateTitle title) {
        this(chat, actionBar, title, null);
    }

    @Deprecated(forRemoval = true)
    public MsgContainer(@Nullable String chat, @Nullable String actionBar) {
        this(chat, actionBar, null);
    }

    @Deprecated(forRemoval = true)
    public MsgContainer(@Nullable String chat) {
        this(chat, null);
    }

    @Override
    public String toString() {
        return "MsgContainer{chat=" + chat + ", actionBar=" + actionBar + ", title=" + title + ", sound=" + sound + ", bossBar=" + bossBar + ", broadcast=" + broadcast + "}";
    }

    @Override
    public MsgContainer use(@NotNull Audience a, @NotNull TextParserContext ctx) {
        if(isBroadcast()) return broadcast(ctx);
        if(a instanceof Player p) return use(p, false, ctx);
        if(chat != null){
            for(String s : chat){
                a.sendMessage(deserialize(s, ctx));
            }
        }
        if(bossBar != null) bossBar.showBossBar(a, ctx);
        return this;
    }

    public MsgContainer use(@NotNull Player p, boolean broadcastCheck, @NotNull TextParserContext ctx){
        if(broadcastCheck && broadcast){
            return broadcast(ctx);
        }
        if(chat != null){
            for(String s : chat){
                p.sendMessage(deserialize(s, ctx));
            }
        }
        if(actionBar != null) p.sendActionBar(deserialize(actionBar, ctx));
        if(title != null) title.use(p, ctx);
        if(sound != null) sound.playFor(p);
        if(bossBar != null) bossBar.showBossBar(p, ctx);
        return this;
    }

    @Override
    public MsgContainer broadcast(@NotNull TextParserContext ctx) {
        for(Player p : Bukkit.getOnlinePlayers()){
            use(p, false, ctx);
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

    protected @NotNull Component deserialize(@Nullable String input, @NotNull TextParserContext ctx){
        if(input == null) return Component.empty();
        return ctx.deserialize(input);
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
        protected CreateBossBar bossBar;
        protected boolean broadcast;

        public @NotNull MsgContainer build(){
            return new MsgContainer(chat, actionBar, title, sound, bossBar).setBroadcast(broadcast);
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

        @Override
        public Communicator.Builder bossBar(CreateBossBar bossBar) {
            this.bossBar = bossBar;
            return this;
        }
    }
}
