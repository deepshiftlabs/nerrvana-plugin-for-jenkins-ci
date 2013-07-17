package com.deepshiftlabs.nerrvana;

import java.security.*;
import java.security.cert.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import javax.net.ssl.*;
import java.util.*;

import net.iharder.Base64;

import org.apache.http.*;
import org.apache.http.message.*;
import org.apache.http.params.HttpParams;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BufferedHttpEntity;
import org.w3c.dom.*;

/**
 * Class which communicates with Nerrvana (by performing API calls and
 * interpreting responses) It is wrapped around Apache HttpClient.
 * 
 * @author <a href="http://www.deepshiftlabs.com/">Deep Shift Labs</a>
 * @author <a href="mailto:wise@deepshiftlabs.com">Victor Orlov</a>
 * @version 1.00
 */
public class HttpCommunicator {
	private NerrvanaPluginSettings settings;

	public HttpCommunicator(NerrvanaPluginSettings settings) {
		this.settings = settings;
	}

	/**
	 * Creates SSL enabled HttpClient
	 * 
	 * @return initialized HttpClient object
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private HttpClient createClient() throws NoSuchAlgorithmException,
			KeyManagementException {
		HttpClient client = new DefaultHttpClient();
		client.getParams().setBooleanParameter("http.nodelay", false);

		SSLContext ctx = SSLContext.getInstance("TLS");
		X509TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] xcs, String string)
					throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] xcs, String string)
					throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		ctx.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory ssf = new SSLSocketFactory(ctx);
		ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		ClientConnectionManager ccm = client.getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", ssf, 443));
		return new DefaultHttpClient(ccm, client.getParams());
	}

	/**
	 * <p>
	 * Calculates 'signature' for the parameters to be sent to Nerrvana.
	 * </p>
	 * <p>
	 * Signature then is appended to the call parameters as last name/value pair
	 * </p>
	 * 
	 * @param params list of name/value pairs of the HTTP call
	 * @return signature string
	 * @throws Exception
	 */
	public String calculateSignature(List<NameValuePair> params)
			throws Exception {
		// 1. sort
		StringBuilder sb = new StringBuilder();
		SortedMap<String, String> map = new TreeMap<String, String>();
		for (Iterator<NameValuePair> it = params.iterator(); it.hasNext();) {
			NameValuePair nvp = it.next();
                        String k = nvp.getName() == null ? "" : nvp.getName().trim();
                        String v = nvp.getValue() == null ? "" : nvp.getValue().trim();
			map.put(k, v);
		}
		Set<Map.Entry<String, String>> set = map.entrySet();
		for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = it.next();
			String value = entry.getValue();
			System.out.println("$params['"+entry.getKey()+"'] = '" + value + "';");
			Logger.traceln("$params['"+entry.getKey()+"'] = '" + value + "';");
			
			sb.append(entry.getKey()).append(value);
		}
		String s = sb.toString();

		// 2. get hash
		Mac mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret = new SecretKeySpec(settings.secretkey.trim().getBytes(), "HmacSHA256");
                byte[] secretEncoded = secret.getEncoded();
                String s64 = Base64.encodeBytes(secretEncoded);
		System.out.println("Secret[base64]: "+s64);
		Logger.traceln("Secret[base64]: "+s64);
		mac.init(secret);
		byte[] digest = mac.doFinal(s.getBytes());
		String d64 = Base64.encodeBytes(digest);
		System.out.println("Signature[base64]: "+d64);
		Logger.traceln("Signature[base64]: "+d64);
		digest = MessageDigest.getInstance("MD5").digest(d64.getBytes());
		sb = new StringBuilder();
		for (int i = 0; i < digest.length; i++) {
			sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}

	public String calculateSignature(List<NameValuePair> params, String charset)
			throws Exception {
		// 1. sort
		StringBuilder sb = new StringBuilder();
		SortedMap<String, String> map = new TreeMap<String, String>();
		for (Iterator<NameValuePair> it = params.iterator(); it.hasNext();) {
			NameValuePair nvp = it.next();
                        String k = nvp.getName() == null ? "" : nvp.getName().trim();
                        String v = nvp.getValue() == null ? "" : nvp.getValue().trim();
			map.put(k, v);
		}
		Set<Map.Entry<String, String>> set = map.entrySet();
		for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = it.next();
			String value = entry.getValue();
			System.out.println("$params['"+entry.getKey()+"'] = '" + value + "';");
			Logger.traceln("$params['"+entry.getKey()+"'] = '" + value + "';");
			sb.append(entry.getKey()).append(value);
		}
		String s = sb.toString();

