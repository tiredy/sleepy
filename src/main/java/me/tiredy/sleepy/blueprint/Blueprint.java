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

        int minChunkX = min.getX() >> 4;
        int minChunkZ = min.getZ() >> 4;
        int maxChunkX = max.getX() >> 4;
        int maxChunkZ = max.getZ() >> 4;
        // TODO: Check if chunk loaded, warn if not then force sync-load
        // TODO: async-load chunk in asyncblueprint

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                world.loadChunk(chunkX, chunkZ, true);

                for (int x = chunkX << 4; x < (chunkX << 4) + 16; x++) {
                    for (int y = min.getY(); y <= max.getY(); y++) {
                        for (int z = chunkZ << 4; z < (chunkZ << 4) + 16; z++) {
                            Block block = world.getBlockAt(x, y, z);
                            BlockVec3 relativePos = new BlockVec3(x, y, z).subtract(min);
                            blocks.add(new BlueprintBlock(block.getType(), block.getBlockData(), relativePos));
                        }
                    }
                }
            }
        }

        return new Blueprint(blocks, new BlockVec3(0, 0, 0), size);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public Blueprint rotate(BlockVec3 rotationVector, int angleX, int angleY, int angleZ) {
        int rotationsX = (angleX / 90) % 4;
        int rotationsY = (angleY / 90) % 4;
        int rotationsZ = (angleZ / 90) % 4;

        ArrayList<BlueprintBlock> newBlocks = new ArrayList<>(blocks);
        BlockVec3 newOrigin = originVector.copy();
        BlockVec3 newSize = sizeVector.copy();

        for (BlueprintBlock block : newBlocks) {
            int x = block.getPos().getX() - rotationVector.getX();
            int y = block.getPos().getY() - rotationVector.getY();
            int z = block.getPos().getZ() - rotationVector.getZ();

            for (int i = 0; i < rotationsX; i++) {
                int tempY = y;
                y = -z;
                z = tempY;
            }

            for (int i = 0; i < rotationsY; i++) {
                int tempX = x;
                x = z;
                z = -tempX;
            }

            for (int i = 0; i < rotationsZ; i++) {
                int tempX = x;
                x = -y;
                y = tempX;
            }

            block.getPos().setX(x + rotationVector.getX());
            block.getPos().setY(y + rotationVector.getY());
            block.getPos().setZ(z + rotationVector.getZ());
        }

        return new Blueprint(newBlocks, newOrigin, newSize);
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
