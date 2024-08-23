package killercreepr.cruxgeneration.util;

public interface CruxNoise {
    static CruxNoise fast(int seed){
        return new CruxFastNoise(seed);
    }
    static CruxNoise fast(){
        return new CruxFastNoise();
    }

    CruxNoise seed(int seed);
    CruxNoise frequency(float value);
    CruxNoise noiseType(NoiseType noiseType);
    CruxNoise rotationType3D(RotationType3D rotationType3D);
    CruxNoise fractalType(FractalType fractalType);
    CruxNoise fractalOctaves(int octaves);
    CruxNoise fractalLacunarity(float lacunarity);
    CruxNoise fractalGain(float gain);
    CruxNoise fractalWeightedStrength(float weightedStrength);
    CruxNoise fractalPingPongStrength(float pingPongStrength);
    CruxNoise cellularDistanceFunction(CellularDistanceFunction cellularDistanceFunction);
    CruxNoise cellularReturnType(CellularReturnType cellularReturnType);
    CruxNoise cellularJitter(float cellularJitter);
    CruxNoise domainWarpType(DomainWarpType domainWarpType);
    CruxNoise domainWarpAmp(float domainWarpAmp);

    float noise(float x, float y);
    float noise(float x,float y, float z);

    CruxNoise domainWarp(Vector2 coord);
    CruxNoise domainWarp(Vector3 coord);

    class Vector2
    {
        public final /*FNLfloat*/ float x;
        public final /*FNLfloat*/ float y;
        public Vector2(/*FNLfloat*/ float x, /*FNLfloat*/ float y)
        {
            this.x = x;
            this.y = y;
        }
    }

    class Vector3
    {
        public final /*FNLfloat*/ float x;
        public final /*FNLfloat*/ float y;
        public final /*FNLfloat*/ float z;
        public Vector3(/*FNLfloat*/ float x, /*FNLfloat*/ float y, /*FNLfloat*/ float z)
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
