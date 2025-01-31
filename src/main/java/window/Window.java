package window;

import tracker.IndexTracker;

import java.time.LocalDateTime;

/**
 * 특정한 시간 간격을 가지는 객체 클래스
 * 시간 간격은 windowSize와 endTime으로 표현한다.(startTime = endTime - windowSize)
 * 
 * IndexTracker 객체를 통해 window 내에서 특정한 값을 기록할 수 있다.
 * previous window 객체를 통해 이전 window에 대한 정보를 추적한다.
 */
public class Window {
	
	private Window previous;
	
	// 모든 시간은 ms 단위
    private long windowSize;
    
    private LocalDateTime endTime;
    
    private IndexTracker tracker;
	
	Window(long windowSize, LocalDateTime endTime, long index) {
		this.windowSize = windowSize;
        this.endTime = endTime;
        this.tracker = new IndexTracker(index);
	}
	public Window getPrevious() {
        return this.previous;
    }
    
    public void setPrevious(Window previous) {
        this.previous = previous;
    }
	
	/**
	 * 현재 window의 endTime이 지났는지 평가한다.
	 * @return boolean
	 */
	public boolean isClosed() {
        return LocalDateTime.now().isAfter(endTime);
    }
	
	public long getWindowSize() {
        return this.windowSize;
    }
    
    public LocalDateTime getEndTime() {
        return this.endTime;
    }
	
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
    
    public long getCurrentWindowIndex() {
        return this.tracker.getIndex();
    }
    
    public void setCurrentWindowIndex(long index) {
        this.tracker.setIndex(index);
    }
    
    public IndexTracker getTracker() {
        return this.tracker;
    }
	
	/**
	 * 새로운 window를 생성한다.
	 * new 객체를 생성하지 않고 현재 객체를 그대로 사용하며, 멤버변수의 값만 변경한다.
	 */
	public void openNewWindow() {		
		//newWindow로 만들기 위해 변수 초기화
		setEndTime(getEndTime().plusSeconds(getWindowSize()));
		setCurrentWindowIndex(0);
		
        Window previousWindow;
		
		// window size가 두 바퀴 이상 돌았을 경우 previous window를 비운채로 초기화 해줘야함
		if(isMultipleWindowsPassed()) {
			previousWindow = new Window(getWindowSize(), getEndTime().minusSeconds(windowSize), 0);
		} else {
			previousWindow = new Window(getWindowSize(), getEndTime(), getCurrentWindowIndex());
		}
        
        setPrevious(previousWindow);        
        
	}
	
	private boolean isMultipleWindowsPassed() {
		return isClosed() && LocalDateTime.now().isAfter(endTime.plusSeconds(windowSize));
	}
	
}
