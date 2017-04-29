package com.hyr.mr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * @category Key的封装对象 weather
 * @author huangyueran
 *
 */
public class Weather implements WritableComparable<Weather> {

	private int year;
	private int hot;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// 反序列化
		this.year = in.readInt();
		this.hot = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// 序列化
		out.writeInt(year);
		out.writeInt(hot);
	}

	@Override
	public int compareTo(Weather other) {
		int res = Integer.compare(year, other.getYear());
		if (res != 0) {
			return res;
		}
		return Integer.compare(hot, other.getHot());
	}

	@Override
	public String toString() {
		return year + "\t" + hot;
	}

	@Override
	public int hashCode() {
		return new Integer(year + hot).hashCode();
	}

}
