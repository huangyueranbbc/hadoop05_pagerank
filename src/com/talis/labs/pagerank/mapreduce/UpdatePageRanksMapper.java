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
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class UpdatePageRanksMapper extends Mapper<LongWritable, Text, Text, Text> {

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
		//
		StringTokenizer st = new StringTokenizer(value.toString());
		
		String page = st.nextToken(); // 当前page
		double pagerank = Double.parseDouble(st.nextToken()); // 当前PR值
		st.nextToken(); // previous pagerank 上一次PR值
		
		// 将入链的page装入集合links
		if (st.hasMoreTokens()) {
			ArrayList<String> links = new ArrayList<String>();
			while (st.hasMoreTokens()) {
				links.add(st.nextToken());
			}

			StringBuffer sb = new StringBuffer().append("pages\t").append(pagerank).append("\t");
			for (String link : links) {
				context.write(new Text(link), new Text("pagerank\t" + (pagerank / links.size()))); // 计算每一个入链的PR值
				sb.append(link).append("\t");
			}			
			context.write(new Text(page), new Text(sb.toString()));
		} else {
			context.write(new Text(page), new Text("pages\t" + pagerank));
		}
	}

}
