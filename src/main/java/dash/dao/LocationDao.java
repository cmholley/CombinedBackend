package dash.dao;

import java.util.List;

import dash.pojo.Location;

/**
 * @Author plindner
 */
public interface LocationDao {
	public List<Location> getLocations(String orderByInsertionDate);
	public List<Location> getRecentLocations(int numberOfDaysToLookBack);

	public Location getLocationByName(String name);
	public Location getLocationById(Long id);

	public void deleteLocation(Location location);

	public Long createLocation(Location location);

	public void updateLocation(Location location);

	public void deleteLocations();
}
