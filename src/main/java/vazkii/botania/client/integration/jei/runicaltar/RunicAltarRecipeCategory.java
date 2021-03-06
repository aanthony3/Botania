/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.runicaltar;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collection;

public class RunicAltarRecipeCategory implements IRecipeCategory {

	private final IDrawable background;
	private final String localizedName;
	private final IDrawable overlay;

	public RunicAltarRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(150, 110);
		localizedName = I18n.translateToLocal("botania.nei.runicAltar");
		overlay = guiHelper.createDrawable(new ResourceLocation("botania", "textures/gui/petalOverlay.png"),
				0, 0, 150, 110);
	}

	@Nonnull
	@Override
	public String getUid() {
		return "botania.runicAltar";
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		overlay.draw(minecraft);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		if(!(recipeWrapper instanceof RunicAltarRecipeWrapper))
			return;
		RunicAltarRecipeWrapper wrapper = ((RunicAltarRecipeWrapper) recipeWrapper);

		recipeLayout.getItemStacks().init(0, true, 64, 52);
		recipeLayout.getItemStacks().set(0, new ItemStack(ModBlocks.runeAltar));

		int index = 1;
		double angleBetweenEach = 360.0 / wrapper.getInputs().size();
		Point point = new Point(64, 20), center = new Point(64, 52);

		for(Object o : wrapper.getInputs()) {
			recipeLayout.getItemStacks().init(index, true, point.x, point.y);
			if(o instanceof Collection) {
				recipeLayout.getItemStacks().set(index, ((Collection<ItemStack>) o));
			}
			if(o instanceof ItemStack) {
				recipeLayout.getItemStacks().set(index, ((ItemStack) o));
			}
			index += 1;
			point = rotatePointAbout(point, center, angleBetweenEach);
		}

		recipeLayout.getItemStacks().init(index, false, 103, 17);
		recipeLayout.getItemStacks().set(index, wrapper.getOutputs().get(0));
	}

	private Point rotatePointAbout(Point in, Point about, double degrees) {
		double rad = degrees * Math.PI / 180.0;
		double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
		double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
		return new Point(((int) newX), ((int) newY));
	}

}
