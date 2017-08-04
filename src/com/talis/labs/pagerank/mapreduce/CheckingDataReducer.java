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
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CheckingDataReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		Set<String> links = new TreeSet<String>() ;
		for (Text value : values) {
			if (!value.equals(CheckingDataMapper.NONE)) { // 如果不是悬挂的页面 才添加到入链集合中
				links.add(value.toString()) ;
			}			
		}
		StringBuffer sb = new StringBuffer() ;
		for (String link : links) {
			sb.append(link).append("\t") ;
		}
		context.write(key, new Text(sb.toString())) ;
		// 得到去重和去掉自己投给自己的票数的结果
//		1	2	
//		10	5	
//		11	
//		2	11	3	5	7	9	
//		3	6	
//		4	
//		5	1	11	2	3	
//		6	
//		7	
//		8	10	
//		9	
//		

	}

}