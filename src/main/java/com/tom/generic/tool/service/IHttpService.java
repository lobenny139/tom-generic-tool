package com.tom.generic.tool.service;

import java.util.Map;

public interface IHttpService {

    public String sendGET( String requestUrl, Map<String, String> headers);

    public String sendSSHGET( String requestUrl, Map<String, String> headers);

    public String sendPOST(String requestUrl, String body, Map<String, String> headers);

    public String sendSSHPOST(String requestUrl, String body, Map<String, String> headers) ;

}
