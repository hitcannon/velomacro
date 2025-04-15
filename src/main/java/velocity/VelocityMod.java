package velocity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@Mod(modid = "VelocityMod", name = "Velocity Mod", version = "1.1")
public class VelocityMod {
    @Mod.Instance
    public static VelocityMod instance;

    private final Minecraft mc = Minecraft.getMinecraft();

    private KeyBinding dragKey;

    private int posX = 10, posY = 10;
    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        dragKey = new KeyBinding("Drag Velocity Display", Keyboard.KEY_LSHIFT, "Velocity Mod");
        ClientRegistry.registerKeyBinding(dragKey);

    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (mc.thePlayer != null) {
            double dx = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double dz = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            double velocity = Math.sqrt(dx * dx + dz * dz);
            double blocksPerSecond = velocity * 20;
            String velocityText = String.format("%.2f m/s", blocksPerSecond);

            int mouseX = Mouse.getX() * mc.displayWidth / mc.displayWidth;
            int mouseY = mc.displayHeight - Mouse.getY() * mc.displayHeight / mc.displayHeight - 1;

            if (dragKey.isKeyDown() && Mouse.isButtonDown(0)) {
                if (!dragging) {
                    // Begin dragging
                    dragging = true;
                    dragOffsetX = mouseX - posX;
                    dragOffsetY = mouseY - posY;
                } else {
                    // Update position while dragging
                    posX = mouseX - dragOffsetX;
                    posY = mouseY - dragOffsetY;
                }
            } else {
                dragging = false;
            }

            mc.fontRendererObj.drawStringWithShadow(velocityText, posX, posY, 0xFFFFFF);
        }
    }
}