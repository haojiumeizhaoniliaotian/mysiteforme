package com.mysiteforme.admin.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>项目名称: springboot-maven </p> 
 * <p>文件名称: HttpClientUtil.java </p> 
 * <p>类型描述: [httpClient工具类,支持http,https请求,支持GET POST PUT DELETE请求方式] </p>
 * <p>创建时间: 2019年11月1日 </p>
 * <p>notes:所有方法返回值均为HttpResponse类,可根据接口实际返回类型使用EntityUtils转为String或者其他类型</p>
 * @author lifanfan
 * @version V1.0
 * @update 2019年11月1日 下午5:20:32 lifanfan 
 */
@Slf4j
public class HttpClientUtil {

	/**
	 * <p>功能描述: [GET请求工具] </p>
	 * @Title doGet
	 * @param host 主机ip,端口信息 形如:"http://主机IP:port"
	 * @param path 请求路径 如/v1/resources/servers(说明:@PathVariable注解修饰的参数应包含在path上)
	 * @param headers 请求头
	 * @param querys 请求参数,不加注解的参数默认为querys
	 * @return
	 * @throws Exception
	 * @CreateDate 2019年11月1日 下午5:20:23
	 * @author lifanfan
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @update 2019年11月1日 下午5:20:23      
	 */
	public static HttpResponse doGet(String host, String path, Map<String, String> headers, Map<String, String> querys)
			throws ClientProtocolException, IOException {
		HttpClient httpClient = wrapClient(host);

		HttpGet request = new HttpGet(buildUrl(host, path, querys));
		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entrySet()) {
				request.addHeader(e.getKey(), e.getValue());
			}
		}
		return httpClient.execute(request);
	}

	/**
	 * <p>功能描述: [post form post请求工具,用于form表单请求] </p>
	 * @Title doPostForm
	 * @param host 主机ip,端口信息 形如:"http://主机IP:port"
	 * @param path 请求路径(说明:@PathVariable注解修饰的参数应包含在path上)
	 * @param headers 请求头
	 * @param querys 请求参数,不加注解的参数默认为querys(入参为key,value形式的参数)
	 * @param bodys 请求体
	 * @return
	 * @throws Exception
	 * @CreateDate 2019年11月1日 下午5:38:03
	 * @author lifanfan
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @update 2019年11月1日 下午5:38:03      
	 */
	public static HttpResponse doPostForm(String host, String path, Map<String, String> headers,
			Map<String, String> querys, Map<String, String> bodys) throws ClientProtocolException, IOException {
		HttpClient httpClient = wrapClient(host);

		HttpPost request = new HttpPost(buildUrl(host, path, querys));

		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entrySet()) {
				request.addHeader(e.getKey(), e.getValue());
			}
		}

		if (bodys != null) {
			List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

			for (Map.Entry<String, String> entry : bodys.entrySet()) {
				nameValuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
			formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
			request.setEntity(formEntity);
		}

		return httpClient.execute(request);
	}

	/**
	 * <p>功能描述: [Post String(支持json字符串)] </p>
	 * 如果body中是json字符串类型,headers中一定要包含<"Content-Type", "application/json">键值对指定类型
	 * @Title doPostString
	 * @param host
	 * @param path
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 * @CreateDate 2019年11月1日 下午5:32:33
	 * @author lifanfan
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @update 2019年11月1日 下午5:32:33      
	 */
	public static HttpResponse doPostString(String host, String path, Map<String, String> headers,
			Map<String, String> querys, String body) throws ClientProtocolException, IOException {
		HttpClient httpClient = wrapClient(host);

		HttpPost request = new HttpPost(buildUrl(host, path, querys));

		if (headers != null) {
			headers.put("Content-Type", "application/json");
			for (Map.Entry<String, String> e : headers.entrySet()) {
				request.addHeader(e.getKey(), e.getValue());
			}
		}
		if (StringUtils.isNotBlank(body)) {
			request.setEntity(new StringEntity(body, "utf-8"));
		}

		return httpClient.execute(request);
	}

	/**
	 * <p>功能描述: [Post Stream] </p>
	 * @Title doPostStream
	 * @param host
	 * @param path
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 * @CreateDate 2019年11月1日 下午5:35:18
	 * @author lifanfan
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @update 2019年11月1日 下午5:35:18      
	 */
	public static HttpResponse doPostStream(String host, String path, Map<String, String> headers,
			Map<String, String> querys, byte[] body) throws ClientProtocolException, IOException {
		HttpClient httpClient = wrapClient(host);

		HttpPost request = new HttpPost(buildUrl(host, path, querys));
		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entrySet()) {
				request.addHeader(e.getKey(), e.getValue());
			}
		}

		if (body != null) {
			request.setEntity(new ByteArrayEntity(body));
		}

		return httpClient.execute(request);
	}

	/**
	 * <p>功能描述: [Put String(支持json字符串)
	 * 如果body中是json字符串类型,headers中一定要包含<"Content-Type", "application/json">键值对指定类型] </p>
	 * @Title doPutString
	 * @param host
	 * @param path
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 * @CreateDate 2019年11月14日 上午9:38:17
	 * @author lifanfan
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @update 2019年11月14日 上午9:38:17      
	 */
	public static HttpResponse doPutString(String host, String path, Map<String, String> headers,
			Map<String, String> querys, String body) throws ClientProtocolException, IOException {
		HttpClient httpClient = wrapClient(host);

		HttpPut request = new HttpPut(buildUrl(host, path, querys));
		if (headers != null) {
			headers.put("Content-Type", "application/json");
			for (Map.Entry<String, String> e : headers.entrySet()) {
				request.addHeader(e.getKey(), e.getValue());
			}
		}
		if (StringUtils.isNotBlank(body)) {
			request.setEntity(new StringEntity(body, "utf-8"));
		}

		return httpClient.execute(request);
	}

	/**
	 * <p>功能描述: [Put stream] </p>
	 * @Title doPutStream
	 * @param host
	 * @param path
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception
	 * @CreateDate 2019年11月14日 上午9:38:59
	 * @author lifanfan
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @update 2019年11月14日 上午9:38:59      
	 */
	public static HttpResponse doPutStream(String host, String path, Map<String, String> headers,
			Map<String, String> querys, byte[] body) throws ClientProtocolException, IOException {
		HttpClient httpClient = wrapClient(host);

		HttpPut request = new HttpPut(buildUrl(host, path, querys));
		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entrySet()) {
				request.addHeader(e.getKey(), e.getValue());
			}
		}

		if (body != null) {
			request.setEntity(new ByteArrayEntity(body));
		}

		return httpClient.execute(request);
	}

	/**
	 * <p>功能描述: [Delete] </p>
	 * @Title doDelete
	 * @param host
	 * @param path
	 * @param headers
	 * @param querys
	 * @return
	 * @throws Exception
	 * @CreateDate 2019年11月1日 下午5:38:35
	 * @author lifanfan
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @update 2019年11月1日 下午5:38:35      
	 */
	public static HttpResponse doDelete(String host, String path, Map<String, String> headers,
			Map<String, String> querys) throws ClientProtocolException, IOException {
		HttpClient httpClient = wrapClient(host);

		HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entrySet()) {
				request.addHeader(e.getKey(), e.getValue());
			}
		}

		return httpClient.execute(request);
	}

	/**
	 * <p>功能描述: [拼接url] </p>
	 * @Title buildUrl
	 * @param host 
	 * @param path 
	 * @param querys key,value形式参数 添加到url上(?传参)
	 * @return
	 * @throws UnsupportedEncodingException
	 * @CreateDate 2019年11月1日 下午5:38:50
	 * @author lifanfan
	 * @update 2019年11月1日 下午5:38:50      
	 */
	private static String buildUrl(String host, String path, Map<String, String> querys)
			throws UnsupportedEncodingException {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(host);
		if (!StringUtils.isBlank(path)) {
			sbUrl.append(path);
		}
		if (null != querys) {
			StringBuilder sbQuery = new StringBuilder();
			for (Map.Entry<String, String> query : querys.entrySet()) {
				if (0 < sbQuery.length()) {
					sbQuery.append("&");
				}
				if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
					sbQuery.append(query.getValue());
				}
				if (!StringUtils.isBlank(query.getKey())) {
					sbQuery.append(query.getKey());
					if (!StringUtils.isBlank(query.getValue())) {
						sbQuery.append("=");
						sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
					}
				}
			}
			if (0 < sbQuery.length()) {
				sbUrl.append("?").append(sbQuery);
			}
		}

		return sbUrl.toString();
	}

	/**
	 * <p>功能描述: [init httpClient] </p>
	 * @Title wrapClient
	 * @param host
	 * @return
	 * @CreateDate 2019年11月1日 下午5:39:24
	 * @author lifanfan
	 * @update 2019年11月1日 下午5:39:24      
	 */
	private static HttpClient wrapClient(String host) {

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		if (host.startsWith("https://")) {
			return createSSLClient(httpClient);
		}
		return httpClient;
	}

	/**
	 * <p>功能描述: [添加https请求支持] </p>
	 * @Title createSSLClient
	 * @param httpClient
	 * @return
	 * @CreateDate 2019年11月1日 下午5:27:08
	 * @author lifanfan
	 * @update 2019年11月1日 下午5:27:08      
	 */
	private static HttpClient createSSLClient(HttpClient httpClient) {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			// HttpClient 4.4以上版本需要指定主机验证程序（NoopH​​ostnameVerifier），以允许接受来自不同主机的证书.
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
					NoopHostnameVerifier.INSTANCE);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			// e.printStackTrace();
			log.error("", e);
		} catch (NoSuchAlgorithmException e) {
			log.error("", e);
		} catch (KeyStoreException e) {
			log.error("", e);
		}
		return HttpClients.createDefault();

	}

}
