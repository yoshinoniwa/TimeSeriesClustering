import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DataCalculator {
	// ファイル読みこみ用
	static String ipaddress = "";
	static String file_name;
	static String day = "2016-09-27";
	static File file;
	static ArrayList<String[]> file_list;
	static String[][] file_data;

	static ArrayList<ArrayList> time_diff_all_list = new ArrayList<ArrayList>();
	static ArrayList<ArrayList> byte_per_minute_list = new ArrayList<ArrayList>();
	static ArrayList<Double> time_ave_list = new ArrayList<Double>();
	static ArrayList<Double> time_var_list = new ArrayList<Double>();
	static ArrayList<Double> data_ave_list = new ArrayList<Double>();
	static ArrayList<Double> data_var_list = new ArrayList<Double>();

	private static Double ave;
	private static Double var;

	public static void calclator(ArrayList<String> iplist) {
		// 各IPアドレスごとにデータ処理を行う
		for (int i = 0; i < iplist.size(); i++) {
			callFile(iplist.get(i));// ファイル読み込み
			// 時間差を求める
			time_diff_all_list.add(timeDifference(file_data, iplist));
//			System.out.println(iplist.get(i));
			// 求めた時間の差をリストに格納
			ArrayList<Long> time = new ArrayList<Long>();
			time = time_diff_all_list.get(i); // 求めた時間の差をリストに格納

			// 時間差をもとにデータ量のひとかたまりを作りリストに収める
			byte_per_minute_list.add(setDataTime(file_data, time));
			ArrayList<Long> byte_per_minute = new ArrayList<Long>();
			byte_per_minute = byte_per_minute_list.get(i);

			// 時間差の0を削除
			// 時間差が発生した時の値を使って特徴量を出すため
			// ArrayList<Long> remove_zero_time = new ArrayList<Long>(time);
			// Iterator<Long> it = remove_zero_time.iterator();
			// while(it.hasNext()){
			// Long j = it.next();
			// if(j == 0) it.remove();
			// }

			// 時間、データ量の平均、標準偏差を求める
			// 時間
			average(time);
			variance(time, getAverage());
			time_ave_list.add(getAverage());
			time_var_list.add(getVariance());
		
			// System.out.println("タイミングの平均(分):"+getAverage() + ",
			// タイミングの標準偏差:"+getVariance());
			// データ量
			// IPアドレスごとのデータ量の平均と標準偏差の場合
//			average(byte_per_minute);
//			variance(byte_per_minute, getAverage());
//			data_ave_list.add(getAverage());
//			data_var_list.add(getVariance());
			// System.out.println("データ量の平均(分):"+getAverage() + ",
			// データ量の標準偏差:"+getVariance());
			// ひとかたまりごとの平均と標準偏差
			// ArrayList<Double> data_ave = new ArrayList<Double>();
			// ArrayList<Double> data_var= new ArrayList<Double>();
			// for(int j=0;j<byte_per_minute.size();j++){
			//// average(byte_per_minute.get(i));
			//// variance(byte_per_minute,getAverage());
			// data_ave_list.add(getAverage());
			// data_var_list.add(getVariance());
			// }
		}
		createFile();
//		 System.out.println(data_var_list.size());

	}

	public static void callFile(String ip) {
		// ファイル名の指定
		ipaddress = ip.replace(".", "");
		file_name = "./ipaddress_data/" + day + "/" + ipaddress + ".txt";
		file = new File(file_name);
		// ファイル読みこみ
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			file_list = new ArrayList<String[]>();
			for (int i = 0; line != null; i++) {
				file_list.add(line.split(",", 0));
				line = br.readLine();
			}
			br.close();

			file_data = new String[file_list.size()][];
			// arrayListから二次元配列へ
			// data[][0]:time data[][1]:length data[][2]:info
			for (int row = 0; row < file_list.size(); row++) {
				file_data[row] = file_list.get(row);
				for (int col = 0; col < 3; col++) {
					file_data[row][col] = file_data[row][col].replace("\"", "");
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static ArrayList<Long> timeDifference(String[][] data, ArrayList<String> list) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:m"); // 時間のデータフォーマットyyyy-MM-dd
																		// HH:mm:ss
		// ミリ秒
		String timeTo, timeFrom;// To:今 From:前
		Date timeToDate;
		Date timeFromDate; // 時間をdate型に変更
		long timeToLong, timeFromLong; // long型に変更
		long time_minute_diff = 0; // 差
		String[] time = new String[data.length];
		ArrayList<Long> time_diff = new ArrayList<Long>();

		for (int i = 0; i < data.length; i++) {
			time[i] = data[i][0];

			if (i > 1) {
				timeTo = time[i];
				timeFrom = time[i - 1];
				// System.out.println(timeTo);
				try {
					// Date型に時間を入れている
					timeToDate = sdf.parse(timeTo);
					timeFromDate = sdf.parse(timeFrom);
					// Date型からlong型へ変更
					timeToLong = timeToDate.getTime();
					timeFromLong = timeFromDate.getTime();
					// 分数の差
					time_minute_diff = (timeToLong - timeFromLong) / (1000 * 60);
					time_diff.add(time_minute_diff);
					// System.out.println(timeToDate);
					// System.out.println(diff);
				} catch (ParseException e) {
					System.out.println(e);
				}
			}
		}
		// System.out.println(data.length);
		return time_diff;
	}

	public static ArrayList<Long> setDataTime(String[][] data, ArrayList<Long> time_list) {
		ArrayList<Long> set_data_time = new ArrayList<Long>();
		ArrayList<Long> data_num = new ArrayList<Long>();
		long data_to_long;
		long sum = Long.parseLong(data[0][1]);
//		int count = 0;
		for (int i = 0; i < time_list.size(); i++) {
			data_to_long = Long.parseLong(data[i + 1][1]);
			
			if (time_list.get(i) == 0) {
				sum += data_to_long;
				data_num.add(data_to_long);
			} else {
				average(data_num);
				variance(data_num,getAverage());
				data_ave_list.add(getAverage());
				data_var_list.add(getVariance());
//				System.out.println("平均" + getAverage()+"標準偏差"+getVariance());
				data_num.clear();
				set_data_time.add(sum);
				sum = Long.parseLong(data[i + 1][1]);
				set_data_time.add(sum);
				data_num.add(data_to_long);
//				count++;
			}
		}
//		System.out.println(count);
//		System.out.println(set_data_time.size());
		return set_data_time;
	}

	public static void average(ArrayList<Long> list) {

		double sum = 0;
		if (list == null || list.size() == 0) {
			// System.exit(0);
		} else {
			for (int i = 0; i < list.size(); i++) {
				sum += list.get(i);
			}
			ave = sum / list.size();
			// traffic_ave_list.add(ave);
			// System.out.println(ave);
		}
	}

	private static double getAverage() {
		return ave;
	}

	// 分散を求めるメソッド
	public static void variance(ArrayList<Long> list, double ave) {
		// double var;
		double sum = 0;
		if (list == null || list.size() == 0) {
			// System.exit(0);
		} else {
			for (int i = 0; i < list.size(); i++) {
				sum += (double) (ave - list.get(i)) * (ave - list.get(i));
			}
			var = sum / list.size();

			// System.out.println(var);
		}
	}

	private static double getVariance() {
		return var;
	}

	// ファイル作成
	public static void createFile() {
		Calendar myCal = Calendar.getInstance();
		DateFormat myFormat = new SimpleDateFormat("yyyy_MM_dd_HHmm");
		String file_csv_name = "./weka_file/" + day + "_" + myFormat.format(myCal.getTime()) + ".csv";
		String file_arff_name = "./weka_file/" + day + "_" + myFormat.format(myCal.getTime()) + ".arff";
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
//				pw.write("\"No.\",\"time_average\",\"time_variance\",\"data_variance\",\"data_average\",\"class\"\n");
//				for (int i = 0; i < time_ave_list.size(); i++) {
//					pw.write(i + "," + String.valueOf(time_ave_list.get(i)) + "," + String.valueOf(time_var_list.get(i))
//							+ "," + String.valueOf(data_ave_list.get(i)) + "," + String.valueOf(data_var_list.get(i))
//							+ ",label\n");
//				}
				pw.write("\"No.\",\"data_Average\",\"data_Variance\",\"class\"\n");
				for (int i = 0; i < data_ave_list.size(); i++) {
					pw.write(i + "," + String.valueOf(data_ave_list.get(i)) + "," + String.valueOf(data_var_list.get(i)) + ",label\n");
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
//				pw.write("@attribute timeave real\n");
//				pw.write("@attribute timevar real\n");
				pw.write("@attribute dataave real\n");
				pw.write("@attribute datavar real\n");
				pw.write("@attribute class {label}\n");
				pw.write("\n");
				pw.write("@data\n");
//				for (int i = 0; i < time_ave_list.size(); i++) {
//					pw.write(String.valueOf(time_ave_list.get(i)) + "," + String.valueOf(time_var_list.get(i)) + ","
//							+ String.valueOf(data_var_list.get(i)) + "," + String.valueOf(data_var_list.get(i))
//							+ ",label\n");
//				}
				for (int i = 0; i < data_ave_list.size(); i++) {
					pw.write(String.valueOf(data_ave_list.get(i)) + "," + String.valueOf(data_var_list.get(i))
							+ ",label\n");
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
