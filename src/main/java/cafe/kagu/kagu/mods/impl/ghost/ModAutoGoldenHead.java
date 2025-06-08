package cafe.kagu.kagu.mods.impl.ghost;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.settings.impl.LabelSetting;
import cafe.kagu.kagu.utils.InventoryUtils;
import cafe.kagu.kagu.utils.TimerUtil;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class ModAutoGoldenHead extends Module {

    public ModAutoGoldenHead() {
        super("AutoGoldenHead", Category.GHOST);
        setSettings(infoLabel, delayAfterEatSetting, eatAtPercentSetting);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            logger.error("Something went wrong while creating robot for trigger bot", e);
            throw new RuntimeException(e);
        }
    }

    private final Logger logger = LogManager.getLogger();

    private LabelSetting infoLabel = new LabelSetting("Automatically eats golden heads so you don't die");
    private IntegerSetting delayAfterEatSetting = new IntegerSetting("Min delay after eat", 750, 0, 3000, 50);
    private IntegerSetting eatAtPercentSetting = new IntegerSetting("Percent to eat", 50, 1, 99, 1);

    private TimerUtil delayAfterEatingTimer = new TimerUtil();
    private boolean isEating = false;
    private boolean switchingBack = false;
    private int previouslyHeldSlot = 0;
    private Robot robot;

    @Override
    public void onEnable() {
        isEating = false;
        switchingBack = false;
    }

    @EventHandler
    private Handler<EventTick> onTick = e -> {
        if (e.isPost())
            return;

        if (switchingBack){
            mc.thePlayer.inventory.currentItem = previouslyHeldSlot;
            switchingBack = false;
            return;
        }

        Slot heldItemSlot = mc.thePlayer.inventoryContainer.getSlot(mc.thePlayer.inventory.currentItem + 36);
        if (isEating && heldItemSlot.getHasStack() && isGoldenHead(heldItemSlot.getStack())){
            robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
            delayAfterEatingTimer.reset();
            switchingBack = mc.thePlayer.inventory.currentItem != previouslyHeldSlot;
            isEating = false;
            return;
        }

        boolean shouldEat = (mc.thePlayer.getHealth() / mc.thePlayer.getMaxHealth()) * 100 <= eatAtPercentSetting.getValue();
        if (!shouldEat || !delayAfterEatingTimer.hasTimeElapsed(delayAfterEatSetting.getValue()))
            return;

        for (int i = 36; i < 45; i++) {

            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (isGoldenHead(itemStack)) {
                    previouslyHeldSlot = mc.thePlayer.inventory.currentItem;
                    isEating = true;
                    mc.thePlayer.inventory.currentItem = i - 36;
                }

            }

        }
    };

    private boolean isGoldenHead(ItemStack itemStack){
        return itemStack.getItem() instanceof ItemSkull
                && itemStack.getDisplayName().equalsIgnoreCase("§6Golden Head");
    }

}
