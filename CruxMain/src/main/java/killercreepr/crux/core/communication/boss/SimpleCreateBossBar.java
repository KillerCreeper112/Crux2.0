package killercreepr.crux.core.communication.boss;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.communication.boss.ActiveBossBar;
import killercreepr.crux.api.communication.boss.CreateBossBar;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.data.entity.PlayerBossBarHolder;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.core.util.CruxDuration;
import killercreepr.crux.core.util.CruxMath;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class SimpleCreateBossBar implements CreateBossBar {
    protected final @Nullable String key;
    protected final @Nullable String title;
    protected final @Nullable String progress;
    protected final @Nullable String color;
    protected final @Nullable String overlay;
    protected final @Nullable String duration;
    protected final @Nullable Collection<String> flags;

    public SimpleCreateBossBar(@Nullable String key, @Nullable String title,
                               @Nullable String progress,
                               @Nullable String color, @Nullable String overlay,
                               @Nullable String duration, @Nullable Collection<String> flags) {
        this.key = key;
        this.title = title;
        this.progress = progress;
        this.color = color;
        this.overlay = overlay;
        this.duration = duration;
        this.flags = flags;
    }

    @Override
    public String toString() {
        return "SimpleCreateBossBar{key=" + key + ", title=" + title + ", progress=" + progress + ", color=" + color + ", overlay=" + overlay + ", duration=" + duration + ", flags=" + flags + "}";
    }

    public BossBar.Color parseColor(@NotNull TextParserContext ctx){
        if(color == null) return BossBar.Color.WHITE;
        return BossBar.Color.NAMES.value(ctx.deserializeString(color));
    }

    public BossBar.Overlay parseOverlay(@NotNull TextParserContext ctx){
        if(overlay == null) return BossBar.Overlay.PROGRESS;
        return BossBar.Overlay.NAMES.value(ctx.deserializeString(overlay));
    }

    public Set<BossBar.Flag> parseFlags(@NotNull TextParserContext ctx){
        if(flags == null || flags.isEmpty()) return Set.of();
        Set<BossBar.Flag> parsed = new HashSet<>();
        for(String flag : flags){
            if(flag == null) continue;
            BossBar.Flag f = BossBar.Flag.NAMES.value(ctx.deserializeString(flag));
            if(f == null){
                Crux.log(Level.WARNING, "Unknown BossBar.Flag: " + flag + "(" + ctx.deserializeString(flag) + ")");
                continue;
            }
            parsed.add(f);
        }
        return parsed;
    }

    protected Component deserialize(@Nullable String text, @NotNull TextParserContext ctx){
        if(text == null) return null;
        return ctx.deserialize(text);
    }

    public @Nullable Duration parseDuration(@NotNull TextParserContext ctx){
        if(duration == null) return null;
        return CruxDuration.ofTicks((int) CruxMath.evaluate(ctx.deserializeString(duration)));
    }

    public @NotNull Key parseKey(@NotNull TextParserContext ctx){
        if(key == null) return Crux.key(UUID.randomUUID().toString());
        return Crux.key(ctx.deserializeString(key));
    }

    @Override
    public @Nullable ActiveBossBar showBossBar(@NotNull Audience audience, @NotNull TextParserContext ctx) {
        if(!(audience instanceof Player p)) return null;
        PlayerBossBarHolder holder = EntityMemory.getOrCreateDataHolder(p, PlayerBossBarHolder.class);
        if(holder == null) return null;
        Key key = parseKey(ctx);
        ActiveBossBar active = holder.getBossBar(key);
        if(active != null){
            active.resetTimer();
            active.update(ctx);
            return active;
        }
        ActiveBossBar bar = build(key, ctx);
        if(!holder.addBossBar(bar)) return null; //Only return the active boss bar if it was actually added.
        return bar;
    }

    @Override
    public @NotNull ActiveBossBar build(@NotNull TextParserContext ctx) {
        Key key = parseKey(ctx);
        return build(key, ctx);
    }

    public @NotNull ActiveBossBar build(@NotNull Key key, @NotNull TextParserContext ctx) {
        return new SimpleActiveBossBar(key, this, getBossBar(ctx), parseDuration(ctx));
    }

    public @NotNull BossBar updateBossBar(@NotNull BossBar bar, @NotNull TextParserContext ctx){
        bar.name(deserialize(title, ctx));
        bar.color(parseColor(ctx));
        bar.overlay(parseOverlay(ctx));
        if(progress != null){
            float x = (float) CruxMath.evaluate(ctx.deserializeString(progress));
            bar.progress(CruxMath.clamp(x, 0f, 1f));
        }
        return bar;
    }

    public @NotNull BossBar getBossBar(@NotNull TextParserContext ctx){
        return buildBossBar(ctx);
    }

    public @NotNull BossBar buildBossBar(@NotNull TextParserContext ctx) {
        BossBar bar = BossBar.bossBar(
            deserialize(title, ctx),
            0f, parseColor(ctx),
            parseOverlay(ctx), parseFlags(ctx)
        );
        if(progress != null){
            float x = (float) CruxMath.evaluate(ctx.deserializeString(progress));
            bar.progress(CruxMath.clamp(x, 0f, 1f));
        }
        return bar;
    }

    public @Nullable String getKey() {
        return key;
    }

    public @Nullable String getTitle() {
        return title;
    }

    public @Nullable String getProgress() {
        return progress;
    }

    public @Nullable String getColor() {
        return color;
    }

    public @Nullable String getOverlay() {
        return overlay;
    }

    public @Nullable String getDuration() {
        return duration;
    }

    public @Nullable Collection<String> getFlags() {
        return flags;
    }

    public static class Builder implements CreateBossBar.Builder{
        private @Nullable String key;
        private @Nullable String title;
        private @Nullable String progress;
        private @Nullable String color;
        private @Nullable String style;
        private @Nullable String duration;
        private @Nullable Collection<String> flags;

        @Override
        public CreateBossBar.Builder key(@Nullable String key) {
            this.key = key;
            return this;
        }

        @Override
        public CreateBossBar.Builder title(@Nullable String title) {
            this.title = title;
            return this;
        }

        @Override
        public CreateBossBar.Builder progress(@Nullable String progress) {
            this.progress = progress;
            return this;
        }

        @Override
        public CreateBossBar.Builder color(@Nullable String color) {
            this.color = color;
            return this;
        }

        @Override
        public CreateBossBar.Builder style(@Nullable String style) {
            this.style = style;
            return this;
        }

        @Override
        public CreateBossBar.Builder duration(@Nullable String duration) {
            this.duration = duration;
            return this;
        }

        @Override
        public CreateBossBar.Builder flags(@Nullable Collection<String> flags) {
            this.flags = flags;
            return this;
        }

        @Override
        public CreateBossBar build() {
            return new SimpleCreateBossBar(key, title, progress, color, style, duration, flags);
        }
    }
}
