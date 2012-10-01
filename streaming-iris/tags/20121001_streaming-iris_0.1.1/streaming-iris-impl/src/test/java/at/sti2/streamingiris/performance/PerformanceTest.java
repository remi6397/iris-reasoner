package at.sti2.streamingiris.performance;

public interface PerformanceTest {

	public void addStartTime(int count, long currentTimeMillis);

	public void addEndTime(int count, long currentTimeMillis);

}
