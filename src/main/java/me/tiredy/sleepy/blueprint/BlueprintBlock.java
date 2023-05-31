package me.tiredy.sleepy.blueprint;

import me.tiredy.sleepy.blueprint.vector.BlockVec3;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

@SuppressWarnings("unused")
public class BlueprintBlock {
    private final Material material;
    private final BlockData data;
    private final BlockVec3 relativePos;

    public BlueprintBlock(Material material, BlockData data, BlockVec3 relativePos) {
        this.material = material;
        this.data = data;
        this.relativePos = relativePos;
    }

    public Material getMaterial() {
        return material;
    }

    public BlueprintBlock setMaterial(Material material) {
        return new BlueprintBlock(material, data, relativePos);
    }

    public BlockData getData() {
        return data;
    }

    public BlueprintBlock setData(BlockData data) {
        return new BlueprintBlock(material, data, relativePos);
    }

    public BlockVec3 getPos() {
        return relativePos;
    }

    // TODO: Most set methods should be like this. (probably define a new set method template in intellij for this lol)
    public BlueprintBlock setPos(BlockVec3 relativePos) {
        return new BlueprintBlock(material, data, relativePos);
    }
}
