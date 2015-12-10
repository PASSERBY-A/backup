package com.cmsz.cloudplatform.wsclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
//import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.utils.ServiceOptionUtil;

/**
 * 
 * @author Zhefang Chen
 *
 */
//@Service("cloudStackApiWebClient")
public class CloudStackApiWebClient {
    public static final Logger log = Logger.getLogger(CloudStackApiWebClient.class.getName());
    public static final String METHOD_TYPE_GET = "GET"; 
    public static final String METHOD_TYPE_POST = "POST"; 
    private String apiSecretUrl = null;
    private String apiUnSecretUrl = null;
    
	public CloudStackApiWebClient() {
		super();
	}
	
	public String getApiSecretUrl() {
		return apiSecretUrl;
	}

	public void setApiSecretUrl(String apiSecretUrl) {
		this.apiSecretUrl = apiSecretUrl;
	}

	public String getApiUnSecretUrl() {
		return apiUnSecretUrl;
	}

	public void setApiUnSecretUrl(String apiUnSecretUrl) {
		this.apiUnSecretUrl = apiUnSecretUrl;
	}
	
//	public static class Response {
//		private String type = null;
//		private String sessionKey = null;
//		private String responseString = null;
//		private int statusCode = 0;
//		private String userid = null;
//		
//		public Response() {
//			super();
//		}
//
//		public String getType() {
//			return type;
//		}
//
//		public void setType(String type) {
//			this.type = type;
//		}
//
//		public String getResponseString() {
//			return responseString;
//		}
//
//		public void setResponseString(String responseString) {
//			this.responseString = responseString;
//		}
//
//
//		public int getStatusCode() {
//			return statusCode;
//		}
//
//		public void setStatusCode(int statusCode) {
//			this.statusCode = statusCode;
//		}
//
//		@Override
//		public String toString() {
//			return "Response [type=" + type + ", sessionKey=" + sessionKey
//					+ ", responseString=" + responseString + ", statusCode="
//					+ statusCode + ", userid=" + userid + "]";
//		}
//
//		public String getUserid() {
//			return userid;
//		}
//
//		public void setUserid(String userid) {
//			this.userid = userid;
//		}
//		
//		
//	}

	
	@SuppressWarnings("deprecation")
	public Response request(String methodType, Map<String, Object[]> params, boolean isSecret) { 
        HttpUriRequest httpReq = null;
        HttpResponse httpResp = null;
        String url = isSecret ? apiSecretUrl : apiUnSecretUrl;
        Response result = new Response(); 
        long timeout = 3l;
        timeout = timeout * 1000;  
        
        if (METHOD_TYPE_GET.equals(methodType)) {
        	StringBuilder sb = new StringBuilder();
        	if (null != params) {
    			for (String key : params.keySet()) {
    				if (false == "secretkey".equalsIgnoreCase(key) && false == "sessionkey".equalsIgnoreCase(key)) {
    					sb.append(key).append("=").append(encoder((String)params.get(key)[0])).append("&");
    				}
    			}
    			sb.deleteCharAt(sb.length() - 1);
    			
    			if (isSecret) {
    				url = url + "?" + sb.toString() + "&signature=" + getSignature(params, true);
    			} else {
    				url = url + "?" + sb.toString();
    			} 
    				
	        	if (log.isDebugEnabled()) {
	        		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ", generic url -> " + url);
//	        		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + ", generic url -> " + url);
	        	}
        	}
            	
        	httpReq = new HttpGet(url);
        	
         } else { 
        	 
        	httpReq = new HttpPost(url);
        	List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        		
			if (null != params) {
				Set<String> keys = params.keySet();
				for (String key : keys) {
					if (false == "secretkey".equalsIgnoreCase(key) && false == "sessionkey".equalsIgnoreCase(key)) {
						postParams.add(new BasicNameValuePair(key, (String)params.get(key)[0]));
					}
				}
				
				if (isSecret) {
					String signature = getSignature(params, false);
					postParams.add(new BasicNameValuePair("signature", signature));
				}
				UrlEncodedFormEntity uefEntity;
				try {
					uefEntity = new UrlEncodedFormEntity(postParams, "UTF-8");
					((HttpPost)httpReq).setEntity(uefEntity);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}        		
			
			if (log.isDebugEnabled()) {
				Iterator<NameValuePair> it = postParams.iterator();
				log.debug("------------------post request params begin-------------------");
				log.debug("------------------post request params begin-------------------");
				while (it.hasNext()) {
					NameValuePair nv = it.next();
					
					if (log.isDebugEnabled()) {
						log.debug(nv.getName() + "=" + nv.getValue());
					}
				}
				log.debug("------------------post request params end-------------------");
				log.debug("------------------post request params end-------------------");
			}
         }
        
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        
        try {
			httpResp = httpclient.execute(httpReq);
			
	        if (null != httpResp) {
	        	HttpEntity entityResp = httpResp.getEntity();
	        	
	        	result.setResponseString(EntityUtils.toString(entityResp, "UTF-8"));
				result.setStatusCode(httpResp.getStatusLine().getStatusCode());
				
	        	if (log.isDebugEnabled()) {
	    	        Header[] hds = httpResp.getAllHeaders();
	    	        if (null != hds) {
		    	        for (Header hd : hds) {
		    	        	log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " : HttpRequest response headName[" + hd.getName() + "] headValue[" + hd.getValue() + "]");
		    	        }
	        		} else {
	        			log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " : HttpRequest response header is empty");
	        		}
	    	        
	            }
	        	
	        }

    	
         } catch (Exception e) {
        	 // TODO
        	log.error(e.getMessage(), e);
        	//TODO throwing and handling
         } finally {
        	 httpclient.getConnectionManager().shutdown();
         }
        	 
    	
    	if (log.isDebugEnabled()) {
    		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": httpclient.execute(new HttpRequest(" + url + ") respone -> " + result);
    	}
        	
        	
        return result; 
 
    } 
 
    
    private static Map<String, String> header2Map(Header[] headers) { 
        Map<String, String> tmp = null; 
        if (null != headers) { 
            tmp = new HashMap<String, String>(); 
            for (Header header : headers) { 
                tmp.put(header.getName(), header.getValue()); 
            } 
        } 
        return tmp; 
    }  
    

	private static String getSignature(Map<String, Object[]> param, boolean isUrlEncode) {
		
		String result = null; 
		if (null != param.get("secretkey")) {
			
	    	String secretkey = (String)param.get("secretkey")[0];
			SortedMap<String, String> need2Sign = new TreeMap<String, String>();
			for (String s : param.keySet()) {
				need2Sign.put(s, encoder(((String)param.get(s)[0]).toLowerCase()).replaceAll("\\+", "%20"));
			}
			
			StringBuilder sb2 = new StringBuilder();
			List<String> keylist = new ArrayList<String>();
			keylist.addAll(need2Sign.keySet());
			Collections.sort(keylist, new Comparator<String>(){
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
			
			Map<String, String> cmdParamToFirstMap = ServiceOptionUtil.obtainCommandParamOrderToFirstMap();
			
			for (String s : keylist) {
				if (false == "secretkey".equalsIgnoreCase(s) && false == "sessionkey".equalsIgnoreCase(s)) {
					String cmd = need2Sign.get("command");
					if (cmdParamToFirstMap.containsKey(cmd) && null != need2Sign.get(cmdParamToFirstMap.get(cmd))) {
						if (cmdParamToFirstMap.get(cmd).equalsIgnoreCase(s)) {
							sb2.insert(0, s.toLowerCase() + "=" + need2Sign.get(s) + "&");
						} else {
							sb2.append(s.toLowerCase()).append("=").append(need2Sign.get(s)).append("&");
						}
					} else {
						sb2.append(s.toLowerCase()).append("=").append(need2Sign.get(s)).append("&");
					}
//					if (("listserviceofferings".equalsIgnoreCase(cmd) || 
//							"findHostsForMigration".equalsIgnoreCase(cmd)
//							) && null != need2Sign.get("virtualmachineid")) {
//						if ("virtualmachineid".equalsIgnoreCase(s)) {
//							sb2.insert(0, s + "=" + need2Sign.get(s) + "&");
//						} else {
//							sb2.append(s).append("=").append(need2Sign.get(s)).append("&");
//						}
//					} else {
//						sb2.append(s).append("=").append(need2Sign.get(s)).append("&");
//					}
				}
				
			}
			sb2.deleteCharAt(sb2.length() - 1);
			log.debug("============================================================================");			
			log.debug("============================================================================");
			log.debug("sb2.toString().toLowerCase(), === "+sb2.toString().toLowerCase());
//			System.out.println("sb2.toString().toLowerCase(), === "+sb2.toString().toLowerCase());
			log.debug("============================================================================");
			log.debug("============================================================================");
			result = HmacSha.encodEncrypt(sb2.toString().toLowerCase(), secretkey, isUrlEncode);
			
		}
		return result;
	}

	/**
	 * URL Encoder tool
	 * @param uncode
	 * @return
	 */
	private static String encoder(final String uncode) {
	    try {
            return URLEncoder.encode(uncode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            return uncode;
        }
	    
	}
	
}


class HmacSha {

	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";

	static String encodEncrypt(String encryptText, String encryptKey, boolean isUrlEncode) {
		return encode2Base64(encrypt(encryptText, encryptKey), isUrlEncode);
	}

	static byte[] encrypt(String encryptText, String encryptKey) {
		try {
			byte[] data = encryptKey.getBytes(ENCODING);
			SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
			Mac mac = Mac.getInstance(MAC_NAME);
			mac.init(secretKey);

			byte[] text = encryptText.getBytes(ENCODING);
			return mac.doFinal(text);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static String encode2Base64(byte[] content, boolean isUrlEncode) {
		Base64 base64 = new Base64();
		byte[] tmp = base64.encode(content);
		if (isUrlEncode) {
			return new String(tmp).replaceAll("[/]", "%2f").replaceAll("[=]", "%3d").replaceAll("[+]", "%2b");
		} else {
			return new String(tmp);
		}
	}
}


