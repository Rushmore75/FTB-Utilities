package latmod.ftbu.api.guide;

import java.io.File;
import java.util.Map;

import ftb.lib.*;
import latmod.ftbu.mod.client.gui.guide.GuideLinkSerializer;
import latmod.lib.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;

public class GuideFile // ServerGuideFile // ClientGuideFile
{
	public final GuideCategory main;
	public final FastMap<String, GuideLink> links;
	
	public GuideFile(IChatComponent title)
	{
		main = new GuideCategory(title);
		main.file = this;
		links = new FastMap<String, GuideLink>();
	}
	
	public GuideCategory getMod(String s)
	{ return main.getSub(new ChatComponentText("Mods")).getSub(new ChatComponentText(s)); }
	
	public GuideLink getGuideLink(String s)
	{
		if(s != null)
		{
			s = FTBLib.removeFormatting(s.trim());
			if(s.length() > 2 && s.charAt(0) == '[' && s.charAt(s.length() - 1) == ']')
				return links.get(s.substring(1, s.length() - 1));
		}
		
		return null;
	}
	
	protected static class LinksMap
	{
		public Map<String, GuideLink> links;
	}
	
	protected static void loadFromFiles(GuideCategory c, File f)
	{
		if(f == null || !f.exists()) return;
		
		String name = f.getName();
		
		if(f.isDirectory())
		{
			File[] f1 = f.listFiles();
			
			if(f1 != null && f1.length > 0)
			{
				GuideCategory c1 = c.getSub(new ChatComponentText(name));
				for(File f2 : f1) loadFromFiles(c1, f2);
			}
		}
		else if(f.isFile())
		{
			if(name.endsWith(".txt"))
			{
				try
				{
					GuideCategory c1 = c.getSub(new ChatComponentText(name.substring(0, name.length() - 4)));
					String text = LMFileUtils.loadAsText(f);
					c1.println(text);
				}
				catch(Exception e)
				{ e.printStackTrace(); }
			}
		}
	}
	
	protected void loadLinksFromFile(File f)
	{
		if(f == null || !f.exists()) return;
		links.clear();
		LinksMap linksMap = LMJsonUtils.fromJsonFile(GuideLinkSerializer.gson, LMFileUtils.newFile(f), LinksMap.class);
		if(linksMap != null && linksMap.links != null) links.putAll(linksMap.links);
	}
	
	public void readFromNBT(NBTTagCompound tag)
	{
		links.clear();
		
		if(tag == null) { main.clear(); return; }
		
		if(tag.hasKey("L"))
		{
			NBTTagList linksList = tag.getTagList("L", LMNBTUtils.MAP);
			
			for(int i = 0; i < linksList.tagCount(); i++)
			{
				NBTTagCompound tag1 = linksList.getCompoundTagAt(i);
				GuideLink l = new GuideLink(LinkType.values()[tag1.getByte("I")], tag1.getString("L"));
				if(tag1.hasKey("T")) l.title = IChatComponent.Serializer.func_150699_a(tag1.getString("T"));
				if(tag1.hasKey("H")) l.hover = IChatComponent.Serializer.func_150699_a(tag1.getString("H"));
				links.put(tag1.getString("ID"), l);
			}
		}
		
		main.readFromNBT(tag);
	}
	
	public void writeToNBT(NBTTagCompound tag)
	{
		if(links.size() > 0)
		{
			NBTTagList linksList = new NBTTagList();
			
			for(int i = 0; i < links.size(); i++)
			{
				GuideLink l = links.values.get(i);
				
				NBTTagCompound tag1 = new NBTTagCompound();
				
				tag1.setByte("I", (byte)l.type.ordinal());
				tag1.setString("ID", links.keys.get(i));
				if(!l.link.isEmpty()) tag1.setString("L", l.link);
				if(l.title != null) tag1.setString("T", IChatComponent.Serializer.func_150696_a(l.title));
				if(l.hover != null) tag1.setString("H", IChatComponent.Serializer.func_150696_a(l.hover));
				
				linksList.appendTag(tag1);
			}
			
			tag.setTag("L", linksList);
		}
		
		main.writeToNBT(tag);
	}
}