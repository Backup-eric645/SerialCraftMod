package org.cmbozeman.forge.mods.serialcraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jssc.SerialPortException;
import jssc.SerialPortList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

// Client side management of serial ports
public class SerialCraftListener {
	private Map<String, SerialPortIO> ports;
	private Map<String, SerialEventHandler> handlers;
	
    public SerialCraftListener() {
    	ports = new HashMap<String, SerialPortIO>();
    	handlers = new HashMap<String, SerialEventHandler>();
    	
    	handlers.put("chat", new SerialEventHandler() {
    		public void handler(String args) {
        		MinecraftForge.EVENT_BUS.post(new SerialCraftChatEvent(
                        Minecraft.getMinecraft().thePlayer, 
                        args
                   )
                  );
    		}
    	});
    	
    	handlers.put("redstone", new SerialEventHandler() {
    		public void handler(String args_str) {
        		try {
    	    		System.out.println(args_str);
    	    		String[] args = args_str.split(" ");
    	
    	    		MinecraftForge.EVENT_BUS.post(new SerialCraftRedstoneEvent(
    	                    Minecraft.getMinecraft().thePlayer, 
    	                    Integer.parseInt(args[0]),
    	                    args[1]
    	               )
    	              );
        		} catch(Exception e) {
        			System.out.println(e);
        		}
    		}
    	});
    	
    	handlers.put("time", new SerialEventHandler() {
    		public void handler(String args) {
        		MinecraftForge.EVENT_BUS.post(new SerialCraftTimeEvent(Minecraft.getMinecraft().thePlayer, args));
    		}
    	});
    	
    	handlers.put("move_forward", new SerialEventHandler() {
    		public void handler(String args) {
        		if(args.equals("forward")) {
        		    ClientProxy.getMovementController().moveForward();
        		} else if(args.equals("back")) {
        			ClientProxy.getMovementController().moveBack();
        		} else {
        			ClientProxy.getMovementController().stopForwardMovement();
        		}
    		}
    	});
    	
    	handlers.put("move_speed", new SerialEventHandler() {
    		public void handler(String arg) {
        		if(arg.equals("sprint")) {
        		    ClientProxy.getMovementController().sprint();
        		} else if(arg.equals("sneak")) {
        			ClientProxy.getMovementController().sneak();
        		} else {
        			ClientProxy.getMovementController().normalMovementSpeed();
        		}
    		}
    	});
    	
    	handlers.put("move_strafe", new SerialEventHandler() {
    		public void handler(String arg) {
        		if(arg.equals("left")) {
        		    ClientProxy.getMovementController().moveLeft();
        		} else if(arg.equals("right")) {
        			ClientProxy.getMovementController().moveRight();
        		} else {
        			ClientProxy.getMovementController().stopStrafeMovement();
        		}
    		}
    	});
    	
    	handlers.put("start_jumping", new SerialEventHandler() {
    		public void handler(String arg) {
    			ClientProxy.getMovementController().startJumping();
    		}
    	});
    	
    	handlers.put("stop_jumping", new SerialEventHandler() {
    		public void handler(String arg) {
    			ClientProxy.getMovementController().stopJumping();
    		}
    	});
    	
    	handlers.put("m",  new SerialEventHandler() {
    		public void handler(String str) {
        		String[] args = str.split(" ");
        		try {
        		    int x = Integer.parseInt(args[0]);
        		    int y = Integer.parseInt(args[1]);
        			ClientProxy.getMovementController().moveMouseWithJoystick(x, y);
        		} catch(Exception e) {
        			System.out.println(e);
        		}
    		}
    	});
    	
    	handlers.put("move_down", new SerialEventHandler() {
    		public void handler(String arg) {
        		ClientProxy.getMovementController().moveDown();
    		}
    	});
    	
    	handlers.put("stop_move_down", new SerialEventHandler() {
    		public void handler(String arg) {
        		ClientProxy.getMovementController().stopMovingDown();
    		}
    	});
    	
    	handlers.put("left_button_press", new SerialEventHandler() {
    		public void handler(String arg) {
    			ClientProxy.getMovementController().leftMousePress();
    		}
    	});
    	
    	handlers.put("left_button_release", new SerialEventHandler() {
    		public void handler(String arg) {
    			ClientProxy.getMovementController().leftMouseRelease();
    		}
    	});
    	
    	handlers.put("right_button_press", new SerialEventHandler() {
    		public void handler(String arg) {
    	    	ClientProxy.getMovementController().rightMousePress();
    		}
    	});
    	
    	handlers.put("right_button_release", new SerialEventHandler() {
    		public void handler(String arg) {
    			ClientProxy.getMovementController().rightMouseRelease(); 
    		}
    	});
    }
    
    public void triggerEvent(String str) {
    	int index = str.indexOf(' ');
    	String msg = str;
    	String args = "";
    	
    	if(index >= 0) {
    		msg = str.substring(0, index);
    	    args = str.substring(index+1);
    	}
    	
    	//System.out.println("received message: " + msg + ", with args: " + args);
    	
    	SerialEventHandler h = handlers.get(msg);
    	if(h != null) {
    		h.handler(args);;
    	}
    }
    
    public static <T> boolean contains(final T[] array, final T v) {
        if (v == null) {
            for (final T e : array)
                if (e == null)
                    return true;
        } else {
            for (final T e : array)
                if (e == v || v.equals(e))
                    return true;
        }

        return false;
    }
    
    public void updatePorts() {
    	String[] portNames = SerialPortList.getPortNames();
        for(Map.Entry<String, SerialPortIO> entry : ports.entrySet()) {
            if(!contains(portNames, entry.getKey())) {
            	entry.getValue().disconnect();
            	ports.remove(entry.getKey());
            }
        }
    }
    
    public int getBaudRate(String name) {
        return ports.get(name).getBaudRate();
    }
    
    public boolean isPortConnected(String name) {
    	return ports.containsKey(name);
    }
    
    public void connectPort(String name, int baud) {
    	try {
    		SerialPortIO port = new SerialPortIO(name, baud);
    		
    		ports.put(name, port);
    	} catch(SerialPortException e) {
    		System.out.println(e);
    	}
    }
    
    public void disconnectPort(String name) {
    	if(ports.containsKey(name)) {
    		
        	ports.get(name).disconnect();
        	ports.remove(name);
    	}
    }
    
    public void sendSerialMessage(String msg) {
    	try {
    		for(Map.Entry<String, SerialPortIO> entry : ports.entrySet()) {
    			entry.getValue().sendMessage(msg);
    		}
    	} catch(SerialPortException ex) {
    		System.out.println(ex);
    	}
    }
}
