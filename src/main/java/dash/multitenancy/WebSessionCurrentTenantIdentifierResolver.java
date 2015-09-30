package dash.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;


public class WebSessionCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    private static final Logger LOG = LoggerFactory.getLogger(WebSessionCurrentTenantIdentifierResolver.class);

    @Autowired
    private HttpServletRequest servletRequest;

    @Override
    public String resolveCurrentTenantIdentifier() {

        //String tenantId = servletRequest.getHeader("X-TenantId");
    	String tenantId = "tenantCHW";
        LOG.info(MessageFormat.format("Found TenantId=\"{0}\"", tenantId));

        return tenantId;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}
