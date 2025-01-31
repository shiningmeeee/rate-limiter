package tracker;

/**
 * IndexTracker를 상속받은 클래스
 * 
 * threshold 변수를 통해 index가 특정한 임계치를 넘었는지 확인하는 용도로 사용한다.
 */
public class ThresholdTracker extends IndexTracker {
		
	private long threshold = 12;
	    
    public ThresholdTracker() {
		super();
	}
	
	public ThresholdTracker(long index) {
		super(index);
	}
	
	public ThresholdTracker(long index, long threshold) {
		super(index);
		this.threshold = threshold;
	}
	
	public boolean isOverThreshold() {
		return getIndex() >= threshold;
	}
	
	public long getThreshold() {
		return threshold;
	}
	
	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}
	
}
