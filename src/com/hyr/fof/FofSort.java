package com.hyr.fof;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * @category 亲密度降序排序
 * @author huangyueran
 *
 */
public class FofSort extends WritableComparator {

	public FofSort() {
		super(Friend.class, true);
	}

	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		Friend f1 = (Friend) a;
		Friend f2 = (Friend) b;

		// 比较亲密度 按照亲密度 进行排序
		// 1.首先比较Friend1好友是否一致
		int c = f1.getFriend1().compareTo(f2.getFriend1());

		// 如果相等 则进行排序
		if (c == 0) {
			return -Integer.compare(f1.getHot(), f2.getHot()); // 亲密度降序排序
		}

		return c;
	}

}
