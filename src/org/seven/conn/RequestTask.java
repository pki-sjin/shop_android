package org.seven.conn;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.seven.data.BillDetailInfo;
import org.seven.data.BillInfo;
import org.seven.data.StoreInfo;
import org.seven.utils.GlobalParam;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class RequestTask {

	private boolean isCancel;

	private final String requestUrl = "http://218.83.243.78:8090/WebService.asmx";

	private Thread t;

	public void login(final String userno, final String password,
			final boolean autoLogin, final Handler mHandler) {
		t = new Thread() {
			public void run() {
				HttpURLConnection conn = null;
				OutputStream os = null;
				InputStream is = null;
				int code = 0;
				try {
					Log.i("1510", GlobalParam.getInstance().IMEI);
					String requestParam = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><Login xmlns=\"http://tempuri.org/\"><username>"
							+ userno
							+ "</username><password>"
							+ password
							+ "</password></Login></S:Body></S:Envelope>";
					URL u = new URL(requestUrl);
					if (isCancel) {
						return;
					}
					ConnectivityManager connMng = (ConnectivityManager) GlobalParam
							.getInstance().getSystemService(
									Context.CONNECTIVITY_SERVICE);
					NetworkInfo netInf = connMng.getActiveNetworkInfo();

					if (netInf != null
							&& "WIFI".equalsIgnoreCase(netInf.getTypeName())) {
						conn = (HttpURLConnection) u.openConnection();
					} else {
						String proxyHost = android.net.Proxy.getDefaultHost();
						if (proxyHost != null) {
							java.net.Proxy p = new java.net.Proxy(
									java.net.Proxy.Type.HTTP,
									new InetSocketAddress(
											android.net.Proxy.getDefaultHost(),
											android.net.Proxy.getDefaultPort()));
							conn = (HttpURLConnection) u.openConnection(p);
						} else {
							conn = (HttpURLConnection) u.openConnection();
						}
					}
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(60000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.addRequestProperty("SOAPAction",
							"\"http://tempuri.org/Login\"");
					conn.addRequestProperty(
							"Accept",
							"text/xml, multipart/related, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
					conn.addRequestProperty("Content-Type",
							"text/xml;charset=\"UTF-8\"");
					conn.addRequestProperty("Host", "218.83.243.78:8090");
					if (isCancel) {
						return;
					}
					os = conn.getOutputStream();
					os.write(requestParam.getBytes("UTF-8"));
					os.flush();
					if (isCancel) {
						return;
					}
					code = conn.getResponseCode();
					is = conn.getInputStream();
					byte[] data = new byte[255 * 255];
					int chunk = 0;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((chunk = is.read(data)) != -1) {
						if (isCancel) {
							return;
						}
						baos.write(data, 0, chunk);
					}
					if (isCancel) {
						return;
					}
					String result = new String(baos.toByteArray(), "UTF-8");
					int start = result.indexOf("<LoginResult>");
					int end = result.indexOf("</LoginResult>");
					String s = result.substring(
							start + "<LoginResult>".length(), end);
					Log.i("1510", s);
					Message m = Message.obtain();
					JSONObject json = (JSONObject) new JSONTokener(s)
							.nextValue();
					if (json.has("status")) {
						m.what = -1;
						Bundle b = new Bundle();
						b.putString("message", json.getString("message"));
						m.setData(b);
					} else {
						String sid = json.getString("sid");
						String username = json.getString("username");
						String position = json.getString("position");
						GlobalParam.getInstance().SetLastLoginInfo(sid, userno,
								username, password, position, autoLogin);
						m.what = GlobalParam.LOGIN_SUCCESS;
						Bundle b = new Bundle();
						b.putString("username", username);
						b.putString("position", position);
						m.setData(b);
					}

					mHandler.sendMessage(m);
				} catch (Exception e) {
					Log.i("1510", e.getMessage(), e);
					if (!isCancel) {
						Message m = Message.obtain();
						Bundle b = new Bundle();
						b.putString("error", code + ":" + e.toString());
						m.setData(b);
						m.what = -1;
						mHandler.sendMessage(m);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception e) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception e) {
						}
					}
					if (conn != null) {
						try {
							conn.disconnect();
						} catch (Exception e) {
						}
					}
				}
			}
		};
		t.start();
	}

	public void getStore(final String sid, final String userno,
			final Handler mHandler) {
		t = new Thread() {
			public void run() {
				HttpURLConnection conn = null;
				OutputStream os = null;
				InputStream is = null;
				int code = 0;
				try {
					Log.i("1510", GlobalParam.getInstance().IMEI);
					String requestParam = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><getStoreInfo xmlns=\"http://tempuri.org/\"><sid>"
							+ sid
							+ "</sid><username>"
							+ userno
							+ "</username></getStoreInfo></S:Body></S:Envelope>";
					URL u = new URL(requestUrl);
					if (isCancel) {
						return;
					}
					ConnectivityManager connMng = (ConnectivityManager) GlobalParam
							.getInstance().getSystemService(
									Context.CONNECTIVITY_SERVICE);
					NetworkInfo netInf = connMng.getActiveNetworkInfo();

					if (netInf != null
							&& "WIFI".equalsIgnoreCase(netInf.getTypeName())) {
						conn = (HttpURLConnection) u.openConnection();
					} else {
						String proxyHost = android.net.Proxy.getDefaultHost();
						if (proxyHost != null) {
							java.net.Proxy p = new java.net.Proxy(
									java.net.Proxy.Type.HTTP,
									new InetSocketAddress(
											android.net.Proxy.getDefaultHost(),
											android.net.Proxy.getDefaultPort()));
							conn = (HttpURLConnection) u.openConnection(p);
						} else {
							conn = (HttpURLConnection) u.openConnection();
						}
					}
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(60000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.addRequestProperty("SOAPAction",
							"\"http://tempuri.org/getStoreInfo\"");
					conn.addRequestProperty(
							"Accept",
							"text/xml, multipart/related, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
					conn.addRequestProperty("Content-Type",
							"text/xml;charset=\"UTF-8\"");
					conn.addRequestProperty("Host", "218.83.243.78:8090");
					if (isCancel) {
						return;
					}
					os = conn.getOutputStream();
					os.write(requestParam.getBytes("UTF-8"));
					os.flush();
					if (isCancel) {
						return;
					}
					code = conn.getResponseCode();
					is = conn.getInputStream();
					byte[] data = new byte[255 * 255];
					int chunk = 0;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((chunk = is.read(data)) != -1) {
						if (isCancel) {
							return;
						}
						baos.write(data, 0, chunk);
					}
					if (isCancel) {
						return;
					}
					String result = new String(baos.toByteArray(), "UTF-8");
					int start = result.indexOf("<getStoreInfoResult>");
					int end = result.indexOf("</getStoreInfoResult>");
					String s = result.substring(
							start + "<getStoreInfoResult>".length(), end);
					Log.i("1510", s);
					// s =
					// "{\"list\":[{\"billcount\":\"5\",\"straddress\":\"\",\"strname\":\"家乐福南昌青云谱店\",\"strno\":\"C_C4791QUP\"},{\"billcount\":\"6\",\"straddress\":\"\",\"strname\":\"家乐福南昌上海路店\",\"strno\":\"C_C4791SHL\"}]}";
					Message m = Message.obtain();
					JSONObject json = null;
					Object o = new JSONTokener(s).nextValue();
					if (o instanceof JSONObject) {
						json = (JSONObject) o;
					} else if (o instanceof JSONArray) {
						json = new JSONObject("{\"list\":" + s + "}");
					} else {
						throw new JSONException("数据格式错误");
					}

					if (json.has("status")) {
						String status = json.getString("status");
						Bundle b = new Bundle();
						if (status.equalsIgnoreCase("2")) {
							// session time out need login again and request
							// again
							String message = loginAgain();
							if (message == null) {
								m.what = 2;
								b.putString("method", "getStore");
								m.setData(b);
							} else {
								// Contains server error message login failed
								m.what = -1;
								b.putString("message", message);
								m.setData(b);
							}
						} else {
							m.what = -1;
							b.putString("message", json.getString("message"));
							m.setData(b);
						}
					} else {
						ArrayList<StoreInfo> list = new ArrayList<StoreInfo>();
						JSONArray array = json.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							StoreInfo store = new StoreInfo();
							store.setBillcount(obj.getString("billcount"));
							store.setStraddress(obj.getString("straddress"));
							store.setStrname(obj.getString("strname"));
							store.setStrno(obj.getString("strno"));
							list.add(store);
						}
						m.what = GlobalParam.GETSTORES_SUCCESS;
						Bundle b = new Bundle();
						b.putSerializable("list", list);
						m.setData(b);
					}

					mHandler.sendMessage(m);
				} catch (Exception e) {
					Log.i("1510", e.getMessage(), e);
					if (!isCancel) {
						Message m = Message.obtain();
						Bundle b = new Bundle();
						b.putString("error", code + ":" + e.toString());
						m.setData(b);
						m.what = -1;
						mHandler.sendMessage(m);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception e) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception e) {
						}
					}
					if (conn != null) {
						try {
							conn.disconnect();
						} catch (Exception e) {
						}
					}
				}
			}
		};
		t.start();
	}

	public void getStoreContainsGeo(final String sid, final String userno,
			final Handler mHandler) {
		t = new Thread() {
			public void run() {
				HttpURLConnection conn = null;
				OutputStream os = null;
				InputStream is = null;
				int code = 0;
				try {
					Log.i("1510", GlobalParam.getInstance().IMEI);
					String requestParam = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><getAllStoreInfo xmlns=\"http://tempuri.org/\"><sid>"
							+ sid
							+ "</sid><userno>"
							+ userno
							+ "</userno></getAllStoreInfo></S:Body></S:Envelope>";
					URL u = new URL(requestUrl);
					if (isCancel) {
						return;
					}
					ConnectivityManager connMng = (ConnectivityManager) GlobalParam
							.getInstance().getSystemService(
									Context.CONNECTIVITY_SERVICE);
					NetworkInfo netInf = connMng.getActiveNetworkInfo();

					if (netInf != null
							&& "WIFI".equalsIgnoreCase(netInf.getTypeName())) {
						conn = (HttpURLConnection) u.openConnection();
					} else {
						String proxyHost = android.net.Proxy.getDefaultHost();
						if (proxyHost != null) {
							java.net.Proxy p = new java.net.Proxy(
									java.net.Proxy.Type.HTTP,
									new InetSocketAddress(
											android.net.Proxy.getDefaultHost(),
											android.net.Proxy.getDefaultPort()));
							conn = (HttpURLConnection) u.openConnection(p);
						} else {
							conn = (HttpURLConnection) u.openConnection();
						}
					}
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(60000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.addRequestProperty("SOAPAction",
							"\"http://tempuri.org/getAllStoreInfo\"");
					conn.addRequestProperty(
							"Accept",
							"text/xml, multipart/related, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
					conn.addRequestProperty("Content-Type",
							"text/xml;charset=\"UTF-8\"");
					conn.addRequestProperty("Host", "218.83.243.78:8090");
					if (isCancel) {
						return;
					}
					os = conn.getOutputStream();
					os.write(requestParam.getBytes("UTF-8"));
					os.flush();
					if (isCancel) {
						return;
					}
					code = conn.getResponseCode();
					is = conn.getInputStream();
					byte[] data = new byte[255 * 255];
					int chunk = 0;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((chunk = is.read(data)) != -1) {
						if (isCancel) {
							return;
						}
						baos.write(data, 0, chunk);
					}
					if (isCancel) {
						return;
					}
					String result = new String(baos.toByteArray(), "UTF-8");
					int start = result.indexOf("<getAllStoreInfoResult>");
					int end = result.indexOf("</getAllStoreInfoResult>");
					String s = result.substring(start
							+ "<getAllStoreInfoResult>".length(), end);
					Log.i("1510", s);
					// s =
					// "{\"list\":[{\"billcount\":\"5\",\"straddress\":\"\",\"strname\":\"家乐福南昌青云谱店\",\"strno\":\"C_C4791QUP\"},{\"billcount\":\"6\",\"straddress\":\"\",\"strname\":\"家乐福南昌上海路店\",\"strno\":\"C_C4791SHL\"}]}";
					Message m = Message.obtain();
					JSONObject json = null;
					Object o = new JSONTokener(s).nextValue();
					if (o instanceof JSONObject) {
						json = (JSONObject) o;
					} else if (o instanceof JSONArray) {
						json = new JSONObject("{\"list\":" + s + "}");
					} else {
						throw new JSONException("数据格式错误");
					}

					if (json.has("status")) {
						String status = json.getString("status");
						Bundle b = new Bundle();
						if (status.equalsIgnoreCase("2")) {
							// session time out need login again and request
							// again
							String message = loginAgain();
							if (message == null) {
								m.what = 2;
								m.setData(b);
							} else {
								// Contains server error message login failed
								m.what = -1;
								b.putString("message", message);
								m.setData(b);
							}
						} else {
							m.what = -1;
							b.putString("message", json.getString("message"));
							m.setData(b);
						}
					} else {
						ArrayList<StoreInfo> list = new ArrayList<StoreInfo>();
						JSONArray array = json.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							StoreInfo store = new StoreInfo();
							store.setLat(obj.getString("lat"));
							store.setLng(obj.getString("lng"));
							store.setStraddress(obj.getString("straddress"));
							store.setStrname(obj.getString("strname"));
							store.setStrno(obj.getString("strno"));
							list.add(store);
						}
						m.what = GlobalParam.GETSTORES_SUCCESS;
						Bundle b = new Bundle();
						b.putSerializable("list", list);
						m.setData(b);
					}

					mHandler.sendMessage(m);
				} catch (Exception e) {
					Log.i("1510", e.getMessage(), e);
					if (!isCancel) {
						Message m = Message.obtain();
						Bundle b = new Bundle();
						b.putString("error", code + ":" + e.toString());
						m.setData(b);
						m.what = -1;
						mHandler.sendMessage(m);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception e) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception e) {
						}
					}
					if (conn != null) {
						try {
							conn.disconnect();
						} catch (Exception e) {
						}
					}
				}
			}
		};
		t.start();
	}

	public void getBill(final String sid, final StoreInfo store,
			final Handler mHandler) {
		t = new Thread() {
			public void run() {
				HttpURLConnection conn = null;
				OutputStream os = null;
				InputStream is = null;
				int code = 0;
				try {
					Log.i("1510", GlobalParam.getInstance().IMEI);
					String requestParam = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><getBillInfo xmlns=\"http://tempuri.org/\"><sid>"
							+ sid
							+ "</sid><strno>"
							+ store.getStrno()
							+ "</strno></getBillInfo></S:Body></S:Envelope>";
					URL u = new URL(requestUrl);
					if (isCancel) {
						return;
					}
					ConnectivityManager connMng = (ConnectivityManager) GlobalParam
							.getInstance().getSystemService(
									Context.CONNECTIVITY_SERVICE);
					NetworkInfo netInf = connMng.getActiveNetworkInfo();

					if (netInf != null
							&& "WIFI".equalsIgnoreCase(netInf.getTypeName())) {
						conn = (HttpURLConnection) u.openConnection();
					} else {
						String proxyHost = android.net.Proxy.getDefaultHost();
						if (proxyHost != null) {
							java.net.Proxy p = new java.net.Proxy(
									java.net.Proxy.Type.HTTP,
									new InetSocketAddress(
											android.net.Proxy.getDefaultHost(),
											android.net.Proxy.getDefaultPort()));
							conn = (HttpURLConnection) u.openConnection(p);
						} else {
							conn = (HttpURLConnection) u.openConnection();
						}
					}
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(60000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.addRequestProperty("SOAPAction",
							"\"http://tempuri.org/getBillInfo\"");
					conn.addRequestProperty(
							"Accept",
							"text/xml, multipart/related, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
					conn.addRequestProperty("Content-Type",
							"text/xml;charset=\"UTF-8\"");
					conn.addRequestProperty("Host", "218.83.243.78:8090");
					if (isCancel) {
						return;
					}
					os = conn.getOutputStream();
					os.write(requestParam.getBytes("UTF-8"));
					os.flush();
					if (isCancel) {
						return;
					}
					code = conn.getResponseCode();
					is = conn.getInputStream();
					byte[] data = new byte[255 * 255];
					int chunk = 0;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((chunk = is.read(data)) != -1) {
						if (isCancel) {
							return;
						}
						baos.write(data, 0, chunk);
					}
					if (isCancel) {
						return;
					}
					String result = new String(baos.toByteArray(), "UTF-8");
					int start = result.indexOf("<getBillInfoResult>");
					int end = result.indexOf("</getBillInfoResult>");
					String s = result.substring(
							start + "<getBillInfoResult>".length(), end);
					Log.i("1510", s);
					// s =
					// "{\"list\":[{\"billno\":\"T00000994\",\"s_date\":\"2014-3-11 13:31:33\",\"s_num\":339,\"s_store\":\"总部仓库\",\"skucount\":60},{\"billno\":\"T00000785\",\"s_date\":\"2014-3-6 21:22:35\",\"s_num\":30,\"s_store\":\"总部仓库\",\"skucount\":1},{\"billno\":\"T00000067\",\"s_date\":\"2014-3-3 9:56:57\",\"s_num\":2093,\"s_store\":\"总部仓库\",\"skucount\":121},{\"billno\":\"T00000426\",\"s_date\":\"2014-3-14 15:22:22\",\"s_num\":364,\"s_store\":\"总部仓库\",\"skucount\":19},{\"billno\":\"T00001700\",\"s_date\":\"2014-3-21 13:53:02\",\"s_num\":329,\"s_store\":\"总部仓库\",\"skucount\":23}]}";
					Message m = Message.obtain();
					JSONObject json = null;
					Object o = new JSONTokener(s).nextValue();
					if (o instanceof JSONObject) {
						json = (JSONObject) o;
					} else if (o instanceof JSONArray) {
						json = new JSONObject("{\"list\":" + s + "}");
					} else {
						throw new JSONException("数据格式错误");
					}

					if (json.has("status")) {
						String status = json.getString("status");
						Bundle b = new Bundle();
						if (status.equalsIgnoreCase("2")) {
							// session time out need login again and request
							// again
							String message = loginAgain();
							if (message == null) {
								// Login Again successful
								m.what = 2;
								b.putString("method", "getBill");
								b.putSerializable("store", store);
								m.setData(b);
							} else {
								// Contains server error message login failed
								m.what = -1;
								b.putString("message", message);
								m.setData(b);
							}
						} else {
							m.what = -1;
							b.putString("message", json.getString("message"));
							m.setData(b);
						}
					} else {
						ArrayList<BillInfo> list = new ArrayList<BillInfo>();
						JSONArray array = json.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							list.add(new BillInfo(obj.getString("billno"), obj
									.getString("s_store"), obj
									.getString("s_date"), obj
									.getString("packagecnt"), obj
									.getString("o_num"),
									obj.getString("a_num"), obj
											.getString("s_num"), obj
											.getString("skucount"), obj
											.getString("remark")));
						}
						m.what = GlobalParam.GETBILLS_SUCCESS;
						Bundle b = new Bundle();
						b.putSerializable("list", list);
						b.putSerializable("store", store);
						m.setData(b);
					}

					mHandler.sendMessage(m);
				} catch (Exception e) {
					Log.i("1510", e.getMessage(), e);
					if (!isCancel) {
						Message m = Message.obtain();
						Bundle b = new Bundle();
						b.putString("error", code + ":" + e.toString());
						m.setData(b);
						m.what = -1;
						mHandler.sendMessage(m);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception e) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception e) {
						}
					}
					if (conn != null) {
						try {
							conn.disconnect();
						} catch (Exception e) {
						}
					}
				}
			}
		};
		t.start();
	}

	public void dealBillByStr(final String sid, final String dealType,
			final String strno, final String billno, final Handler mHandler) {
		t = new Thread() {
			public void run() {
				HttpURLConnection conn = null;
				OutputStream os = null;
				InputStream is = null;
				int code = 0;
				try {
					Log.i("1510", GlobalParam.getInstance().IMEI);
					String requestParam = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><dealBillByStr xmlns=\"http://tempuri.org/\"><sid>"
							+ sid
							+ "</sid><dealtype>"
							+ dealType
							+ "</dealtype><strno>"
							+ strno
							+ "</strno><billno>"
							+ billno
							+ "</billno></dealBillByStr></S:Body></S:Envelope>";
					URL u = new URL(requestUrl);
					if (isCancel) {
						return;
					}
					ConnectivityManager connMng = (ConnectivityManager) GlobalParam
							.getInstance().getSystemService(
									Context.CONNECTIVITY_SERVICE);
					NetworkInfo netInf = connMng.getActiveNetworkInfo();

					if (netInf != null
							&& "WIFI".equalsIgnoreCase(netInf.getTypeName())) {
						conn = (HttpURLConnection) u.openConnection();
					} else {
						String proxyHost = android.net.Proxy.getDefaultHost();
						if (proxyHost != null) {
							java.net.Proxy p = new java.net.Proxy(
									java.net.Proxy.Type.HTTP,
									new InetSocketAddress(
											android.net.Proxy.getDefaultHost(),
											android.net.Proxy.getDefaultPort()));
							conn = (HttpURLConnection) u.openConnection(p);
						} else {
							conn = (HttpURLConnection) u.openConnection();
						}
					}
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(60000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.addRequestProperty("SOAPAction",
							"\"http://tempuri.org/dealBillByStr\"");
					conn.addRequestProperty(
							"Accept",
							"text/xml, multipart/related, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
					conn.addRequestProperty("Content-Type",
							"text/xml;charset=\"UTF-8\"");
					conn.addRequestProperty("Host", "218.83.243.78:8090");
					if (isCancel) {
						return;
					}
					os = conn.getOutputStream();
					os.write(requestParam.getBytes("UTF-8"));
					os.flush();
					if (isCancel) {
						return;
					}
					code = conn.getResponseCode();
					is = conn.getInputStream();
					byte[] data = new byte[255 * 255];
					int chunk = 0;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((chunk = is.read(data)) != -1) {
						if (isCancel) {
							return;
						}
						baos.write(data, 0, chunk);
					}
					if (isCancel) {
						return;
					}
					String result = new String(baos.toByteArray(), "UTF-8");
					int start = result.indexOf("<dealBillByStrResult>");
					int end = result.indexOf("</dealBillByStrResult>");
					String s = result.substring(
							start + "<dealBillByStrResult>".length(), end);
					Log.i("1510", s);
					Message m = Message.obtain();
					JSONObject json = null;
					Object o = new JSONTokener(s).nextValue();
					if (o instanceof JSONObject) {
						json = (JSONObject) o;
					} else if (o instanceof JSONArray) {
						json = new JSONObject("{\"list\":" + s + "}");
					} else {
						throw new JSONException("数据格式错误");
					}

					if (json.has("status")) {
						String status = json.getString("status");
						Bundle b = new Bundle();
						if (status.equalsIgnoreCase("2")) {
							// session time out need login again and request
							// again
							String message = loginAgain();
							if (message == null) {
								// Login Again successful
								m.what = 2;
								b.putString("method", "getDeal");
								b.putString("strno", strno);
								b.putString("billno", billno);
								m.setData(b);
							} else {
								// Contains server error message login failed
								m.what = -1;
								b.putString("message", message);
								m.setData(b);
							}
						} else if (status.equalsIgnoreCase("5")) {
							m.what = 1;
							b.putString("message", json.getString("message"));
							m.setData(b);
						} else {
							m.what = -1;
							b.putString("message", json.getString("message"));
							m.setData(b);
						}
					} else {
						throw new JSONException("数据格式错误");
					}

					mHandler.sendMessage(m);
				} catch (Exception e) {
					Log.i("1510", e.getMessage(), e);
					if (!isCancel) {
						Message m = Message.obtain();
						Bundle b = new Bundle();
						b.putString("error", code + ":" + e.toString());
						m.setData(b);
						m.what = -1;
						mHandler.sendMessage(m);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception e) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception e) {
						}
					}
					if (conn != null) {
						try {
							conn.disconnect();
						} catch (Exception e) {
						}
					}
				}
			}
		};
		t.start();
	}

	public void getBillDetail(final String sid, final String strno,
			final String billno, final Handler mHandler) {
		t = new Thread() {
			public void run() {
				HttpURLConnection conn = null;
				OutputStream os = null;
				InputStream is = null;
				int code = 0;
				try {
					Log.i("1510", GlobalParam.getInstance().IMEI);
					String requestParam = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><getBillDetail xmlns=\"http://tempuri.org/\"><sid>"
							+ sid
							+ "</sid><strno>"
							+ strno
							+ "</strno><billno>"
							+ billno
							+ "</billno></getBillDetail></S:Body></S:Envelope>";
					URL u = new URL(requestUrl);
					if (isCancel) {
						return;
					}
					ConnectivityManager connMng = (ConnectivityManager) GlobalParam
							.getInstance().getSystemService(
									Context.CONNECTIVITY_SERVICE);
					NetworkInfo netInf = connMng.getActiveNetworkInfo();

					if (netInf != null
							&& "WIFI".equalsIgnoreCase(netInf.getTypeName())) {
						conn = (HttpURLConnection) u.openConnection();
					} else {
						String proxyHost = android.net.Proxy.getDefaultHost();
						if (proxyHost != null) {
							java.net.Proxy p = new java.net.Proxy(
									java.net.Proxy.Type.HTTP,
									new InetSocketAddress(
											android.net.Proxy.getDefaultHost(),
											android.net.Proxy.getDefaultPort()));
							conn = (HttpURLConnection) u.openConnection(p);
						} else {
							conn = (HttpURLConnection) u.openConnection();
						}
					}
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(60000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.addRequestProperty("SOAPAction",
							"\"http://tempuri.org/getBillDetail\"");
					conn.addRequestProperty(
							"Accept",
							"text/xml, multipart/related, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
					conn.addRequestProperty("Content-Type",
							"text/xml;charset=\"UTF-8\"");
					conn.addRequestProperty("Host", "218.83.243.78:8090");
					if (isCancel) {
						return;
					}
					os = conn.getOutputStream();
					os.write(requestParam.getBytes("UTF-8"));
					os.flush();
					if (isCancel) {
						return;
					}
					code = conn.getResponseCode();
					is = conn.getInputStream();
					byte[] data = new byte[255 * 255];
					int chunk = 0;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((chunk = is.read(data)) != -1) {
						if (isCancel) {
							return;
						}
						baos.write(data, 0, chunk);
					}
					if (isCancel) {
						return;
					}
					String result = new String(baos.toByteArray(), "UTF-8");
					int start = result.indexOf("<getBillDetailResult>");
					int end = result.indexOf("</getBillDetailResult>");
					String s = result.substring(
							start + "<getBillDetailResult>".length(), end);
					Log.i("1510", s);
					Message m = Message.obtain();
					JSONObject json = null;
					Object o = new JSONTokener(s).nextValue();
					if (o instanceof JSONObject) {
						json = (JSONObject) o;
					} else if (o instanceof JSONArray) {
						json = new JSONObject("{\"list\":" + s + "}");
					} else {
						throw new JSONException("数据格式错误");
					}

					if (json.has("status")) {
						String status = json.getString("status");
						Bundle b = new Bundle();
						if (status.equalsIgnoreCase("2")) {
							// session time out need login again and request
							// again
							String message = loginAgain();
							if (message == null) {
								// Login Again successful
								m.what = 2;
								b.putString("method", "getDetail");
								b.putString("strno", strno);
								b.putString("billno", billno);
								m.setData(b);
							} else {
								// Contains server error message login failed
								m.what = -1;
								b.putString("message", message);
								m.setData(b);
							}
						} else {
							m.what = -1;
							b.putString("message", json.getString("message"));
							m.setData(b);
						}
					} else {

						ArrayList<BillDetailInfo> list = new ArrayList<BillDetailInfo>();
						JSONArray array = json.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);

							list.add(new BillDetailInfo(
									obj.getString("plu_no"), obj
											.getString("plu_name"), obj
											.getString("Mainbarcode"), obj
											.getString("plu_style"), obj
											.getString("unit"), obj
											.getString("stan_pack"), obj
											.getString("o_num"), obj
											.getString("a_num"), obj
											.getString("s_num"), obj.getString("wh_num")));
						}

						m.what = GlobalParam.GETDETAIL_SUCCESS;
						Bundle b = new Bundle();
						b.putSerializable("list", list);
						m.setData(b);
					}
					mHandler.sendMessage(m);
				} catch (Exception e) {
					Log.i("1510", e.getMessage(), e);
					if (!isCancel) {
						Message m = Message.obtain();
						Bundle b = new Bundle();
						b.putString("error", code + ":" + e.toString());
						m.setData(b);
						m.what = -1;
						mHandler.sendMessage(m);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception e) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception e) {
						}
					}
					if (conn != null) {
						try {
							conn.disconnect();
						} catch (Exception e) {
						}
					}
				}
			}
		};
		t.start();
	}

	public void signInAndOut(final String sid, final String userno,
			final String strno, final double lat, final double lng,
			final Handler mHandler) {
		t = new Thread() {
			public void run() {
				HttpURLConnection conn = null;
				OutputStream os = null;
				InputStream is = null;
				int code = 0;
				try {
					Log.i("1510", GlobalParam.getInstance().IMEI);
					String requestParam = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><signInAndOut xmlns=\"http://tempuri.org/\"><sid>"
							+ sid
							+ "</sid><userno>"
							+ userno
							+ "</userno><strno>"
							+ strno
							+ "</strno><lat>"
							+ lat
							+ "</lat><lng>"
							+ lng
							+ "</lng></signInAndOut></S:Body></S:Envelope>";
					URL u = new URL(requestUrl);
					if (isCancel) {
						return;
					}
					ConnectivityManager connMng = (ConnectivityManager) GlobalParam
							.getInstance().getSystemService(
									Context.CONNECTIVITY_SERVICE);
					NetworkInfo netInf = connMng.getActiveNetworkInfo();

					if (netInf != null
							&& "WIFI".equalsIgnoreCase(netInf.getTypeName())) {
						conn = (HttpURLConnection) u.openConnection();
					} else {
						String proxyHost = android.net.Proxy.getDefaultHost();
						if (proxyHost != null) {
							java.net.Proxy p = new java.net.Proxy(
									java.net.Proxy.Type.HTTP,
									new InetSocketAddress(
											android.net.Proxy.getDefaultHost(),
											android.net.Proxy.getDefaultPort()));
							conn = (HttpURLConnection) u.openConnection(p);
						} else {
							conn = (HttpURLConnection) u.openConnection();
						}
					}
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(60000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.addRequestProperty("SOAPAction",
							"\"http://tempuri.org/signInAndOut\"");
					conn.addRequestProperty(
							"Accept",
							"text/xml, multipart/related, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
					conn.addRequestProperty("Content-Type",
							"text/xml;charset=\"UTF-8\"");
					conn.addRequestProperty("Host", "218.83.243.78:8090");
					if (isCancel) {
						return;
					}
					os = conn.getOutputStream();
					os.write(requestParam.getBytes("UTF-8"));
					os.flush();
					if (isCancel) {
						return;
					}
					code = conn.getResponseCode();
					is = conn.getInputStream();
					byte[] data = new byte[255 * 255];
					int chunk = 0;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((chunk = is.read(data)) != -1) {
						if (isCancel) {
							return;
						}
						baos.write(data, 0, chunk);
					}
					if (isCancel) {
						return;
					}
					String result = new String(baos.toByteArray(), "UTF-8");
					int start = result.indexOf("<signInAndOutResult>");
					int end = result.indexOf("</signInAndOutResult>");
					String s = result.substring(
							start + "<signInAndOutResult>".length(), end);
					Log.i("1510", s);
					Message m = Message.obtain();
					JSONObject json = null;
					Object o = new JSONTokener(s).nextValue();
					if (o instanceof JSONObject) {
						json = (JSONObject) o;
					} else if (o instanceof JSONArray) {
						json = new JSONObject("{\"list\":" + s + "}");
					} else {
						throw new JSONException("数据格式错误");
					}

					if (json.has("status")) {
						String status = json.getString("status");
						Bundle b = new Bundle();
						if (status.equalsIgnoreCase("2")) {
							// session time out need login again and request
							// again
							String message = loginAgain();
							if (message == null) {
								m.what = 2;
								b.putString("method", "signInAndOut");
								m.setData(b);
							} else {
								// Contains server error message login failed
								m.what = -1;
								b.putString("message", message);
								m.setData(b);
							}
						} else if (status.equalsIgnoreCase("8")) {
							m.what = GlobalParam.SIGN_SUCCESS;
							b.putString("message", json.getString("message")
									+ "\n距离店铺：" + json.getString("distance"));
							m.setData(b);
						} else {
							m.what = -1;
							b.putString("message", json.getString("message"));
							m.setData(b);
						}
					} else {
						Bundle b = new Bundle();
						m.what = -1;
						b.putString("message", "返回数据有误");
						m.setData(b);
					}

					mHandler.sendMessage(m);
				} catch (Exception e) {
					Log.i("1510", e.getMessage(), e);
					if (!isCancel) {
						Message m = Message.obtain();
						Bundle b = new Bundle();
						b.putString("error", code + ":" + e.toString());
						m.setData(b);
						m.what = -1;
						mHandler.sendMessage(m);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception e) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception e) {
						}
					}
					if (conn != null) {
						try {
							conn.disconnect();
						} catch (Exception e) {
						}
					}
				}
			}
		};
		t.start();
	}

	public void registerStore(final String sid, final String strno,
			final double lat, final double lng, final Handler mHandler) {
		t = new Thread() {
			public void run() {
				HttpURLConnection conn = null;
				OutputStream os = null;
				InputStream is = null;
				int code = 0;
				try {
					Log.i("1510", GlobalParam.getInstance().IMEI);
					String requestParam = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><registerStore xmlns=\"http://tempuri.org/\"><sid>"
							+ sid
							+ "</sid><strno>"
							+ strno
							+ "</strno><lat>"
							+ lat
							+ "</lat><lng>"
							+ lng
							+ "</lng></registerStore></S:Body></S:Envelope>";
					URL u = new URL(requestUrl);
					if (isCancel) {
						return;
					}
					ConnectivityManager connMng = (ConnectivityManager) GlobalParam
							.getInstance().getSystemService(
									Context.CONNECTIVITY_SERVICE);
					NetworkInfo netInf = connMng.getActiveNetworkInfo();

					if (netInf != null
							&& "WIFI".equalsIgnoreCase(netInf.getTypeName())) {
						conn = (HttpURLConnection) u.openConnection();
					} else {
						String proxyHost = android.net.Proxy.getDefaultHost();
						if (proxyHost != null) {
							java.net.Proxy p = new java.net.Proxy(
									java.net.Proxy.Type.HTTP,
									new InetSocketAddress(
											android.net.Proxy.getDefaultHost(),
											android.net.Proxy.getDefaultPort()));
							conn = (HttpURLConnection) u.openConnection(p);
						} else {
							conn = (HttpURLConnection) u.openConnection();
						}
					}
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(60000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.addRequestProperty("SOAPAction",
							"\"http://tempuri.org/registerStore\"");
					conn.addRequestProperty(
							"Accept",
							"text/xml, multipart/related, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
					conn.addRequestProperty("Content-Type",
							"text/xml;charset=\"UTF-8\"");
					conn.addRequestProperty("Host", "218.83.243.78:8090");
					if (isCancel) {
						return;
					}
					os = conn.getOutputStream();
					os.write(requestParam.getBytes("UTF-8"));
					os.flush();
					if (isCancel) {
						return;
					}
					code = conn.getResponseCode();
					is = conn.getInputStream();
					byte[] data = new byte[255 * 255];
					int chunk = 0;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((chunk = is.read(data)) != -1) {
						if (isCancel) {
							return;
						}
						baos.write(data, 0, chunk);
					}
					if (isCancel) {
						return;
					}
					String result = new String(baos.toByteArray(), "UTF-8");
					int start = result.indexOf("<registerStoreResult>");
					int end = result.indexOf("</registerStoreResult>");
					String s = result.substring(
							start + "<registerStoreResult>".length(), end);
					Log.i("1510", s);
					Message m = Message.obtain();
					JSONObject json = null;
					Object o = new JSONTokener(s).nextValue();
					if (o instanceof JSONObject) {
						json = (JSONObject) o;
					} else if (o instanceof JSONArray) {
						json = new JSONObject("{\"list\":" + s + "}");
					} else {
						throw new JSONException("数据格式错误");
					}

					if (json.has("status")) {
						String status = json.getString("status");
						Bundle b = new Bundle();
						if (status.equalsIgnoreCase("2")) {
							// session time out need login again and request
							// again
							String message = loginAgain();
							if (message == null) {
								m.what = 2;
								b.putString("method", "registerStore");
								m.setData(b);
							} else {
								// Contains server error message login failed
								m.what = -1;
								b.putString("message", message);
								m.setData(b);
							}
						} else if (status.equalsIgnoreCase("8")) {
							m.what = GlobalParam.REGISTER_SUCCESS;
							b.putString("message", json.getString("message"));
							m.setData(b);
						} else {
							m.what = -1;
							b.putString("message", json.getString("message"));
							m.setData(b);
						}
					} else {
						Bundle b = new Bundle();
						m.what = -1;
						b.putString("message", "返回数据有误");
						m.setData(b);
					}

					mHandler.sendMessage(m);
				} catch (Exception e) {
					Log.i("1510", e.getMessage(), e);
					if (!isCancel) {
						Message m = Message.obtain();
						Bundle b = new Bundle();
						b.putString("error", code + ":" + e.toString());
						m.setData(b);
						m.what = -1;
						mHandler.sendMessage(m);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception e) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception e) {
						}
					}
					if (conn != null) {
						try {
							conn.disconnect();
						} catch (Exception e) {
						}
					}
				}
			}
		};
		t.start();
	}

	private String loginAgain() {
		HttpURLConnection conn = null;
		OutputStream os = null;
		InputStream is = null;
		int code = 0;
		try {
			Log.i("1510", GlobalParam.getInstance().IMEI);
			String userno = GlobalParam.getInstance().GetLastLoginInfo()
					.getUserno();
			String password = GlobalParam.getInstance().GetLastLoginInfo()
					.getPassword();
			boolean isAutoLogin = GlobalParam.getInstance().GetLastLoginInfo()
					.isAutoLogin();
			String requestParam = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><Login xmlns=\"http://tempuri.org/\"><username>"
					+ userno
					+ "</username><password>"
					+ password
					+ "</password></Login></S:Body></S:Envelope>";
			URL u = new URL(requestUrl);
			ConnectivityManager connMng = (ConnectivityManager) GlobalParam
					.getInstance().getSystemService(
							Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInf = connMng.getActiveNetworkInfo();

			if (netInf != null && "WIFI".equalsIgnoreCase(netInf.getTypeName())) {
				conn = (HttpURLConnection) u.openConnection();
			} else {
				String proxyHost = android.net.Proxy.getDefaultHost();
				if (proxyHost != null) {
					java.net.Proxy p = new java.net.Proxy(
							java.net.Proxy.Type.HTTP, new InetSocketAddress(
									android.net.Proxy.getDefaultHost(),
									android.net.Proxy.getDefaultPort()));
					conn = (HttpURLConnection) u.openConnection(p);
				} else {
					conn = (HttpURLConnection) u.openConnection();
				}
			}
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(60000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.addRequestProperty("SOAPAction",
					"\"http://tempuri.org/Login\"");
			conn.addRequestProperty(
					"Accept",
					"text/xml, multipart/related, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
			conn.addRequestProperty("Content-Type",
					"text/xml;charset=\"UTF-8\"");
			conn.addRequestProperty("Host", "218.83.243.78:8090");
			os = conn.getOutputStream();
			os.write(requestParam.getBytes("UTF-8"));
			os.flush();
			code = conn.getResponseCode();
			is = conn.getInputStream();
			byte[] data = new byte[255 * 255];
			int chunk = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((chunk = is.read(data)) != -1) {
				baos.write(data, 0, chunk);
			}
			String result = new String(baos.toByteArray(), "UTF-8");
			int start = result.indexOf("<LoginResult>");
			int end = result.indexOf("</LoginResult>");
			String s = result.substring(start + "<LoginResult>".length(), end);
			Log.i("1510", s);
			Message m = Message.obtain();
			JSONObject json = (JSONObject) new JSONTokener(s).nextValue();
			if (json.has("status")) {
				return json.getString("message");
			} else {
				String sid = json.getString("sid");
				String username = json.getString("username");
				String position = json.getString("position");
				GlobalParam.getInstance().SetLastLoginInfo(sid, userno,
						username, password, position, isAutoLogin);
				m.what = GlobalParam.LOGIN_SUCCESS;
				Bundle b = new Bundle();
				b.putString("username", username);
				b.putString("position", position);
				m.setData(b);
			}

			return null;
		} catch (Exception e) {
			Log.i("1510", e.getMessage(), e);
			return code + ":" + e.toString();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception e) {
				}
			}
		}
	}

	public void cancel() {
		Log.i("1510", "Request cancel");
		isCancel = true;
		if (t != null) {
			synchronized (t) {
				try {
					t.interrupt();
				} catch (Exception e) {

				}
			}
		}
	}
}
