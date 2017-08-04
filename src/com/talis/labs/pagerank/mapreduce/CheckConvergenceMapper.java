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

public class CheckConvergenceMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {

	private Text KEY_NAME = new Text("tolerance") ;
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		StringTokenizer st = new StringTokenizer(value.toString()) ;
//		1	0.07561521284826986	0.07561521383830992	2	
//		10	0.08440094085679561	0.08440093972549581	5	
//		11	0.09939619777961779	0.0993961986521285	
//		2	0.1398881446108333	0.13988814665498786	11	3	5	7	9	
//		3	0.09939619777961779	0.0993961986521285	6	
//		4	0.04562213061830689	0.04562212969233969	
//		5	0.14114391431632625	0.14114391637629634	1	11	2	3	
//		6	0.1301088994726161	0.13010889770365736	
//		7	0.06940311554965482	0.06940311450615826	
//		8	0.04562213061830689	0.04562212969233969	10	
//		9	0.06940311554965482	0.06940311450615826	
		st.nextToken() ; // page
		double pagerank = Double.parseDouble(st.nextToken()) ; // 当前PR值
		double previous_pagerank = Double.parseDouble(st.nextToken()) ; // 上一次PR值
		
		context.write(KEY_NAME, new DoubleWritable(Math.abs(pagerank - previous_pagerank))) ;
	}

}