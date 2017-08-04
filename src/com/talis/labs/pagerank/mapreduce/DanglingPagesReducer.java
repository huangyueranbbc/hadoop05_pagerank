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

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DanglingPagesReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

	@Override
	public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
		//		1	0.09090909090909091	0.09090909090909091	2	
		//		10	0.09090909090909091	0.09090909090909091	5	
		//		11	0.09090909090909091	0.09090909090909091	
		//		2	0.09090909090909091	0.09090909090909091	11	3	5	7	9	
		//		3	0.09090909090909091	0.09090909090909091	6	
		//		4	0.09090909090909091	0.09090909090909091	
		//		5	0.09090909090909091	0.09090909090909091	1	11	2	3	
		//		6	0.09090909090909091	0.09090909090909091	
		//		7	0.09090909090909091	0.09090909090909091	
		//		8	0.09090909090909091	0.09090909090909091	10	
		//		9	0.09090909090909091	0.09090909090909091	

		
		double sum = 0 ;
		
		for (DoubleWritable value : values) {
			sum += value.get() ; // 第一次计算时为0.09090909090909091*5。 也就是没有入链的PR值相加 计算所有悬挂网页的PR值之和
		}
		context.write(key, new DoubleWritable(sum)) ;
	}

}