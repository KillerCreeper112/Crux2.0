package killercreepr.crux.data;

public record Pos2D(int x, int z) {
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + x;
        result = 31 * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pos2D other = (Pos2D) obj;
        return x == other.x && z == other.z;
    }
}
