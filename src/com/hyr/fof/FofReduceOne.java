package com.hyr.fof;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @category 拿到真实的二度关系(去掉已知关系) 并统计亲密度
 * @author huangyueran
 *
 */
public class FofReduceOne extends Reducer<Text, IntWritable, Text, NullWritable> {

	@Override
	protected void reduce(Text text, Iterable<IntWritable> iterable,
			Reducer<Text, IntWritable, Text, NullWritable>.Context context) throws IOException, InterruptedException {
		int sum = 0; // 标识亲密度

		boolean flag = true;

		for (IntWritable i : iterable) {
			if (i.get() == 0) { // 已知好友关系 非二度好友
				flag = false;
				break;
			}
			sum += i.get(); // sum++
		}

		if (flag) {
			String msg = text.toString() + "-" + sum;
			context.write(new Text(msg), NullWritable.get());
		}

	}

}
