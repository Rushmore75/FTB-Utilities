package latmod.core.mod;
import net.minecraftforge.common.MinecraftForge;
import latmod.core.ODItems;
import latmod.core.base.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;

@Mod(modid = LC.MODID, name = LC.MODNAME, version = LC.VERSION)
public class LC
{
	public static final String MODID = "latcore";
	public static final String MODNAME = "LatCore";
	public static final String VERSION = "1.3.1";
	
	@Mod.Instance(LC.MODID)
	public static LC inst;
	
	@SidedProxy(clientSide = "latmod.core.mod.LCClient", serverSide = "latmod.core.mod.LCCommon")
	public static LCCommon proxy;
	
	public static LMMod mod;
	
	public LC()
	{
		MinecraftForge.EVENT_BUS.register(new LCEventHandler());	
	}
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		mod = new LMMod(MODID);
		proxy.preInit();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent e)
	{
		ODItems.register();
		proxy.init();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		proxy.postInit();
	}
}