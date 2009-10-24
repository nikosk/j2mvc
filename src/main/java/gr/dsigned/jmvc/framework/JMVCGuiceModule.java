package gr.dsigned.jmvc.framework;

import javax.persistence.EntityManager;

import com.google.inject.AbstractModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JMVCGuiceModule extends AbstractModule {
	private final EntityManager em;
        private final HttpServletRequest request;
        private final HttpServletResponse response;

	public JMVCGuiceModule(EntityManager em, HttpServletRequest request, HttpServletResponse response) {
		this.em = em;
                this.request = request;
                this.response = response;
	}

	@Override
	protected void configure() {
		bind(EntityManager.class).toInstance(em);		
		bind(HttpServletRequest.class).toInstance(request);
		bind(HttpServletResponse.class).toInstance(response);
	}

}
