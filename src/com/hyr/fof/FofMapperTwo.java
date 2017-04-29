package com.hyr.fof;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

/**
 * @category 按照亲密度进行排序输出
 * @author huangyueran
 *
 */
public class FofMapperTwo extends Mapper<LongWritable, Text, Friend, IntWritable> {

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Friend, IntWritable>.Context context)
			throws IOException, InterruptedException {
		// world-tom-2
		String[] strs = StringUtils.split(value.toString(), '-');

		Friend f1 = new Friend();
		f1.setFriend1(strs[0]);
		f1.setFriend2(strs[1]);
		f1.setHot(Integer.parseInt(strs[2]));

		context.write(f1, new IntWritable(f1.getHot()));

		// 颠倒好友关系
		Friend f2 = new Friend();
		f2.setFriend1(strs[1]);
		f2.setFriend2(strs[0]);
		f2.setHot(Integer.parseInt(strs[2]));

		context.write(f2, new IntWritable(f2.getHot()));

	}

}
