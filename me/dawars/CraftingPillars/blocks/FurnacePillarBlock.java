package me.dawars.CraftingPillars.blocks;

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.dawars.CraftingPillars.CraftingPillars;
import me.dawars.CraftingPillars.tile.TileEntityCraftingPillar;
import me.dawars.CraftingPillars.tile.TileEntityFurnacePillar;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class FurnacePillarBlock extends BaseBlockContainer
{
	public FurnacePillarBlock(int id, Material mat)
	{
		super(id, mat);
	}
	
	@Override
	public int getRenderType()
	{
		return CraftingPillars.furnacePillarRenderID;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			TileEntityFurnacePillar pillarTile = (TileEntityFurnacePillar) world.getBlockTileEntity(x, y, z);
			
			if(pillarTile.getStackInSlot(2) != null)
			{
				if(player.isSneaking())
				{
					onCrafting(pillarTile.getStackInSlot(2), player);
					pillarTile.dropItemFromSlot(2, pillarTile.getStackInSlot(2).stackSize);
				}
				else
				{
					ItemStack itemStack = pillarTile.getStackInSlot(2).copy();
					itemStack.stackSize = 1;
					onCrafting(itemStack, player);
					pillarTile.dropItemFromSlot(2, 1);
				}
			}
		}
	}
	
	public void onCrafting(ItemStack itemstack, EntityPlayer player)//?field_75228_b
	{
		int field_75228_b = itemstack.stackSize;
		itemstack.onCrafting(player.worldObj, player, field_75228_b);

		if(itemstack != null)
		{
		    if (!player.worldObj.isRemote)
		    {
		        int i = field_75228_b;
		        float f = FurnaceRecipes.smelting().getExperience(itemstack);
		        int j;
		
		        if (f == 0.0F)
		        {
		            i = 0;
		        }
		        else if (f < 1.0F)
		        {
		            j = MathHelper.floor_float((float)i * f);
		
		            if (j < MathHelper.ceiling_float_int((float)i * f) && (float)Math.random() < (float)i * f - (float)j)
		            {
		                ++j;
		            }
		
		            i = j;
		        }
		
		        while (i > 0)
		        {
		            j = EntityXPOrb.getXPSplit(i);
		            i -= j;
		            player.worldObj.spawnEntityInWorld(new EntityXPOrb(player.worldObj, player.posX, player.posY + 0.5D, player.posZ + 0.5D, j));
		        }
		    }
		
		    field_75228_b = 0;
		
		    GameRegistry.onItemSmelted(player, itemstack);
		
		    if (itemstack.itemID == Item.ingotIron.itemID)
		    {
		    	player.addStat(AchievementList.acquireIron, 1);
		    }
		
		    if (itemstack.itemID == Item.fishCooked.itemID)
		    {
		    	player.addStat(AchievementList.cookFish, 1);
		    }
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return true;
		
		TileEntityFurnacePillar pillarTile = (TileEntityFurnacePillar) world.getBlockTileEntity(x, y, z);
		
		if(player.isSneaking())
		{
			if(hitY < 1F)
			{
				pillarTile.dropItemFromSlot(1, 1);
			}
			else
			{
				pillarTile.dropItemFromSlot(0, 1);
			}
		}
		else if(player.getCurrentEquippedItem() != null)
		{
			if(hitY < 1F)
			{
				if(pillarTile.getStackInSlot(1) == null)
				{
					ItemStack in = new ItemStack(player.getCurrentEquippedItem().itemID, 1, player.getCurrentEquippedItem().getItemDamage());
					if(TileEntityFurnace.isItemFuel(in))
					{
						pillarTile.setInventorySlotContents(1, in);
						
						if(!player.capabilities.isCreativeMode)
							player.getCurrentEquippedItem().stackSize--;
					}
				}
				else if (pillarTile.getStackInSlot(1).isItemEqual(player.getCurrentEquippedItem()) && (pillarTile.getStackInSlot(1).stackSize < pillarTile.getStackInSlot(1).getMaxStackSize()))
				{
					if(!player.capabilities.isCreativeMode)
						player.getCurrentEquippedItem().stackSize--;
					
					pillarTile.decrStackSize(1, -1);
				}
			}
			else
			{
				if(pillarTile.getStackInSlot(0) == null)
				{
					ItemStack in = new ItemStack(player.getCurrentEquippedItem().itemID, 1, player.getCurrentEquippedItem().getItemDamage());
					if(FurnaceRecipes.smelting().getSmeltingResult(in) != null)
					{
						pillarTile.setInventorySlotContents(0, in);

						if(!player.capabilities.isCreativeMode)
							player.getCurrentEquippedItem().stackSize--;
					}
				}
				else if(pillarTile.getStackInSlot(0).isItemEqual(player.getCurrentEquippedItem()) && (pillarTile.getStackInSlot(0).stackSize < pillarTile.getStackInSlot(0).getMaxStackSize()))
				{
					if(!player.capabilities.isCreativeMode)
						player.getCurrentEquippedItem().stackSize--;
					
					pillarTile.decrStackSize(0, -1);
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		if(!world.isRemote)
		{
			TileEntityFurnacePillar pillarTile = (TileEntityFurnacePillar) world.getBlockTileEntity(x, y, z);
			
			for(int i = 0; i < pillarTile.getSizeInventory(); i++)
			{
				if(pillarTile.getStackInSlot(i) != null)
				{
					EntityItem itemDropped = new EntityItem(world, x + 0.5D, y, z + 0.5D, pillarTile.getStackInSlot(i));
					itemDropped.motionX = itemDropped.motionY = itemDropped.motionZ = 0D;
					
					if(pillarTile.getStackInSlot(i).hasTagCompound())
						itemDropped.getEntityItem().setTagCompound((NBTTagCompound) pillarTile.getStackInSlot(i).getTagCompound().copy());
					
					world.spawnEntityInWorld(itemDropped);
				}
			}
		}
		
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		TileEntityFurnacePillar tile = new TileEntityFurnacePillar();
		return tile;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		if(((TileEntityFurnacePillar) world.getBlockTileEntity(x, y, z)).burnTime > 0)
		{
			double rx = x + rand.nextDouble() / 2 + 0.25D;
			double ry = y + rand.nextDouble() / 2 + 0.25D;
			double rz = z + rand.nextDouble() / 2 + 0.25D;
			
			world.spawnParticle("smoke", rx, ry, rz, 0D, 0D, 0D);
			world.spawnParticle("flame", rx, ry, rz, 0D, 0D, 0D);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister itemIcon)
	{
		this.blockIcon = itemIcon.registerIcon(CraftingPillars.id + ":furnacePillar");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return this.blockIcon;
	}
}
