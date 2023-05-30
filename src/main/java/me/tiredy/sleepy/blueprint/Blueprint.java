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
    protected List<BlueprintBlock> blocks;
    protected BlockVec3 originVector;
    protected BlockVec3 sizeVector;

    protected Blueprint(List<BlueprintBlock> blocks, BlockVec3 origin, BlockVec3 sizeVector) {
        this.blocks = blocks;
        this.originVector = origin;
        this.sizeVector = sizeVector;
    }

    public void to(BlockVec3 location, World world) {
        for (BlueprintBlock block : blocks) {
            Block targetBlock = world.getBlockAt(
                    location.getX() + block.getPos().getX(),
                    location.getY() + block.getPos().getY(),
                    location.getZ() + block.getPos().getZ()
            );
            targetBlock.setType(block.getMaterial(), false);
            targetBlock.setBlockData(block.getData(), false);
        }
    }

    public static Blueprint from(CuboidRegion region) {
        ArrayList<BlueprintBlock> blocks = new ArrayList<>();

        BlockVec3 min = region.getMin();
        BlockVec3 max = region.getMax();

        BlockVec3 size = max.subtract(min);

        World world = region.getWorld();

        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int y = min.getY(); y <= max.getY(); y++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    BlockVec3 relativePos = new BlockVec3(min.getX() - x, min.getY() - y, min.getZ());
                    blocks.add(new BlueprintBlock(block.getType(), block.getBlockData(), relativePos));
                }
            }
        }

        return new Blueprint(blocks, new BlockVec3(0,0,0), size);
    }

    public Blueprint rotateY(int degrees) {
        int rotation = Math.round(degrees / 90f) % 4;
        if (rotation < 0) {
            rotation += 4;
        }

        List<BlueprintBlock> rotatedBlocks = new ArrayList<>();
        BlockVec3 rotatedOriginVector = switch (rotation) {
            case 1 -> new BlockVec3(originVector.getX(), originVector.getY(), -originVector.getZ());
            case 2 -> new BlockVec3(-originVector.getX(), originVector.getY(), -originVector.getZ());
            case 3 -> new BlockVec3(-originVector.getX(), originVector.getY(), originVector.getZ());
            default -> originVector.copy();
        };

        BlockVec3 rotatedSizeVector = switch (rotation) {
            case 1 -> new BlockVec3(sizeVector.getX(), sizeVector.getY(), -sizeVector.getZ());
            case 2 -> new BlockVec3(-sizeVector.getX(), sizeVector.getY(), -sizeVector.getZ());
            case 3 -> new BlockVec3(-sizeVector.getX(), sizeVector.getY(), sizeVector.getZ());
            default -> sizeVector.copy();
        };

        for (BlueprintBlock block : blocks) {
            BlockVec3 pos = block.getPos();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            switch (rotation) {
                case 1 -> rotatedBlocks.add(block.setPos(new BlockVec3(x, y, -z)));
                case 2 -> rotatedBlocks.add(block.setPos(new BlockVec3(-x, y, -z)));
                case 3 -> rotatedBlocks.add(block.setPos(new BlockVec3(-x, y, z)));
                default -> rotatedBlocks.add(block); // No rotation needed
            }
        }
        return new Blueprint(rotatedBlocks, rotatedOriginVector, rotatedSizeVector);
    }

    public List<BlueprintBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlueprintBlock> blocks) {
        this.blocks = blocks;
    }

    public BlockVec3 getOrigin() {
        return originVector;
    }

    public void setOrigin(BlockVec3 origin) {
        this.originVector = origin;
    }

    public BlockVec3 getSize() {
        return sizeVector;
    }

    public void setSize(BlockVec3 sizeVector) {
        this.sizeVector = sizeVector;
    }
}
