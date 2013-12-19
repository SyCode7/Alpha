package de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import de.uni_potsdam.hpi.cloudstore20.meta.CommunicationInformation;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingClass;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.dataList.DataList;

public class ServletCommunicator {

	// private static void sendPostRequestToServlet(String content) throws IOException {
	//
	// HttpClient httpclient = new DefaultHttpClient();
	// HttpPost httppost = new HttpPost("http://localhost:8080/Cloudstore_Serverbackend/DefaultServlet");
	//
	// // Request parameters and other properties.
	// List<NameValuePair> params = new ArrayList<NameValuePair>(2);
	// params.add(new BasicNameValuePair("param-1", content));
	// params.add(new BasicNameValuePair("param-2", "Hello!"));
	// httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
	//
	// // Execute and get the response.
	// HttpResponse response = httpclient.execute(httppost);
	// HttpEntity entity = response.getEntity();
	//
	// if (entity != null) {
	// InputStream instream = entity.getContent();
	// try {
	// // do something useful
	// } finally {
	// instream.close();
	// }
	// }
	//
	// }

	private static String sendGetRequestToServlet(CommunicationInformation info) throws IOException {

		URL url = new URL("http://localhost:8080/Cloudstore_Serverbackend/DefaultServlet?message=" + info);
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		String answer = null;
		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine);
			if (inputLine.startsWith("answer")) {
				answer = inputLine.split("=")[1];
			}
		}
		in.close();

		return answer;
	}

	public static long getProviderSpeed(String providerName) throws ServletCommunicationException {

		// TODO: implementieren: abfrage der Informationen vom Server über ServletCommunicator
		return 1l;
	}

	public static DataList getDataList() throws ServletCommunicationException {

		try {
			String answer = ServletCommunicator.sendGetRequestToServlet(CommunicationInformation.dataList);

			if ("".equals(answer) || answer == null) {
				throw new ServletCommunicationException("Es wurde nichts übertragen");
			}

			return (DataList) DataTransmittingClass.fromString(answer);
		} catch (IOException e) {
			throw new ServletCommunicationException(e.getMessage(), e.getCause());
		} catch (DataTransmittingException e) {
			throw new ServletCommunicationException(e.getMessage(), e.getCause());
		}

	}

}
