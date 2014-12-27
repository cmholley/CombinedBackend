package dash.dao;

import java.util.List;

import dash.pojo.Hour;
import dash.pojo.Task;

/*
 * @Author tswensen
 */
public interface HourDao {

	public List<Hour> getHours(int numberOfHours, Long startIndex,
			boolean onlyPending, String orderBy);

	public List<Hour> getHours(int numberOfHours, Long startIndex,
			Task task, boolean onlyPending);

	public int getNumberOfHours();

	/**
	 * Returns a hour given its id
	 *
	 * @param id
	 * @return
	 */
	public Hour getHourById(Long id);

	public void deleteHourById(Hour hour);

	public Long createHour(Hour hour);

	public void updateHour(Hour hour);

	/** removes all hours */
	public void deleteHours();

}
