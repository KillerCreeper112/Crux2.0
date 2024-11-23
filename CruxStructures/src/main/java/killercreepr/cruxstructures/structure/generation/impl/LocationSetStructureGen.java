package killercreepr.cruxstructures.structure.generation.impl;

import killercreepr.crux.api.math.Pos2D;
import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.result.GenerateResult;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public abstract class LocationSetStructureGen implements StructureGenerator {
    protected final @Nullable NumberProvider chunkRangeX;
    protected final @Nullable NumberProvider chunkRangeZ;
    protected final @Nullable NumberProvider minDistanceApart;

    protected Collection<Pos2D> setChunks = null;
    public LocationSetStructureGen(@Nullable NumberProvider chunkRangeX, @Nullable NumberProvider chunkRangeZ, @Nullable NumberProvider minDistanceApart) {
        this.chunkRangeX = chunkRangeX;
        this.chunkRangeZ = chunkRangeZ;
        this.minDistanceApart = minDistanceApart;
    }

    public Collection<Pos2D> generateSetChunks(@NotNull World world, int minDistance){
        InputContext ctx = InputContext.simple(TagContainer.string().hook(world));
        int amount = getGenerateStructureAmount(ctx);
        if (amount < 1) return null;

        int rangeX = chunkRangeX == null ? (int) world.getWorldBorder().getSize() :
            chunkRangeX.sample(ctx).intValue();
        int rangeZ = chunkRangeZ == null ? (int) world.getWorldBorder().getSize() :
            chunkRangeZ.sample(ctx).intValue();

        Collection<Pos2D> list = new HashSet<>();

        int minDistanceSquared = minDistance * minDistance;
        while (amount > 0) {
            Pos2D pos;
            boolean isValidPosition;

            if(minDistanceSquared > 0){
                do {
                    pos = Pos2D.at(CruxMath.random(-rangeX, rangeX), CruxMath.random(-rangeZ, rangeZ));
                    isValidPosition = true;

                    // Check the distance to all existing positions
                    for (Pos2D existingPos : list) {
                        int squaredDistance = (pos.x() - existingPos.x()) * (pos.x() - existingPos.x()) +
                            (pos.z() - existingPos.z()) * (pos.z() - existingPos.z());
                        if (squaredDistance < minDistanceSquared) {
                            isValidPosition = false;
                            break;
                        }
                    }
                } while (!isValidPosition); // Repeat until a valid position is found
            }else pos = Pos2D.at(CruxMath.random(-rangeX, rangeX), CruxMath.random(-rangeZ, rangeZ));

            list.add(pos);
            amount--;
        }
        return list;
    }

    @Override
    public @NotNull GenerateResult generate(@NotNull Structure structure, @NotNull Chunk at) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull GenerateResult generate(@NotNull Chunk at) {
        if(setChunks == null) setChunks = generateSetChunks(at.getWorld(), minDistanceApart == null ? 0 : minDistanceApart.value().intValue());
        if(setChunks == null) return GenerateResult.empty();
        if(!setChunks.contains(Pos2D.at(at.getX(), at.getZ()))) return GenerateResult.empty();
        List<StructureGenerator> populated = populateLoot(at);
        if(populated.isEmpty()) return GenerateResult.empty();
        StructureGenerator gen = populated.getFirst();
        return gen.generate(at);
    }

    public abstract List<StructureGenerator> populateLoot(@NotNull Chunk at);
    public abstract int getGenerateStructureAmount(@NotNull InputContext ctx);

    @Override
    public @NotNull GenerateResult generate(@NotNull Location at) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull GenerateResult generate(@NotNull Structure structure, @NotNull Location at) {
        throw new UnsupportedOperationException();
    }

    public @Nullable NumberProvider getChunkRangeX() {
        return chunkRangeX;
    }

    public @Nullable NumberProvider getChunkRangeZ() {
        return chunkRangeZ;
    }

    public @Nullable NumberProvider getMinDistanceApart() {
        return minDistanceApart;
    }

    public Collection<Pos2D> getSetChunks() {
        return setChunks;
    }
}
