package strategy;

import exception.ShutdownException;
import exception.ThresholdExceededException;
import tracker.ThresholdTracker;
import window.SlidingWindow;

import java.time.LocalDateTime;

/**
 * SlidingWindow 구현체
 * feed()가 호출되면 sliding window 알고리즘으로 index를 계산한다.
 * index가 threshold를 넘어서면 sleepDuration 만큼 Thread.sleep()을 수행한다.
 * 
 * shutdownPolicy를 true로 설정하면 ShutdownException이 발생할 수 있다.
 * exponentialBackoffPolicy에 따라 sleepDuration을 점진적으로 늘려, 스레드를 더 오래 대기시킬 수 있다.
 */
public class SlidingWindowStrategy implements RateLimitingStrategy {
     
    private final SlidingWindow window;
    
    private boolean shutdownPolicy = false;
    
    private boolean exponentialBackOffPolicy = false;
    
    private double sleepDuration = 2.0;
        
    private ThresholdTracker indexTracker;
    
    private ThresholdTracker sleepTracker;
        
    public SlidingWindowStrategy(long windowSize, int threshold) {
        this.window = new SlidingWindow(windowSize, LocalDateTime.now().plusSeconds(windowSize));
        this.indexTracker = new ThresholdTracker(0, threshold);
        this.sleepTracker = new ThresholdTracker();
    }
    
    /**
     * rate limiter의 index 값을 계산한다.
     * 
     * sliding window 알고리즘에 따라 window가 닫혔다면 새로운 window를 생성한다.
     * rate limiter의 index가 threshold를 넘으면 sleep()을 호출한다.
     * @throws ShutdownException
     */
    @Override
    public void feed() throws ShutdownException {
        
        if(window.isClosed()) {
            
            long threshold = indexTracker.getThreshold();
            
            window.openNewWindow();
            this.indexTracker = new ThresholdTracker(0, threshold);
            this.sleepTracker = new ThresholdTracker();
        }
        
        setTrackerIndex();
        
        if(indexTracker.isOverThreshold()) {
            try {
                sleepTracker.increment();
                sleep();
            } catch(ThresholdExceededException thresholdExceededException) {
                throw new ShutdownException(thresholdExceededException.getMessage());
            } catch(ArithmeticException arithmeticException) {
                throw new ShutdownException(arithmeticException.getMessage());
            }
        }
        
    }
    
    /**
     * Thread.sleep()을 호출한다.
     * 
     * shutdownPolicy 값에 따라 ThresholdExceededException을 던질 수 있다.
     * exponentialBackOffPolicy 값에 따라 sleepDuration을 지수적으로 증가시킨다.
     * @throws ThresholdExceededException
     */
    private void sleep() throws ThresholdExceededException {
        if(shutdownPolicy && sleepTracker.isOverThreshold()) throw new ThresholdExceededException("shutdown exception", sleepTracker.getThreshold());
        
        long sleepCount = sleepTracker.getIndex();
        
        if(exponentialBackOffPolicy) {
            sleepDuration = Math.pow(sleepDuration, sleepCount);
        }
        
        try {
            Thread.sleep((long)sleepDuration);
        } catch (InterruptedException e) {}
        
    }
    
    /**
     * SlidingWindow의 index 값을 계산한다.
     * 
     * 이전 window가 존재하는 경우 sliding window 알고리즘에 의해 이전 window, 현재 window를 모두 참고하여 index를 계산한다.
     */
    private void setTrackerIndex() {
        long index = 0;
        
        if(window.getPrevious() != null) {
            index = window.getSlidingWindowIndex();
        } else {
            index = window.getCurrentWindowIndex();
        }
        
        indexTracker.setIndex(index);
    }
    
    public void setShutdownPolicy(boolean shutdownPolicy) {
        this.shutdownPolicy = shutdownPolicy;
    }
    
    public void setExponentialBackOffPolicy(boolean exponentialBackOffPolicy) {
        this.exponentialBackOffPolicy = exponentialBackOffPolicy;
    }
    
    public void setShutdownThreshold(long shutdownThreshold) {
        this.sleepTracker.setThreshold(shutdownThreshold);
    }
    
}