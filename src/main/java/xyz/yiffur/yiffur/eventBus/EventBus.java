package xyz.yiffur.yiffur.eventBus;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import xyz.yiffur.yiffur.mods.Module;

/**
 * @author lavaflowglow
 *
 */
public class EventBus {
	
	private static Map<Subscriber<? extends Event>, Class<? extends Event>> subscribers = new HashMap<Subscriber<? extends Event>, Class<? extends Event>>();
	private static Map<Subscriber<? extends Event>, Module> moduleSubscribers = new HashMap<Subscriber<? extends Event>, Module>();
	private static Logger logger = LogManager.getLogger();
	
	/**
	 * Starts the event manager, this class is used to push events to subscribers
	 */
	public static void start() {
		setSubscriber(new EventBus(), true);
		distribute(null);
	}
	
	/**
	 * Distributes the event to all the subscribers
	 * 
	 * @param e The event that's being distributed
	 */
	public static void distribute(Event e) {
		
		// Don't send events if the world or player is null
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.theWorld == null || mc.thePlayer == null) {
			return;
		}
		
		// Send the event
		for (Subscriber<? extends Event> subscriber : subscribers.keySet()) {
			if (e.getClass().isAssignableFrom(subscribers.get(subscriber))) {
				
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
		
	}
	
	/**
	 * Finds all event subscribers in an object
	 * 
	 * @param obj The object to check
	 */
	@SuppressWarnings("unchecked")
	public static void setSubscriber(Object obj, boolean subscribed){
		
		// Search through all fields and find ones with the @YiffEvents annotation
		for (Field field : obj.getClass().getDeclaredFields()) {
			
			// If field is null than check next field
			if (field == null) {
				continue;
			}
			
			// If the field has the @YiffEvents annotation than register it
			YiffEvents[] events = field.getDeclaredAnnotationsByType(YiffEvents.class);
			if (events.length == 0) {
				continue;
			}
			field.setAccessible(true);
			
			try {
				
				// Check if the field is a subscriber object
				if (!(field.get(obj) instanceof Subscriber<?>)) {
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
					subscribers.putIfAbsent((Subscriber<?>) field.get(obj), (Class<? extends Event>) eventClass);
					
					// If module then map the subscriber to the module
					if (obj instanceof Module) {
						moduleSubscribers.putIfAbsent((Subscriber<?>) field.get(obj), (Module) obj);
					}
				}else {
					subscribers.remove((Subscriber<?>) field.get(obj), eventClass);
					
					// If module then remove map the subscriber to the module
					if (obj instanceof Module) {
						moduleSubscribers.remove((Subscriber<?>) field.get(obj), (Module) obj);
					}
				}
				
			} catch (Exception e) {
				logger.error("Failed to register event listener " + obj.getClass().getName().replace(".", "/") + field.getName(), e);
			}
			
		}
		
	}
	
}
