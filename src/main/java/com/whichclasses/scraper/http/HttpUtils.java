package com.whichclasses.scraper.http;

import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

public class HttpUtils {
  public static String getFirstQueryParameter(String url, String name) {
    try {
      // Thanks, Obama.
      url = url.replace(" ", "%20");
      List<NameValuePair> queryParameterList = new URIBuilder(url).getQueryParams();
      for (NameValuePair queryParameter : queryParameterList) {
        if (queryParameter.getName().equals(name)) {
          return queryParameter.getValue();
        }
      }
    } catch (URISyntaxException e) {}
    return null;
  }
}
