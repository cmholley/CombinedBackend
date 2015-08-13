package dash.tran;

import org.springframework.transaction.annotation.Transactional;

import dash.errorhandling.AppException;
import dash.pojo.Group;
import dash.pojo.Hour;
import dash.service.GroupService;
import dash.service.HourService;

public class HourTransactionImpl {
	
	@Transactional("transactionManagerCHW")
	public long createHour1(Hour hour, int ds, HourService hourService) throws AppException {
		return hourService.createHour(hour, ds);
	}

	@Transactional("transactionManagerVMA")
	public long createHour2(Hour hour, int ds, HourService hourService) throws AppException {
		return hourService.createHour(hour, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void deleteHour1(Hour hour, int ds, HourService hourService) throws AppException {
		hourService.deleteHour(hour, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public void deleteHour2(Hour hour, int ds, HourService hourService) throws AppException {
		hourService.deleteHour(hour, ds);
	}
	
	@Transactional("transactionManagerCHW")
	public void approveHour1(Hour hour, int ds, HourService hourService) throws AppException {
		hourService.deleteHour(hour, ds);
	}
	
	@Transactional("transactionManagerVMA")
	public void approveHour2(Hour hour, int ds, HourService hourService) throws AppException {
		hourService.deleteHour(hour, ds);
	}
}
