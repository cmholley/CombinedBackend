package dash.multitenancy;

import org.hibernate.service.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vtajzich
 * Date: 8/21/13
 */
public class SimpleMultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private Map<String, DataSource> dataSourceMap;

    @Override
    protected DataSource selectAnyDataSource() {
        return (DataSource) dataSourceMap.values().toArray()[0];
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return dataSourceMap.get(tenantIdentifier);
    }

    public Map<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }
}
