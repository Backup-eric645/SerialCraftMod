package com.allwinedesigns.forge.mods.serialcraft;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.world.World;

public class CommonProxy {

	@EventHandler
    public void preinit(FMLPreInitializationEvent event) {

	}
	
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	GameRegistry.registerTileEntity(TileEntitySerialRedstone.class, "serialRedstone");
    	GameRegistry.registerBlock(new BlockSerialRedstone(), "serialRedstone");
    	
    	GameRegistry.registerTileEntity(TileEntitySendSerialMessage.class, "sendSerialMessageBlock");
    	GameRegistry.registerBlock(new BlockSendSerialMessage(), "sendSerialMessageBlock");

    }
    
    public void openSerialRedstoneGUI(World world, int x, int y, int z) {

    }
    
    public void openConfigGUI() {
    
    }
}