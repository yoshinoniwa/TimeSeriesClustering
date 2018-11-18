import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AnalysisByIPAddress {
	static ArrayList<String> ip_address = new ArrayList<String>();
	static String file_name;
	static File new_file[];
	static PrintWriter pw[];

	// IPアドレスのリストの作成
	public static void setIPAddressList(String[][] data) throws IOException {
		Boolean flag = true;
		for (int i = 1; i < data.length; i++) {

			for (int j = 0; j < ip_address.size(); j++) {
				if (data[i][3].equals(ip_address.get(j))) {
					flag = false;
				}
			}
			if (flag) {
				ip_address.add(data[i][3]);
			}
			flag = true;
		}
		for (int i = 0; i < ip_address.size(); i++) {
//			System.out.println(ip_address.get(i));
		}
		createFileByIpAddress(ip_address, data);

	}

	private static void createFileByIpAddress(ArrayList<String> list, String data[][]) {
		String ip = "";
		new_file = new File[list.size()];
		pw = new PrintWriter[list.size()];
		// for (int i = 0; i < data.length; i++) {

		try {
			for (int i = 0; i < list.size(); i++) {
				// if (i == 0) {
				ip = list.get(i);
				ip = ip.replace(".", "");
				file_name = "./ipaddress_data/2018-11-18/" + ip + ".txt";
				new_file[i] = new File(file_name);
				// ファイル作成
				new_file[i].createNewFile();
				if (new_file[i].createNewFile()) {
					System.out.println("ファイルの作成に成功しました");
				} else {
					System.out.println("ファイルの作成に失敗しました");
				}

				// ファイル書き込み
				if (checkBeforeWritefile(new_file[i])) {

					pw[i] = new PrintWriter(new_file[i]);
					
					// pw.write("\"time\",\"length\",\"info\"\n");
					// pw.write("\"" + data[i][1] + "\",\"" + data[i][5] +
					// "\",\"" + data[i][6] + "\"");
//					pw[i].write("\"time\",\"length\",\"info\"\n");
					pw[i].close();
					// pw[i].close();
				} else {
					System.out.println("ファイルに書き込めません");
				}	
			}
			writeFileByIpAddress(ip_address, data);
		} catch (IOException e) {
			System.out.println(e);
		}

		// }
		// }

	}

	//ファイルに追記
	private static void writeFileByIpAddress(ArrayList<String> list, String data[][]) throws IOException {
		String ip = "";
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < list.size(); j++) {
				if (data[i][3].equals(list.get(j))) {
					ip = list.get(j);
					ip = ip.replace(".", "");
					if (checkBeforeWritefile(new_file[j])) {
						FileWriter filewriter = new FileWriter(new_file[j], true);
						filewriter.write("\"" + data[i][1] + "\",\"" + data[i][5] + "\",\"" + data[i][6] + "\"\n");
						filewriter.close();
					} else {
						System.out.println("ファイルに書き込めません");
					}

				}
			}
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
	public static ArrayList<String> getIPAddressList(){
		return ip_address;
	}

}
