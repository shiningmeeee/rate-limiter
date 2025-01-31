package window;

import tracker.IndexTracker;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * sliding window 알고리즘을 사용하기 위한 SlidingWindow 객체 클래스
 */
public class SlidingWindow extends Window {
    
    public SlidingWindow(long windowSize, LocalDateTime endTime) {
        this(windowSize, endTime, 0);
    }
    
    public SlidingWindow(long windowSize, LocalDateTime endTime, long index) {
        super(windowSize, endTime, index);
    }
    
    /**
     * 이전 window, 현재 window를 참고해 index를 계산한다.
     * @return long
     */
    public long getSlidingWindowIndex() {
        Duration windowElapsedTime = Duration.between(LocalDateTime.now(), getEndTime());
        long previousWindowIndex = getPrevious().getCurrentWindowIndex();
        long currentWindowIndex = getCurrentWindowIndex();
        
        return ((previousWindowIndex * windowElapsedTime.getSeconds()) / getWindowSize()) + currentWindowIndex;
    }
    
}
