package com.hyr.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Run {
	public static void main(String[] args) {
		Configuration conf = new Configuration();
		// conf.set("fs.default.name", "hdfs://slave1:8020");
		conf.set("mapred.jar", "H:\\jar\\weather_lib.jar");

		try {
			FileSystem fs = FileSystem.get(conf);
			Job job = Job.getInstance();

			job.setJobName("hot");
			job.setMapperClass(HotMapper.class);
			job.setReducerClass(HotReduce.class);
			job.setMapOutputKeyClass(Weather.class);
			job.setMapOutputValueClass(Text.class);

			job.setNumReduceTasks(3); // 设置Reduce任务个数
			job.setPartitionerClass(MyPartition.class);// 设置自定义的分区策略,按年份分区,每个区对应一个ReduceTask
			job.setSortComparatorClass(SortHot.class); // 设置排序策略
			job.setGroupingComparatorClass(GroupHot.class); // 设置比较分组策略

			FileInputFormat.setInputPaths(job, "/test2");
			FileOutputFormat.setOutputPath(job, new Path("/test2/ouput"));

			if (fs.exists(new Path("/test2/ouput"))) {
				fs.delete(new Path("/test2/ouput"), true);
			}

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
