package dash;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import dash.errorhandling.AppExceptionMapper;
import dash.errorhandling.GenericExceptionMapper;
import dash.errorhandling.NotFoundExceptionMapper;
import dash.filters.LoggingResponseFilter;
import dash.pojo.CommentResource;
import dash.pojo.GroupResource;
import dash.pojo.HourResource;
import dash.pojo.MessageResource;
import dash.pojo.TaskResource;
import dash.pojo.UsersResource;
import dash.pojo.PostResource;

/**
 * Registers the components to be used by the JAX-RS application
 *
 * @author plindner
 *
 */
public class DashApplicationSetup extends ResourceConfig {

	/**
	 * Register JAX-RS application components.
	 */
	public DashApplicationSetup() {
		// register application resources
		register(UsersResource.class);
		register(GroupResource.class);
		register(TaskResource.class);
		register(PostResource.class);
		register(CommentResource.class);
		register(MessageResource.class);
		register(HourResource.class);

		// register filters
		register(RequestContextFilter.class);
		register(LoggingResponseFilter.class);

		// register exception mappers
		register(GenericExceptionMapper.class);
		register(AppExceptionMapper.class);
		register(NotFoundExceptionMapper.class);

		// register features
		register(JacksonFeature.class);
		register(MultiPartFeature.class);
		register(EntityFilteringFeature.class);

		// property(EntityFilteringFeature.ENTITY_FILTERING_SCOPE,
		// new Annotation[] { UserDetailedView.Factory.get() });
	}
}
