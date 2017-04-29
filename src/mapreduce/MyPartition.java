package mapreduce;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @category 自定义分区 partition
 * @author huangyueran
 *
 */
public class MyPartition extends Partitioner<Weather, Text> {

	@Override
	public int getPartition(Weather key, Text value, int num) {
		return (key.getYear() * 127) % num;
	}

}
