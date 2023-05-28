package me.tiredy.sleepy.blueprint.vec;

public class BlockVec2 {
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

    private int x, y;

    public BlockVec2(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
