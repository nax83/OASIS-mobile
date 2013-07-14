package it.oasis.bussoleno;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper class used to communicate with the demo server.
 */
public final class ServerUtilities {
	private static final String TAG = "ServerUtilities";

	/**
	 * Issue a POST request to the server.
	 * 
	 * @param endpoint
	 *            POST address.
	 * @param params
	 *            request parameters.
	 * 
	 * @throws IOException
	 *             propagated from POST.
	 */
	static void post(String endpoint, Map<String, String> params)
			throws IOException {
		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	static void get(String endpoint, Map<String, String> params)
			throws IOException {

		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		bodyBuilder.append('?');
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(endpoint + bodyBuilder.toString());
		Log.d(TAG, endpoint + bodyBuilder.toString());
		HttpResponse response = httpclient.execute(httpget);
		InputStream content = null;
		content = response.getEntity().getContent();
		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(content));
		String s = "response: ";
		String line = "";
		// Read response until the end
		while ((line = rd.readLine()) != null) {
			s += line;
		}
		Log.d(TAG, s);
		rd.close();

	}

	static String get(String endpoint, String params) throws IOException {

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(endpoint + params);
		System.out.println("get " + endpoint + params);
		HttpResponse response = httpclient.execute(httpget);
		InputStream content = null;
		content = response.getEntity().getContent();
		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(content));
		StringBuilder sb = new StringBuilder();
		String line = "";
		// Read response until the end
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		Log.d(TAG, sb.toString());
		rd.close();
		return sb.toString();
	}

	static JSONObject getJSON(String endpoint, String params) throws IOException{
		
		JSONObject jObj;
		String json = get(endpoint, params);
		
		try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
        	jObj = new JSONObject();
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
		return jObj;
	}
	
}
