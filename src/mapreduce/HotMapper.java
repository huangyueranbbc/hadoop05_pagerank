package mapreduce;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @category 自定义mapper
 * @author huangyueran
 *
 */
public class HotMapper extends Mapper<LongWritable, Text, Weather, Text> {

	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Weather, Text>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String[] ss = line.split("\t");

		if (ss.length == 2) {
			try {
				Date date = format.parse(ss[0]);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				int year = calendar.get(1);
				String hot = ss[1].substring(0, ss[1].indexOf("℃"));

				System.out.println(year + "===============" + hot);
				// 输出
				Weather keyPari = new Weather();
				keyPari.setYear(year);
				keyPari.setHot(Integer.parseInt(hot));

				context.write(keyPari, value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
