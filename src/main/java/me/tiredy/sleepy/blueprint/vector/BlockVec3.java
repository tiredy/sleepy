package me.tiredy.sleepy.blueprint.vector;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

@SuppressWarnings("unused")
public class BlockVec3 {
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    private int x, y, z;

    public BlockVec3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static BlockVec3 from(Location location) {
        return new BlockVec3((int) location.getX(), (int) location.getY(), (int) location.getZ());
    }

    public static BlockVec3 from(Vector vector) {
        return new BlockVec3((int) vector.getX(), (int) vector.getY(), (int) vector.getZ());
    }


    public BlockVec3 subtract(BlockVec3 v) {
        return new BlockVec3(x - v.x, y - v.y, z - v.z);
    }

    public BlockVec3 add(BlockVec3 v) {
        return new BlockVec3(x + v.x, y + v.y, z + v.z);
    }

    public BlockVec3 multiply(BlockVec3 v) {
        return new BlockVec3(x * v.x, y * v.y, z * v.z);
    }

    public BlockVec3 copy() {
        return new BlockVec3(x, y, z);
    }

    public Location toLocation(World world) {
        return new Location(world, x, y, z);
    }
}
