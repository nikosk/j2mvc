package gr.dsigned.jmvc;

/**
 *
 * @author Chris Corbyn <chris@w3style.co.uk>
 */

/**
 * Intercepts the request before it hits the front controller.
 * This filter is checking if the request that's been passed is for an actual file.
 * If the request is for a real file, the file is served and the servlet is skipped.
 */
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import java.net.URL;
import java.net.MalformedURLException;
import javax.servlet.http.HttpServletRequest;

/**
 * Checks the request for real files and intercepts the controller where needed.
 */
public class FileFilter implements Filter {

    /**
     * Filter configuration.
     */
    protected FilterConfig filterConfig;

    /**
     * Required init method to get config.
     * @param FilterConfig filterConfig
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Runs this filter.
     * @param ServletRequest request
     * @param ServletResponse response
     * @param FilterChain filterChain
     * @throws ServletException
     * @throws IOException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ctxPath = httpRequest.getContextPath();
        String requestUri = httpRequest.getRequestURI().substring(ctxPath.length());
        if (httpRequest.getMethod().equals("GET") && isFile(requestUri)) {
            serveFile(requestUri, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Checks if the servlet context is able to get a resource at this path.
     * @param String path
     * @return boolean
     * @throws IOException
     */
    private boolean isFile(String path) throws IOException {
        URL url = null;
        ServletContext context = filterConfig.getServletContext();
        try {
            url = context.getResource(path);
        } catch (MalformedURLException e) {
            return false;
        }
        if (url == null) {
            return false;
        }
        return true;
    }

    /**
     * Serves the file if possible.
     * If an error occurs, either a IOException will be raised, or nothing will be streamed
     * depending upon the cause of the error.
     * @param String path
     * @param ServletResponse response
     * @throws IOException
     */
    private void serveFile(String path, ServletResponse response) throws IOException {
        URL url = null;
        ServletContext context = filterConfig.getServletContext();
        try {
            url = context.getResource(path);
        } catch (MalformedURLException e) {
            return;
        }
        if (url == null) {
            return;
        }
        String mimeType = context.getMimeType(path);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        ServletOutputStream os = null;
        InputStream is = null;
        try {
            os = response.getOutputStream();
            is = url.openStream();
            response.setContentType(mimeType);
            response.setContentLength(is.available());
            byte b[] = new byte[8192];
            int c  = is.read(b);
            while (c > -1) {
                os.write(b);
                os.flush();
                c = is.read(b);
            }
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Required destroy() method.
     */
    public void destroy() {
        filterConfig = null;
    }
    }

