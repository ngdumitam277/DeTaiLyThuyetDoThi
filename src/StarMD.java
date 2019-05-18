import java.util.Comparator;

public class StarMD implements Comparator<State> {

// Tính toán Mannhatan
	@Override
	public int compare(State o1, State o2) {
		if((o1.manhDist() + o1.getCost()) == (o2.manhDist() + o2.getCost()))
			return 0;
		return (((o1.manhDist() + o1.getCost())  < (o2.manhDist() + o2.getCost())) ? -1 : 1);
	}

}
