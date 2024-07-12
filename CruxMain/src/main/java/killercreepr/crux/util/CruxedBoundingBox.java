package killercreepr.crux.util;

import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CruxedBoundingBox {
    protected @NotNull BoundingBox box;
    protected @Nullable Vector centerPoint;
    public CruxedBoundingBox(@NotNull BoundingBox box) {
        this.box = box;
    }

    public @NotNull BoundingBox box() {
        return box;
    }

    public CruxedBoundingBox box(@NotNull BoundingBox box) {
        this.box = box; return this;
    }

    public @Nullable Vector centerPoint() {
        return centerPoint;
    }

    public CruxedBoundingBox centerPoint(@Nullable Vector centerPoint) {
        this.centerPoint = centerPoint; return this;
    }

    public @NotNull BoundingBox rotateX(double angleDegrees){
        Objects.requireNonNull(centerPoint);
        return rotateX(angleDegrees, centerPoint.getX(), centerPoint.getY(), centerPoint.getZ());
    }

    public @NotNull BoundingBox rotateY(double angleDegrees){
        Objects.requireNonNull(centerPoint);
        return rotateY(angleDegrees, centerPoint.getX(), centerPoint.getY(), centerPoint.getZ());
    }

    public @NotNull BoundingBox rotateZ(double angleDegrees){
        Objects.requireNonNull(centerPoint);
        return rotateZ(angleDegrees, centerPoint.getX(), centerPoint.getY(), centerPoint.getZ());
    }

    /**
     * @return A new BoundingBox.
     */
    public @NotNull BoundingBox rotateX(double angleDegrees, double centerX, double centerY, double centerZ) {
        if(angleDegrees==0D) return box.clone();
        // Convert angle to radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Get the min and max corners of the bounding box
        double minX = box.getMinX() - centerX;
        double minY = box.getMinY() - centerY;
        double minZ = box.getMinZ() - centerZ;
        double maxX = box.getMaxX() - centerX;
        double maxY = box.getMaxY() - centerY;
        double maxZ = box.getMaxZ() - centerZ;

        // Rotate the corners around the X axis
        Vector minRotated = rotatePointX(minX, minY, minZ, angleRadians);
        Vector maxRotated = rotatePointX(maxX, maxY, maxZ, angleRadians);

        // Create a new bounding box with the new coordinates
        return new BoundingBox(
            minRotated.getX() + centerX, minRotated.getY() + centerY, minRotated.getZ() + centerZ,
            maxRotated.getX() + centerX, maxRotated.getY() + centerY, maxRotated.getZ() + centerZ
        );
    }

    public @NotNull BoundingBox rotateY(double angleDegrees, double centerX, double centerY, double centerZ) {
        if(angleDegrees==0D) return box.clone();
        // Convert angle to radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Get the min and max corners of the bounding box
        double minX = box.getMinX() - centerX;
        double minY = box.getMinY() - centerY;
        double minZ = box.getMinZ() - centerZ;
        double maxX = box.getMaxX() - centerX;
        double maxY = box.getMaxY() - centerY;
        double maxZ = box.getMaxZ() - centerZ;

        // Rotate the corners around the Y axis
        Vector minRotated = rotatePointY(minX, minY, minZ, angleRadians);
        Vector maxRotated = rotatePointY(maxX, maxY, maxZ, angleRadians);

        // Create a new bounding box with the new coordinates
        return new BoundingBox(
            minRotated.getX() + centerX, minRotated.getY() + centerY, minRotated.getZ() + centerZ,
            maxRotated.getX() + centerX, maxRotated.getY() + centerY, maxRotated.getZ() + centerZ
        );
    }

    public @NotNull BoundingBox rotateZ(double angleDegrees, double centerX, double centerY, double centerZ) {
        if(angleDegrees==0D) return box.clone();
        // Convert angle to radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Get the min and max corners of the bounding box
        double minX = box.getMinX() - centerX;
        double minY = box.getMinY() - centerY;
        double minZ = box.getMinZ() - centerZ;
        double maxX = box.getMaxX() - centerX;
        double maxY = box.getMaxY() - centerY;
        double maxZ = box.getMaxZ() - centerZ;

        // Rotate the corners around the Z axis
        Vector minRotated = rotatePointZ(minX, minY, minZ, angleRadians);
        Vector maxRotated = rotatePointZ(maxX, maxY, maxZ, angleRadians);

        // Create a new bounding box with the new coordinates
        return new BoundingBox(
            minRotated.getX() + centerX, minRotated.getY() + centerY, minRotated.getZ() + centerZ,
            maxRotated.getX() + centerX, maxRotated.getY() + centerY, maxRotated.getZ() + centerZ
        );
    }

    private static Vector rotatePointX(double x, double y, double z, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double newY = y * cos - z * sin;
        double newZ = y * sin + z * cos;

        return new Vector(x, newY, newZ);
    }

    private static Vector rotatePointY(double x, double y, double z, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double newX = x * cos + z * sin;
        double newZ = -x * sin + z * cos;

        return new Vector(newX, y, newZ);
    }

    private static Vector rotatePointZ(double x, double y, double z, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double newX = x * cos - y * sin;
        double newY = x * sin + y * cos;

        return new Vector(newX, newY, z);
    }
}
