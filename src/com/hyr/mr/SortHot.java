package com.hyr.mr;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * @category 自定义温度排序
 * @author huangyueran
 *
 */
public class SortHot extends WritableComparator {

	protected SortHot() {
		super(Weather.class, true);
	}

	/**
	 * 比较两个对象
	 */
	@Override
	public int compare(WritableComparable key1, WritableComparable key2) {
		Weather o1 = (Weather) key1;
		Weather o2 = (Weather) key2;
		int res = Integer.compare(o1.getYear(), o2.getYear());
		if (res != 0) { // 年份不相等
			return res;
		}

		// 年份相等 进行温度比较
		return -Integer.compare(o1.getHot(), o2.getHot()); // 降序排序
	}

}
