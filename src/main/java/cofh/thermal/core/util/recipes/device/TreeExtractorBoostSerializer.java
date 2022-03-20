package cofh.thermal.core.util.recipes.device;

import cofh.thermal.core.util.managers.device.TreeExtractorManager;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

import static cofh.lib.util.recipes.RecipeJsonUtils.*;

public class TreeExtractorBoostSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<TreeExtractorBoost> {

    @Override
    public TreeExtractorBoost fromJson(ResourceLocation recipeId, JsonObject json) {

        Ingredient ingredient;
        float outputMod = 1.0F;
        int cycles = TreeExtractorManager.instance().getDefaultEnergy();

        /* INPUT */
        ingredient = parseIngredient(json.get(INGREDIENT));

        if (json.has(OUTPUT)) {
            outputMod = json.get(OUTPUT).getAsFloat();
        } else if (json.has(OUTPUT_MOD)) {
            outputMod = json.get(OUTPUT_MOD).getAsFloat();
        }
        if (json.has(CYCLES)) {
            cycles = json.get(CYCLES).getAsInt();
        }
        return new TreeExtractorBoost(recipeId, ingredient, outputMod, cycles);
    }

    @Nullable
    @Override
    public TreeExtractorBoost fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

        Ingredient ingredient = Ingredient.fromNetwork(buffer);

        float outputMod = buffer.readFloat();
        int cycles = buffer.readInt();

        return new TreeExtractorBoost(recipeId, ingredient, outputMod, cycles);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, TreeExtractorBoost recipe) {

        recipe.ingredient.toNetwork(buffer);

        buffer.writeFloat(recipe.outputMod);
        buffer.writeInt(recipe.cycles);
    }

}
