package cafe.kagu.kagu.mods.impl.ghost;

import cafe.kagu.kagu.Kagu;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventSettingUpdate;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.mods.impl.combat.ModKillAura;
import cafe.kagu.kagu.mods.impl.player.ModAntiBot;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.DoubleSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class ModTriggerBot extends Module {

    public ModTriggerBot() {
        super("TriggerBot", Category.GHOST);
        setSettings(minAps, maxAps, swingAtMissedShots, swingAtMissedShotsLateReactionTime);
        Kagu.getEventBus().subscribe(new ModTriggerBot.ApsMinMaxFixer(this));
        setAps();
        try {
            robot = new Robot();
        } catch (AWTException e) {
            logger.error("Something went wrong while creating robot for trigger bot", e);
            throw new RuntimeException(e);
        }
    }

    private final Logger logger = LogManager.getLogger();
    private DoubleSetting minAps = new DoubleSetting("Min APS", 12, 1, 20, 0.5);
    private DoubleSetting maxAps = new DoubleSetting("Max APS", 15, 1, 20, 0.5);
    private BooleanSetting swingAtMissedShots = new BooleanSetting("Swing at missed shots", true);
    private IntegerSetting swingAtMissedShotsLateReactionTime = new IntegerSetting("Missed shots max reaction time",
            250, 50, 500, 25).setDependency(swingAtMissedShots::isEnabled);

    private double currentAps = 14;
    private TimerUtil apsTimer = new TimerUtil(), missedShotReactionTimer = new TimerUtil();
    private Robot robot;
    private void setAps(){
        this.currentAps = ThreadLocalRandom.current().nextDouble(minAps.getValue(), maxAps.getValue());
        setInfo(new DecimalFormat("0.00").format(this.currentAps) + " APS");
    }

    private void click(){
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        setAps();
    }

    @EventHandler
    private Handler<EventTick> onTick = evt -> {
        if (evt.isPost() || !Minecraft.getMinecraft().inGameHasFocus)
            return;

        boolean shouldSwing = apsTimer.hasTimeElapsed((long)(1000d / currentAps));

        Entity target = mc.pointedEntity;
        if (target == null){
            if (swingAtMissedShots.isEnabled() && shouldSwing
                    && !missedShotReactionTimer.hasTimeElapsed(swingAtMissedShotsLateReactionTime.getValue())){
                click();
                apsTimer.reset();
            }
            return;
        }
        missedShotReactionTimer.reset();

        ModAntiBot modAntiBot = Kagu.getModuleManager().getModule(ModAntiBot.class);
        if (target instanceof EntityPlayer && modAntiBot.isEnabled() && modAntiBot.isBot((EntityPlayer) target)){
            return;
        }

        if (shouldSwing){
            click();
        }
    };

    private static class ApsMinMaxFixer{

        public ApsMinMaxFixer(ModTriggerBot modTriggerBot) {
            this.modTriggerBot = modTriggerBot;
        }

        private ModTriggerBot modTriggerBot;

        /**
         * Used to make sure the min aps isn't greater than the max aps and vice versa
         */
        @EventHandler
        private Handler<EventSettingUpdate> onSettingUpdate = e -> {

            if (e.getSetting() == modTriggerBot.minAps) {
                if (modTriggerBot.minAps.getValue() > modTriggerBot.maxAps.getValue())
                    modTriggerBot.maxAps.setValue(modTriggerBot.minAps.getValue());
                if (modTriggerBot.currentAps < modTriggerBot.minAps.getValue())
                    modTriggerBot.setAps();
            }
            else if (e.getSetting() == modTriggerBot.maxAps) {
                if (modTriggerBot.maxAps.getValue() < modTriggerBot.minAps.getValue())
                    modTriggerBot.minAps.setValue(modTriggerBot.maxAps.getValue());
                if (modTriggerBot.currentAps > modTriggerBot.maxAps.getValue())
                    modTriggerBot.setAps();
            }

        };

    }

}
