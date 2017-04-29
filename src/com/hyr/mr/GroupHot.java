package com.hyr.mr;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * @category 自定义按照年份分组
 * @author huangyueran
 *
 */
public class GroupHot extends WritableComparator {

	protected GroupHot() {
		super(Weather.class, true);
	}

	/**
	 * 比较两个对象
	 */
	@Override
	public int compare(WritableComparable key1, WritableComparable key2) {
		Weather keyPariA = (Weather) key1;
		Weather keyPariB = (Weather) key2;
		return Integer.compare(keyPariA.getYear(), keyPariB.getYear());
	}

}
