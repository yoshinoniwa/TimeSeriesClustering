import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClusterCalculator {
	// クラスタ1のデータの平均と分散
	static ArrayList<Long> cluster0_data_ave = new ArrayList<Long>();
	static ArrayList<Double> cluster0_data_var = new ArrayList<Double>();
	// クラスタ1のデータの平均と分散
	static ArrayList<Long> cluster1_data_ave = new ArrayList<Long>();
	static ArrayList<Double> cluster1_data_var = new ArrayList<Double>();
	// クラスタ1のデータの平均と分散
	static ArrayList<Long> cluster2_data_ave = new ArrayList<Long>();
	static ArrayList<Double> cluster2_data_var = new ArrayList<Double>();
	// クラスタ1のデータの平均と分散
	static ArrayList<Long> cluster3_data_ave = new ArrayList<Long>();
	static ArrayList<Double> cluster3_data_var = new ArrayList<Double>();
	
	static final String file_day = "16-09-30";
	static final String file_name = "./traffic_log/"+file_day+".csv";
	public static void CallFile(){
		
	}
	public static void DivideCluster(String[][] data, ArrayList list) {
		ArrayList<String> line_data = new ArrayList<String>();
		int cluster_num;
		Pattern pattern;
		Matcher matcher;
		for (int i = 0; i < list.size(); i++) {
			cluster_num = Integer.parseInt(data[i][3].replace("cluster", ""));
			// System.out.println(cluster_num);
			if (cluster_num == 0) {
				cluster0_data_ave.add(Long.parseLong(data[i][1]));
				cluster0_data_var.add(Double.parseDouble(data[i][2]));
			} else if (cluster_num == 1) {
				cluster1_data_ave.add(Long.parseLong(data[i][1]));
				cluster1_data_var.add(Double.parseDouble(data[i][2]));
			} else if (cluster_num == 2) {
				cluster2_data_ave.add(Long.parseLong(data[i][1]));
				cluster2_data_var.add(Double.parseDouble(data[i][2]));
			}else if (cluster_num == 3) {
				cluster3_data_ave.add(Long.parseLong(data[i][1]));
				cluster3_data_var.add(Double.parseDouble(data[i][2]));
			}
		}
	}
}
