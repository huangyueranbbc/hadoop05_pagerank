package hdfs02.qq.friend;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @category 自定义二度好友匹配Mapper
 * @author huangyueran
 *
 */
public class FriendMapper extends Mapper<LongWritable, Text, Text, Text> {

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString(); // 一组好友关系
		String[] ss = line.split("\t");

		context.write(new Text(ss[0]), new Text(ss[1]));
		context.write(new Text(ss[1]), new Text(ss[0]));
	}

}
