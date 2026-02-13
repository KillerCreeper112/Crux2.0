package killercreepr.cruxgeneration.util;

public interface CruxNoise {
    static CruxNoise fast(int seed){
        return new CruxFastNoise(seed);
    }
    static CruxNoise fast(long seed){
        return new CruxFastNoise(seed);
    }
    static CruxNoise fast(){
        return new CruxFastNoise();
    }

    CruxNoise seed(int seed);
    CruxNoise frequency(double value);
    CruxNoise noiseType(NoiseType noiseType);
    CruxNoise rotationType3D(RotationType3D rotationType3D);
    CruxNoise fractalType(FractalType fractalType);
    CruxNoise fractalOctaves(int octaves);
    CruxNoise fractalLacunarity(double lacunarity);
    CruxNoise fractalGain(double gain);
    CruxNoise fractalWeightedStrength(double weightedStrength);
    CruxNoise fractalPingPongStrength(double pingPongStrength);
    CruxNoise cellularDistanceFunction(CellularDistanceFunction cellularDistanceFunction);
    CruxNoise cellularReturnType(CellularReturnType cellularReturnType);
    CruxNoise cellularJitter(double cellularJitter);
    CruxNoise domainWarpType(DomainWarpType domainWarpType);
    CruxNoise domainWarpAmp(double domainWarpAmp);

    double noise(double x, double y);
    double noise(double x,double y, double z);

    CruxNoise domainWarp(Vector2 coord);
    CruxNoise domainWarp(Vector3 coord);

    class Vector2
    {
        public final /*FNLdouble*/ double x;
        public final /*FNLdouble*/ double y;
        public Vector2(/*FNLdouble*/ double x, /*FNLdouble*/ double y)
        {
            this.x = x;
            this.y = y;
        }
    }

    class Vector3
    {
        public final /*FNLdouble*/ double x;
        public final /*FNLdouble*/ double y;
        public final /*FNLdouble*/ double z;
        public Vector3(/*FNLdouble*/ double x, /*FNLdouble*/ double y, /*FNLdouble*/ double z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    enum NoiseType
    {
        OpenSimplex2,
        OpenSimplex2S,
        Cellular,
        Perlin,
        ValueCubic,
        Value
    }

    enum RotationType3D
    {
        None,
        ImproveXYPlanes,
        ImproveXZPlanes
    }

    enum FractalType
    {
        None,
        FBm,
        Ridged,
        PingPong,
        DomainWarpProgressive,
        DomainWarpIndependent
    }

    enum CellularDistanceFunction
    {
        Euclidean,
        EuclideanSq,
        Manhattan,
        Hybrid
    }

    enum CellularReturnType
    {
        CellValue,
        Distance,
        Distance2,
        Distance2Add,
        Distance2Sub,
        Distance2Mul,
        Distance2Div
    }

    enum DomainWarpType
    {
        OpenSimplex2,
        OpenSimplex2Reduced,
        BasicGrid
    }
}
