package gr.dsigned.jmvc;

import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.framework.Utils;
import gr.dsigned.jmvc.libraries.Input;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Adapter extends HttpServlet {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * This is the entry point of your application. Given the URI of the request
	 * it loads the appropriate controller and passes to it the appropriate
	 * environment variables (The GET, POST and Context objects).
	 * 
	 * @todo the adapter does not load the correct method of the controller yet.
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	@SuppressWarnings("unchecked")
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			response.setContentType("text/html;charset="+ Settings.DEFAULT_ENCODING);
			out = response.getWriter();
			String path = request.getRequestURI();
			Class<Controller> c = (Class<Controller>) Class.forName("gr.dsigned.jmvc.controllers." + Utils.capitalize(Input.getController(path).get("controller")));
			gr.dsigned.jmvc.framework.Controller o = c.newInstance();
			o.$.setEnv(request, response, this.getServletContext());			
			Method m = c.getMethod(Input.getController(path).get("method"), new Class[0]);
			if (m.getModifiers() != java.lang.reflect.Modifier.PRIVATE) {
				m.invoke(o, new Object[0]);
			} else {
				Exception e = new Exception("Method not found");
				this.getServletContext().log(e.getMessage());
				Jmvc.loadErrorPage(e, response, this.getServletContext());
			}
		} catch (Exception e) {
			this.getServletContext().log(e.getMessage());
			Jmvc.loadErrorPage(e, response, this.getServletContext());
		} finally {
			out.close();
		}
	}

	

	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}
}
