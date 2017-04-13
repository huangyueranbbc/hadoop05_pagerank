package hdfs02;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @category 自定义Mapper 相当于中间件 分解
 * @author huangyueran
 *
 */
public class WcMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	/**
	 * 每次调用map方法会传入split中一行数据
	 * 
	 * @key:改行数据所在文件的位置下表
	 * @value:这行数据
	 */
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();// 获取文本Text的文本数据
		StringTokenizer st = new StringTokenizer(line);

		while (st.hasMoreTokens()) {
			String world = st.nextToken();
			context.write(new Text(world), new IntWritable(1)); // map的输出
		}
	}

}
