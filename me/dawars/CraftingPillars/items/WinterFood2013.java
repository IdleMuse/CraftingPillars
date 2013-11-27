package me.dawars.CraftingPillars.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import me.dawars.CraftingPillars.CraftingPillars;

public class WinterFood2013 extends BaseItemEatable
{
	private static Icon[] iconArray = new Icon[9];
	public static String[] itemNames = { "Christmas Candy", "Candy Cane", "Lollipop", "Gingerbread Man", "Star Biscuit", "Tree Biscuit", "Blue Szaloncukor", "Gold Szaloncukor", "Red Szaloncukor"};
	private static String[] iconNames = { "ChristmasCandy", "CandyCane", "Lollipop", "GingerbreadMan", "StarBiscuit", "TreeBiscuit", "BlueSzaloncukor", "GoldSzaloncukor", "RedSzaloncukor"};
	
	public WinterFood2013(int id, int heal, float saturation)
    {
        super(id, heal, saturation);
        this.setHasSubtypes(true); // This allows the item to be marked as a metadata item.
        this.setMaxDamage(0); // This makes it so your item doesn't have the damage bar at the bottom of its icon, when "damaged" similar to the Tools.
    }
	
	public WinterFood2013(int id, int heal)
	{
		this(id, heal, 0.6F);
	}
	
	@Override
    public Icon getIconFromDamage(int i){
            return this.iconArray[i];
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconReg)
	{
       for(int i = 0; i < iconArray.length; i++)
       {
          iconArray[i] = iconReg.registerIcon(CraftingPillars.id + ":" + iconNames[i]);
       }
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs tab, List list)
	{
	    for (int i = 0; i < iconArray.length; i++)
	    {
	        list.add(new ItemStack(this, 1, i));
	    }
	}
	
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
	    int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 15);
	    return super.getUnlocalizedName() + "." + i;
	}
}
