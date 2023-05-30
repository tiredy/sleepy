package me.tiredy.sleepy.blueprint.vector;

import org.bukkit.Location;
import org.bukkit.util.Vector;

@SuppressWarnings("unused")
public class BlockVec2 {
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int y) {
        this.z = y;
    }

    private int x, z;

    public BlockVec2(int x, int y) {
        this.x = x;
        this.z = y;
    }

    public static BlockVec2 from(Location location) {
        return new BlockVec2((int) location.getX(), (int) location.getZ());
    }

    public static BlockVec2 from(Vector vector) {
        return new BlockVec2((int) vector.getX(), (int) vector.getZ());
    }

    public BlockVec2 subtract(BlockVec2 v) {
        return new BlockVec2(x - v.x, z - v.z);
    }

    public BlockVec2 add(BlockVec2 v) {
        return new BlockVec2(x + v.x, z + v.z);
    }
}
