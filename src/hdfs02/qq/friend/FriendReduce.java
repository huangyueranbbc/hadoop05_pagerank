package hdfs02.qq.friend;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @category 自定义二度好友Reduce
 * @author huangyueran
 *
 */
public class FriendReduce extends Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text key, Iterable<Text> iterable, Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		Set<String> set = new HashSet<String>();
		for (Text t : iterable) {
			set.add(t.toString());
		}

		if (set.size() > 1) {
			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				String name = (String) iterator.next(); // 好友名字

				for (Iterator k = set.iterator(); k.hasNext();) {
					String other = (String) k.next();
					if (!name.equals(other)) { // 有关系
						context.write(new Text(name), new Text(other));
					}
				}

			}
		}

	}

}
