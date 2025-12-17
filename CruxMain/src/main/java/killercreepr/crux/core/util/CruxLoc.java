package killercreepr.crux.core.util;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CruxLoc {
    public static List<Location> getAdjLocations(Location center, boolean includeCenter, boolean includeUpDown){
        List<Location> list = new ArrayList<>();
        if(includeCenter) list.add(center);
        list.add(center.clone().add(1, 0, 0));
        list.add(center.clone().add(-1, 0, 0));
        list.add(center.clone().add(0, 0, 1));
        list.add(center.clone().add(1, 0, -1));

        if(includeUpDown){
            list.add(center.clone().add(0, 1, 0));
            list.add(center.clone().add(0, -1, 0));
        }
        return list;
    }

    public List<Location> getHollowCube(Location pos1, Location pos2, double distance) {
        List<Location> result = new ArrayList<>();
        World world = pos1.getWorld();
        double minX = Math.min(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        for (double x = minX; x <= maxX; x+=distance) {
            for (double y = minY; y <= maxY; y+=distance) {
                for (double z = minZ; z <= maxZ; z+=distance) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }
                }
            }
        }

        return result;
    }

    public static Set<Block> getNearbyBlocks(Block center, int radius){
        Set<Block> list = new HashSet<>();
        for (int x = radius; x >= -radius; x--) {
            for (int y = radius; y >= -radius; y--) {
                for (int z = radius; z >= -radius; z--) {
                    list.add(center.getRelative(x, y, z));
                }
            }
        }
        return list;
    }

    public static Axis convertBlockFaceToAxis(BlockFace face) {
        return switch (face) {
            case NORTH, SOUTH -> Axis.Z;
            case UP, DOWN -> Axis.Y;
            default -> Axis.X;
        };
    }

    public static @NotNull List<Block> getAdjBlocks(@NotNull Block center, boolean includeCenter, boolean includeUpDown){
        List<Block> list = new ArrayList<>();
        if(includeCenter) list.add(center);
        list.add(center.getRelative(BlockFace.WEST));
        list.add(center.getRelative(BlockFace.EAST));
        list.add(center.getRelative(BlockFace.SOUTH));
        list.add(center.getRelative(BlockFace.NORTH));

        if(includeUpDown){
            list.add(center.getRelative(BlockFace.UP));
            list.add(center.getRelative(BlockFace.DOWN));
        }
        return list;
    }

    public static boolean inRegion(@NotNull Location checkLoc, @NotNull Location loc1, @NotNull Location loc2){
        double x1 = Math.min(loc1.getX(), loc2.getX());
        double y1 = Math.min(loc1.getY(), loc2.getY());
        double z1 = Math.min(loc1.getZ(), loc2.getZ());

        double x2 = Math.max(loc1.getX(), loc2.getX());
        double y2 = Math.max(loc1.getY(), loc2.getY());
        double z2 = Math.max(loc1.getZ(), loc2.getZ());

        double x = checkLoc.getX();
        double y = checkLoc.getY();
        double z = checkLoc.getZ();

        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }

    public static boolean inRegion(@NotNull Location checkLoc,
                                   @NotNull Vector pos1,
                                   @NotNull Vector pos2) {

        return inRegion(
            checkLoc,
            pos1.getX(), pos1.getY(), pos1.getZ(),
            pos2.getX(), pos2.getY(), pos2.getZ()
        );
    }

    public static boolean inRegion(@NotNull Location checkLoc,
                                   double posX1, double posY1, double posZ1,
                                   double posX2, double posY2, double posZ2) {

        double x1 = Math.min(posX1, posX2);
        double y1 = Math.min(posY1, posY2);
        double z1 = Math.min(posZ1, posZ2);

        double x2 = Math.max(posX1, posX2);
        double y2 = Math.max(posY1, posY2);
        double z2 = Math.max(posZ1, posZ2);

        double x = checkLoc.getX();
        double y = checkLoc.getY();
        double z = checkLoc.getZ();

        return x >= x1 && x <= x2
            && y >= y1 && y <= y2
            && z >= z1 && z <= z2;
    }


    private static final double EPSILON = Math.ulp(1.0d) * 2d;
    private static boolean isSignificant(double value) {
        return Math.abs(value) >= EPSILON;
    }
    @Contract(pure = true)
    public static @NotNull Location relative(@NotNull Location l, double forward, double up, double right) {
        Location newLocation = l.clone();
        Vector direction = null;
        if (isSignificant(forward)) {
            direction = l.getDirection();
            newLocation.add(direction.clone().multiply(forward));
        }
        boolean hasUp = isSignificant(up);
        if (hasUp && direction == null) direction = l.getDirection();
        if (isSignificant(right) || hasUp) {
            Vector rightDirection;
            if (direction != null && isSignificant(Math.abs(direction.getY()) - 1)) {
                rightDirection = direction.clone();
                double factor = Math.sqrt(1 - Math.pow(rightDirection.getY(), 2)); // a shortcut that lets us not normalize which is slow
                double nx = -rightDirection.getZ() / factor;
                double nz = rightDirection.getX() / factor;
                rightDirection.setX(nx);
                rightDirection.setY(0d);
                rightDirection.setZ(nz);
            } else {
                float yaw = l.getYaw() + 90f;
                double yawRad = yaw * (Math.PI / 180d);
                double z = Math.cos(yawRad);
                double x = -Math.sin(yawRad);
                rightDirection = new Vector(x, 0d, z);
            }
            newLocation.add(rightDirection.clone().multiply(right));
            if (hasUp) {
                Vector upDirection = rightDirection.crossProduct(direction);
                newLocation.add(upDirection.clone().multiply(up));
            }
        }
        return l;
    }

    @Contract(pure = true)
    public static @NotNull Location shift(@NotNull Location loc, @NotNull Vector dir, double forward, double up, double right){
        Location newLocation = loc.clone();

        Vector forwardVec = dir.clone().normalize();
        Vector upWorld = new Vector(0, 1, 0);

        // Right = forward × upWorld
        Vector rightVec = forwardVec.clone().crossProduct(upWorld).normalize();

        // True up = right × forward (orthogonalize)
        Vector upVec = rightVec.clone().crossProduct(forwardVec).normalize();

        // Apply offsets
        newLocation.add(forwardVec.multiply(forward));
        newLocation.add(rightVec.multiply(right));
        newLocation.add(upVec.multiply(up));

        return newLocation;

        /*Location newLocation = loc.clone();
        Location locDirection = loc.clone().setDirection(dir);
        //+ FORWARD - BACKWARD
        if(forward != 0D) newLocation.add(locDirection.getDirection().multiply(forward));
        //- LEFT + RIGHT
        if(right != 0D){

            Vector sideDirection = locDirection.getDirection().setY(0).normalize();  // Remove vertical component
            sideDirection = new Vector(-sideDirection.getZ(), 0, sideDirection.getX()); // Rotate 90 degrees

            newLocation.add(sideDirection.multiply(right));

            *//*locDirection.setYaw(90 - loc.getYaw());
            locDirection.setPitch(0);
            newLocation.add(locDirection.getDirection().multiply(right));*//*
        }
        //+ UP - DOWN
        if(up != 0D){
            locDirection.setYaw(loc.getYaw());
            locDirection.setPitch(loc.getPitch() - 90);
            newLocation.add(locDirection.getDirection().multiply(up));
        }
        return newLocation;*/
    }

    @Contract(pure = true)
    public static @NotNull Location shift(@NotNull Location loc, double forward, double up, double right){
        return shift(loc, loc.getDirection(), forward, up, right);
    }
    @Contract(pure = true)
    public static @NotNull Location shiftToward(@NotNull Location loc, @NotNull Location loc1, double amount){
        return shiftToward(loc, loc1, amount, false);
    }
    @Contract(pure = true)
    public static @NotNull Location shiftToward(@NotNull Location loc, @NotNull Location loc1, double amount, boolean keepOldRotation){
        return shiftToward(loc, loc1, amount, 0D, 0D, keepOldRotation);
    }
    @Contract(pure = true)
    public static @NotNull Location shiftToward(@NotNull Location loc, @NotNull Location loc1,
                                                double forward, double up, double right){
        return shiftToward(loc, loc1, forward, right, up, false);
    }
    @Contract(pure = true)
    public static @NotNull Location shiftToward(@NotNull Location loc, @NotNull Location toward,
                                                double forward, double up, double right,
                                                boolean keepOldRotation){
        // Calculate direction vector from loc to toward
        Vector direction = toward.toVector().subtract(loc.toVector()).normalize();

        // Calculate the right vector (perpendicular to the direction vector)
        Vector rightVector = direction.clone().crossProduct(new Vector(0, 1, 0)).normalize();

        // Calculate the up vector
        Vector upVector = rightVector.clone().crossProduct(direction).normalize();

        // Calculate the new location by shifting
        Vector shiftVector = direction.multiply(forward)
            .add(upVector.multiply(up))
            .add(rightVector.multiply(right));
        Location newLocation = loc.clone().add(shiftVector);

        // Set the rotation
        if (keepOldRotation) {
            newLocation.setYaw(loc.getYaw());
            newLocation.setPitch(loc.getPitch());
        } else {
            float newYaw = (float) Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));
            float newPitch = (float) Math.toDegrees(Math.asin(direction.getY()));
            newLocation.setYaw(newYaw);
            newLocation.setPitch(newPitch);
        }

        return newLocation;
    }

    public static @NotNull List<Location> getCircle(@NotNull Location loc, double r){
        List<Location> locs = new ArrayList<>();
        double t = 0;
        while (t < Math.PI * 8) {
            t = t + Math.PI / 16;
            double x = r * Math.cos(t);
            double z = r * Math.sin(t);
            Vector v = new Vector(x, 0, z);
            v.rotateAroundX(loc.getPitch());
            v.rotateAroundY(loc.getYaw());
            locs.add(loc.clone().add(v));
        }
        return locs;
    }

    public static @NotNull List<Location> getCircle(@NotNull Location loc, double r, double spacing) {
        List<Location> locs = new ArrayList<>();
        double t = 0;

        // Calculate the increment based on the spacing
        double increment = Math.PI * 2 / (r / spacing);

        while (t < Math.PI * 2) {
            t += increment;
            double x = r * Math.cos(t);
            double z = r * Math.sin(t);
            Vector v = new Vector(x, 0, z);
            v.rotateAroundX(Math.toRadians(loc.getPitch()));
            v.rotateAroundY(Math.toRadians(-loc.getYaw()));
            locs.add(loc.clone().add(v));
        }

        return locs;
    }

    /**
     * Pitch and yaw must be 0, 0 to get a cone pointing towards.
     * Otherwise, it generates some interesting results.
     */
    public static @NotNull List<Location> getInterestingCone(@NotNull Location center, double radius, int numPoints, double downwardPitch) {
        double angleIncrement = 2 * Math.PI / numPoints; // Angle increment between points

        // Get the center's yaw and pitch
        float centerYaw = center.getYaw();
        float centerPitch = center.getPitch();

        List<Location> list = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            double angle = i * angleIncrement;

            // Calculate x and z using the angle
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            double y = center.getY(); // Assuming we want all points at the same y level

            // Create the location
            Location loc = new Location(center.getWorld(), x, y, z);

            // Calculate direction vector relative to the center's orientation
            Vector direction = new Vector(center.getX() - x, center.getY() - y, center.getZ() - z).normalize();

            // Apply slight downward pitch relative to the center's orientation
            Vector directionRelative = direction.clone();

            // Convert downward pitch to radians
            double pitchRad = Math.toRadians(downwardPitch);
            directionRelative.setY(directionRelative.getY() - Math.sin(pitchRad));
            directionRelative.normalize();

            // Calculate yaw and pitch adjustments
            double yawAdjustment = centerYaw + Math.toDegrees(Math.atan2(directionRelative.getZ(), directionRelative.getX())) - 90;
            double pitchAdjustment = centerPitch - Math.toDegrees(Math.asin(directionRelative.getY()));

            // Apply adjustments to the location's yaw and pitch
            loc.setYaw((float) yawAdjustment);
            loc.setPitch((float) pitchAdjustment);

            list.add(loc);
        }
        return list;
    }

    public static @NotNull List<Location> getCircleRelative(@NotNull Location loc, double r, double spacing) {
        List<Location> locs = new ArrayList<>();

        // Calculate the circumference of the circle
        double circumference = 2 * Math.PI * r;

        // Calculate the number of points needed based on the spacing
        int numPoints = (int) Math.ceil(circumference / spacing);

        // Calculate the angle increment
        double angleIncrement = 2 * Math.PI / numPoints;

        for (int i = 0; i < numPoints; i++) {
            double t = i * angleIncrement;
            double x = r * Math.cos(t);
            double z = r * Math.sin(t);
            Vector v = new Vector(x, 0, z);

            // Rotate the vector around the X and Y axes based on pitch and yaw
            v.rotateAroundX(Math.toRadians(loc.getPitch()));
            v.rotateAroundY(Math.toRadians(-loc.getYaw()));

            locs.add(loc.clone().add(v));
        }

        return locs;
    }

    // Helper method to rotate a vector around the X axis
    private static Vector rotateAroundX(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return new Vector(v.getX(), y, z);
    }

    // Helper method to rotate a vector around the Y axis
    private static Vector rotateAroundY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getZ() * cos - v.getX() * sin;
        return new Vector(x, v.getY(), z);
    }

    public static @NotNull List<Location> getCube(@NotNull Location center, float r, boolean hollow, boolean wire) {
        List<Location> locs = new ArrayList<>();

        Location pos1 = shift(center.clone(), r, r, r);
        Location pos2 = shift(center.clone(), -r, -r, -r);

        Vector max = Vector.getMaximum(pos1.toVector(), pos2.toVector());
        Vector min = Vector.getMinimum(pos1.toVector(), pos2.toVector());

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    if (wire) {
                        if ((x == min.getBlockX() || x == max.getBlockX()) && (y == min.getBlockY() ||
                                y == max.getBlockY()) || (x == min.getBlockX() || x == max.getBlockX()) && (z == min.getBlockZ() ||
                                z == max.getBlockZ()) || (y == min.getBlockY() || y == max.getBlockY()) && (z == min.getBlockZ() || z == max.getBlockZ()))
                            locs.add(center.getWorld().getBlockAt(x, y, z).getLocation());
                        continue;
                    }
                    if (hollow) {
                        if (x == min.getBlockX() || x == max.getBlockX() || y == min.getBlockY() || y == max.getBlockY() || z == min.getBlockZ() || z == max.getBlockZ())
                            locs.add(center.getWorld().getBlockAt(x, y, z).getLocation());
                        continue;
                    }

                    locs.add(center.getWorld().getBlockAt(x, y, z).getLocation());
                }
            }
        }

        return locs;
    }

    public static @NotNull List<Location> getRegion(@NotNull Location pos1, @NotNull Location pos2) {
        List<Location> locs = new ArrayList<>();

        Vector max = Vector.getMaximum(pos1.toVector(), pos2.toVector());
        Vector min = Vector.getMinimum(pos1.toVector(), pos2.toVector());

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    locs.add(pos1.getWorld().getBlockAt(x, y, z).getLocation());
                }
            }
        }

        return locs;
    }

    public static @NotNull Location lookAt(@NotNull Location loc, @NotNull Location target){
        return loc.setDirection(target.toVector().subtract(loc.toVector()));
    }

    public @NotNull List<Location> createCylinder(@NotNull Location center, float radius, int height){
        List<Location> list = new ArrayList<>();
        for(int h = 0; h < height; h++){
            for(float x = -radius; x < radius; x++){
                for(float z = -radius; z < radius; z++){
                    Location loc = center.clone().add(x, h, z);
                    list.add(loc);
                }
            }
        }
        return list;
    }

    public static @NotNull List<Location> cluster(@NotNull Location center, int amount, double spacing){
        return cluster(center, amount, spacing, spacing, spacing);
    }

    public static @NotNull List<Location> cluster(@NotNull Location center, int amount, double x, double y, double z){
        List<Location> locs = new ArrayList<>();
        for(; amount > 0; amount--){
            locs.add(center.clone().add(CruxMath.random(-x, x), CruxMath.random(-y, y), CruxMath.random(-z, z)));
        }
        return locs;
    }

    public static Location getLocAroundCircle(Location center, double radius, double angleInRadian, boolean lookToward) {
        double x = center.getX() + radius * Math.cos(angleInRadian);
        double z = center.getZ() + radius * Math.sin(angleInRadian);
        double y = center.getY();

        Location loc = new Location(center.getWorld(), x, y, z);
        if(lookToward){
            Vector difference = center.toVector().clone().subtract(loc.toVector());
            loc.setDirection(difference);
        }
        return loc;
    }

    public static boolean inRegion(Location location, BoundingBox boundingBox) {
        // Check if the location is within the bounding box
        return location.getX() >= boundingBox.getMinX() && location.getX() <= boundingBox.getMaxX() &&
            location.getY() >= boundingBox.getMinY() && location.getY() <= boundingBox.getMaxY() &&
            location.getZ() >= boundingBox.getMinZ() && location.getZ() <= boundingBox.getMaxZ();
    }
}
