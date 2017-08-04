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

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DanglingPagesMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {

	private Text KEY_NAME = new Text("dangling") ;
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		//		1	0.07561539146106866	0.07561540915709948	2	
		//		10	0.08440072378469027	0.08440158603935527	5	
		//		11	0.0993963664151863	0.09939602029485614	
		//		2	0.1398884892446032	0.1398880879653979	11	3	5	7	9	
		//		3	0.0993963664151863	0.09939602029485614	6	
		//		4	0.04562196207213878	0.04562207260300175	
		//		5	0.14114428515970842	0.14114555006555238	1	11	2	3	
		//		6	0.13010857932276648	0.13010781349536246	
		//		7	0.06940293702625643	0.0694026837407584	
		//		8	0.04562196207213878	0.04562207260300175	10	
		//		9	0.06940293702625643	0.0694026837407584	
		StringTokenizer st = new StringTokenizer(value.toString()) ; // 将每一行转为StringTokenizer对象
		
		if (st.hasMoreTokens()) {
			
			st.nextToken() ; // 当前网页
			if (st.hasMoreTokens()) {
				double pagerank = Double.parseDouble(st.nextToken()) ; // 当前计算的PR值
				st.nextToken() ; // previous pagerank 之前计算的PR值
				if (!st.hasMoreTokens()) { // 如果没有更多内容
					context.write(KEY_NAME, new DoubleWritable(pagerank)) ;
				}
			}
			
		}
	}
	
}