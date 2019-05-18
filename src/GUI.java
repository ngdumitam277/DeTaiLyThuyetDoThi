import java.io.File;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



public class GUI extends Application {
//	Giao diện
	private Canvas canvas = new Canvas();
	private GridPane menu = new GridPane();
	private BorderPane pane = new BorderPane();
	private ComboBox<String> comboBox = new ComboBox<String>();
//	Biến vào
	private File mapFile = null;
	private Stage stage = null;
	private ObservableList<String> stat = FXCollections.observableArrayList();
	private STree tree = null;
	private String solution = "";
	private int step = 0;
	private State node;
	private String fileName;
	private boolean loadMap = false;
	private boolean computeSolution = false;
	private Text filePath = new Text(" ");
	

	public static void main(String[] args) {

		launch(args);
	}
	

	public void start(Stage primaryStage) throws Exception {
		
		

//		chạy file map
		Button btLoadMap = new Button("Nạp vòng chơi: ");
		btLoadMap.setOnAction( e -> handleLoadMap());

		Text searchTypes = new Text("Các thuật toán cần chạy [1-7]:");
//		menu xổ xuống
		
		comboBox.setMaxWidth(100);
        comboBox.getItems().add("1) BFS");
        comboBox.getItems().add("2) DFS");
        comboBox.getItems().add("3) UCS");
        comboBox.getItems().add("4) A* (với heuristic là open goals)");
        comboBox.getItems().add("5) A* (với heuristic là  Manhattan distance)"); 
        comboBox.setValue("1) BFS");	
//		Box tính toán
		Button btStart = new Button("Kết quả tính toán:");
		btStart.setOnAction(e-> handleComputeResult());
		
//		Nút đi tiếp từ người chơi
		Button btNext = new Button("Đi tiếp (↵)");
		btNext.setOnAction(e-> handleNextStep());
		

		ListView<String> statList = new ListView<String>();
		statList.setItems(this.stat);
		
		btLoadMap.setFocusTraversable(false);
		searchTypes.setFocusTraversable(false);
		comboBox.setFocusTraversable(false);
		btStart.setFocusTraversable(false);
		statList.setFocusTraversable(false);
		btNext.setFocusTraversable(false);
		
		menu.setPadding(new Insets(11, 12, 13, 14));
		menu.setHgap(5);
		menu.setVgap(5);
		menu.add(btLoadMap, 0, 0);
		menu.add(filePath, 0, 1, 2, 1);
	
		menu.add(searchTypes, 0, 2);
		menu.add(comboBox, 1, 2);
		menu.add(btStart, 0, 3);
		menu.add(statList, 0, 4);
		menu.add(btNext, 0, 5);
		
		Text instruction = new Text("Trước khi trò chơi được giả, bạn có thể tự chơi với các phím: "
				+ "\n Đi lên: ↑"
				+ "\n Đi xuống: ↓"
				+ "\n Đi qua trái: ←"
				+ "\n Đi qua phải: → "
				+ "\n Undo lại: L");
		
		VBox containerCanvas = new VBox(50);
		containerCanvas.getChildren().addAll(canvas,instruction);
		containerCanvas.setAlignment(Pos.CENTER);
		
		
		
		pane.setCenter(containerCanvas);
		pane.setRight(menu);
	     
	     Scene scene = new Scene(pane, 900, 600, Color.BLACK);
	     
	     scene.setOnKeyPressed(e->handleKeyPressed(e));
	     
	     stage = primaryStage;
	     stage.setTitle("Trò chơi đẩy thùng");
	     stage.setScene(scene);
	     stage.show();
	}
	
	
	private void handleKeyPressed(KeyEvent keyEvent) {
		if(keyEvent.getCode()== KeyCode.ENTER) {
			this.handleNextStep();
		}
		if(this.loadMap ==true && this.computeSolution == false) {
			switch (keyEvent.getCode()) {
            case UP:
                	this.handleMove('u');
                	break;
            case DOWN:
            		this.handleMove('d');
            		break;
            case LEFT:
	        		this.handleMove('l');
	        		break;
            case RIGHT:
	        		this.handleMove('r');
	        		break;
            case L:
	        		this.handleMove(' ');
	        		break;
			default:
				break;
			}
		}
	}
	
// Lấy trạng thái khi di chuyển phím mũi tên
	private void handleMove(char dir) {
		State child = null;
		switch (dir) {
        case 'u':
            if(this.node.isUpValid()) {
            		child = new State(node, 'u');
            		printState(child);
            		this.node = child;
            }
            	break;
        case 'd':
            if(this.node.isDownValid()) {
            		child = new State(node, 'd');
            		printState(child);
            		this.node = child;
            }
            	break;
        case 'l':
            if(this.node.isLeftValid()) {
            		child = new State(node, 'l');
            		printState(child);
            		this.node = child;
            }
            	break;
        case 'r':
            if(this.node.isRightValid()) {
            		child = new State(node, 'r');
            		printState(child);
            		this.node = child;
            }
            	break;
        case ' ':           
        		State parent = node.getParent();
        		printState(parent);
        		this.node = parent;
            	break;
		default:
			break;
		}
	}

