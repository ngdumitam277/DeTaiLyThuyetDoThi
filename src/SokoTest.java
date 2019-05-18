import java.io.File;
import java.util.Scanner;
public class SokoTest {

	public static void main(String[] args) {

		String levelpath;
		int searchtype;
		char stats;

		if(args.length < 3){

			Scanner input = new Scanner(System.in);
	
			levelpath = input.nextLine();
			searchtype = input.nextInt();
			stats = input.next().charAt(0);
		}
		else {
			levelpath = args[0];
			searchtype = Integer.parseInt(args[1]);
			stats = args[2].charAt(0);
		}


		File lvl_src = new File(levelpath);
		LevelLoader ll = new LevelLoader(lvl_src);		
		STree tree = new STree(ll.init());

		String searchstring;
		switch(searchtype) {
		case 1:
			searchstring = "BFS";
			break;
		case 2:
			searchstring = "DFS";
			break;
		case 3:
			searchstring = "UCS";
			break;
		case 4:
			searchstring = "Greedy (using OG)";
			break;
		case 5:
			searchstring = "Greedy (using MD)";
			break;
		case 6:
			searchstring = "A* (using OG)";
			break;
		case 7:
			searchstring = "A* (using MD)";
			break;
		default:
			searchstring = "nothing";
		}
		System.out.println("Running " + searchstring + " on,");
		tree.getRoot().printState();

		
		
		switch(searchtype) {
		case 1:
			if(stats == 'y')
				for(String s : tree.BFS())
					System.out.println(s);
			else
				System.out.println(tree.BFS()[0]);
			break;
		case 2:
			if(stats == 'y')
				for(String s : tree.DFS())
					System.out.println(s);
			else
				System.out.println(tree.DFS()[0]);
			break;
		case 3:
			if(stats == 'y')
				for(String s : tree.UCS())
					System.out.println(s);
			else
				System.out.println(tree.UCS()[0]);
			break;
		case 4:
			if(stats == 'y')
				for(String s : tree.GreedyOG())
					System.out.println(s);
			else
				System.out.println(tree.GreedyOG()[0]);
			break;
		case 5:
			if(stats == 'y')
				for(String s : tree.GreedyMD())
					System.out.println(s);
			else
				System.out.println(tree.GreedyMD()[0]);
			break;
		case 6:
			if(stats == 'y')
				for(String s : tree.AStarOG())
					System.out.println(s);
			else
				System.out.println(tree.AStarOG()[0]);
			break;
		case 7:
			if(stats == 'y')
				for(String s : tree.AStarMD())
					System.out.println(s);
			else
				System.out.println(tree.AStarMD()[0]);
			break;
		}
	
		File src = new File(levelpath);
		LevelLoader LL = new LevelLoader(src);	
		STree tre = new STree(LL.init());
		STree.JUSTKEEPSWIMMING = true;

		for(String s : tre.AStarOG()) {
			System.out.print("!");
			System.out.println(s);
		}
			
	}



}