package me.tiredy.sleepy.blueprint;

import me.tiredy.sleepy.blueprint.vector.BlockVec3;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

@SuppressWarnings("unused")
public class BlueprintBlock {
    private Material material;
    private BlockData data;
    private final BlockVec3 relativePos;

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

    public void setData(BlockData data) {
        this.data = data;
    }

    public BlockVec3 getPos() {
        return relativePos;
    }

    // TODO: Most set methods should be like this. (probably define a new set method template in intellij for this lol)
    public BlueprintBlock setPos(BlockVec3 relativePos) {
        return new BlueprintBlock(material, data, relativePos);
    }
}
