package exception;

/**
 * 프로그램 또는 스레드의 종료를 요청시킬 때 발생하는 예외
 */
public class ShutdownException extends RuntimeException {
	
	public ShutdownException(String message) {
		super(message);
	}
	
	public ShutdownException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
