package me.tiredy.sleepy.blueprint;

import me.tiredy.sleepy.blueprint.vector.BlockVec3;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Blueprint implements Serializable {
    public List<BlueprintBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlueprintBlock> blocks) {
        this.blocks = blocks;
    }

    public BlockVec3 getOrigin() {
        return origin;
    }

    public void setOrigin(BlockVec3 origin) {
        this.origin = origin;
    }

    protected List<BlueprintBlock> blocks;
    protected BlockVec3 origin;

    protected Blueprint(ArrayList<BlueprintBlock> blocks, BlockVec3 origin) {
        this.blocks = blocks;
        this.origin = origin;
    }

    public void to(BlockVec3 location, World world) {
        for (BlueprintBlock block : blocks) {
            Block targetBlock = world.getBlockAt(
                    location.getX() + block.getPos().getX(),
                    location.getY() + block.getPos().getY(),
                    location.getZ() + block.getPos().getZ()
            );
            targetBlock.setType(block.getMaterial(), false);
            targetBlock.setBlockData(block.getData());
        }
    }

    public static Blueprint from(World world, BlockVec3 min, BlockVec3 max) {
        ArrayList<BlueprintBlock> blocks = new ArrayList<>();

        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int y = min.getY(); y <= max.getY(); y++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    blocks.add(new BlueprintBlock(block.getType(), block.getBlockData(), new BlockVec3(x,y,z)));
                }
            }
        }

        return new Blueprint(blocks,new BlockVec3(0,0,0));
    }
}
