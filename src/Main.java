import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	// --------------ファイル読み込み用--------------
	static final String file_day = "16-09-30";
	static final String file_name = "./traffic_log/"+file_day+".csv";
	static File arff_file = new File(file_name);
	static ArrayList<String[]> arff_file_list = new ArrayList<String[]>();
	static String[][] arff_data;
	//データの平均と分散
	static ArrayList<Long> traffic_ave_list = new ArrayList<Long>();
	static ArrayList<Double> traffic_var_list = new ArrayList<Double>();
	// // クラスタ1のデータの平均と分散
	// static ArrayList<Long> cluster1_data_ave = new ArrayList<Long>();
	// static ArrayList<Double> cluster1_data_var = new ArrayList<Double>();
	// // クラスタ2のデータの平均と分散
	// static ArrayList<Long> cluster2_data_ave = new ArrayList<Long>();
	// static ArrayList<Double> cluster2_data_var = new ArrayList<Double>();

	// static long data_ave;
	// static double data_var;
	static String[] traffic_data_set;
	static ArrayList<String> traffic_data_list = new ArrayList<String>();
	static ArrayList<Long[]> traffic_sum_set = new ArrayList<Long[]>();
	static Long[][] sum_num;
	static ArrayList<Long> sum_list;
	static ArrayList<ArrayList> traffic_sum_num = new ArrayList<ArrayList>();

	// メインメソッド
	public static void main(String args[]) {
		callFile();
		setTrafficDataSet(arff_data);

		for (int i = 0; i < traffic_sum_num.size(); i++) {
			Average(traffic_sum_num.get(i));
			Variance(traffic_sum_num.get(i), traffic_ave_list.get(i));
//			System.out.println(i + " : " +traffic_sum_num.get(i));
		}
		System.out.println(traffic_ave_list.size());
		inputFile();
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
				for (int col = 0; col < 7; col++) {
					arff_data[row][col] = arff_data[row][col].replace("\"", "");
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void setTrafficDataSet(String[][] data) {
		// 文字比較
		String ip_address = data[2][2];
		String comparsion_str = ip_address + " is at";

		//
		traffic_data_set = new String[arff_file_list.size()];
		int count = 0;
		int t_count = 0;
		Long[] t_sum_num = new Long[arff_file_list.size()];
		for (int i = 0; i < arff_file_list.size(); i++) {
			if (arff_data[i][6].contains(comparsion_str)) {
				count++;
//				System.out.println(i + " : " +count);
			}

			if (count == 1) {
				t_sum_num[t_count] = Long.parseLong(arff_data[i][5]);
//				System.out.println(i+" : "+t_sum_num[t_count]);
				t_count++;
			} else if (count == 2) {
				traffic_sum_set.add(t_sum_num);
				// nullを消している
				sum_list = new ArrayList<Long>(Arrays.asList(t_sum_num));
				sum_list.removeAll(Collections.singleton(null));
				traffic_sum_num.add(sum_list);
//				System.out.println(i + " : " + arff_data[i][1]+ " : " + sum_list);
				count = 1;
				t_count = 0;
				t_sum_num = new Long[arff_file_list.size()];
			}
		}
	}

	// 平均を求めるメソッド
	public static void Average(ArrayList<Long> list) {
		long ave;
		long sum = 0;
		if (list == null || list.size() == 0) {
			// System.exit(0);
		} else {
			for (int i = 0; i < list.size(); i++) {
				sum += list.get(i);
			}
			ave = sum / list.size();
			traffic_ave_list.add(ave);
//			System.out.println(ave);
		}
	}

	// 分散を求めるメソッド
	public static void Variance(ArrayList<Long> list, long ave) {
		double var;
		double sum = 0;
		if (list == null || list.size() == 0) {
			// System.exit(0);
		} else {
			for (int i = 0; i < list.size(); i++) {
				sum += (double) (ave - list.get(i)) * (ave - list.get(i));
			}
			var = sum / list.size();
			traffic_var_list.add(var);
//			System.out.println(var);
		}
	}
	
	//ファイル作成
	public static void inputFile() {
		Calendar myCal = Calendar.getInstance();
		DateFormat myFormat = new SimpleDateFormat("yyyy_MM_dd_HHmm");
		String file_csv_name = "./weka_file/"+ file_day +"_" + myFormat.format(myCal.getTime()) + ".csv";
		String file_arff_name = "./weka_file/"+ file_day + "_" + myFormat.format(myCal.getTime()) + ".arff";
		File new_csv_file = new File(file_csv_name);
		File new_arff_file = new File(file_arff_name);
		// ファイル作成
		try {
			new_csv_file.createNewFile();
			new_arff_file.createNewFile();
			if (new_csv_file.createNewFile()) {
				System.out.println("ファイルの作成に成功しました");
			} else {
				System.out.println("ファイルの作成に失敗しました");
			}
			// CSVファイル作成
			if (checkBeforeWritefile(new_csv_file)) {
				PrintWriter pw = new PrintWriter(new_csv_file);
				pw.write("\"No.\",\"average\",\"variance\",\"class\"\n");
				for (int i = 0; i < traffic_ave_list.size(); i++) {
					pw.write(i + "," + String.valueOf(traffic_ave_list.get(i)) + ","
							+ String.valueOf(traffic_var_list.get(i)) + ",label\n");
				}
				pw.close();
			} else {
				System.out.println("ファイルに書き込めません");
			}
			

			// ARFFファイル作成
			if (new_arff_file.createNewFile()) {
				System.out.println("ファイルの作成に成功しました");
			} else {
				System.out.println("ファイルの作成に失敗しました");
			}
			if (checkBeforeWritefile(new_arff_file)) {
				PrintWriter pw = new PrintWriter(new_arff_file);
				pw.write("@relation traffic_pattern\n");
				pw.write("\n");
				pw.write("@attribute dataave real\n");
				pw.write("@attribute datavar real\n");
				pw.write("@attribute class {label}\n");
				pw.write("\n");
				pw.write("@data\n");
				for (int i = 0; i < traffic_ave_list.size(); i++) {
					pw.write(String.valueOf(traffic_ave_list.get(i)) + ","
							+ String.valueOf(traffic_var_list.get(i)) + ",label\n");
//					System.out.println(i + " : " + arff_data[i][1]+ " : " + sum_list);
				}
				pw.close();
			} else {
				System.out.println("ファイルに書き込めません");
			}
			
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	private static boolean checkBeforeWritefile(File file) {
		if (file.exists()) {
			if (file.isFile() && file.canWrite()) {
				return true;
			}
		}
		return false;
	}
	
}

// クラスタごとに平均、分散を分ける
// public static void DivideCluster(String[][] data) {
// ArrayList<String> line_data = new ArrayList<String>();
// int cluster_num;
// Pattern pattern;
// Matcher matcher;
// for (int i = 0; i < arff_file_list.size(); i++) {
// cluster_num = Integer.parseInt(data[i][3].replace("cluster", ""));
//// System.out.println(cluster_num);
// if (cluster_num == 0) {
// cluster0_data_ave.add(Long.parseLong(data[i][1]));
// cluster0_data_var.add(Double.parseDouble(data[i][2]));
// } else if (cluster_num == 1) {
// cluster1_data_ave.add(Long.parseLong(data[i][1]));
// cluster1_data_var.add(Double.parseDouble(data[i][2]));
// } else if (cluster_num == 2) {
// cluster2_data_ave.add(Long.parseLong(data[i][1]));
// cluster2_data_var.add(Double.parseDouble(data[i][2]));
// }
// }
// }
//