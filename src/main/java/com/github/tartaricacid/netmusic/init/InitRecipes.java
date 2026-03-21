package com.github.tartaricacid.netmusic.init;

import btw.crafting.recipe.RecipeManager;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public final class InitRecipes {
    private InitRecipes() {
    }

    public static void registerRecipes() {
        registerMusicPlayerRecipes();
        registerMachineRecipes();
        registerMusicCdRecipe();
    }

    private static void registerMusicPlayerRecipes() {
        for (int plankSubtype = 0; plankSubtype <= 3; plankSubtype++) {
            RecipeManager.addRecipe(new ItemStack(InitBlocks.MUSIC_PLAYER), new Object[]{
                    "ABA", "ACA", "AAA",
                    'A', new ItemStack(Block.planks, 1, plankSubtype),
                    'B', new ItemStack(Item.book),
                    'C', new ItemStack(Item.diamond)});
        }
    }

    private static void registerMachineRecipes() {
        RecipeManager.addShapelessRecipe(new ItemStack(InitBlocks.CD_BURNER), new Object[]{
                new ItemStack(Block.jukebox),
                new ItemStack(Item.ingotIron),
                new ItemStack(Item.redstone)});

        RecipeManager.addShapelessRecipe(new ItemStack(InitBlocks.COMPUTER), new Object[]{
                new ItemStack(Block.jukebox),
                new ItemStack(Item.ingotGold),
                new ItemStack(Item.redstone)});
    }

    private static void registerMusicCdRecipe() {
        RecipeManager.addShapelessRecipe(new ItemStack(InitItems.MUSIC_CD), new Object[]{
                new ItemStack(Item.dyePowder, 1, 8),
                new ItemStack(Item.dyePowder, 1, 3),
                new ItemStack(Item.clay),
                new ItemStack(Item.clay)});
    }
}

