/*
 * Copyright © 2011 Talis Systems Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.talis.labs.pagerank.mapreduce;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @category 检查并格式数据,去重和去自引用
 * @author huangyueran
 *
 */
public class CheckingData extends Configured implements Tool {

	public static final Log logger = LogFactory.getLog(CheckingData.class);
	
	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: CheckingData <input path> <output path>");
			return -1;
		}

		FileSystem.get(getConf()).delete (new Path(args[1]), true);

		Job job = new Job(getConf(), "CheckingData");
		job.setJarByClass(getClass());

		FileInputFormat.addInputPath(job, new Path(args[0])); // 输入测试数据集 的路径
		FileOutputFormat.setOutputPath(job, new Path(args[1])); // 计算结果的输出路径

		job.setMapperClass(CheckingDataMapper.class);
		job.setReducerClass(CheckingDataReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new Configuration(), new CheckingData(), args);
		System.exit(exitCode);
	}
	
}
