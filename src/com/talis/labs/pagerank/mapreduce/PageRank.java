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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class PageRank extends Configured implements Tool {

	public static final int NUM_REPLICAS = 3 ;

	public static final String JAR_FILENAME = "build" + File.separator + "pagerank.jar" ;
	public static final int CHECK_CONVERGENCE_FREQUENCY = 10 ; // 检查收敛的频率
	public static final DecimalFormat formatter = new DecimalFormat("#.####################") ;

	private static final int HTML_TABLE_ROWS = 200 ;
	private static final boolean HTML_TABLE = false ;
	
	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 4) {
			System.err.println("Usage: PageRank <input path> <output path> <iterations> <tolerance>") ;
			return -1 ;
		}
		
		FileSystem fs = FileSystem.get(getConf()) ;
		String input = args[1] + File.separator + "previous-pageranks";
		String output = args[1] + File.separator + "current-pageranks" ;
		
		// 1.检查并格式数据 去重+去自引用
		ToolRunner.run(getConf(), new CheckingData(), new String[] { args[0], output } ) ;
		
		// 2. 统计网页总数
		ToolRunner.run(getConf(), new CountPages(), new String[] { output, args[1] + File.separator + "count" } ) ;
		String count = read (fs, args[1] + File.separator + "count") ; // 网页总数
		System.out.println("==========网页统计总数为:"+count+"==========");
		
		// 3.初始化每个网页的默认初始PR值= 1.0 / count
		ToolRunner.run(getConf(), new InitializePageRanks(), new String[] { output, input, count } ) ;
		
		int i = 0 ;
		//		/user/castagna/src/test/resources/test.dat /user/castagna/result 30 0.00001
		while ( i < Integer.parseInt(args[2]) ) { // 30
			// 4. 计算/更新所有悬挂网页的PR值之和=dangling值
			ToolRunner.run(getConf(), new DanglingPages(), new String[] { input, args[1] + File.separator + "dangling" } ) ; // 参1:/user/castagna/result/previous-pageranks  参2:/user/castagna/result/dangling
			String dangling = read (fs, args[1] + File.separator + "dangling") ;  // 孤立网页 只有入读没有出度
			
			// 5. 更新网页的PR值 pagerank = d*(pagerank) + d*dangling/count + (1-d)/count
			ToolRunner.run(getConf(), new UpdatePageRanks(), new String[] { input, output, count, dangling } ) ; // 参1:previous-pageranks  参2:current-pageranks
			swap ( fs, new Path(output),  new Path(input) ) ; // 释放目录 如果文件夹存在则删除 并改名
			if ( ( i > CHECK_CONVERGENCE_FREQUENCY) && ( i % CHECK_CONVERGENCE_FREQUENCY == 0 ) ) { // 根据检查收敛频率判断当前是否需要检查收敛
				// 6. 检查是否收敛，如果收敛则停止循环。current_pagerank - previous_pagerank
				ToolRunner.run(getConf(), new CheckConvergence(), new String[] { input, args[1] + File.separator + "convergence" } ) ;
				double tolerance = Double.parseDouble(read (fs, args[1] + File.separator + "convergence")) ;
				if ( tolerance <= Double.parseDouble(args[3]) ) { // 如果小于等于传入的误差值,则收敛。停止计算
					break ;
				}
			}
			
			if (HTML_TABLE) {
				ToolRunner.run(getConf(), new SortPageRanks(), new String[] { input, args[1] + File.separator + "sorted-pageranks" } ) ;
				BufferedReader in = new BufferedReader(new InputStreamReader(fs.open(new Path(args[1] + File.separator + "sorted-pageranks" + File.separator + "part-00000")))) ;
				PrintWriter out = new PrintWriter(fs.create(new Path(args[1] + File.separator + "sorted-pageranks-" + i + ".dat")).getWrappedStream()) ;
				for (int j = 0 ; j < HTML_TABLE_ROWS ; j++ ) {
					StringTokenizer st = new StringTokenizer(in.readLine()) ;
					out.write(st.nextToken() + "\n") ;
				}
				in.close() ;
				out.close() ;				
			}

			i++ ;
		}
			
		ToolRunner.run(getConf(), new SortPageRanks(), new String[] { input, args[1] + File.separator + "sorted-pageranks" } ) ;
		
		if (HTML_TABLE) {
			ToolRunner.run(getConf(), new HTMLTable(), new String[] { args[1], Integer.toString(HTML_TABLE_ROWS), Integer.toString(i) } ) ;			
		}

		return 0;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new Configuration(), new PageRank(), args);
		System.exit(exitCode);
	}
	
	private static String read (FileSystem fs, String path) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(fs.open(new Path(path + File.separator + "part-r-00000")))) ;
		StringTokenizer st = new StringTokenizer(in.readLine()) ;
		st.nextToken() ;
		String result = st.nextToken() ;
		in.close() ;
		
		return result ;
	}
	
	private static void swap (FileSystem fs, Path src, Path dst) throws IOException {
		fs.delete(dst, true) ;
		fs.rename(src,  dst) ;
	}

}
