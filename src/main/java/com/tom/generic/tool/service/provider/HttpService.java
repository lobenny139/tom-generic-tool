package com.tom.generic.tool.service.provider;

import com.tom.generic.tool.service.IHttpService;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Getter
@Setter
@Component
@Service
public class HttpService implements IHttpService {

//    https://www.ewdna.com/2009/11/apache-httpclient-4x-get-post.html
//    https://mkyong.com/java/apache-httpclient-examples/

    private static Logger logger = LoggerFactory.getLogger(HttpService.class);

    @Override
    public String sendGET( String requestUrl, Map<String, String> headers) {
        return    sendGET(false, requestUrl, headers);
    }

    @Override
    public String sendSSHGET( String requestUrl, Map<String, String> headers) {
        return    sendGET(true, requestUrl, headers);
    }

    @Override
    public String sendPOST(String requestUrl, String body, Map<String, String> headers) {
        return sendPOST(false,requestUrl,  body, headers);
    }

    @Override
    public String sendSSHPOST(String requestUrl, String body, Map<String, String> headers) {
        return sendPOST(true,requestUrl,  body, headers);
    }

    protected CloseableHttpClient getClient(boolean isSSL) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CloseableHttpClient httpClient = null;
        if(isSSL){
            httpClient = HttpClients
                    .custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
        }else{
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }

    protected String sendGET(boolean isSSL, String requestUrl, Map<String, String> headers)  {
        String result = null;
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try{
            httpClient = this.getClient(isSSL);
            HttpGet request = new HttpGet( requestUrl );
            logger.info("Try to send " + request.getMethod() + " request to remote[url=" + requestUrl + "]");
            logger.info("Security " + request.getMethod() + ":" + isSSL);

            // add request headers
            if(headers != null && headers.size() > 0) {
                for (Map.Entry entry : headers.entrySet()) {
//                    System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());
                    logger.info("Headers:[" + entry.getKey()+":" + entry.getValue() +"]");
                    request.addHeader(entry.getKey().toString(), entry.getValue().toString());
                }
            }

            response = httpClient.execute(request);
            logger.info("Sent, and remote response status[" + response.getStatusLine().toString() +"]");

            result = handleResponse(response);

        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            }catch(IOException e){}
        }
        return result;
    }


    protected String sendPOST(boolean isSSL, String requestUrl, String body, Map<String, String> headers) {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = null;

        try{
            httpClient = this.getClient(isSSL);
            HttpPost request = new HttpPost(requestUrl);
            logger.info("Try to send" + request.getMethod() + " request to remote[url=" + requestUrl + "]");
            logger.info("Security " + request.getMethod() + ":" + isSSL);

            // add request headers
            if(headers != null && headers.size() > 0) {
                for (Map.Entry entry : headers.entrySet()) {
//                    System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());
                    logger.info("Headers:[" + entry.getKey()+":" + entry.getValue() +"]");
                    request.addHeader(entry.getKey().toString(), entry.getValue().toString());
                }
            }
            // add body
            if(body != null && body.trim().length() > 0){
                request.setEntity(new StringEntity( body.trim() ));
            }

            response = httpClient.execute(request);
            logger.info("Sent, and remote response status[" + response.getStatusLine().toString() +"]");

            result = handleResponse(response);

        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            }catch(IOException e){}
        }
        return result;
    }

    protected String handleResponse(CloseableHttpResponse response) throws IOException {
        String result = null;
        if(response!= null && response.getStatusLine().getStatusCode() == 200){
//            System.out.println(response.getProtocolVersion());              // HTTP/1.1
//            System.out.println(response.getStatusLine().getStatusCode());   // 200
//            System.out.println(response.getStatusLine().getReasonPhrase()); // OK
//            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // return page content
                result = EntityUtils.toString(entity);
            }else{
                result = response.getStatusLine().toString();
            }
        }else if(response!= null && response.getStatusLine().getStatusCode() != 200){
            result = Integer.toString(response.getStatusLine().getStatusCode());
        }else{
            result = "Remote NO Response";
            logger.warn(result);
        }
        return result;
    }

}
