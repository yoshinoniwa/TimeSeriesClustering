import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
	static ArrayList<String[]> udp_only_data_list = new ArrayList<String[]>(); // プロトコルがudpのみのファイル
	static String[][] udp_only_data;
	static ArrayList<Long[]> udp_traffic_sum_set = new ArrayList<Long[]>(); // プロトコル[UDP]のデータ量を格納
	static ArrayList<Long> udp_traffic_sum_list;
	static ArrayList<ArrayList> udp_traffic_sum_num = new ArrayList<ArrayList>();
	static String[] udp_traffic_data_set;

	// static
	public static void CallFile() throws ParseException {
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
			DivideCluster(udp_data);

		} catch (IOException e) {
			System.out.println(e);
		}
		// TCPのファイル
	}

	public static void DivideCluster(String[][] data) throws ParseException {
		

	}

	// 差を求める
//	public static long timedifference(String timeTo, String timeFrom) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 時間のデータフォーマット
//																			// ミリ秒
//		Date timeToDate, timeFromDate; // 時間をdate型に変更
//		long timeToLong, timeFromLong; // long型に変更
//		long time_diff = 0; // 差
//
//		try {
//			// Date型に時間を入れている
//			timeToDate = sdf.parse(timeTo);
//			timeFromDate = sdf.parse(timeFrom);
//			// Date型からlong型へ変更
//			timeToLong = timeToDate.getTime();
//			timeFromLong = timeFromDate.getTime();
//			// 秒数の差
//			time_diff = (timeToLong - timeFromLong) / 1000;
//			// System.out.println(timeToDate);
//			// System.out.println(diff);
//		} catch (ParseException e) {
//			System.out.println(e);
//		}
//		return time_diff;
//	}
}

//
// public static void DivideCluster(String[][] data) {
//// ArrayList<String> line_data = new ArrayList<String>();
////
//// String ip_address[] = new String[data.length];
//// String file_data[][] = new String[data.length][4];
//
// udp_traffic_data_set = new String[udp_file_list.size()];
// int count = 0;
// int t_count = 0;
// Long[] t_sum_num = new Long[udp_file_list.size()];
//// udp_only_data = new String[udp_file_list.size()][];
// String comparsion_str = "UDP";
// for (int i = 0; i < udp_file_list.size(); i++) {
//
// if (data[i][4].contains(comparsion_str)) {
// udp_only_data_list.add(data[i]);
// count++;
// // System.out.println(i + " : " +count);
// }
//
//// if (count == 1) {
//// t_sum_num[t_count] = Long.parseLong(data[i][5]);
//// // System.out.println(i+" : "+t_sum_num[t_count]);
//// t_count++;
//// } else if (count == 2) {
//// udp_traffic_sum_set.add(t_sum_num);
//// // nullを消している
//// udp_traffic_sum_list = new ArrayList<Long>(Arrays.asList(t_sum_num));
//// udp_traffic_sum_list.removeAll(Collections.singleton(null));
//// udp_traffic_sum_num.add(udp_traffic_sum_list);
//// System.out.println(i + " : " + data[i][1] + " : " + udp_traffic_sum_list);
//// count = 1;
//// t_count = 0;
//// t_sum_num = new Long[udp_file_list.size()];
//// }
// }
// udp_only_data = new String[udp_only_data_list.size()][];
// for(int i=0;i<udp_only_data_list.size();i++){
// udp_only_data[i] = udp_only_data_list.get(i);
// }
// for(int i=0;i<udp_only_data.length;i++){
// for(int j=0;j<7;j++){
// System.out.print(udp_only_data[i][j] + ", ");
// }
// System.out.println();
// }
// // String file_time[][] = new String[data.length][];
//
// // for (int i = 0; i < data.length; i++) {
// //// ip_address[i] = data[i][3];
// // if(i>=1){
// // //直前のIPアドレスと今のIPアドレスが同じ場合 データ量と時間を追加
// // if(data[i][3].equals(data[i-1][3])){
// // file_data[i][0] = ip_address[i];
// // file_data[i][1] = data[i][3];
// // }else{
// //
// // }
// // }
// // for(int j = 0; j < data.length; j++){
// // for(int k = 0; k < data.length; k++){
// //
// // }
// // }
// // }
// }
