package de.ellpeck.actuallyadditions.mod.util;


import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.mod.crafting.ActuallyRecipes;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;

@SuppressWarnings("deprecation")
public class ResourceReloader implements IResourceManagerReloadListener {
    private final DataPackRegistries data;
    public ResourceReloader(DataPackRegistries dataPackRegistries) {
        data = dataPackRegistries;
    }

    @Override
    public void onResourceManagerReload(IResourceManager pResourceManager) {
        RecipeManager recipeManager = data.getRecipeManager();
        ActuallyAdditionsAPI.EMPOWERER_RECIPES.clear();
        ActuallyAdditionsAPI.EMPOWERER_RECIPES.addAll(recipeManager.getAllRecipesFor(ActuallyRecipes.Types.EMPOWERING));

        ActuallyAdditionsAPI.SOLID_FUEL_RECIPES.clear();
        ActuallyAdditionsAPI.SOLID_FUEL_RECIPES.addAll(recipeManager.getAllRecipesFor(ActuallyRecipes.Types.SOLID_FUEL));
    }
}
