package cafe.kagu.kagu.eventBus;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cafe.kagu.kagu.eventBus.impl.EventCheatTick;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.utils.DrawUtils3D;
import net.minecraft.client.Minecraft;

/**
 * @author lavaflowglow
 *
 */
public class EventBus {
	
	private static Map<Handler<? extends Event>, Class<? extends Event>> subscribers = new HashMap<Handler<? extends Event>, Class<? extends Event>>();
	private static Map<Handler<? extends Event>, Module> moduleSubscribers = new HashMap<Handler<? extends Event>, Module>();
	private static Logger logger = LogManager.getLogger();
	
	/**
	 * Starts the event manager, this class is used to push events to subscribers
	 */
	public static void start() {
		setSubscriber(new EventBus(), true);
	}
	
	/**
	 * Distributes the event to all the subscribers
	 * 
	 * @param e The event that's being distributed
	 */
	public static void distribute(Event e) {
		// Don't send events if the world or player is null
		Minecraft mc = Minecraft.getMinecraft();
		if ((mc.theWorld == null || mc.thePlayer == null) && !(e instanceof EventCheatTick)) {
			return;
		}
		
		// Send the event
		for (Handler<? extends Event> subscriber : subscribers.keySet()) {
			
			if (!e.getClass().isAssignableFrom(subscribers.get(subscriber))) {
				continue;
			}
			
			// If subscriber is linked to module AND the module is disabled then cancel
			if (moduleSubscribers.containsKey(subscriber) && moduleSubscribers.get(subscriber).isDisabled()) {
				continue;
			}
			
			// Send event
			try {
				subscriber.onEventNoGeneric(e);
			} catch (Exception e2) {
				logger.error("Had an issue dispatching an event", e2);
			}
			
		}
		
	}
	
	/**
	 * Finds all event subscribers in an object
	 * @param obj The object to check
	 * @param subscribed True if the subscribers that the object contains should be added to the event bus, false if they should be unsubscribed
	 */
	@SuppressWarnings("unchecked")
	public static void setSubscriber(Object obj, boolean subscribed){
		
		// Search through all fields and find ones with the @EventHandler annotation
		for (Field field : obj.getClass().getDeclaredFields()) {
			
			// If field is null than check next field
			if (field == null) {
				continue;
			}
			
			// If the field has the @EventHandler annotation than register it
			EventHandler[] events = field.getDeclaredAnnotationsByType(EventHandler.class);
			if (events.length == 0) {
				continue;
			}
			field.setAccessible(true);
			
			try {
				
				// Check if the field is a subscriber object
				if (!(field.get(obj) instanceof Handler<?>)) {
					continue;
				}
				
				// Get the event it's listening for
				Type type = ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
				Class<?> eventClass = (Class<?>) Class.forName(type.getTypeName());
				
				// Check if the event class is an instance of event
				if (!Event.class.isAssignableFrom(eventClass)) {
					continue;
				}
				
				// Subscribe or unsubscribe the subscriber
				if (subscribed) {
					subscribers.putIfAbsent((Handler<?>) field.get(obj), (Class<? extends Event>) eventClass);
					
					// If module then map the subscriber to the module
					if (obj instanceof Module) {
						moduleSubscribers.putIfAbsent((Handler<?>) field.get(obj), (Module) obj);
					}
				}else {
					subscribers.remove((Handler<?>) field.get(obj), eventClass);
					
					// If module then remove map the subscriber to the module
					if (obj instanceof Module) {
						moduleSubscribers.remove((Handler<?>) field.get(obj), (Module) obj);
					}
				}
				
			} catch (Exception e) {
				logger.error("Failed to register event listener " + obj.getClass().getName().replace(".", "/") + field.getName(), e);
			}
			
		}
		
	}
	
}
