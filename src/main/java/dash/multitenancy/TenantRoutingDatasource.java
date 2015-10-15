package dash.multitenancy;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantRoutingDatasource extends AbstractRoutingDataSource {

	protected Object determineCurrentLookupKey() {
		// Retrieve the current tenant id from the thread local object
		// where it was stored in the filter earlier
		String lookupKey = (String) ThreadLocalContextUtil.getTenantId();
		return lookupKey;
	}
}
