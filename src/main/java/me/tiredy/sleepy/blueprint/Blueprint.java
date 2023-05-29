package me.tiredy.sleepy.blueprint;

import me.tiredy.sleepy.blueprint.region.CuboidRegion;
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

    public static Blueprint from(World world, CuboidRegion region) {
        ArrayList<BlueprintBlock> blocks = new ArrayList<>();

        BlockVec3 min = region.getMin();
        BlockVec3 max = region.getMax();

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

    public void rotateY(int degrees) {
        int rotation = Math.round(degrees / 90f) % 4;
        if (rotation < 0) {
            rotation += 4;
        }
        for (BlueprintBlock block : blocks) {
            BlockVec3 pos = block.getPos();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            switch (rotation) {
                case 1 -> block.setPos(new BlockVec3(x, y, -z));
                case 2 -> block.setPos(new BlockVec3(-x, y, -z));
                case 3 -> block.setPos(new BlockVec3(-x, y, z));
            }
        }
        switch (rotation) {
            case 1 -> origin = new BlockVec3(origin.getX(), origin.getY(), -origin.getZ());
            case 2 -> origin = new BlockVec3(-origin.getX(), origin.getY(), -origin.getZ());
            case 3 -> origin = new BlockVec3(-origin.getX(), origin.getY(), origin.getZ());
        }
    }
}
