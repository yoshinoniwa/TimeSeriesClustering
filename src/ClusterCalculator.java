import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClusterCalculator {
	static String[] traffic_data_set;
	// UDPファイル
	static final String udp_day = "16-09-27-udp"; // ファイル名
	static final String udp_file_name = "./traffic_log/" + udp_day + ".csv"; // ファイルの場所
	static File udp_file = new File(udp_file_name);// ファイル
	static ArrayList<String[]> udp_file_list = new ArrayList<String[]>(); // csvファイルをArrayListに格納
	static String[][] udp_data; // ArrayList→二次元配列
	static ArrayList<Long[]> udp_traffic_sum_set = new ArrayList<Long[]>(); // プロトコル[UDP]のデータ量を格納
	static ArrayList<Long> udp_traffic_sum_list;
	static ArrayList<ArrayList> udp_traffic_sum_num = new ArrayList<ArrayList>();
	static String[] udp_traffic_data_set;
	// TCPファイル
	static final String tcp_day = "16-09-27-tcp";
	static final String tcp_file_name = "./traffic_log/" + tcp_day + ".csv";
	static File tcp_file = new File(tcp_file_name);
	static ArrayList<String[]> tcp_file_list = new ArrayList<String[]>();
	static String[][] tcp_data;

	// static
	public static void CallFile() {
		// UDPのファイル
		try {
			// ファイル読み込み
			// オリジナルファイル
			BufferedReader br = new BufferedReader(new FileReader(udp_file));
			String line = br.readLine();
			for (int row = 0; line != null; row++) {
				udp_file_list.add(line.split(",", 0));
				line = br.readLine();
			}
			br.close();

			udp_data = new String[udp_file_list.size()][];

			// arrayListから二次元配列へ
			for (int row = 0; row < udp_file_list.size(); row++) {
				udp_data[row] = udp_file_list.get(row);
				for (int col = 0; col < 7; col++) {
					udp_data[row][col] = udp_data[row][col].replace("\"", "");
				}
			}

		} catch (IOException e) {
			System.out.println(e);
		}
		// TCPのファイル
	}

	public static void DivideCluster(String[][] data,ArrayList list) {
//		ArrayList<String> line_data = new ArrayList<String>();
//
//		String ip_address[] = new String[data.length];
//		String file_data[][] = new String[data.length][4];

		udp_traffic_data_set = new String[list.size()];
		int count = 0;
		int t_count = 0;
		Long[] t_sum_num = new Long[list.size()];

		String comparsion_str = "";
		for (int i = 0; i < list.size(); i++) {

			if (data[i][6].contains(comparsion_str)) {
				count++;
				// System.out.println(i + " : " +count);
			}

			if (count == 1) {
				t_sum_num[t_count] = Long.parseLong(data[i][5]);
				// System.out.println(i+" : "+t_sum_num[t_count]);
				t_count++;
			} else if (count == 2) {
				udp_traffic_sum_set.add(t_sum_num);
				// nullを消している
				udp_traffic_sum_list = new ArrayList<Long>(Arrays.asList(t_sum_num));
				udp_traffic_sum_list.removeAll(Collections.singleton(null));
				udp_traffic_sum_num.add(udp_traffic_sum_list);
				System.out.println(i + " : " + data[i][1] + " : " + udp_traffic_sum_list);
				count = 1;
				t_count = 0;
				t_sum_num = new Long[udp_file_list.size()];
			}
		}
		// String file_time[][] = new String[data.length][];

		// for (int i = 0; i < data.length; i++) {
		//// ip_address[i] = data[i][3];
		// if(i>=1){
		// //直前のIPアドレスと今のIPアドレスが同じ場合 データ量と時間を追加
		// if(data[i][3].equals(data[i-1][3])){
		// file_data[i][0] = ip_address[i];
		// file_data[i][1] = data[i][3];
		// }else{
		//
		// }
		// }
		// for(int j = 0; j < data.length; j++){
		// for(int k = 0; k < data.length; k++){
		//
		// }
		// }
		// }
	}
}
