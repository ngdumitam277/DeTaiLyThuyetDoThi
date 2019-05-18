import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class State implements Comparable<State> {

	private String statestring;


	public char[][] level;

	
	private int x;
	private int y;

	private int cost;


	private String path;


	private State parent;

	public State (char[][] level, int x, int y) {
		parent = null;

		this.level = new char[level.length][];
		for(int i = 0; i < level.length; i++){
			this.level[i] = new char[level[i].length];
			for(int j = 0; j < level[i].length; j++)
				this.level[i][j]=level[i][j];
		}

		this.x = x;
		this.y = y;
		cost = 0;
		path = "";

		statestring = "";
		for(char[] row : level)
			for(char c : row)
				statestring += c;
	}

	public State (State par, char dir) {

		parent = par;
		this.cost = par.getCost() + 1;
		path = par.getPath() + dir;

		char[][] tmplevel = computeState(par, dir);

		level = new char[tmplevel.length][];
		for(int i = 0; i < tmplevel.length; i++){
			level[i] = new char[tmplevel[i].length];
			for(int j = 0; j < tmplevel[i].length; j++)
				level[i][j]=tmplevel[i][j];
		}

		switch (dir) {
		case 'u': 
			x = par.getX() - 1;
			y = par.getY();
			break;
		case 'd': 
			x = par.getX() + 1;
			y = par.getY();
			break;
		case 'l': 
			x = par.getX();
			y = par.getY() - 1;
			break;
		case 'r': 
			x = par.getX();
			y = par.getY() + 1;
			break;	
		}

		statestring = "";
		for(char[] row : level)
			for(char c : row)
				statestring += c;
	}

	public int getCost() {
		return cost;
	}

	


	public int manhDist() {
		ArrayList<int[]> goals = new ArrayList<int[]>();
		ArrayList<int[]> boxes = new ArrayList<int[]>();
		int sum = 0;

		for(int i = 0; i < level.length; i++) {
			for(int j = 0; j < level[i].length; j++) {
				if(level[i][j] == '.' || level[i][j] == '*')
					goals.add(new int[] {i, j});
				if(level[i][j] == '$')
					boxes.add(new int[] {i, j});			
			}
		}
		
		for(int[] b : boxes){
			int min = 1000;
			for(int[] g : goals){
				int md = Math.abs(b[0] - g[0]) + Math.abs(b[1] - g[1]);
				if(md < min)
					min = md;
			}
			sum += min;
		}
		return sum;
	}

	public int openGoals() {
		int opengoals = 0;
		for(char[] row : level)
			for(char c : row)
				if(c == '.')
					opengoals++;
		return opengoals;
	}

	public char[][] getState() {
		return level;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void logState() {
		try {
			FileWriter fw = new FileWriter("log.txt", true);


			for(char[] row : level){
				for(char c : row){
					fw.write(c);
				}
				fw.write('\n');
			}
			fw.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void printState() {
		for(char[] row : level){
			for(char c : row){
				System.out.print(c);
			}
			System.out.println();
		}
		System.out.println();

	}


	public void log(String line) {
		try {
			FileWriter fw = new FileWriter("log.txt", true);
			fw.write(line);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public String getStateString() {
		return statestring;
	}

	public State getParent() {
		return parent;
	}

	public String getPath() {
		return path;
	}

	public int hashCode() {
		return statestring.hashCode();
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof State))
			return false;
		return ( ((State) obj).getStateString().equals(this.getStateString()) ) ? true : false;
	}

	public boolean isGoal() {
		for(int i = 0; i < statestring.length(); i++) { 
			char c = statestring.charAt(i); 
			if(c == '.' || c == '+')
				return false;
		}
		return true;
	}


	public boolean isUpValid() {
		char up = level[x - 1][y];;
		if(up == '#') 
			return false;
		if(up == ' ' || up == '.') 
			return true;
		if(level[x - 2].length <= y)
			return false;
		char up2 = level[x - 2][y];
		if( (up == '$' || up == '*') && (up2 == ' ' || up2 == '.') )
			return true;
		return false;
	}

	public boolean isDownValid() {
		
		
		char down = level[x + 1][y];;
		
		if(down == '#') 
			
			return false;
		
		if(down == ' ' || down == '.') 
			return true;
		
		
		if(level[x + 2].length <= y)
			
			
			return false;
		
		char down2 = level[x + 2][y];
		
		if( (down == '$' || down == '*') && (down2 == ' ' || down2 == '.') )
			return true;
		
		
		return false;
	}

	public boolean isLeftValid() {
		
		
		char left = level[x][y - 1];;
		
		if(left == '#') 
			
			return false;
		
		if(left == ' ' || left == '.') 
			return true;
		
		if(y <= 1)
			
			
			return false;
		
		char left2 = level[x][y - 2];
		
		if( (left == '$' || left == '*') && (left2 == ' ' || left2 == '.') )
			return true;
		
		
		return false;
	}

	/**
	 * check whether the right action is available
	 * @return boolean if available return true, else return false
	 */
	public boolean isRightValid() {
		
		
		char right = level[x][y + 1];;
		
		if(right == '#') 
			
			return false;
		
		if(right == ' ' || right == '.') 
			return true;
		
		if(level[x].length <= y + 2)
			
			
			return false;
		
		char right2 = level[x][y + 2];
		
		if( (right == '$' || right == '*') && (right2 == ' ' || right2 == '.') )
			return true;
		
		
		return false;
	}

	public ArrayList<Character> getValidMoves() {

		ArrayList<Character> moves = new ArrayList<Character>();

		if(isUpValid()) 
			moves.add('u');

		if(isDownValid()) 
			moves.add('d');

		if(isLeftValid()) 
			moves.add('l');

		if(isRightValid()) 
			moves.add('r');

		return moves;
	}

	private char[][] computeState(State par, char dir) {
		char[][] oldlevel = par.getState();
		int x = par.getX();
		int y = par.getY();

		
		char[][] newlevel = new char[oldlevel.length][];
		for(int i = 0; i < oldlevel.length; i++){
			newlevel[i] = new char[oldlevel[i].length];
			for(int j = 0; j < oldlevel[i].length; j++)
				newlevel[i][j]=oldlevel[i][j];
		}

		switch (dir) {
		
		case 'u': 
			
			if(newlevel[x - 1][y] == ' ')
				
				newlevel[x - 1][y] = '@';
			
			if(newlevel[x - 1][y] == '.')
				
				newlevel[x - 1][y] = '+';
			
			if(newlevel[x - 1][y] == '$') {
				cost++;
				
				if(newlevel[x - 2][y] == ' '){
					
					newlevel[x - 2][y] = '$';
					
					newlevel[x - 1][y] = '@';
				}
				
				else{
					
					newlevel[x - 2][y] = '*';
					
					newlevel[x - 1][y] = '@';
				}	
			}
			
			if(newlevel[x - 1][y] == '*') {
				cost++;
				
				if(newlevel[x - 2][y] == ' '){
					
					newlevel[x - 2][y] = '$';
					
					newlevel[x - 1][y] = '+';
				}
				
				else{
					
					newlevel[x - 2][y] = '*';
					
					newlevel[x - 1][y] = '+';
				}	
			}
			
			
			
			if(newlevel[x][y] == '@')
				
				newlevel[x][y] = ' ';
			
			else
				
				newlevel[x][y] = '.';
			break;	
		case 'd': 
			
			if(newlevel[x + 1][y] == ' ')
				
				newlevel[x + 1][y] = '@';
			
			if(newlevel[x + 1][y] == '.')
				
				newlevel[x + 1][y] = '+';
			
			if(newlevel[x + 1][y] == '$') {
				cost++;
				
				if(newlevel[x + 2][y] == ' '){
					
					newlevel[x + 2][y] = '$';
					
					newlevel[x + 1][y] = '@';
				}
				
				else{
					
					newlevel[x + 2][y] = '*';
					
					newlevel[x + 1][y] = '@';
				}	
			}
			
			if(newlevel[x + 1][y] == '*') {
				cost++;
				
				if(newlevel[x + 2][y] == ' '){
					
					newlevel[x + 2][y] = '$';
					
					newlevel[x + 1][y] = '+';
				}
				
				else{
					
					newlevel[x + 2][y] = '*';
					
					newlevel[x + 1][y] = '+';
				}	
			}
			
			
			
			if(newlevel[x][y] == '@')
				
				newlevel[x][y] = ' ';
			
			else
				
				newlevel[x][y] = '.';
			break;	
		case 'l': 
			
			if(newlevel[x][y - 1] == ' ')
				
				newlevel[x][y - 1] = '@';
			
			if(newlevel[x][y - 1] == '.')
				
				newlevel[x][y - 1] = '+';
			
			if(newlevel[x][y - 1] == '$') {
				cost++;
				
				if(newlevel[x][y - 2] == ' '){
					
					newlevel[x][y - 2] = '$';
					
					newlevel[x][y - 1] = '@';
				}
				
				else{
					
					newlevel[x][y - 2] = '*';
					
					newlevel[x][y - 1] = '@';
				}	
			}
			
			if(newlevel[x][y - 1] == '*') {
				cost++;
				
				if(newlevel[x][y - 2] == ' '){
					
					newlevel[x][y - 2] = '$';
					
					newlevel[x][y - 1] = '+';
				}
				
				else{
					
					newlevel[x][y - 2] = '*';
					
					newlevel[x][y - 1] = '+';
				}	
			}
			
			
			
			if(newlevel[x][y] == '@')
				
				newlevel[x][y] = ' ';
			
			else
				
				newlevel[x][y] = '.';
			break;	
		case 'r': 
			
			if(newlevel[x][y + 1] == ' ')
				
				newlevel[x][y + 1] = '@';
			
			if(newlevel[x][y + 1] == '.')
				
				newlevel[x][y + 1] = '+';
			
			if(newlevel[x][y + 1] == '$') {
				cost++;
				
				if(newlevel[x][y + 2] == ' '){
					
					newlevel[x][y + 2] = '$';
					
					newlevel[x][y + 1] = '@';
				}
				
				else{
					
					newlevel[x][y + 2] = '*';
					
					newlevel[x][y + 1] = '@';
				}	
			}
			
			if(newlevel[x][y + 1] == '*') {
				cost++;
				
				if(newlevel[x][y + 2] == ' '){
					
					newlevel[x][y + 2] = '$';
					
					newlevel[x][y + 1] = '+';
				}
				
				else{
					
					newlevel[x][y + 2] = '*';
					
					newlevel[x][y + 1] = '+';
				}	
			}

			
			
			
			if(newlevel[x][y] == '@')
				
				newlevel[x][y] = ' ';
			
			else
				
				newlevel[x][y] = '.';
			break;
		}
		return newlevel;
	}

	@Override
	public int compareTo(State o) {
		if(this.getCost() == o.getCost())
			return 0;
		return (getCost() < o.getCost() ? -1 : 1);
	}
	public String toString() {	
		return this.statestring+" [x]: "+x+" [y]:"+y;
	}

}
