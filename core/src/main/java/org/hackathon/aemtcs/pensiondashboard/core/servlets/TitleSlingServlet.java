package org.hackathon.aemtcs.pensiondashboard.core.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

@SlingServlet(paths = "/bin/company/titleservlet")
public class TitleSlingServlet extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = -3960692666512058118L;

	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		response.getWriter()
				.print("<h1>Sling Servlet injected this title</h1>");
	}
}
