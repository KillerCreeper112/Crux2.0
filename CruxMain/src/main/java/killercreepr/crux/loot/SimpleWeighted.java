package killercreepr.crux.loot;

public class SimpleWeighted implements WeightedObject{
    protected final int weight;
    protected final float quality;
    public SimpleWeighted(int weight, float quality) {
        this.weight = weight;
        this.quality = quality;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public float getQuality() {
        return quality;
    }
}
