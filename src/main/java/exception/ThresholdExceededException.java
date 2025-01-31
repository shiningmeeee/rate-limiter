package exception;

/**
 * 설정한 threshold를 넘어서는 경우 발생하는 예외
 */
public class ThresholdExceededException extends RuntimeException {
	
	private long threshold;
		
	public ThresholdExceededException(String message, Throwable cause, long threshold) {
		super(message, cause);
		this.threshold = threshold;
	}
	
	public ThresholdExceededException(String message, long threshold) {
		super(message);
		this.threshold = threshold;
	}
	
	public ThresholdExceededException(String message) {
		super(message);
	}
	
	public ThresholdExceededException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public long getThreshold() {
		return threshold;
	}
	
	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}
	
	
}
