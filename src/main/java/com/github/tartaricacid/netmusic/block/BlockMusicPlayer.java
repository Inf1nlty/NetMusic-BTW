package com.github.tartaricacid.netmusic.block;

import com.github.tartaricacid.netmusic.NetMusic;
import com.github.tartaricacid.netmusic.api.netease.NeteaseVipMusicApi;
import com.github.tartaricacid.netmusic.api.qq.QqMusicApi;
import com.github.tartaricacid.netmusic.client.renderer.RenderTypes;
import com.github.tartaricacid.netmusic.config.PlayerVipCookieStore;
import com.github.tartaricacid.netmusic.creativetab.NetMusicCreativeTab;
import com.github.tartaricacid.netmusic.item.ItemMusicCD;
import com.github.tartaricacid.netmusic.tileentity.TileEntityMusicPlayer;
import com.github.tartaricacid.netmusic.util.SongInfoHelper;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockMusicPlayer extends BlockContainer {

    public static final int CYCLE_DISABLE_MASK = 4;
    private static final Pattern NETEASE_OUTER_ID_PATTERN = Pattern.compile("(?i)[?&]id=(\\d+)(?:\\.mp3)?");
    private static final Pattern NETEASE_FRAGMENT_ID_PATTERN = Pattern.compile("(?i)(?:netmusic_songid)(?:=|/)(\\d+)");

    public BlockMusicPlayer(int id) {
        super(id, Material.wood);
        this.setHardness(0.5F);
        this.setStepSound(soundWoodFootstep);
        this.setCreativeTab(NetMusicCreativeTab.TAB);
        this.setUnlocalizedName("music_player");
        this.setTextureName("netmusic:music_player");
        this.initBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.375F, 0.875F);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityMusicPlayer();
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
        return RenderTypes.musicPlayerRenderType;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        int metadata = getFacingFromEntity(placer) | CYCLE_DISABLE_MASK;
        world.setBlockMetadataWithNotify(x, y, z, metadata, 3);
    }

    public static boolean isCycleDisabled(int metadata) {
        return (metadata & CYCLE_DISABLE_MASK) != 0;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityMusicPlayer musicPlayer) {
            if (musicPlayer.getItem(0) != null) {
                return musicPlayer.isPlay() ? 15 : 7;
            }
        }
        return 0;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockId) {
        playMusic(world, x, y, z, world.isBlockIndirectlyGettingPowered(x, y, z));
    }

    private static void playMusic(World world, int x, int y, int z, boolean signal) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityMusicPlayer musicPlayer) {
            if (signal != musicPlayer.hasSignal()) {
                if (signal) {
                    if (musicPlayer.isPlay()) {
                        musicPlayer.setPlay(false);
                        musicPlayer.setSignal(true);
                        musicPlayer.setChanged();
                        return;
                    }
                    ItemStack stackInSlot = musicPlayer.getItem(0);
                    if (stackInSlot == null) {
                        musicPlayer.setSignal(true);
                        musicPlayer.setChanged();
                        return;
                    }
                    ItemMusicCD.SongInfo songInfo = ItemMusicCD.getSongInfo(stackInSlot);
                    if (songInfo != null) {
                        musicPlayer.setPlayToClient(songInfo);
                    }
                }
                musicPlayer.setSignal(signal);
                musicPlayer.setChanged();
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float offsetX, float offsetY, float offsetZ) {
        if (world.isRemote) {
            return true;
        }

        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (!(tileEntity instanceof TileEntityMusicPlayer musicPlayer)) {
            return false;
        }

        ItemStack stack = musicPlayer.getItem(0);
        if (stack != null) {
            if (musicPlayer.isPlay()) {
                musicPlayer.setPlay(false);
                musicPlayer.setCurrentTime(0);
            }
            ItemStack removed = musicPlayer.removeItem(0, 1);
            if (removed != null && !player.inventory.addItemStackToInventory(removed)) {
                player.dropPlayerItem(removed);
            }
            musicPlayer.setChanged();
            return true;
        }

        ItemStack heldStack = player.getHeldItem();
        ItemMusicCD.SongInfo info = ItemMusicCD.getSongInfo(heldStack);
        if (info == null) {
            return false;
        }
        if (info.vip && !PlayerVipCookieStore.hasVipCookieForUrl(player, info.songUrl)) {
            player.addChatMessage("message.netmusic.music_player.need_vip");
            return true;
        }

        ItemMusicCD.SongInfo playbackInfo = resolvePlaybackInfoForServer(info, player);
        if (playbackInfo == null) {
            playbackInfo = SongInfoHelper.sanitize(info);
        }

        ItemStack one = heldStack.copy();
        one.stackSize = 1;
        musicPlayer.setItem(0, one);
        musicPlayer.setPreparedSongInfo(playbackInfo);
        if (!player.capabilities.isCreativeMode) {
            heldStack.stackSize--;
            if (heldStack.stackSize <= 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
        }
        musicPlayer.setPlayToClient(playbackInfo != null ? playbackInfo : info);
        musicPlayer.setChanged();
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int blockId, int metadata) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityMusicPlayer musicPlayer) {
            ItemStack stack = musicPlayer.getItem(0);
            if (stack != null) {
                this.dropBlockAsItem_do(world, x, y, z, stack);
            }
        }
        super.breakBlock(world, x, y, z, blockId, metadata);
    }

    private static int getFacingFromEntity(EntityLivingBase placer) {
        if (placer == null) {
            return 0;
        }
        int rot = MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return (rot + 2) & 3;
    }

    private static ItemMusicCD.SongInfo resolvePlaybackInfoForServer(ItemMusicCD.SongInfo source, EntityPlayer player) {
        ItemMusicCD.SongInfo base = SongInfoHelper.sanitize(source);
        if (base == null) {
            return null;
        }
        if (!(player instanceof EntityPlayerMP)) {
            return base;
        }

        PlayerVipCookieStore.CookiePair cookiePair = PlayerVipCookieStore.getCookies(player);
        try {
            if (isQqSongUrl(base.songUrl)) {
                ItemMusicCD.SongInfo resolved = SongInfoHelper.sanitize(QqMusicApi.resolveSong(base.songUrl, cookiePair.qqCookie));
                return mergeResolvedSongInfo(base, resolved);
            }
            if (isNeteaseSongUrl(base.songUrl)) {
                String songId = extractNeteaseSongId(base.songUrl);
                String resolvedUrl = NeteaseVipMusicApi.resolveByOuterUrl(base.songUrl, cookiePair.neteaseCookie);
                if (StringUtils.isNotBlank(resolvedUrl)) {
                    base.songUrl = withNeteaseSongIdMarker(resolvedUrl.trim(), songId);
                }
            }
            return base;
        } catch (Exception e) {
            NetMusic.LOGGER.warn("Failed to resolve server playback url for {}", base.songUrl, e);
            return base;
        }
    }

    private static ItemMusicCD.SongInfo mergeResolvedSongInfo(ItemMusicCD.SongInfo fallback, ItemMusicCD.SongInfo resolved) {
        if (resolved == null) {
            return fallback;
        }
        if (StringUtils.isBlank(resolved.songName)) {
            resolved.songName = fallback.songName;
        }
        if (resolved.songTime <= 0) {
            resolved.songTime = fallback.songTime;
        }
        if (resolved.artists == null || resolved.artists.isEmpty()) {
            resolved.artists.clear();
            if (fallback.artists != null) {
                resolved.artists.addAll(fallback.artists);
            }
        }
        resolved.readOnly = fallback.readOnly;
        resolved.vip = fallback.vip;
        return SongInfoHelper.sanitize(resolved);
    }

    private static String withNeteaseSongIdMarker(String url, String songId) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(songId)) {
            return url;
        }
        if (NETEASE_FRAGMENT_ID_PATTERN.matcher(url).find()) {
            return url;
        }
        int hash = url.indexOf('#');
        if (hash < 0) {
            return url + "#netmusic_songid=" + songId;
        }
        String fragment = url.substring(hash + 1);
        if (StringUtils.isBlank(fragment)) {
            return url + "netmusic_songid=" + songId;
        }
        return url + "&netmusic_songid=" + songId;
    }

    private static String extractNeteaseSongId(String url) {
        if (StringUtils.isBlank(url)) {
            return "";
        }
        Matcher fragmentMatcher = NETEASE_FRAGMENT_ID_PATTERN.matcher(url);
        if (fragmentMatcher.find()) {
            return fragmentMatcher.group(1);
        }
        Matcher outerMatcher = NETEASE_OUTER_ID_PATTERN.matcher(url);
        if (outerMatcher.find()) {
            return outerMatcher.group(1);
        }
        return "";
    }

    private static boolean isQqSongUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        String lower = url.toLowerCase();
        return lower.contains("y.qq.com")
                || lower.contains("qqmusic.qq.com")
                || lower.contains("aqqmusic.tc.qq.com")
                || lower.contains("stream.qqmusic.qq.com");
    }

    private static boolean isNeteaseSongUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        return url.toLowerCase().contains("music.163.com");
    }
}
