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
                    BlockVec3 relativePos = new BlockVec3(x, y, z).subtract(min);
                    blocks.add(new BlueprintBlock(block.getType(), block.getBlockData(), relativePos));
                }
            }
        }

        return new Blueprint(blocks, new BlockVec3(0,0,0), size);
    }

    public Blueprint rotateY(int degrees) {
        int rotation = Math.floorMod(degrees, 360);
        if (rotation < 0) {
            rotation += 360;
        }

        List<BlueprintBlock> rotatedBlocks = new ArrayList<>();
        int quarterTurns = rotation / 90;

        BlockVec3 rotatedOriginVector = rotateVectorY(originVector, quarterTurns);
        BlockVec3 rotatedSizeVector = rotateVectorY(sizeVector, quarterTurns);

        // Calculate the translation vector based on the rotation
        BlockVec3 translationVector = new BlockVec3(0, 0, 0);
        for (int i = 0; i < quarterTurns; i++) {
            int temp = translationVector.getX();
            translationVector.setX(-translationVector.getZ());
            translationVector.setZ(temp);
        }

        for (BlueprintBlock block : blocks) {
            BlockVec3 pos = block.getPos();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            int newX = x + translationVector.getX();
            int newZ = z + translationVector.getZ();

            rotatedBlocks.add(block.setPos(new BlockVec3(newX, y, newZ)));
        }

        return new Blueprint(rotatedBlocks, rotatedOriginVector, rotatedSizeVector);
    }

    private BlockVec3 rotateVectorY(BlockVec3 vector, int quarterTurns) {
        int x = vector.getX();
        int y = vector.getY();
        int z = vector.getZ();

        for (int i = 0; i < quarterTurns; i++) {
            int temp = x;
            x = -z;
            z = temp;
        }

        return new BlockVec3(x, y, z);
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
