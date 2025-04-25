package killercreepr.cruxpotions.core.persistence;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxpotions.api.potion.ActivePotion;
import killercreepr.cruxpotions.api.potion.CruxPotion;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class SimpleStoredPotion implements StoredPotion {
    protected final CruxPotion potion;
    protected final int duration;
    protected final int amplifier;
    protected final Creator creator;
    protected final Function<FileContext<?>,FileObject> fileSerializer;

    public SimpleStoredPotion(@NotNull CruxPotion potion, int duration, int amplifier, Creator creator, Function<FileContext<?>,FileObject> fileSerializer) {
        this.potion = potion;
        this.duration = duration;
        this.amplifier = amplifier;
        this.creator = creator == null ?
            (e, inflict, stored) -> potion.create(e, stored.getDuration(), stored.getAmplifier(), inflict) : creator;
        this.fileSerializer = fileSerializer;
    }

    @Override
    public @NotNull ActivePotion create(@NotNull Entity e){
        return create(e,null);
    }
    @Override
    public @NotNull ActivePotion create(@NotNull Entity e, @Nullable PotionInflictor inflictor){
        return creator.create(e, inflictor, this);
        //return potion.create(e, duration, amplifier, inflictor);
    }
    @Override
    public @NotNull CruxPotion getPotion() {
        return potion;
    }

    @Override
    public int getDuration() {
        return duration;
    }
    @Override
    public int getAmplifier() {
        return amplifier;
    }

    @Override
    public @NotNull StoredPotion withDuration(int duration) {
        return new SimpleStoredPotion(potion, duration, amplifier, creator, fileSerializer);
    }

    @Override
    public @NotNull StoredPotion withAmplifier(int amplifier) {
        return new SimpleStoredPotion(potion, duration, amplifier, creator, fileSerializer);
    }

    @Override
    public @NotNull StoredPotion withPotion(@NotNull CruxPotion potion) {
        return new SimpleStoredPotion(potion, duration, amplifier, creator, fileSerializer);
    }

    @Override
    public @Nullable FileObject serializeDataToFile(FileContext<?> ctx) {
        return fileSerializer == null ? null : fileSerializer.apply(ctx);
    }

    public static class Builder implements StoredPotion.Builder{
        protected CruxPotion potion;
        protected int duration;
        protected int amplifier;
        protected Creator creator;
        protected Function<FileContext<?>,FileObject> fileSerializer;
        @Override
        public StoredPotion.Builder potion(CruxPotion potion) {
            this.potion = potion;
            return this;
        }

        @Override
        public StoredPotion.Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        @Override
        public StoredPotion.Builder amplifier(int amplifier) {
            this.amplifier = amplifier;
            return this;
        }

        @Override
        public StoredPotion.Builder creator(Creator creator) {
            this.creator = creator;
            return this;
        }

        @Override
        public StoredPotion.Builder fileSerializer(Function<FileContext<?>,FileObject> serializer) {
            this.fileSerializer = serializer;
            return this;
        }

        @Override
        public StoredPotion build() {
            return new SimpleStoredPotion(potion, duration, amplifier, creator, fileSerializer);
        }
    }
}
