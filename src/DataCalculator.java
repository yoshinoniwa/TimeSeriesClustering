import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DataCalculator {
	// ファイル読みこみ用
	static String ipaddress = "";
	static String file_name;
	static String day = "2018-11-18";
	static File file;
	static ArrayList<String[]> file_list;
	static String[][] file_data;

	static ArrayList<ArrayList> time_diff_all_list = new ArrayList<ArrayList>();// 時間差を格納するリスト
	static ArrayList<ArrayList> byte_per_minute_list = new ArrayList<ArrayList>();// 時間ごとのデータを格納するリスト
	static ArrayList<Double> time_ave_list = new ArrayList<Double>();
	static ArrayList<Double> time_var_list = new ArrayList<Double>();
	static ArrayList<Double> data_ave_list = new ArrayList<Double>();
	static ArrayList<Double> data_var_list = new ArrayList<Double>();

	static ArrayList<String> arff_list = new ArrayList<String>();// 時間差を格納するリスト
	static ArrayList<String> csv_list = new ArrayList<String>();// 時間差を格納するリスト
	// static ArrayList<ArrayList>

	private static Double ave;
	private static Double var;

	// 計算メソッドを呼び出して処理するメソッド
	public static void calclator(ArrayList<String> iplist) {
		// System.out.println(" ------ " + iplist.size());
		// 各IPアドレスごとにデータ処理を行う
		for (int i = 0; i < iplist.size(); i++) {
			callFile(iplist.get(i));// ファイル読み込み
			System.out.println(iplist.get(i));
			// 時間差を求める
			time_diff_all_list.add(timeDifference(file_data));
			// System.out.println(iplist.get(i));
			// 求めた時間の差をリストに格納
			ArrayList<Long> time = new ArrayList<Long>();
			time = time_diff_all_list.get(i); // 求めた時間の差をリストに格納
			// System.out.println(time.get(time.size()));
			// if(i==0){
			// for(int j=0;j<time.size();j++){
			// System.out.println(" "+time.get(j));
			// }
			// }

			// 時間差をもとにデータ量のひとかたまりを作りリストに収める
			byte_per_minute_list.add(setDataTime(file_data, time));
			ArrayList<Long> byte_per_minute = new ArrayList<Long>();
			byte_per_minute = byte_per_minute_list.get(i);

			// createIPaddressFile(iplist.get(i),byte_per_minute);
			// 時間差の0を削除
			// 時間差が発生した時の値を使って特徴量を出すため
			// ArrayList<Long> remove_zero_time = new ArrayList<Long>(time);
			// Iterator<Long> it = remove_zero_time.iterator();
			// while(it.hasNext()){
			// Long j = it.next();
			// if(j == 0) it.remove();
			// }

			// /
		}
		createFile();
		System.out.println(data_var_list.size());

	}

	// public static void calclatorAll(String[][] data) {
	// // 求めた時間の差をリストに格納
	// ArrayList<Long> time = new ArrayList<Long>();
	// time = timeDifference(data); // 求めた時間の差をリストに格納
	//
	// // 時間差をもとにデータ量のひとかたまりを作りリストに収める
	// ArrayList<Long> byte_per_minute = new ArrayList<Long>();
	// byte_per_minute = setDataTime(data, time);
	// createFile("origin", byte_per_minute);
	// }

	// ファイルの呼び出し
	// IPアドレスごとのファイルを呼び出す
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

	public static ArrayList<Long> timeDifference(String[][] data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 時間のデータフォーマットyyyy-MM-dd

		// ミリ秒
		String timeTo, timeFrom;// To:今 From:前
		Date timeToDate;
		Date timeFromDate = null; // 時間をdate型に変更
		long timeToLong, timeFromLong = 0; // long型に変更
		long time_minute_diff = 0; // 差
		String[] time = new String[data.length];
		ArrayList<Long> time_diff = new ArrayList<Long>();
		Boolean flag_day = true;

		// 最初の値
		SimpleDateFormat firstTime = new SimpleDateFormat("yyyy-MM-dd HH", Locale.ENGLISH);
		String firstTimeString;
		Date firstTimeDate;
		long firstTimeLong;

		// 最後の値
		SimpleDateFormat finishTime = new SimpleDateFormat("yyyy-MM-dd HH", Locale.ENGLISH);
		String finishTimeString;
		Date finishTimeDate;
		long finishTimeLong = 0;

		// long setfinishtimelong;

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

					if (flag_day) {
						// 日付が今日だったら
						if (time[i - 1].contains(day)) {
							String setFirstTime = "";

							// baseTimeLong = 0;// 仮
							// 最初の値を前日の23時に合わせる
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(timeFromDate);
							// 日にちを前日にする
							calendar.add(Calendar.DATE, -1);
							// 文字列に形式を合わせて入れる
							setFirstTime = String.valueOf(calendar.get(Calendar.YEAR)) + "-"
									+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-"
									+ String.valueOf(calendar.get(Calendar.DATE) + " 23:00:00:00");
							firstTimeString = setFirstTime;
							firstTimeDate = firstTime.parse(firstTimeString);
							firstTimeLong = firstTimeDate.getTime();
							flag_day = false;
						} else {
							// 日付が前日だった場合
							firstTimeString = time[i - 1];
							firstTimeDate = firstTime.parse(firstTimeString);
							firstTimeLong = firstTimeDate.getTime();
							flag_day = false;
						}
						time_minute_diff = (timeFromLong - firstTimeLong) / (1000);
						time_diff.add(time_minute_diff);
						System.out.println("秒数 : " + time_minute_diff);
					}

					// 分数の差
					time_minute_diff = (timeToLong - timeFromLong) / (1000);
					time_diff.add(time_minute_diff);
					System.out.println("秒数 : " + time_minute_diff);

					// リストの最後に追加する
					String setFinishTime = "";
					// 最後の値を当日の23時に合わせる
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(timeFromDate);
					// 文字列に形式を合わせて入れる
					setFinishTime = String.valueOf(calendar.get(Calendar.YEAR)) + "-"
							+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "-"
							+ String.valueOf(calendar.get(Calendar.DATE) + " 23:00:00:00");
					finishTimeString = setFinishTime;
					finishTimeDate = finishTime.parse(finishTimeString);
					finishTimeLong = finishTimeDate.getTime();
				} catch (ParseException e) {
					System.out.println(e);
				}
			}
		}
		// リストの最後に最後の値と23:00までの
		time_minute_diff = (finishTimeLong - timeFromLong) / (1000 * 60);
		time_diff.add(time_minute_diff);
		// System.out.println(time_diff.get(time_diff.size()-1));
		// System.out.println(time_minute_diff);
		System.out.println(timeFromDate);
		time_minute_diff = 0;
		flag_day = true;
		// System.out.println(data.length);
		return time_diff;
	}

	// 1分毎にデータをまとめる
	public static ArrayList<Long> setDataTime(String[][] data, ArrayList<Long> time_list) {
		ArrayList<Long> set_data_time = new ArrayList<Long>();
		ArrayList<Long> data_num = new ArrayList<Long>();
		ArrayList<Long> time_num = new ArrayList<Long>();
		long data_to_long;
		long sum = Long.parseLong(data[0][1]);
		double time_ave = 0;
		double time_var = 0;
		// long sum = Long.parseLong(data[1][5]);
		int count = 0;
		// int count = 0;
		// time_list:時間差が格納せれているリスト
		// 0-同一分数 １分の中に複数通信を持つ
		// 1以上-数分の時間通信されていない

		for (int i = 0; i < (time_list.size() - 1); i++) {
			// for (int i = 1; i < (time_list.size() - 1); i++) {
			data_to_long = Long.parseLong(data[i + 1][1]);
			// data_to_long = Long.parseLong(data[i + 1][5]);
			if (time_list.get(i) == 0) {
				// 1分以上の差がない場合データ量を足していく
				sum += data_to_long;
				data_num.add(data_to_long);
				// System.out.println(data[i+1][0]);
			} else {
				count++;
				// １分以上の差がある場合それまでのsumをリストに格納
				// リストに値が入っていなければ今の値を入れてあげる
				// 間の時間のデータ量を0とする
				if (data_num.isEmpty()) {
					data_num.add(data_to_long);
					average(data_num);
					variance(data_num, getAverage());
					System.out.println("平均" + getAverage());
					
				} else {
					average(data_num);
					variance(data_num, getAverage());
//					System.out.println("平均" + getAverage());
				}
				data_ave_list.add(getAverage());
				data_var_list.add(getVariance());
				time_num.add(time_list.get(i));
//				data_ave_list.add(getAverage());
//				data_var_list.add(getVariance());

				data_num.clear();
				
				set_data_time.add(sum);

				// 通信されていない間のデータ量は0とする
				if (time_list.get(i) > 1) {
					for (int j = 0; j < (time_list.get(i) - 1); j++) {
						set_data_time.add((long) 0);
						count++;
					}
				}
				sum = Long.parseLong(data[i + 1][1]);
				// sum = Long.parseLong(data[i + 1][5]);
				// set_data_time.add(sum);
				// data_num.add(data_to_long);
				// count++;
				// System.out.println(data[i+1][0]);
			}
			if (i == (time_list.size() - 2)) {
				average(time_num);
				variance(time_num, getAverage());
				time_ave = getAverage();
				time_var = getVariance();
			}
		}
		for (int i = 0; i < time_list.get(time_list.size() - 1); i++) {
			set_data_time.add((long) 0);
			count++;
		}
		for (int i = 0; i < data_ave_list.size(); i++) {
			csv_list.add(String.valueOf(time_ave)+","+String.valueOf(time_ave)+","+String.valueOf(data_ave_list.get(i))+","+String.valueOf(data_var_list.get(i)) );
		}
		System.out.println(set_data_time.size());

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
	// 平均、分散を格納するファイル
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
				//
				pw.write("\"No.\",\"time_average\",\"time_variance\",\"data_variance\",\"data_average\",\"class\"\n");
				// for (int i = 0; i < time_ave_list.size(); i++) {
				// pw.write(i + "," + String.valueOf(time_ave_list.get(i)) + ","
				// + String.valueOf(time_var_list.get(i))
				// + "," + String.valueOf(data_ave_list.get(i)) + "," +
				// String.valueOf(data_var_list.get(i))
				// + ",label\n");
				// }
				// pw.write("\"No.\",\"data_Average\",\"data_Variance\",\"class\"\n");
				for (int i = 0; i < csv_list.size(); i++) {
					pw.write(i + "," + csv_list.get(i) + ",label\n");
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
				pw.write("@attribute timeave real\n");
				pw.write("@attribute timevar real\n");
				pw.write("@attribute dataave real\n");
				pw.write("@attribute datavar real\n");
				pw.write("@attribute class {label}\n");
				pw.write("\n");
				pw.write("@data\n");
				// for (int i = 0; i < time_ave_list.size(); i++) {
				// pw.write(String.valueOf(time_ave_list.get(i)) + "," +
				// String.valueOf(time_var_list.get(i)) + ","
				// + String.valueOf(data_var_list.get(i)) + "," +
				// String.valueOf(data_var_list.get(i))
				// + ",label\n");
				// }
				for (int i = 0; i < csv_list.size(); i++) {
					pw.write(csv_list.get(i)+",label\n");
				}
				pw.close();
			} else {
				System.out.println("ファイルに書き込めません");
			}

		} catch (IOException e) {
			System.out.println(e);
		}
	}
	// IPアドレスごとの時間軸を揃えたファイル
	// public static void createIPaddressFile(String ip, ArrayList list) {
	// Calendar myCal = Calendar.getInstance();
	// DateFormat myFormat = new SimpleDateFormat("yyyy_MM_dd_HHmm");
	// // String ip="";
	// String file_time_name = "./ipaddress_data/" + day + "_time/" + ip +
	// ".csv";
	// File new_time_file = new File(file_time_name);
	//
	// // ファイル作成
	// try {
	// new_time_file.createNewFile();
	// if (new_time_file.createNewFile()) {
	// System.out.println("ファイルの作成に成功しました");
	// } else {
	// System.out.println("ファイルの作成に失敗しました");
	// }
	// // 時間軸を合わせたファイル作成
	// if (checkBeforeWritefile(new_time_file)) {
	// PrintWriter pw = new PrintWriter(new_time_file);
	// //
	// pw.write("\"No.\",\"time_average\",\"time_variance\",\"data_variance\",\"data_average\",\"class\"\n");
	// // for (int i = 0; i < time_ave_list.size(); i++) {
	// // pw.write(i + "," + String.valueOf(time_ave_list.get(i)) + ","
	// // + String.valueOf(time_var_list.get(i))
	// // + "," + String.valueOf(data_ave_list.get(i)) + "," +
	// // String.valueOf(data_var_list.get(i))
	// // + ",label\n");
	// // }
	// pw.write("\"length\"\n");
	// for (int i = 0; i < list.size(); i++) {
	// pw.write(String.valueOf(list.get(i)) + "\n");
	// }
	// pw.close();
	// } else {
	// System.out.println("ファイルに書き込めません");
	// }
	// } catch (IOException e) {
	// System.out.println(e);
	// }
	// }

	private static boolean checkBeforeWritefile(File file) {
		if (file.exists()) {
			if (file.isFile() && file.canWrite()) {
				return true;
			}
		}
		return false;
	}
}
