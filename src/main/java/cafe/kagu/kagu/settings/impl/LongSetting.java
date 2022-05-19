/**
 * 
 */
package cafe.kagu.kagu.settings.impl;

import cafe.kagu.kagu.eventBus.impl.EventSettingUpdate;
import cafe.kagu.kagu.settings.Setting;

/**
 * @author lavaflowglow
 *
 */
public class LongSetting extends Setting {

	/**
	 * @param dependsOn The setting that this setting depends on, can be null
	 * @param name      The name of the setting
	 * @param value     The initial value of the setting
	 * @param min       The min value of the setting
	 * @param max       The max value of the setting
	 * @param increment How much this setting should increment by
	 */
	public LongSetting(Setting dependsOn, String name, long value, long min, long max, long increment) {
		super(dependsOn, name);
		this.value = value;
		this.min = min;
		this.max = max;
		this.increment = increment;
	}
	
	/**
	 * @param name      The name of the setting
	 * @param value     The initial value of the setting
	 * @param min       The min value of the setting
	 * @param max       The max value of the setting
	 * @param increment How much this setting should increment by
	 */
	public LongSetting(String name, long value, long min, long max, long increment) {
		this(null, name, value, min, max, increment);
	}
	
	private long value, min, max, increment;

	/**
	 * @return the value
	 */
	public long getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(long value) {
		double precision = 1 / increment;
		this.value = (long)(Math.round(Math.max(min, Math.min(max, value)) * precision) / precision);
		
		// Kagu hook
		{
			EventSettingUpdate eventSettingUpdate = new EventSettingUpdate(this);
			eventSettingUpdate.post();
		}
	}

	/**
	 * @return the min
	 */
	public long getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(long min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public long getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(long max) {
		this.max = max;
	}

	/**
	 * @return the increment
	 */
	public long getIncrement() {
		return increment;
	}

	/**
	 * @param increment the increment to set
	 */
	public void setIncrement(long increment) {
		this.increment = increment;
	}

}
