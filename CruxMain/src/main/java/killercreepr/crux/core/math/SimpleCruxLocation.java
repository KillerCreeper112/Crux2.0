package killercreepr.crux.core.math;

import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.util.CruxLoc;
import killercreepr.crux.core.util.CruxMath;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SimpleCruxLocation implements CruxLocation {
    protected final double x;
    protected final double y;
    protected final double z;
    protected final float yaw;
    protected final float pitch;

    public SimpleCruxLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SimpleCruxLocation blockPos = (SimpleCruxLocation) obj;
        return x == blockPos.x && y == blockPos.y && z == blockPos.z && blockPos.yaw == yaw && blockPos.pitch == pitch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return "SimpleCruxLocation{x=" + x + ", y=" + y + ", z=" + z + ", yaw=" + yaw + ", pitch=" + pitch + "}";
    }

    @Override
    public @NotNull CruxLocation subtract(double x, double y, double z) {
        return new SimpleCruxLocation(this.x - x, this.y - y, this.z - z, yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation add(double x, double y, double z) {
        return new SimpleCruxLocation(this.x + x, this.y + y, this.z + z, yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation rotateAroundX(@NotNull CruxPosition center, double angle) {
        double radians = Math.toRadians(angle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Translate point to origin
        double translatedY = this.y - center.y();
        double translatedZ = this.z - center.z();

        // Apply rotation matrix
        double rotatedY = Math.round(translatedY * cosTheta - translatedZ * sinTheta);
        double rotatedZ = Math.round(translatedY * sinTheta + translatedZ * cosTheta);

        // Translate point back
        return new SimpleCruxLocation(this.x, rotatedY + center.blockY(), rotatedZ + center.blockZ(), yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation rotateAroundY(@NotNull CruxPosition center, double angle) {
        double radians = Math.toRadians(angle*-1);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Translate point to origin
        double translatedX = this.x - center.x();
        double translatedZ = this.z - center.z();

        // Apply rotation matrix
        double rotatedX = Math.round(translatedX * cosTheta - translatedZ * sinTheta);
        double rotatedZ = Math.round(translatedX * sinTheta + translatedZ * cosTheta);

        // Translate point back
        return new SimpleCruxLocation(rotatedX + center.blockX(), this.y, rotatedZ + center.blockZ(), yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation rotateAroundZ(@NotNull CruxPosition center, double angle) {
        double radians = Math.toRadians(angle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Translate point to origin
        double translatedX = this.x - center.x();
        double translatedY = this.y - center.y();

        // Apply rotation matrix
        double rotatedX = Math.round(translatedX * cosTheta - translatedY * sinTheta);
        double rotatedY = Math.round(translatedX * sinTheta + translatedY * cosTheta);

        // Translate point back
        return new SimpleCruxLocation(rotatedX + center.blockX(), rotatedY + center.blockY(), this.z, yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation addRelative(double forward, double up, double right) {
        double x = x();
        double y = y();
        double z = z();
        Vector locDirection = getDirectionVector(yaw, pitch);
        //Location locDirection = loc.clone().setDirection(dir);
        //+ FORWARD - BACKWARD
        if(forward != 0D){
            Vector v = locDirection.clone().multiply(forward);
            x += v.getX();
            y += v.getY();
            z += v.getZ();
        }
        //- LEFT + RIGHT
        if(right != 0D){

            Vector sideDirection = locDirection.clone().setY(0).normalize();  // Remove vertical component
            sideDirection = new Vector(-sideDirection.getZ(), 0, sideDirection.getX()); // Rotate 90 degrees
            sideDirection.multiply(right);
            x += sideDirection.getX();
            y += sideDirection.getY();
            z += sideDirection.getZ();
        }
        //+ UP - DOWN
        if(up != 0D){
            SimpleCruxLocation l = (SimpleCruxLocation) this.withPitch(pitch() - 90);
            Vector v = l.getDirectionVector(l.yaw(), l.pitch()).multiply(up);
            x += v.getX();
            y += v.getY();
            z += v.getZ();
        }
        return new SimpleCruxLocation(x, y, z, yaw, pitch);
    }

    private float calculateYaw(CruxPosition vector){
        final double _2PI = 2 * Math.PI;
        final double x = vector.x();
        final double z = vector.z();

        if (x == 0 && z == 0) {
            return yaw();
        }

        double theta = Math.atan2(-x, z);
        return (float) Math.toDegrees((theta + _2PI) % _2PI);
    }

    private float calculatePitch(CruxPosition vector){
        final double x = vector.x();
        final double z = vector.z();

        if (x == 0 && z == 0) {
            return vector.y() > 0 ? -90 : 90;
        }

        double x2 = NumberConversions.square(x);
        double z2 = NumberConversions.square(z);
        double xz = Math.sqrt(x2 + z2);
        return (float) Math.toDegrees(Math.atan(-vector.y() / xz));
    }

    private CruxLocation setDirection(double x, double y, double z){
        final double _2PI = 2 * Math.PI;

        float pitch;
        float yaw;

        if (x == 0 && z == 0) {
            pitch = y > 0 ? -90 : 90;
            return new SimpleCruxLocation(this.x, this.y, this.z, yaw(), pitch);
        }

        double theta = Math.atan2(-x, z);
        yaw = (float) Math.toDegrees((theta + _2PI) % _2PI);

        double x2 = NumberConversions.square(x);
        double z2 = NumberConversions.square(z);
        double xz = Math.sqrt(x2 + z2);
        pitch = (float) Math.toDegrees(Math.atan(-y / xz));

        return new SimpleCruxLocation(this.x, this.y, this.z, yaw, pitch);
    }

    private CruxLocation setDirection(CruxPosition vector){
        final double _2PI = 2 * Math.PI;
        final double x = vector.x();
        final double z = vector.z();

        float pitch;
        float yaw;

        if (x == 0 && z == 0) {
            pitch = vector.y() > 0 ? -90 : 90;
            return new SimpleCruxLocation(this.x, this.y, this.z, yaw(), pitch);
        }

        double theta = Math.atan2(-x, z);
        yaw = (float) Math.toDegrees((theta + _2PI) % _2PI);

        double x2 = NumberConversions.square(x);
        double z2 = NumberConversions.square(z);
        double xz = Math.sqrt(x2 + z2);
        pitch = (float) Math.toDegrees(Math.atan(-vector.y() / xz));

        return new SimpleCruxLocation(this.x, this.y, this.z, yaw, pitch);
    }

    private CruxPosition getDirection(float yaw, float pitch){

        double y = -Math.sin(Math.toRadians(pitch));

        double xz = Math.cos(Math.toRadians(pitch));

        double x = -xz * Math.sin(Math.toRadians(yaw));
        double z = xz * Math.cos(Math.toRadians(yaw));

        return CruxPosition.precise(x, y, z);
    }

    private Vector getDirectionVector(float yaw, float pitch){

        double y = -Math.sin(Math.toRadians(pitch));

        double xz = Math.cos(Math.toRadians(pitch));

        double x = -xz * Math.sin(Math.toRadians(yaw));
        double z = xz * Math.cos(Math.toRadians(yaw));

        return new Vector(x, y, z);
    }

    private CruxPosition getDirection(){
        return getDirection(yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation subtractRelative(double forward, double up, double right) {
        return addRelative(forward * -1, up * -1, right * -1);
    }

    @Override
    public @NotNull Location toLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation withYaw(float yaw) {
        return new SimpleCruxLocation(x, y, z, yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation withPitch(float pitch) {
        return new SimpleCruxLocation(x, y, z, yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation withX(double x) {
        return new SimpleCruxLocation(x, y, z, yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation withY(double y) {
        return new SimpleCruxLocation(x, y, z, yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation withZ(double z) {
        return new SimpleCruxLocation(x, y, z, yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation lookAt(@NotNull CruxLocation target) {
        //todo MAKE PROPER DIRECTION FOR CRUXLOCATIONS AHHHHHHHHHHHHH
        CruxPosition v = target.subtract(this);
        return setDirection(v.x(), v.y(), v.z());
    }

    @Override
    public @NotNull CruxLocation shiftToward(@NotNull CruxLocation target, double amount) {
        CruxLocation loc = lookAt(target).addRelative(amount, 0D, 0D);
        return CruxLocation.location(loc.x(), loc.y(), loc.z(), yaw, pitch);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public int blockX() {
        return (int) x;
    }

    @Override
    public int blockY() {
        return (int) y;
    }

    @Override
    public int blockZ() {
        return (int) z;
    }

    @Override
    public float yaw() {
        return yaw;
    }

    @Override
    public float pitch() {
        return pitch;
    }
}
