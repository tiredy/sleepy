package me.tiredy.sleepy.blueprint.vector;

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
}
