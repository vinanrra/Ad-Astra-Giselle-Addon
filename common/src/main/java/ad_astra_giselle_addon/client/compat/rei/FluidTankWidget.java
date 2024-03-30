package ad_astra_giselle_addon.client.compat.rei;

import com.mojang.blaze3d.systems.RenderSystem;

import earth.terrarium.adastra.client.utils.GuiUtils;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.minecraft.client.gui.GuiGraphics;

public class FluidTankWidget extends EntryWidget
{
	public FluidTankWidget(Rectangle bounds)
	{
		super(bounds);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta)
	{
		super.render(graphics, mouseX, mouseY, delta);

		Rectangle bounds = this.getBounds();

		RenderSystem.disableDepthTest();
		graphics.blitSprite(GuiUtils.FLUID_BAR, bounds.x, bounds.y, bounds.width, bounds.height);
		RenderSystem.enableDepthTest();
	}

}
