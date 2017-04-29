package com.hyr.fof;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @category 拿到真实的二度关系(去掉已知关系) 并统计亲密度
 * @author huangyueran
 *
 */
public class FofJobTwo {

	public static void main(String[] args) {
		try {
			Configuration conf = new Configuration();
			// conf.set("fs.default.name", "hdfs://slave1:8020");
			conf.set("mapred.jar", "H:\\jar\\fof.jar");

			FileSystem fs = FileSystem.get(conf);
			Job job = Job.getInstance(conf);

			job.setJarByClass(FofJobTwo.class);                          

			job.setMapperClass(FofMapperTwo.class);
			job.setReducerClass(FofReduceTwo.class);

			job.setMapOutputKeyClass(Friend.class);
			job.setMapOutputValueClass(IntWritable.class);

			// job.setNumReduceTasks(3); // 设置Reduce任务个数
			// job.setPartitionerClass(MyPartition.class);
			// 设置自定义的分区策略,按年份分区,每个区对应一个ReduceTask
			job.setSortComparatorClass(FofSort.class); // 设置排序策略
			job.setGroupingComparatorClass(FofGroup.class); // 设置比较分组策略

			FileInputFormat.setInputPaths(job, "/fof/output");

			if (fs.exists(new Path("/fof/output2"))) {
				fs.delete(new Path("/fof/output2"), true);
			}

			FileOutputFormat.setOutputPath(job, new Path("/fof/output2"));

			job.waitForCompletion(true); // 是否等待完成

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
