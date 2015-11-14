package org.imu.olapbrowser.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imu.olapbrowser.utility.OlapUtility;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class OlapGet
 */
public class OlapGet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   private OlapUtility olapUtility = OlapUtility.getInstance();
   
    public OlapGet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MainModule(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MainModule(request,response);
	}

	private void MainModule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer action = Integer.parseInt(request.getParameter("action"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		if (action == 0 ) { //get cube
			out.print(olapUtility.GetCubes());
		}
		if (action == 1 ) { //get data from mdx
			String mdx=request.getParameter("mdx");
			if (mdx != null) {
				mdx = java.net.URLDecoder.decode(mdx, "UTF-8");
				JsonArray o = (JsonArray)new JsonParser().parse(mdx);
				mdx = o.get(0).getAsJsonObject().get("result").getAsString();
				out.print(olapUtility.getDataMdx(mdx));
			}
		}
		out.flush();
	}
}
