import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	// --------------ファイル読み込み用--------------
	static File arff_file = new File("./traffic_log/slide_30_window_120.txt");
	static ArrayList<String[]> arff_file_list = new ArrayList<String[]>();
	static String[][] arff_data;
	// クラスタ0のデータの平均と分散
	static ArrayList<Long> cluster0_data_ave = new ArrayList<Long>();
	static ArrayList<Double> cluster0_data_var = new ArrayList<Double>();
	// クラスタ1のデータの平均と分散
	static ArrayList<Long> cluster1_data_ave = new ArrayList<Long>();
	static ArrayList<Double> cluster1_data_var = new ArrayList<Double>();
	// クラスタ2のデータの平均と分散
	static ArrayList<Long> cluster2_data_ave = new ArrayList<Long>();
	static ArrayList<Double> cluster2_data_var = new ArrayList<Double>();

	static long data_ave;
	static double data_var;

	// メインメソッド
	public static void main(String args[]) {
		callFile();
		DivideCluster(arff_data);
		Average(cluster0_data_ave);
		Average(cluster1_data_ave);
		Average(cluster2_data_ave);
		Variance(cluster0_data_var);
		Variance(cluster1_data_var);
		Variance(cluster2_data_var);
	}

	// ファイル読み込み
	public static void callFile() {
		try {
			// ファイル読み込み
			BufferedReader br = new BufferedReader(new FileReader(arff_file));
			String line = br.readLine();
			for (int row = 0; line != null; row++) {
				arff_file_list.add(line.split(",", 0));
				line = br.readLine();
			}
			br.close();
			arff_data = new String[arff_file_list.size()][];
			// arrayListから二次元配列へ
			for (int row = 0; row < arff_file_list.size(); row++) {
				arff_data[row] = arff_file_list.get(row);
				for (int col = 0; col < 4; col++) {
					// System.out.println(arff_data[row][col]);
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	// クラスたごとに平均、分散を分ける
	public static void DivideCluster(String[][] data) {
		ArrayList<String> line_data = new ArrayList<String>();
		int cluster_num;
		Pattern pattern;
		Matcher matcher;
		for (int i = 0; i < arff_file_list.size(); i++) {
			cluster_num = Integer.parseInt(data[i][3].replace("cluster", ""));
//			System.out.println(cluster_num);
			if (cluster_num == 0) {
				cluster0_data_ave.add(Long.parseLong(data[i][1]));
				cluster0_data_var.add(Double.parseDouble(data[i][2]));
			} else if (cluster_num == 1) {
				cluster1_data_ave.add(Long.parseLong(data[i][1]));
				cluster1_data_var.add(Double.parseDouble(data[i][2]));
			} else if (cluster_num == 2) {
				cluster2_data_ave.add(Long.parseLong(data[i][1]));
				cluster2_data_var.add(Double.parseDouble(data[i][2]));
			}
		}
	}

	public static void Average(ArrayList<Long> list) {
		long ave;
		long sum = 0;
		if (list == null || list.size() == 0) {
//			System.exit(0);
		} else {
			for (int i = 0; i < list.size(); i++) {
				sum += list.get(i);
			}
			ave = sum / list.size();
			System.out.println(ave);
		}
	};

	public static void Variance(ArrayList<Double> list) {
		double ave;
		double sum = 0;
		if (list == null || list.size() == 0) {
//			System.exit(0);
		} else {
			for (int i = 0; i < list.size(); i++) {
				sum += list.get(i);
			}
			ave = sum / list.size();
			System.out.println(ave);
		}
	}

	

}
