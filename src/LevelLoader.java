import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelLoader {


	// Ta dùng mảng 2 chiều để lưu trữ thế giới quan của trò chơi
	//	# Tường
	//	_ Khoảng trắng
	//	. Đích rỗng
	//	@ Vị trí người chơi hiện tại
	//	+ Vị trí người chơi tại đích của người chơi (xung quanh cái hộp, và hộp tại đích)
	//  $ Vị trí hộp hiện tại
	//	* Vị trí hộp ở đích

	public char[][] levelmap;

//Load vòng chơi
	public LevelLoader (File levelsource) {
		parseRowList(loadRowList(levelsource));
	}

// Nạp vòng chơi vào
	public State init() {
		return new State(levelmap, getX(), getY());
	}

//Đọc file
	private ArrayList<String> loadRowList(File levelsource) {
		Scanner input;
		ArrayList<String> rowlist = new ArrayList<String>();
		try {
			input = new Scanner(levelsource);
			//Bắt đầu bằng kích thước mảng trò chơi
			if(input.hasNextInt()){
				int height = Integer.parseInt(input.nextLine());
				//System.out.println(height);
				//iterate over that full height/depth
				for(int i = 0; i < height; i++) {
					//Kiểm Tra giá trị đầu vào
					if(input.hasNextLine()){
						//Nạp dòng vào string map chuỗi
						rowlist.add(input.nextLine());
					}
					else {
						System.out.println("Khong phai la file tro choi");
					}
				}
			}

			input.close();
		} catch (FileNotFoundException e) {
			// Bắt lỗi
			System.out.println("Khong tim thay dc file");
			e.printStackTrace();
		}
		return rowlist;
	}


	private void parseRowList(ArrayList<String> rowlist) {
		int height = rowlist.size();
		levelmap = new char[height][];
		for(int i = 0; i < height; i++) {
			levelmap[i] = rowlist.get(i).toCharArray();
		}
	}


	private void printLevel() {
		for(char[] row : levelmap){
			for(char c : row){
				System.out.print(c);
			}
			System.out.println();
		}
	}
	

	private int[] getPlayerLocation() {
		for(int i = 0; i < levelmap.length; i++) {
			for(int j = 0; j < levelmap[i].length; j++) {
				if(levelmap[i][j] == '@' || levelmap[i][j] == '+')
					return new int[] {i, j};
			}
		}
		return new int[2];
	}

	private int getX() {
		return getPlayerLocation()[0];
	}
	

	private int getY() {
		return getPlayerLocation()[1];
	}

}
