package gr.dsigned.jmvc.framework;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.db.DB;
import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.libraries.Input;
import gr.dsigned.jmvc.libraries.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Jmvc {

    private static Jmvc instance;
    public HttpServletRequest request;
    public ServletContext context;
    public HttpServletResponse response;
    /*
     * These are the default auto-loaded libraries To load
     * others use Jmvc.loadLibrary()
     */
    public DB db;
    public Input input;
    public Session session;

    private Jmvc() throws Exception {
        init();
    }

	public static Jmvc getIstance() throws Exception {
		return (instance == null) ? new Jmvc() : instance;		
	}

    public static Jmvc getIstance() throws Exception {
        if (instance == null) {
            return new Jmvc();
        } else {
            return instance;
        }
    }

    /**
     * This method passes the current request object to the
     * class. This is done before every request is
     * processed.
     * 
     * @param req
     *            (passed from the adapter)
     */
    public void setRequest(HttpServletRequest req) {
        this.request = req;
        this.input = new Input(req);
        if (Settings.AutoLoad.SESSION.loadIt()) {
            session = new Session(req);
        }
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    /**
     * Passes the current context to this object.
     * 
     * @param cont
     *            (the context, passed from the adapter)
     */
    public void setContext(ServletContext cont) {
        this.context = cont;
    }

    /**
     * The response object is passed for direct manipulation
     * 
     * @param resp
     *            (passed from the adapter)
     */
    public void setResponse(HttpServletResponse resp) {
        this.response = resp;
    }

    /**
     * Convenience method created so you can set the
     * environment methods all at once
     * (setResponse,setRequest & setContext)
     * 
     * @param req
     * @param resp
     * @param cont
     */
    public void setEnv(HttpServletRequest req, HttpServletResponse resp, ServletContext cont) {
        this.request = req;
        this.response = resp;
        this.context = cont;
        this.input = new Input(req);
        if (Settings.AutoLoad.SESSION.loadIt()) {
            session = new Session(req);
        }
    }

    /**
     * Loads a template and replaces tags with the variables
     * stored in the HashMap. The key is used to find the
     * tag in the template.
     * 
     * @param view_name
     *            The name of the template (the path and
     *            extension is added automatically)
     * @param data
     *            LinkedHashMap with the key being the name
     *            of the tag to be replaced and the value
     *            the replacement.
     */
    public void loadView(String view_name, LinkedHashMap<String, String> data) throws IOException {
        String template = "";
        template = Jmvc.readWithStringBuilder(context.getRealPath("/") + "/views/" + view_name + ".html");
        PrintWriter out = response.getWriter();
        if (data != null) {
            for (String key : data.keySet()) {
                template = template.replaceAll("<% ?" + key + " ?%>", data.get(key));
            }
        }
        template = Parser.parse(template);
        out.println(template);
        out.flush();
    }

    public static void loadErrorPage(Exception e, HttpServletResponse response, ServletContext cont) {
        try {
            String template = "";
            template = Jmvc.readWithStringBuilder(cont.getRealPath("/") + "error_pages" + File.separator + "404.html");
            PrintWriter out = response.getWriter();
            String message = e.toString();
            String html = "<h1>Something went wrong: </h1>";
            html += "<pre>" + message + "</pre>";
            StackTraceElement traceElements[] = e.getStackTrace();
            html += "<h2>Stacktrace: </h2>";
            html += "<pre>";
            for (StackTraceElement elem : traceElements) {
                html += elem.getClassName() + ": " + elem.getMethodName() + " on line:" + elem.getLineNumber() + "\n";
            }
            html += "</pre>";
            Throwable c = e.getCause();
            if (c != null) {
                String cause = e.getCause().toString();
                html += "<h2>Exception caused by: </h2>";
                html += "<pre>" + cause + "</pre>";
                html += "<pre>";
                StackTraceElement[] causeTraceElements = e.getCause().getStackTrace();
                for (StackTraceElement elem : causeTraceElements) {
                    html += elem.getClassName() + ": " + elem.getMethodName() + " on line:" + elem.getLineNumber()
                            + "\n";
                }
                html += "</pre>";
            }
            template = template.replace("<% error %>", html);
            out.println(template);
            out.flush();
        } catch (Exception exc) {
            System.out.println(e.toString());
            exc.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private static LinkedHashMap<String, String> getParamsMap(String path) {
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        ArrayList<String> uriParts = new ArrayList<String>(Arrays.asList(path.split("/")));
        for (int i = 0; i < uriParts.size(); i++) {
            if (i + 1 < uriParts.size()) {
                params.put(uriParts.get(i), uriParts.get(i + 1));
            } else {
                params.put(uriParts.get(i), "");
            }
        }
        return params;
    }

    @SuppressWarnings("unused")
    private static ArrayList<String> getParamsArray(String path) {
        ArrayList<String> params = new ArrayList<String>(Arrays.asList(path.split("/")));
        return params;
    }

    /**
     * Reads the file from disk and returns the content as a
     * string. Used to load templates.
     * 
     * @param fileName
     *            The name of the file to be read.
     * @return Returns the contents of the file.
     * @throws java.io.IOException
     */
    static String readWithStringBuilder(String fileName) throws IOException {
        FileReader fr = new FileReader(fileName); // ~3MB
        BufferedReader br = new BufferedReader(fr);
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = br.readLine()) != null) {
            result.append(line + "\n");
        }
        br.close();
        fr.close();
        return result.toString();
    }

    /**
     * Loads the model with the name passed in.
     * 
     * @param modelName
     *            The name of the model you need to load
     * @return the model
     */
    @SuppressWarnings("unchecked")
    public <T extends Model> T loadModel(String modelName) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        Class<T> c = (Class<T>) Class.forName("gr.dsigned.jmvc.models." + modelName);
        T m = c.newInstance();
        return m;
    }

    /**
     * Loads the Renderer with the name passed in.
     * 
     * @param rendererName
     *            The name of the Renderer you need to load
     * @return the Renderer
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public <T extends Renderer> T loadRenderer(String rendererName) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Class<T> c = (Class<T>) Class.forName("gr.dsigned.jmvc.renderers." + rendererName);
        T m = c.newInstance();
        return m;
    }

    /**
     * Loads the requested library.
     * 
     * @param libraryName
     *            The name of the library
     * @return The library object you named.
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public <T extends Library> T loadLibrary(String libraryName) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        Class<T> c = (Class<T>) Class.forName("gr.dsigned.jmvc.libraries." + libraryName);
        T m = c.newInstance();
        return m;
    }
}