		// 2. get hash
		Mac mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret = new SecretKeySpec(settings.secretkey.trim().getBytes(charset),	"HmacSHA256");
                byte[] secretEncoded = secret.getEncoded();
                String s64 = Base64.encodeBytes(secretEncoded);
		System.out.println("Secret[base64]: "+s64);
		Logger.traceln("Secret[base64]: "+s64);
		mac.init(secret);
		byte[] digest = mac.doFinal(s.getBytes(charset));
		String d64 = Base64.encodeBytes(digest);
		System.out.println("Signature[base64]: "+d64);
		Logger.traceln("Signature[base64]: "+d64);
		digest = MessageDigest.getInstance("MD5").digest(d64.getBytes(charset));
		sb = new StringBuilder();
		for (int i = 0; i < digest.length; i++) {
			sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	/**
	 * Function for log/debug
	 * 
	 * @param response
	 * @throws Exception
	 */
	public void traceResponseHeaders(HttpResponse response) throws Exception {
		Logger.traceln("---BEGIN RESPONSE HEADERS---");
		for (HeaderIterator it = response.headerIterator(); it.hasNext();) {
			Header h = it.nextHeader();
			Logger.traceln("\t" + h.getName() + " => " + h.getValue());
		}
		Logger.traceln("---END RESPONSE HEADERS---");
	}

	/**
	 * Function for log/debug
	 * 
	 * @param params
	 * @throws Exception
	 */
	public void traceParams(HttpParams params) throws Exception {
		Logger.traceln("---BEGIN PARAMS---");
		for (int i = 0; i < PARAMS.length; i++) {
			String key = PARAMS[i];
			Object o = params.getParameter(PARAMS[i]);
			if (o != null) {
				Logger.traceln("\t" + key + " => " + o.toString());
			}
		}
		Logger.traceln("-----END PARAMS---");
	}

	/**
	 * Sends HTTP request to Nerrvana and returns response
	 * 
	 * @param resource address of the HTTP server
	 * @param params list of name/value pairs
	 * @param bGet which HTTP method to use: GET or POST
	 * @return server response as HttpEntity
	 * @throws Exception
	 */
	private HttpEntity request(String resource, List<NameValuePair> params,
			boolean bGet) throws Exception {
		HttpClient client = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		String fullUrl = null;

		try {
			params.add(new BasicNameValuePair("apikey", settings.apikey));
			String signature = calculateSignature(params);
			Logger.traceln("signature => " + signature);
			params.add(new BasicNameValuePair("signature", signature));

			client = createClient();
			if (bGet) {
				fullUrl = makeUrl(settings.httpurl, resource) + "?"
						+ URLEncodedUtils.format(params, "UTF-8");
				Logger.traceln("GET " + fullUrl);
				HttpGet get = new HttpGet(fullUrl);
				response = client.execute(get);
			} else {
				fullUrl = makeUrl(settings.httpurl, resource);
				Logger.traceln("POST " + fullUrl);
				HttpPost post = new HttpPost(fullUrl);
				post.getParams().setBooleanParameter(
						"http.protocol.warn-extra-input", true);
				post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				response = client.execute(post);
			}
			// traceResponseHeaders(response);
			StatusLine sline = response.getStatusLine();
			int statusCode = sline.getStatusCode();
			entity = new BufferedHttpEntity(response.getEntity());
			if (statusCode == 200) {
				return entity;
			} else {
				StringBuilder sb = new StringBuilder(
						"Error occured when communicating with Nerrvana server\n---BEGIN ERROR--\n");
				sb.append(sline.toString()).append(": ")
						.append(Utils.parseErrorResponse(entity))
						.append("\n-----END ERROR--\n");
				throw new ResponseException(sb.toString(), statusCode);
			}
		} finally {
			if (client != null) {
				client.getConnectionManager().shutdown();
			}
		}
	}

	/**
	 * HTTP GET shortcut
	 * 
	 * @param resource address of the HTTP server
	 * @param params list of name/value pairs
	 * @return server response as HttpEntity
	 * @throws Exception
	 */
	public HttpEntity get(String resource, List<NameValuePair> params)
			throws Exception {
		return request(resource, params, true);
	}

	/**
	 * HTTP POST shortcut
	 * 
	 * @param resource address of the HTTP server
	 * @param params list of name/value pairs
	 * @return server response as HttpEntity
	 * @throws Exception
	 */
	public HttpEntity post(String resource, List<NameValuePair> params)
			throws Exception {
		return request(resource, params, false);
	}

	/**
	 * <p>
	 * Adds additional logging to exception occured during HTTP call.
	 * </p>
	 * 
	 * @param doc
	 *            parsed XML of Nerrvana API response
	 * @param e
	 *            exception occurred during response processing
	 * @return original exception
	 * @throws Exception
	 */
	private Exception handleResponseError(Document doc, Exception e)
			throws Exception {
		if (e instanceof ResponseException)
			;
		else if (doc == null) {
			Logger.infoln("Failed parsing XML response from Nerrvana");
		} else {
			Logger.infoln("XML response unserialization failed");
			Logger.infoln(Utils.xml2string(doc));
		}
		return e;
	}

	/**
	 * Assembles server name with resource name to the full URL
	 * 
	 * @param server HTTP server name
	 * @param resource resource name
	 * @return assembled resource address
	 */
	private String makeUrl(String server, String resource) {
		if (resource == null)
			return server;
		while (server.endsWith("/"))
			server = server.substring(0, server.length() - 2);
		while (resource.startsWith("/"))
			resource = resource.substring(1);
		return server + "/" + resource;
	}

	/**
	 * Calls Nerrvana API to get status of the execution by id
	 * 
	 * @param exec_id ID of the NerrvanaExecution to get status of
	 * @return NerrvanaExecution object from server response
	 * @throws Exception
	 */
	public NerrvanaExecution getExecutionStatus(String exec_id)
			throws Exception {
		Document doc = null;
		HttpEntity entity = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", exec_id));
			entity = get("testrun/execution", params);
			doc = Utils.parseResponse(entity);
			List<NerrvanaExecution> list = NerrvanaExecution.xml2list(doc);
			return list.get(0);
		} catch (Exception e) {
			throw handleResponseError(doc, e);
		}
	}

