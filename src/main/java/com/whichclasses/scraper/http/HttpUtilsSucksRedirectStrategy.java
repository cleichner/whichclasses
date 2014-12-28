package com.whichclasses.scraper.http;

import java.net.URI;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectStrategy;

public class HttpUtilsSucksRedirectStrategy extends DefaultRedirectStrategy {
  @Override protected URI createLocationURI(String location) throws ProtocolException {
    // Webauth generates redirects URLs with spaces in them, which HttpUtils says are "not valid".
    if (location.indexOf(' ') >= 0) {
      location = location.replace(" ", "%20");
    }

    return super.createLocationURI(location);
  }
}
