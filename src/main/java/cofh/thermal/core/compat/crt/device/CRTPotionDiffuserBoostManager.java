package cofh.thermal.core.compat.crt.device;

import cofh.thermal.core.init.TCoreRecipeTypes;
import cofh.thermal.core.util.recipes.device.PotionDiffuserBoost;
import cofh.thermal.lib.compat.crt.base.CRTHelper;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.handler.IReplacementRule;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ZenRegister
@ZenCodeType.Name ("mods.thermal.PotionDiffuserBoost")
@IRecipeHandler.For (PotionDiffuserBoost.class)
public class CRTPotionDiffuserBoostManager implements IRecipeManager, IRecipeHandler<PotionDiffuserBoost> {

    @ZenCodeType.Method
    public void addBoost(String name, IIngredientWithAmount inputItem, int amplifier, float durationMod, int cycles) {

        name = fixRecipeName(name);
        ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);

        PotionDiffuserBoost mapping = new PotionDiffuserBoost(resourceLocation, CRTHelper.mapIIngredientWithAmount(inputItem), amplifier, durationMod, cycles);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, mapping));
    }

    @Override
    public RecipeType<PotionDiffuserBoost> getRecipeType() {

        return TCoreRecipeTypes.BOOST_POTION_DIFFUSER;
    }

    @Override
    public void removeByInput(IItemStack input) {

        removeBoost(input);
    }

    @ZenCodeType.Method
    public void removeBoost(IItemStack input) {

        CraftTweakerAPI.apply(new ActionRemoveRecipe(this, recipe -> {
            if (recipe instanceof PotionDiffuserBoost) {
                return ((PotionDiffuserBoost) recipe).getIngredient().test(input.getInternal());
            }
            return false;
        }));
    }

    @Override
    public String dumpToCommandString(IRecipeManager manager, PotionDiffuserBoost recipe) {

        return String.format("<recipetype:%s>.addBoost(\"%s\", %s, %s, %s, %s);", recipe.getType(), recipe.getId(), IIngredient.fromIngredient(recipe.getIngredient()).getCommandString(), recipe.getAmplifier(), recipe.getDurationMod(), recipe.getCycles());
    }

    @Override
    public Optional<Function<ResourceLocation, PotionDiffuserBoost>> replaceIngredients(IRecipeManager manager, PotionDiffuserBoost recipe, List<IReplacementRule> rules) {

        final Optional<Ingredient> ingredient = IRecipeHandler.attemptReplacing(recipe.getIngredient(), Ingredient.class, recipe, rules);
        return ingredient.map(value -> id -> new PotionDiffuserBoost(id, value, recipe.getAmplifier(), recipe.getDurationMod(), recipe.getCycles()));
    }

}
