package ad_astra_giselle_addon.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;

import ad_astra_giselle_addon.common.block.entity.IRocketSensingType;
import ad_astra_giselle_addon.common.block.entity.RocketSensorBlockEntity;
import ad_astra_giselle_addon.common.network.AddonNetwork;
import ad_astra_giselle_addon.common.network.RocketSensorMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RocketSensingTypeList extends ObjectSelectionList<RocketSensingTypeList.RocketSensingTypeEntry>
{
	private final Screen screen;
	private final RocketSensorBlockEntity rocketSensor;

	public RocketSensingTypeList(Screen screen, RocketSensorBlockEntity rocketSensor, Minecraft pMinecraft, int pWidth, int pHeight, int pY0, int pY1)
	{
		super(pMinecraft, pWidth, pHeight, pY0, pY1, 20);
		this.screen = screen;
		this.rocketSensor = rocketSensor;
		this.setRenderSelection(false);
		this.setRenderTopAndBottom(false);
		this.setRenderBackground(false);
		this.refreshList();
	}

	@Override
	public void centerScrollOn(RocketSensingTypeList.RocketSensingTypeEntry entry)
	{
		super.centerScrollOn(entry);
	}

	@Override
	protected void renderBackground(PoseStack pPoseStack)
	{
		super.renderBackground(pPoseStack);

		GuiComponent.fill(pPoseStack, this.x0 - 2, this.y0, this.x1 + 2, this.y1, 0xFF000000);
		GuiComponent.fill(pPoseStack, this.x0, this.y0 + 2, this.x1, this.y1 - 2, 0xFF202020);
	}

	public void renderTooltip(PoseStack poseStack, int mouseX, int mouseY)
	{
		RocketSensingTypeEntry entry = this.getEntryAtPosition(mouseX, mouseY);

		if (entry != null && this.y0 <= mouseY && mouseY <= this.y1)
		{
			this.getScreen().renderComponentTooltip(poseStack, entry.getType().getTooltip(), mouseX, mouseY);
		}

	}

	public void select(IRocketSensingType sensingType)
	{
		this.setSelected(null);

		for (RocketSensingTypeEntry entry : this.children())
		{
			if (entry.getType() == sensingType)
			{
				this.setSelected(entry);
				break;
			}

		}

	}

	public void refreshList()
	{
		this.clearEntries();
		IRocketSensingType.getRocketSensingTypes().stream().map(type -> new RocketSensingTypeEntry(this, type)).forEach(this::addEntry);
	}

	public Screen getScreen()
	{
		return this.screen;
	}

	public RocketSensorBlockEntity getRocketSensor()
	{
		return this.rocketSensor;
	}

	@Override
	protected int getScrollbarPosition()
	{
		return this.x0 + this.width;
	}

	@Override
	public int getRowWidth()
	{
		return this.width;
	}

	public class RocketSensingTypeEntry extends ObjectSelectionList.Entry<RocketSensingTypeEntry>
	{
		private final RocketSensingTypeList parent;
		private final IRocketSensingType type;

		public RocketSensingTypeEntry(RocketSensingTypeList parent, IRocketSensingType type)
		{
			this.parent = parent;
			this.type = type;
		}

		@Override
		public void render(PoseStack pPoseStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick)
		{
			RocketSensingTypeList parent = this.getParent();
			Minecraft minecraft = parent.minecraft;
			int y0 = Math.max(pTop, parent.y0 + 2);
			int y1 = Math.min(pTop + pHeight, parent.y1 - 2);

			if (y1 - y0 <= 0)
			{
				return;
			}

			int y = pTop + pHeight + 1;
			int y2 = Math.max(y + 0, parent.y0);
			int y3 = Math.min(y + 1, parent.y1 - 1);

			if (y3 > y2)
			{
				try (var si = RenderUtils.createScissorBox(minecraft, pPoseStack, pLeft - 2, y2, pWidth, y3 - y2))
				{
					GuiComponent.fill(pPoseStack, pLeft - 2, y, pLeft + pWidth - 2, y + 1, 0xFF000000);
				}

			}

			if (parent.getSelected() == this)
			{
				int y4 = Math.max(pTop - 2, parent.y0 + 2);
				int y5 = Math.min(pTop + pHeight + 2, parent.y1 - 2);

				try (var si = RenderUtils.createScissorBox(minecraft, pPoseStack, pLeft - 2, y4, pWidth, y5 - y4))
				{
					int i = parent.isFocused() ? -1 : -8355712;
					parent.renderSelection(pPoseStack, pTop, pWidth, pHeight, i, -16777216);
				}

			}

			try (var si = RenderUtils.createScissorBox(minecraft, pPoseStack, pLeft, y0, pWidth - 2, y1 - y0))
			{
				IRocketSensingType sensingType = this.getType();
				int itemLeft = pLeft + 2;
				int itemTop = pTop + (pHeight - 16) / 2;
				minecraft.getItemRenderer().renderGuiItem(sensingType.getDisplayIcon(), itemLeft, itemTop);

				Component name = sensingType.getDisplayName();
				Font font = minecraft.font;
				font.draw(pPoseStack, name, itemLeft + 20, pTop + (pHeight - font.lineHeight) / 2.0F, 0xFFFFFF);
			}

		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button)
		{
			AddonNetwork.CHANNEL.sendToServer(new RocketSensorMessage.SensingType(this.getParent().getRocketSensor(), this.getType()));
			return true;
		}

		public RocketSensingTypeList getParent()
		{
			return this.parent;
		}

		public IRocketSensingType getType()
		{
			return this.type;
		}

		@Override
		public Component getNarration()
		{
			return Component.translatable("narrator.select", this.getType().getDisplayName());
		}

	}

}
