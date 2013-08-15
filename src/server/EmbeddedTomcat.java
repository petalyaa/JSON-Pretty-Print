package server;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;

public class EmbeddedTomcat {
	
	private static final int PORT = 8088;

	private static final String APP_BASE = ".";
	
	private static final String ROOT_PATH = "webapps/ROOT";

	private static final String BASE_DIR = "tomcat/";

	private static final String CONTEXT_PATH = "/";

	public static void main(String[] args) {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(PORT);

		tomcat.setBaseDir(BASE_DIR);
		tomcat.getHost().setAppBase(APP_BASE);
		
		// Add AprLifecycleListener
		StandardServer server = (StandardServer) tomcat.getServer();
		AprLifecycleListener listener = new AprLifecycleListener();
		server.addLifecycleListener(listener);
		try {
			tomcat.addWebapp(CONTEXT_PATH, ROOT_PATH);
			tomcat.start();
			tomcat.getServer().await();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (LifecycleException e) {
			e.printStackTrace();
		}
	}
}
