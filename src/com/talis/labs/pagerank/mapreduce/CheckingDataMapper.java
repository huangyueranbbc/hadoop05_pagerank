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
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CheckingDataMapper extends Mapper<LongWritable, Text, Text, Text> {

	public static final Text NONE = new Text("dangling") ;
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		//		1	1	2	2	
		//		2	3	5	7	9	11
		//		3	3	3   6
		//		4	
		//		5   1	2	3	11
		//		8   10
		//		10  5
		//  一行一行的读取数据 将每行数据转为StringTokenizer对象
		StringTokenizer st = new StringTokenizer(value.toString()) ; // StringTokenizer是一个用来分隔String的应用类，相当于VB的split函数。
		// 构造一个用来解析str的StringTokenizer对象。java默认的分隔符是“空格”、“制表符(‘\t’)”、“换行符(‘\n’)”、“回车符(‘\r’)”。

		boolean first = true ;
		String page = null ;
		Set<String> links = new HashSet<String>() ; // 当前page的入链值
		while (st.hasMoreTokens()) {  //返回是否还有分隔符。
			if (first) { // 如果是第一个数 也就是当前page值
				page = st.nextToken() ; // 返回从当前位置到下一个分隔符的字符串。 获取当前page值 //5  1	2	3	11 也就是获取5
				context.write(new Text(page), NONE) ;
				first = false ;
			} else { // 否则是当前page的入链page值
				String token = st.nextToken() ;
				// this is to remove self-references and duplicates 这是删除自引用和重复。
				if ( (links.add(token)) && (!page.equals(token)) ) { // 去除自己投给自己的无效票数和重复的入链
					context.write(new Text(page), new Text(token)) ;
					context.write(new Text(token), NONE) ; // 这是用于暴露孤立页面。		
				}
			}
		}
	}

}