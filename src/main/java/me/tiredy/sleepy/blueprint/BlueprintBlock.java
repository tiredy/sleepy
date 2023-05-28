package me.tiredy.sleepy.blueprint;

import me.tiredy.sleepy.blueprint.vector.BlockVec3;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

@SuppressWarnings("unused")
public class BlueprintBlock {
    private Material material;
    private BlockData data;
    private BlockVec3 relativePos;

    public BlueprintBlock(Material material, BlockData data, BlockVec3 relativePos) {
        this.material = material;
        this.data = data;
        this.relativePos = relativePos;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public BlockData getData() {
        return data;
    }

    @Override
    public String toString() {
        return "BlueprintBlock{" + "material=" + material + ", data=" + data + ", relativePos=" + relativePos + '}';
    }

    public void setData(BlockData data) {
        this.data = data;
    }

    public BlockVec3 getPos() {
        return relativePos;
    }

    public void setPos(BlockVec3 relativePos) {
        this.relativePos = relativePos;
    }
}
