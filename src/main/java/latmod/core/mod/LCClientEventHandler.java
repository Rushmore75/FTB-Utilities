package latmod.core.mod;

import latmod.core.client.LatCoreClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class LCClientEventHandler
{
	public LCClientEventHandler()
	{
	}
	
	@SubscribeEvent
	public void preTexturesLoaded(TextureStitchEvent.Pre e)
	{
		if(e.map.getTextureType() == 0)
			LatCoreClient.blockNullIcon = e.map.registerIcon(LC.mod.assets + "nullIcon");
	}
	
	/*
	@SubscribeEvent
	public void renderPlayer(RenderPlayerEvent.Post e)
	{
		GL11.glPushMatrix();
		
		GL11.glPopMatrix();
	}*/
}