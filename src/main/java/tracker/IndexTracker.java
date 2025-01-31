package tracker;

/**
 * 특정한 값(index)를 추적하는 클래스
 * 
 * index는 increment(), decrement()를 호출하거나 임의의 값으로 주는 방식으로 값을 변경할 수 있다.
 */
public class IndexTracker {
	
	private long index;
	
	public IndexTracker() {
		this.index = 0;
	}
	
	public IndexTracker(long index) {
		this.index = index;
	}
	
	public long getIndex() {
		return this.index;
	}
	
	public void setIndex(long index) {
		this.index = index;
	}
	
	public void increment() throws ArithmeticException {
		if(Long.MAX_VALUE == this.index) throw new ArithmeticException("long type overflow");
		this.index++;
	}
	
	public void decrement() throws ArithmeticException {
		if(Long.MIN_VALUE == this.index) throw new ArithmeticException("long type overflow");
		this.index--;
	}
	
}
