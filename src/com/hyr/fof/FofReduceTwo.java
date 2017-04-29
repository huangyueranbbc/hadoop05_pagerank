package com.hyr.fof;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @category 进行分组，并按照亲密度排序
 * @author huangyueran
 *
 */
public class FofReduceTwo extends Reducer<Friend, IntWritable, Text, NullWritable> {

	@Override
	protected void reduce(Friend friend, Iterable<IntWritable> iterable,
			Reducer<Friend, IntWritable, Text, NullWritable>.Context context) throws IOException, InterruptedException {
		for (IntWritable i : iterable) {
			String msg = friend.getFriend1() + "-" + friend.getFriend2() + ":" + i.get(); 
			context.write(new Text(msg), NullWritable.get());
		}
	}

}
