package killercreepr.crux.core.util;

import killercreepr.crux.api.math.CruxPosition;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxedBoundingBox {
    public static @NotNull CruxedBoundingBox wrap(@NotNull BoundingBox box){
        return new CruxedBoundingBox(box);
    }
    public static @NotNull BoundingBox boundingBox(@NotNull Location center, double x, double y, double z){
        return BoundingBox.of(center, x/2, y/2, z/2);
    }

    public static @NotNull BoundingBox boundingBox(@NotNull Location center, double xz, double y){
        return boundingBox(center, xz, y, xz);
    }

    public static @NotNull BoundingBox boundingBox(@NotNull Location center, double radius){
        return boundingBox(center, radius, radius);
    }

    public static @NotNull BoundingBox boundingBox(@NotNull Vector center, double x, double y, double z){
        return BoundingBox.of(center, x/2, y/2, z/2);
    }

    public static @NotNull BoundingBox boundingBox(@NotNull Vector center, double xz, double y){
        return boundingBox(center, xz, y, xz);
    }

    public static @NotNull BoundingBox boundingBox(@NotNull Vector center, double radius){
        return boundingBox(center, radius, radius);
    }

    public static @NotNull BoundingBox boundingBox(@NotNull CruxPosition center, double x, double y, double z){
        return BoundingBox.of(center.toVector(), x/2, y/2, z/2);
    }

    public static @NotNull BoundingBox boundingBox(@NotNull CruxPosition center, double xz, double y){
        return boundingBox(center, xz, y, xz);
    }

    public static @NotNull BoundingBox boundingBox(@NotNull CruxPosition center, double radius){
        return boundingBox(center, radius, radius);
    }

    protected @NotNull BoundingBox box;
    protected @Nullable CruxPosition centerPoint;
    public CruxedBoundingBox(@NotNull BoundingBox box) {
        this.box = box;
    }

    public @NotNull BoundingBox box() {
        return box;
    }

    public CruxedBoundingBox box(@NotNull BoundingBox box) {
        this.box = box; return this;
    }

    public @Nullable CruxPosition centerPoint() {
        return centerPoint;
    }

    public CruxedBoundingBox centerPoint(@Nullable CruxPosition centerPoint) {
        this.centerPoint = centerPoint; return this;
    }

    public @NotNull CruxPosition centerPointOrBoxCenter(){
        return this.centerPoint == null ? CruxPosition.location(box.getCenter()) : this.centerPoint;
    }

    public @NotNull CruxedBoundingBox rotateX(double angleDegrees){
        CruxPosition centerPoint = centerPointOrBoxCenter();
        return rotateX(angleDegrees, centerPoint.x(), centerPoint.y(), centerPoint.z());
    }
    public @NotNull CruxedBoundingBox rotateY(double angleDegrees){
        CruxPosition centerPoint = centerPointOrBoxCenter();
        return rotateY(angleDegrees, centerPoint.x(), centerPoint.y(), centerPoint.z());
    }
    public @NotNull CruxedBoundingBox rotateZ(double angleDegrees){
        CruxPosition centerPoint = centerPointOrBoxCenter();
        return rotateZ(angleDegrees, centerPoint.x(), centerPoint.y(), centerPoint.z());
    }

    /**
     * @return A new BoundingBox.
     */
    public @NotNull CruxedBoundingBox rotateX(double angleDegrees, double centerX, double centerY, double centerZ) {
        if(angleDegrees==0D) return this;
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
        box(
            new BoundingBox(
                minRotated.getX() + centerX, minRotated.getY() + centerY, minRotated.getZ() + centerZ,
                maxRotated.getX() + centerX, maxRotated.getY() + centerY, maxRotated.getZ() + centerZ
            )
        );
        return this;
    }
    public @NotNull CruxedBoundingBox rotateY(double angleDegrees, double centerX, double centerY, double centerZ) {
        if(angleDegrees==0D) return this;
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
        box(
            new BoundingBox(
                minRotated.getX() + centerX, minRotated.getY() + centerY, minRotated.getZ() + centerZ,
                maxRotated.getX() + centerX, maxRotated.getY() + centerY, maxRotated.getZ() + centerZ
            )
        );
        return this;
    }
    public @NotNull CruxedBoundingBox rotateZ(double angleDegrees, double centerX, double centerY, double centerZ) {
        if(angleDegrees==0D) return this;
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
        box(
            new BoundingBox(
                minRotated.getX() + centerX, minRotated.getY() + centerY, minRotated.getZ() + centerZ,
                maxRotated.getX() + centerX, maxRotated.getY() + centerY, maxRotated.getZ() + centerZ
            )
        );
        return this;
    }

    public @NotNull CruxedBoundingBox moveTo(@NotNull CruxPosition from, @NotNull CruxPosition to){
        return moveTo(
            from.x(), from.y(), from.z(),
            to.x(), to.y(), to.z()
        );
    }

    public @NotNull CruxedBoundingBox moveTo(@NotNull Vector position){
        return moveTo(position.getX(), position.getY(), position.getZ());
    }
    public @NotNull CruxedBoundingBox moveTo(@NotNull Location position){
        return moveTo(position.getX(), position.getY(), position.getZ());
    }
    public @NotNull CruxedBoundingBox moveTo(@NotNull CruxPosition position){
        return moveTo(position.x(), position.y(), position.z());
    }

    public @NotNull CruxedBoundingBox moveTo(double x, double y, double z){
        CruxPosition origin = centerPointOrBoxCenter();
        return moveTo(
            origin.x(), origin.y(), origin.z(),
            x, y, z
        );
    }

    public @NotNull CruxedBoundingBox moveTo(
        double fromX, double fromY, double fromZ,
        double toX, double toY, double toZ
    ){
        double offsetX = toX - fromX;
        double offsetY = toY - fromY;
        double offsetZ = toZ - fromZ;

        BoundingBox box = this.box.clone();

        box = box.shift(offsetX, offsetY, offsetZ);
        box(
            new BoundingBox(
                box.getMinX(), box.getMinY(), box.getMinZ(),
                box.getMaxX()+1, box.getMaxY()+1, box.getMaxZ()+1
            )
        );
        return this;
    }

    private static double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    @ApiStatus.Experimental
    public CruxedBoundingBox expand(double rotationAngle, double scaleFactor) {
        // Calculate the original dimensions
        double width = box.getWidthX();
        double height = box.getHeight();
        double depth = box.getWidthZ();

        // Calculate the new width and height based on the rotation
        double radians = Math.toRadians(rotationAngle);
        double newWidth = width * scaleFactor * Math.abs(Math.cos(radians));
        double newHeight = height * scaleFactor * Math.abs(Math.sin(radians));
        double newDepth = depth * scaleFactor; // Assuming depth is not affected by rotation

        // Calculate new min/max coordinates based on the center of the original bounding box
        double centerX = box.getMinX() + width / 2;
        double centerY = box.getMinY() + height / 2;
        double centerZ = box.getMinZ() + depth / 2;

        // Create the new BoundingBox
        box(
            new BoundingBox(
                centerX - newWidth / 2, centerY - newHeight / 2, centerZ - newDepth / 2,
                centerX + newWidth / 2, centerY + newHeight / 2, centerZ + newDepth / 2
            )
        );
        return this;
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
