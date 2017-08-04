求pagerank值  
基本步骤:  
	1.检查并格式数据 去重+去自引用  
	2.统计网页总数  
	3.初始化每个网页的默认初始PR值= 1.0 / count  
	4.计算/更新所有悬挂网页的PR值之和=dangling值  
	5.更新网页的PR值 pagerank = d*(pagerank) + d*dangling/count + (1-d)/count  
	6. 检查是否收敛，如果收敛则停止循环。current_pagerank - previous_pagerank  

执行命令:hadoop jar /root/pagerank_file/pagerank-hdeploy.jar com.talis.labs.pagerank.mapreduce.PageRank /user/castagna/src/test/resources/test.dat /user/castagna/result 30 0.00001  

 
  hadoop fs -mkdir hdfs://localhost/user/castagna/src/  
  hadoop fs -mkdir hdfs://localhost/user/castagna/src/test  
  hadoop fs -mkdir hdfs://localhost/user/castagna/src/test/resources  
  hadoop fs -copyFromLocal src/test/resources/* hdfs://localhost/user/castagna/src/test/resources/  
  hadoop fs -rmr hdfs://localhost/user/castagna/target/  
  mvn hadoop:pack; hadoop --config conf/hadoop-local.xml jar ./target/hadoop-deploy/pagerank-hdeploy.jar com.talis.labs.pagerank.mapreduce.PageRank ./src/test/resources/datasets/small/ ./target/output 30 0.00001  
  
Find PageRank value
Basic steps:
1. check and format data to weight + go to self reference
2. statistics page total
3. initializes the default initial PR value of each page = 1 / count
4. calculate / update the value of the PR value and the =dangling value of all hanging pages
5. update the PR value of the page, PageRank = d* (PageRank) + d*dangling/count + (1-D) /count
6. check whether the convergence, if convergence, then stop the cycle. Current_pagerank - previous_pagerank