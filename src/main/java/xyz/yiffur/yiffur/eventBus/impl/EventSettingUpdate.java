/**
 * 
 */
package xyz.yiffur.yiffur.eventBus.impl;

import xyz.yiffur.yiffur.eventBus.Event;
import xyz.yiffur.yiffur.settings.Setting;

/**
 * @author lavaflowglow
 *
 */
public class EventSettingUpdate extends Event {

	/**
	 * @param eventPosition
	 */
	public EventSettingUpdate(Setting setting) {
		super(EventPosition.POST);
		this.setting = setting;
	}

	private Setting setting;

	/**
	 * @return the setting
	 */
	public Setting getSetting() {
		return setting;
	}

}
