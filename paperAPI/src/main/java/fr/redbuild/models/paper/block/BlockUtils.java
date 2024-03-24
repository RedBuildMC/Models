package fr.redbuild.models.paper.block;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;


/**
 * The BlockUtils class provides utility methods for manipulating blocks in a world.
 */
public class BlockUtils {

    /**
     * Fills a rectangular region with a specified material in a given world.
     *
     * @param x   The x-coordinate of the first corner of the region.
     * @param y   The y-coordinate of the first corner of the region.
     * @param z   The z-coordinate of the first corner of the region.
     * @param x1  The x-coordinate of the second corner of the region.
     * @param y1  The y-coordinate of the second corner of the region.
     * @param z1  The z-coordinate of the second corner of the region.
     * @param world  The world in which the region exists.
     * @param mat  The material to fill the region with.
     */
    public void fill(int x, int y, int z, int x1, int y1, int z1, World world, Material mat) {
        int minX = Math.min(x, x1);
        int minY = Math.min(y, y1);
        int minZ = Math.min(z, z1);
        int basex = minX;
        int basey = minY;

        int maxX = Math.max(x, x1);
        int maxY = Math.max(y, y1);
        int maxZ = Math.max(z, z1);
        while (true) {
            if (world.getBlockAt(minX, minY, minZ).getType() != mat)
                world.getBlockAt(minX, minY, minZ).setType(mat);
            if (minX == maxX && minY == maxY && minZ == maxZ) {
                break;
            }else if (minX != maxX) {
                if (minX > maxX) {
                    minX--;
                } else {
                    minX++;
                }
            } else if (minY != maxY) {
                minX = basex;
                if (minY > maxY) {
                    minY--;
                } else {
                    minY++;
                }
            } else {
                minX = basex;
                minY = basey;
                if (minZ > maxZ) {
                    minZ--;
                } else {
                    minZ++;
                }
            }
        }
    }

        /**
     * Fills a rectangular region with a specified material in a given world.
     *
     * @param start  The starting location of the region.
     * @param end    The ending location of the region.
     * @param mat    The material to fill the region with.
     */
    public void fill(Location start,Location end,Material mat){
        fill(start.getBlockX(),start.getBlockY(),start.getBlockZ(),end.getBlockX(),end.getBlockY(),end.getBlockZ(),start.getWorld(),mat);
    }

    public void fill(List<RBlock> blocks){
        for(RBlock block : blocks){
            block.getLocation().getBlock().setType(block.getMaterial());
            block.getLocation().getBlock().setBlockData(block.getData());
        }
    }
}
