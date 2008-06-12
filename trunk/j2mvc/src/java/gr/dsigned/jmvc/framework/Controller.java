package gr.dsigned.jmvc.framework;


/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Controller {

    public Jmvc $ = null;

    public Controller() throws Exception{
    	$ = Jmvc.getIstance();
    }
}
