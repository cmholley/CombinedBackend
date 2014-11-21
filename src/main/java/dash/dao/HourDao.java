package dash.dao;

import java.util.List;
import dash.pojo.Hour;
import dash.pojo.Task;

/*
 * @Author tswensen
 */
public interface HourDao {

	public List<HourEntity> getHours(int numberOfHours, Long startIndex,
			boolean onlyPending, String orderBy);

	public List<HourEntity> getHours(int numberOfHours, Long startIndex,
			Task task, boolean onlyPending);

	public int getNumberOfHours();

	/**
	 * Returns a hour given its id
	 *
	 * @param id
	 * @return
	 */
	public HourEntity getHourById(Long id);

	public void deleteHourById(Hour hour);

	public Long createHour(HourEntity hour);

	public void updateHour(HourEntity hour);

	/** removes all hours */
	public void deleteHours();

}
