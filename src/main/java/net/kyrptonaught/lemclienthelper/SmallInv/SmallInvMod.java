package net.kyrptonaught.lemclienthelper.SmallInv;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import oshi.util.tuples.Pair;

import java.util.HashMap;

public class SmallInvMod {

    public static String MOD_ID = "smallinv";
    public static HashMap<Integer, Pair<Integer, Integer>> SMALLINVSLOTS = new HashMap<>();

    public static KeyMapping closeSmallInvKey;

    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new SmallInvConfig());
        LEMClientHelperMod.configManager.load(MOD_ID);
        closeSmallInvKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(LEMClientHelperMod.MOD_ID + ".key.closesmallinv", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, "key.category." + LEMClientHelperMod.MOD_ID));

        registerSmallSlot(5, 55, 9);
        registerSmallSlot(6, 55, 27);
        registerSmallSlot(7, 55, 45);
        registerSmallSlot(8, 55, 63);
        registerSmallSlot(45, 134, 63);
        registerSmallSlot(36, 8, 99);
        registerSmallSlot(37, 26, 99);
        registerSmallSlot(38, 44, 99);
        registerSmallSlot(39, 62, 99);
        registerSmallSlot(40, 80, 99);
        registerSmallSlot(41, 98, 99);
        registerSmallSlot(42, 116, 99);
        registerSmallSlot(43, 134, 99);
        registerSmallSlot(44, 152, 99);
    }

    public static SmallInvConfig getConfig() {
        return (SmallInvConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }

    public static boolean isSmallInv(Player player) {
        //if (true) return true; // force enabling for testing
        if (!getConfig().enabled) return false;

        //give @p knowledge_book{display:{Name:'{"text":" "}'},SmallInv:1,CustomModelData:1}
        for (ItemStack itemStack : player.getInventory().items) {
            if (isSmallSlot(itemStack))
                return true;
        }
        return false;
    }

    public static void tryMoveSlot(MovableSlot slot) {
        if (SMALLINVSLOTS.containsKey(slot.index)) {
            Pair<Integer, Integer> pos = SMALLINVSLOTS.get(slot.index);
            slot.setPos(pos.getA(), pos.getB());
            slot.isEnabled = true;
        } else slot.isEnabled = false;
    }

    private static void registerSmallSlot(int id, int x, int y) {
        SMALLINVSLOTS.put(id, new Pair<>(x, y));
    }

    public static boolean isKeybindPressed(int pressedKeyCode, boolean isMouse) {
        InputConstants.Key keycode = KeyBindingHelper.getBoundKeyOf(closeSmallInvKey);

        if (isMouse) {
            if (keycode.getType() != InputConstants.Type.MOUSE) return false;
        } else {
            if (keycode.getType() != InputConstants.Type.KEYSYM) return false;
        }
        return keycode.getValue() == pressedKeyCode;
    }

    public static boolean isSmallSlot(ItemStack stack) {
        return stack.is(Items.KNOWLEDGE_BOOK) &&
                stack.has(DataComponents.CUSTOM_DATA) &&
                stack.get(DataComponents.CUSTOM_DATA).contains("SmallInv");
    }
}