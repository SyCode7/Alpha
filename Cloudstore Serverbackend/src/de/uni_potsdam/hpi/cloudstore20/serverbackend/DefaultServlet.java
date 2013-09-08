package de.uni_potsdam.hpi.cloudstore20.serverbackend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.uni_potsdam.hpi.cloudstore20.meta.CommunicationInformation;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList.DataListException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList.DataListTest;

/**
 * Servlet implementation class DefaultServlet
 */
@WebServlet("/DefaultServlet")
public class DefaultServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DefaultServlet() {

		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String content = request.getParameter("message");

		if (content == null) {
			return;
		}

		PrintWriter writer = response.getWriter();

		writer.println("<html>");
		writer.println("<head><title>Hello World Servlet</title></head>");
		writer.println("<body>");

		if (content.contains(CommunicationInformation.dataList.toString())) {
			try {
				writer.println("answer=" + DataListTest.getSampleDataList("servlet").getClassAsString());
			} catch (DataListException e) {}
		}

		writer.println("<body>");
		writer.println("</html>");

		writer.close();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		DatabaseStorageFake.getInstance().addContent((String) request.getAttribute("param-1"));

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
	}

}
