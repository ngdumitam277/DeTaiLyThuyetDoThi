import java.util.Comparator;

public class StarOG implements Comparator<State> {

//Hàm kiểm tra chi phí và trạng thái
	@Override
	public int compare(State o1, State o2) {
		if((o1.openGoals() + o1.getCost()) == (o2.openGoals() + o2.getCost()))
			return 0;
		return (((o1.openGoals() + o1.getCost())  < (o2.openGoals() + o2.getCost())) ? -1 : 1);
	}


}
