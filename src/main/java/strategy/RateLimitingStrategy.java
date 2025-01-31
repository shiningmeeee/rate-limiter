package strategy;

import exception.ShutdownException;


/**
 * rate limiting(요청량 제한)에 사용되는 알고리즘 인터페이스
 */
public interface RateLimitingStrategy {
	
	/**
	 * rate limiter의 값을 증가시킨다.
	 * @throws ShutdownException
	 */
	void feed() throws ShutdownException;
	
}
