package dash.multitenancy;

public class ThreadLocalContextUtil {
	 private static final ThreadLocal<String> contextHolder =
	            new ThreadLocal<String>();
	 
	   public static void setTenantId(String tenantId) {
	      contextHolder.set(tenantId);
	   }
	 
	   public static String getTenantId() {
	      return (String) contextHolder.get();
	   }
	 
	   public static void clearTenant() {
	      contextHolder.remove();
	   }

}