	private void handleNextStep() {
		char[] solutionArray = solution.toCharArray();
		State child = null;
		if(this.computeSolution==true && step<solutionArray.length) {
			child = new State(node, solutionArray[step]);
			printState(child);
			this.node = child;
			this.step++;
		}			
	}
	
//  Tính toán kết quả
	private void handleComputeResult() {
		this.mapFile = null;
		this.stat.clear();
		this.tree = null;
		STree.JUSTKEEPSWIMMING = true;
		this.solution = "";
		this.step = 0;
		this.node = null;
		this.computeSolution = true;

		if(this.fileName != null) {
			this.mapFile = new File(this.fileName);
			LevelLoader ll = new LevelLoader(this.mapFile);		
			this.tree = new STree(ll.init());
			this.node = this.tree.getRoot();
			printState(this.tree.getRoot());
		}
		String searchtype = ((String)comboBox.getValue()).substring(0, 1);


		switch(searchtype) {
			case "1":
				for(String statItem : this.tree.BFS()) {
					this.stat.add(statItem);
				}
				solution =  this.stat.get(0);
				break;
			case "2":
				for(String statItem : tree.DFS()) {
					this.stat.add(statItem);
				}
				solution =  this.stat.get(0);
				break;
			case "3":
				for(String statItem : tree.UCS()) {
					this.stat.add(statItem);
				}
				solution =  this.stat.get(0);
				break;
			case "4":
				for(String statItem : tree.AStarOG()) {
					this.stat.add(statItem);
				}
				solution =  this.stat.get(0);
				break;
			case "5":
				for(String statItem : tree.AStarMD()) {
					this.stat.add(statItem);
				}
				solution =  this.stat.get(0);
				break;
		}
	}

// Load bản đồ
	private void handleLoadMap() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		this.mapFile = fileChooser.showOpenDialog(this.stage);
		this.fileName = this.mapFile.getPath();
		LevelLoader ll = new LevelLoader(this.mapFile);		
		this.tree = new STree(ll.init());
		this.node = this.tree.getRoot();
		printState(this.tree.getRoot());
		this.stat.clear();
		this.loadMap = true;
		this.computeSolution = false;
		filePath.setText(this.fileName);

	}

	private void printState(State state) {
		GraphicsContext gc= canvas.getGraphicsContext2D();
		char[][] map = state.getState();
		
		
		int height = map.length;
		int width = 0;
		for(char[] row : map) {
			int j = 0;
			for(char c : row) {
				j++;
			}
			if (j>=width) {
				width=j;
			}		
		}
		
		Image box = new Image("images/box.png");
		Image player = new Image("images/player.png");
		Image floor = new Image("images/floor.png");
		Image target = new Image("images/target.png");
		Image wall = new Image("images/wall.png");
		Image success = new Image("images/success.png");
			
		canvas.setWidth(width*32);
		canvas.setHeight(height*32);
		gc.setFill(Color.WHITE);
		gc.fillRect(0,0,width*32, height*32);
		
		for(int h = 0; h< height;h++) {
			for(int w = 0; w< width;w++) {
				gc.drawImage(floor, w*32, h*32);
			}
		}
		

		for(int h = 0; h< map.length;h++) {
			for(int w = 0; w< map[h].length;w++) {
				switch(state.level[h][w]) {
				case '#':
					gc.drawImage(wall, w*32, h*32);
					break;
				case '@':
					gc.drawImage(player, w*32, h*32);
					break;
				case '$':
					gc.drawImage(box, w*32, h*32);
					break;
				case ' ':
					gc.drawImage(floor, w*32, h*32);
					break;
				case '.':
					gc.drawImage(target, w*32, h*32);
					break;
				case '*':
					gc.drawImage(success, w*32, h*32);
					break;
				case '+':
					gc.drawImage(player, w*32, h*32);
					break;
				}
			}
		}
	}
}
