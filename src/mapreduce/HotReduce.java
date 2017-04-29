package mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @category 自定义reduce
 * @author huangyueran
 *
 */
public class HotReduce extends Reducer<Weather, Text, Weather, Text> {

	@Override
	protected void reduce(Weather key, Iterable<Text> iterable, Reducer<Weather, Text, Weather, Text>.Context context)
			throws IOException, InterruptedException {
		for (Text v : iterable) {
			context.write(key, v);
		}
	}

}