	/**
	 * <p>
	 * Calls Nerrvana API to create
	 * {@link <a href="https://nerrvana.com/docs/test-runs">Nerrvana test run</a>}
	 * </p>
	 * All function parameters are obtained from @see NerrvanaPluginSettings
	 * object, which, in turn obtains them from plugin settings.
	 * 
	 * @param space_id
	 * @param name
	 * @param descr
	 * @param platforms
	 * @param executable_file
	 * @param bValidation
	 * @param nodes_count
	 * @return
	 * @throws Exception
	 */
	public Testrun createTestrun(String space_id, String name, String descr,
			List<Platform> platforms, String executable_file,
			boolean bValidation, int nodes_count) throws Exception {
		Document doc = null;
		HttpEntity entity = null;
		StringBuilder sb = new StringBuilder();
		for (Iterator<Platform> it = platforms.iterator(); it.hasNext();) {
			if (sb.length() > 0)
				sb.append(",");
			sb.append(it.next().code);
		}
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("space_id", space_id));
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("description", descr));
			params.add(new BasicNameValuePair("platforms", sb.toString()));
			params.add(new BasicNameValuePair("executable_file",
					executable_file));
			params.add(new BasicNameValuePair("validation", bValidation ? "1"
					: "0"));
			params.add(new BasicNameValuePair("nodes_count", "" + nodes_count));
			entity = post("testrun", params);
			doc = Utils.parseResponse(entity);
			return Testrun.getTestrun(doc.getDocumentElement());
		} catch (Exception e) {
			throw handleResponseError(doc, e);
		}
	}

	public static final String[] PARAMS = { "http.useragent",
			"http.protocol.version", "http.protocol.unambiguous-statusline",
			"http.protocol.single-cookie-header",
			"http.protocol.strict-transfer-encoding",
			"http.protocol.reject-head-body",
			"http.protocol.head-body-timeout", "http.protocol.expect-continue",
			"http.protocol.credential-charset",
			"http.protocol.element-charset", "http.protocol.content-charset",
			"http.protocol.cookie-policy", "http.protocol.warn-extra-input",
			"http.protocol.status-line-garbage-limit", "http.socket.timeout",
			"http.method.retry-handler", "http.dateparser.patterns",
			"http.method.response.buffer.warnlimit",
			"http.method.multipart.boundary", "http.socket.timeout",
			"http.tcp.nodelay", "http.socket.sendbuffer",
			"http.socket.receivebuffer", "http.socket.linger",
			"http.connection.timeout", "http.connection.stalecheck",
			"http.connection-manager.max-per-host",
			"http.connection-manager.max-total", "http.default-headers",
			"http.connection-manager.timeout", "http.connection-manager.class",
			"http.authentication.preemptive",
			"http.protocol.reject-relative-redirect",
			"http.protocol.max-redirects",
			"http.protocol.allow-circular-redirects", };
}
