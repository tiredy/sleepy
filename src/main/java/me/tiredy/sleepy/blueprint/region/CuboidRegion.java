package me.tiredy.sleepy.blueprint.region;

import me.tiredy.sleepy.blueprint.vector.BlockVec3;

@SuppressWarnings("unused")
public class CuboidRegion {
    private BlockVec3 min;
    private BlockVec3 max;

    public CuboidRegion(BlockVec3 min, BlockVec3 max) {
        this.min = min;
        this.max = max;
    }

    public BlockVec3 getMin() {
        return min;
    }

    public void setMin(BlockVec3 min) {
        this.min = min;
    }

    public BlockVec3 getMax() {
        return max;
    }

    public void setMax(BlockVec3 max) {
        this.max = max;
    }
}
