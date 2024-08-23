package killercreepr.cruxgeneration.util;

public class CruxFastNoise implements CruxNoise{
    protected final FastNoiseLite fastNoiseLite;
    public CruxFastNoise(FastNoiseLite fastNoiseLite) {
        this.fastNoiseLite = fastNoiseLite;
    }

    public CruxFastNoise(int seed){
        this(new FastNoiseLite(seed));
    }

    public CruxFastNoise(){
        this(new FastNoiseLite());
    }

    @Override
    public CruxNoise seed(int seed) {
        fastNoiseLite.SetSeed(seed);
        return this;
    }

    @Override
    public CruxNoise frequency(float value) {
        fastNoiseLite.SetFrequency(value);
        return this;
    }

    @Override
    public CruxNoise noiseType(NoiseType noiseType) {
        fastNoiseLite.SetNoiseType(FastNoiseLite.NoiseType.values()[noiseType.ordinal()]);
        return this;
    }

    @Override
    public CruxNoise rotationType3D(RotationType3D rotationType3D) {
        fastNoiseLite.SetRotationType3D(FastNoiseLite.RotationType3D.values()[rotationType3D.ordinal()]);
        return this;
    }

    @Override
    public CruxNoise fractalType(FractalType fractalType) {
        fastNoiseLite.SetFractalType(FastNoiseLite.FractalType.values()[fractalType.ordinal()]);
        return this;
    }

    @Override
    public CruxNoise fractalOctaves(int octaves) {
        fastNoiseLite.SetFractalOctaves(octaves);
        return this;
    }

    @Override
    public CruxNoise fractalLacunarity(float lacunarity) {
        fastNoiseLite.SetFractalLacunarity(lacunarity);
        return this;
    }

    @Override
    public CruxNoise fractalGain(float gain) {
        fastNoiseLite.SetFractalGain(gain);
        return this;
    }

    @Override
    public CruxNoise fractalWeightedStrength(float weightedStrength) {
        fastNoiseLite.SetFractalWeightedStrength(weightedStrength);
        return this;
    }

    @Override
    public CruxNoise fractalPingPongStrength(float pingPongStrength) {
        fastNoiseLite.SetFractalPingPongStrength(pingPongStrength);
        return this;
    }

    @Override
    public CruxNoise cellularDistanceFunction(CellularDistanceFunction cellularDistanceFunction) {
        fastNoiseLite.SetCellularDistanceFunction(FastNoiseLite.CellularDistanceFunction.values()[cellularDistanceFunction.ordinal()]);
        return this;
    }

    @Override
    public CruxNoise cellularReturnType(CellularReturnType cellularReturnType) {
        fastNoiseLite.SetCellularReturnType(FastNoiseLite.CellularReturnType.values()[cellularReturnType.ordinal()]);
        return this;
    }

    @Override
    public CruxNoise cellularJitter(float cellularJitter) {
        fastNoiseLite.SetCellularJitter(cellularJitter);
        return this;
    }

    @Override
    public CruxNoise domainWarpType(DomainWarpType domainWarpType) {
        fastNoiseLite.SetDomainWarpType(FastNoiseLite.DomainWarpType.values()[domainWarpType.ordinal()]);
        return this;
    }

    @Override
    public CruxNoise domainWarpAmp(float domainWarpAmp) {
        fastNoiseLite.SetDomainWarpAmp(domainWarpAmp);
        return this;
    }

    @Override
    public float noise(float x, float y) {
        return fastNoiseLite.GetNoise(x, y);
    }

    @Override
    public float noise(float x, float y, float z) {
        return fastNoiseLite.GetNoise(x, y, z);
    }

    @Override
    public CruxNoise domainWarp(Vector2 coord) {
        fastNoiseLite.DomainWarp(new FastNoiseLite.Vector2(coord.x, coord.y));
        return this;
    }

    @Override
    public CruxNoise domainWarp(Vector3 coord) {
        fastNoiseLite.DomainWarp(new FastNoiseLite.Vector3(coord.x, coord.y, coord.z));
        return this;
    }
}
