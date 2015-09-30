package dash.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dash.multitenancy.ThreadLocalContextUtil;

/**
 * Servlet Filter implementation class MultiTenantIdentifierFilter
 */
@WebFilter(description = "This filter uses an HTTP request to identify the tenant the request came from", urlPatterns = { "/MultiTenantIdentifierFilter" })
public class MultiTenantIdentifierFilter implements Filter {

    /**
     * Default constructor. 
     */
    public MultiTenantIdentifierFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(response instanceof HttpServletResponse){
			// Retrieve user id, password and tenant Id from the login page
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			
			String tenantId = httpRequest.getHeader("X-TenantId");
			
			// Set the tenant Id into a ThreadLocal object
			ThreadLocalContextUtil.setTenantId(tenantId);
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
