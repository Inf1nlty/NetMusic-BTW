package com.github.tartaricacid.netmusic.block;

import com.github.tartaricacid.netmusic.client.renderer.RenderTypes;
import com.github.tartaricacid.netmusic.creativetab.NetMusicCreativeTab;
import com.github.tartaricacid.netmusic.inventory.ComputerMenu;
import com.github.tartaricacid.netmusic.network.NetworkHandler;
import com.github.tartaricacid.netmusic.network.message.OpenMenuMessage;
import com.github.tartaricacid.netmusic.tileentity.TileEntityComputer;
import com.github.tartaricacid.netmusic.util.ServerWindowIdHelper;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockComputer extends BlockContainer {

    public BlockComputer(int id) {
        super(id, Material.wood);
        this.setHardness(0.5F);
        this.setStepSound(soundWoodFootstep);
        this.setCreativeTab(NetMusicCreativeTab.TAB);
        this.setUnlocalizedName("computer");
        this.setTextureName("netmusic:computer");
        this.initBlockBounds(0.0F, 0.0F, 0.03125F, 1.0F, 1.0625F, 1.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityComputer();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return RenderTypes.computerRenderType;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        int metadata = getFacingFromEntity(placer);
        world.setBlockMetadataWithNotify(x, y, z, metadata, 3);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float offsetX, float offsetY, float offsetZ) {
        if (world.isRemote) {
            return true;
        }
        if (player instanceof EntityPlayerMP mp) {
            openMenu(mp, x, y, z);
        }
        return true;
    }

    private static void openMenu(EntityPlayerMP player, int x, int y, int z) {
        ComputerMenu menu = new ComputerMenu(player);
        if (player.openContainer != player.inventoryContainer) {
            player.closeScreen();
        }

        int windowId = ServerWindowIdHelper.nextWindowId(player);
        player.openContainer = menu;
        player.openContainer.windowId = windowId;

        NetworkHandler.sendToClientPlayer(new OpenMenuMessage(OpenMenuMessage.Type.COMPUTER, windowId, x, y, z), player);
        player.openContainer.onCraftGuiOpened(player);
    }

    private static int getFacingFromEntity(EntityLivingBase placer) {
        if (placer == null) {
            return 0;
        }
        int rot = MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return (rot + 2) & 3;
    }
}
