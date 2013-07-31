package de.uni_potsdam.hpi.cloudstore.clientfrontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class ServletCommunicator {

	public static void sendPostRequestToServlet(String content)
			throws IOException {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://localhost:8080/Cloudstore_Serverbackend/DefaultServlet");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("param-1", content));
		params.add(new BasicNameValuePair("param-2", "Hello!"));
		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		// Execute and get the response.
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			InputStream instream = entity.getContent();
			try {
				// do something useful
			} finally {
				instream.close();
			}
		}

	}

	public static void sendGetRequestToServlet(String content)
			throws IOException {

		URL url = new URL(
				"http://localhost:8080/Cloudstore_Serverbackend/DefaultServlet?message="
						+ content);
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream()));
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			System.out.println(inputLine);
		in.close();

	}

}
