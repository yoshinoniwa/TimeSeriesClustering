import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	// --------------ファイル読み込み用--------------
	// ファイル
	static File csv_file;
	static String csv_file_name = "./traffic_log/slide_30_window_60.arff";
	// オリジナルのcsvデータを格納するリスト
	static ArrayList<String[]> file_list = new ArrayList<String[]>();
	// ダブルクォーテーションを無くしたcsvファイル
	static String[][] csv_data = new String[file_list.size()][];
	// ダブルクォーテーションを無くしたcsvファイルのリスト
	static ArrayList<String[]> csv_data_list = new ArrayList<String[]>();

	// arff ファイル読み込み用
	static File arff_file;
	static String arff_file_name = "./traffic_log/slide_30_window_60.txt";
	// オリジナルのcsvデータを格納するリスト
	static ArrayList<String[]> arff_file_list = new ArrayList<String[]>();
	// ダブルクォーテーションを無くしたcsvファイル
	static String[][] arff_data = new String[file_list.size()][];
	// ダブルクォーテーションを無くしたcsvファイルのリスト
	static ArrayList<String[]> arff_data_list = new ArrayList<String[]>();

	// メインメソッド
	public static void main(String args[]) throws FileNotFoundException {
		callFile();
//		csv_data = toStringArray();
//		System.out.println(csv_data[10][1]);
		System.out.println(arff_data[10][1]);
	}

	// ファイル読み込み
	public static void callFile() throws FileNotFoundException {
		try {
			csv_file = new File(csv_file_name);
			BufferedReader br = new BufferedReader(new FileReader(csv_file));
			// ArrayList:filelistにCSVファイルを格納
			while (br.ready()) {
				String line = br.readLine();
				file_list.add(line.split(",")); // ,で区切って読み込み
			}
			//arff file 読み込み
			arff_file = new File(arff_file_name);
			BufferedReader br_arff = new BufferedReader(new FileReader(arff_file));
			// ArrayList:filelistにCSVファイルを格納
			while (br_arff.ready()) {
				String line = br_arff.readLine();
				file_list.add(line.split(",")); // ,で区切って読み込み
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

//	// ファイルのダブルクォーテーションを消すメソッド
//	public static String[][] toStringArray() {
//		// data[][]のなかにダブルクォーテーションを消した値を入れる
//		String[][] data = new String[file_list.size()][];
//		// fileDbleQuoはArrayListから二次元配列にするための仮の配列
//		String[][] fileDbleQuo = new String[file_list.size()][];
//		// dataの配列に値を入れるための仮置場
//		String dbleQuo;
//		for (int i = 0; i < file_list.size(); i++) {
//			fileDbleQuo[i] = file_list.get(i);
//			// ArrayListから二次元配列
//			data[i] = file_list.get(i);
//			for (int k = 0; k < 5; k++) {
//				dbleQuo = fileDbleQuo[i][k];
//				data[i][k] = dbleQuo.replace("\"", "");// ダブルクォーテーションを消す
//			}
//			csv_data_list = extractCsvData(data[i]);
//		}
//		return data;
//	}

	public static ArrayList<String[]> extractCsvData(String[] data) {
		ArrayList<String[]> csv_data_list = new ArrayList<String[]>();
		csv_data_list.add(data);
		return csv_data_list;
	}

}
