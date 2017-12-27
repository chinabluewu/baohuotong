package com.demo.app.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.demo.app.AppContext;
import com.demo.app.AppException;
import com.demo.app.Data;
import com.demo.app.bean.NewsList;
import com.demo.app.bean.URLs;
import com.demo.app.common.StringUtils;
import com.demo.app.ui.LoginActivity;
import com.demo.app.ui.MainActivity;

/**
 * API客户端接口：用于访问网络数据
 * 
 * @version 1.0
 * @created 2012-3-21
 */
public class ApiClient {

	public static final String UTF_8 = "UTF-8";
	public static final String DESC = "descend";
	public static final String ASC = "ascend";

	private final static int TIMEOUT_CONNECTION = 8000;
	private final static int TIMEOUT_SOCKET = 8000;
	private final static int RETRY_TIME = 2;

	private static String appCookie;
	private static String appUserAgent;

	public static void cleanCookie() {
		appCookie = "";

	}

	public static void cleanCookie(AppContext appContext) {
		appCookie = "";
		appContext.setProperty("cookie", appCookie);

	}

	public static String getCookie(AppContext appContext) {
		if (appCookie == null || appCookie == "") {
			appCookie = appContext.getProperty("cookie");

		}
		return appCookie;
	}

	private static String getUserAgent(AppContext appContext) {
		if (appUserAgent == null || appUserAgent == "") {
			StringBuilder ua = new StringBuilder("APP");
			ua.append('/' + appContext.getPackageInfo().versionName + '_'
					+ appContext.getPackageInfo().versionCode);// App版本
			ua.append("/Android");// 手机系统平台
			ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
			ua.append("/" + android.os.Build.MODEL); // 手机型号
			ua.append("/" + appContext.getAppId());// 客户端唯一标识
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}

	private static HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		httpClient.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		// 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset("GBk");
		return httpClient;
	}

	private static GetMethod getHttpGet(String url, String cookie,
			String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		// 设置 请求超时时间
		// cookie=" CYTY%5FERP=realname=002500010103&W%5Fno=0025000101&userID=11&username=002500010103";
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Host", URLs.HOST);
		httpGet.setRequestHeader("Connection", "Keep-Alive");
		httpGet.setRequestHeader("Cookie", cookie);
		httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}

	private static PostMethod getHttpPost(String url, String cookie,
			String userAgent) {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Host", URLs.HOST);
		httpPost.setRequestHeader("Connection", "Keep-Alive");
		httpPost.setRequestHeader("Cookie", cookie);
		httpPost.setRequestHeader("User-Agent", userAgent);
		httpPost.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");

		return httpPost;
	}

	private static String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');

		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			// 不做URLEncoder处理
			// url.append(URLEncoder.encode(String.valueOf(params.get(name)),
			// UTF_8));
		}

		return url.toString().replace("?&", "?");
	}

	/**
	 * get请求URL
	 * 
	 * @param url
	 * @throws AppException
	 */
	private static String http_get(AppContext appContext, String url)
			throws AppException {
		// System.out.println("get_url==> "+url);
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, cookie, userAgent);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				// e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		return responseBody;
	}

	private static JSONArray http_post(AppContext appContext,JSONObject user, String url)
			throws AppException {
         int time=0;
		JSONArray DataList=null;
		do {
			HttpPost httpPost = new HttpPost(url);

			// 将请求体内容加入请求中
			try {
				httpPost.setEntity(new StringEntity(user.toString()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// httpPost.setEntity(requestHttpEntity);

			// 需要客户端对象来发送请求
			org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();

			// 发送请求
			HttpResponse response = null;
			try {
				response = httpClient.execute(httpPost);
			} catch (IOException e) {
				e.printStackTrace();
			}

			int re = response.getStatusLine().getStatusCode();


			// 显示响应
			// showResponseResult(response);
			//System.out.println("Response  from server 0: "+ re);
			// System.out.println("Response Content from server: "+ response.toString());

			String retSrc = null;
			try {
				retSrc = EntityUtils.toString(response.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}

			//System.out.println("解析数据0：" + retSrc);

			// 发送请求

				//HttpResponse response = httpClient.execute(httpPost);
				//String retSrc = response.getEntity().toString();

				// 生成 JSON 对象，并截取其中的result和info信息
				JSONObject result = null;
				try {
					result = new JSONObject(retSrc);
					System.out.println("解析数据01：" + result.toString());
					DataList = result.getJSONArray("list");

					System.out.println("解析数据1：" + DataList.toString());
					break;
				} catch (JSONException e) {
					e.printStackTrace();
					time++;
				}

		}while (time < RETRY_TIME);

		return DataList;
	}

	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	private static String _post(AppContext appContext, String url,
			NameValuePair[] data, Map<String, File> files) throws AppException {
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		PostMethod httpPost = null;

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie, userAgent);
				httpPost.setRequestBody(data);
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				} else if (statusCode == HttpStatus.SC_OK) {
					Cookie[] cookies = httpClient.getState().getCookies();
					String tmpcookies = "";
					for (Cookie ck : cookies) {
						tmpcookies += ck.toString() + ";";
					}
					// 保存cookie
					if (appContext != null && tmpcookies != "") {
						appContext.setProperty("cookie", tmpcookies);
						appCookie = tmpcookies;
					}
				}
				responseBody = httpPost.getResponseBodyAsString();
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		return responseBody;
	}

	/**
	 * post请求URL
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 * @throws IOException
	 * @throws
	 */
	// private static Result http_post(AppContext appContext, String url,
	// Map<String, Object> params, Map<String,File> files) throws AppException,
	// IOException {
	// return Result.parse(_post(appContext, url, params, files));
	// }
	/**
	 * 获取新闻数据列表
	 * 
	 * @return
	 * @throws AppException
	 */
	public static NewsList getNewsList(AppContext appContext)
			throws AppException {
		String newUrl = URLs.NEWS_LIST;
		//String newUrl = URLs.URL_FORUSER;

		try {
			System.out.println("获取新闻列表：" + newUrl);
			//LoginActivity.UserPostThread userThread = new LoginActivity.UserPostThread(user, newUrl);
			//userThread.start();

            /*
			System.out.println("解析数据000："+ StringUtils.toJSONArray(http_get(appContext,
					newUrl)));
			return NewsList.parse(StringUtils.toJSONArray(http_get(appContext,
					newUrl)));
             */

			//JSONArray DataList = (new JSONObject(http_get(appContext,
			//		newUrl))).getJSONArray("list");

			//System.out.println("解析数据：" + DataList.toString());

			JSONObject user = new JSONObject();
			try {
				user.put("Act", "oList");
				user.put("PSize", "10");
				user.put("PNo", "1");
				user.put("STime", "");
				user.put("ETime", "");

				//user.put("SId", MainActivity.sID);
				user.put("SId", Data.getsID());
				user.put("Type", Data.getBillType());    //2 ：基本; 3：综合4：鲜活

			} catch (JSONException e) {
				e.printStackTrace();
			}
			//System.out.println("解析数据： + DataList.toString()");
			return NewsList.parse(http_post(appContext,user,newUrl));

		} catch (Exception e) {

			System.out.println(e);
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	/**
	 * 获取网络图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getNetBitmap(String url) throws AppException {
		// System.out.println("image_url==> "+url);
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		Bitmap bitmap = null;
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, null, null);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
				InputStream inStream = httpGet.getResponseBodyAsStream();
				bitmap = BitmapFactory.decodeStream(inStream);
				inStream.close();
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return bitmap;
	}

}
