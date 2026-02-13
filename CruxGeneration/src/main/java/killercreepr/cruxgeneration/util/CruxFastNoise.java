package killercreepr.cruxgeneration.util;

public class CruxFastNoise implements CruxNoise{
    protected final FastNoiseLite fastNoiseLite;
    public CruxFastNoise(FastNoiseLite fastNoiseLite) {
        this.fastNoiseLite = fastNoiseLite;
    }

    public CruxFastNoise(int seed){
        this(new FastNoiseLite(seed));
    }
    public CruxFastNoise(long seed){
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
    public CruxNoise frequency(double value) {
        fastNoiseLite.SetFrequency((float) value);
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
    public CruxNoise fractalLacunarity(double lacunarity) {
        fastNoiseLite.SetFractalLacunarity((float) lacunarity);
        return this;
    }

    @Override
    public CruxNoise fractalGain(double gain) {
        fastNoiseLite.SetFractalGain((float) gain);
        return this;
    }

    @Override
    public CruxNoise fractalWeightedStrength(double weightedStrength) {
        fastNoiseLite.SetFractalWeightedStrength((float) weightedStrength);
        return this;
    }

    @Override
    public CruxNoise fractalPingPongStrength(double pingPongStrength) {
        fastNoiseLite.SetFractalPingPongStrength((float) pingPongStrength);
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
    public CruxNoise cellularJitter(double cellularJitter) {
        fastNoiseLite.SetCellularJitter((float) cellularJitter);
        return this;
    }

    @Override
    public CruxNoise domainWarpType(DomainWarpType domainWarpType) {
        fastNoiseLite.SetDomainWarpType(FastNoiseLite.DomainWarpType.values()[domainWarpType.ordinal()]);
        return this;
    }

    @Override
    public CruxNoise domainWarpAmp(double domainWarpAmp) {
        fastNoiseLite.SetDomainWarpAmp((float) domainWarpAmp);
        return this;
    }

    @Override
    public double noise(double x, double y) {
        return fastNoiseLite.GetNoise((float) x, (float) y);
    }

    @Override
    public double noise(double x, double y, double z) {
        return fastNoiseLite.GetNoise((float) x, (float) y, (float) z);
    }

    @Override
    public CruxNoise domainWarp(Vector2 coord) {
        fastNoiseLite.DomainWarp(new FastNoiseLite.Vector2((float) coord.x, (float) coord.y));
        return this;
    }

    @Override
    public CruxNoise domainWarp(Vector3 coord) {
        fastNoiseLite.DomainWarp(new FastNoiseLite.Vector3((float) coord.x, (float) coord.y, (float) coord.z));
        return this;
    }
}
