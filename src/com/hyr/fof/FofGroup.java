package com.hyr.fof;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class FofGroup extends WritableComparator {

	// 必须重写 并传入true
	protected FofGroup() {
		super(Friend.class, true);
	}

	/**
	 * 按照Friend1进行分组
	 */
	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		Friend f1 = (Friend) a;
		Friend f2 = (Friend) b;

		int c = f1.getFriend1().compareTo(f2.getFriend1());

		return c;
	}

}
