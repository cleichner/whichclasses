package com.whichclasses.scraper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.whichclasses.http.HttpUtilsSucksRedirectStrategy;

/**
 * Makes all Http requests to the TCE website, authenticating
 */
@Singleton
public class AuthenticatedClient {
  private static final String WEBAUTH_LOGIN_URL = "https://webauth.arizona.edu/webauth/login";
  private static final String WEBAUTH_USERNAME = System.getProperty("webauthUsername", "gunsch");
  private static final String WEBAUTH_PASSWORD = System.getProperty("webauthPassword");

  @Inject
  public AuthenticatedClient() {}

  private final CookieStore cookieStore = new BasicCookieStore();
  private boolean isWebauthAuthenticated = false;
  private CloseableHttpClient client = HttpClients.custom()
      .setDefaultCookieStore(cookieStore)
      .setRedirectStrategy(new HttpUtilsSucksRedirectStrategy())
      .build();

  /**
   * Loads a page with an HttpClient that has active Webauth cookies (or will login to
   * get active Webauth cookies if necessary).
   * 
   * @param url absolute URL to fetch
   * @return parsed Jsoup document of the given page
   */
  public Document getPage(String url) {
    try {
      return getPage(getAuthorizedClient(), url);
    } catch (Exception e) {
      // Yes, this is awful. So are checked exceptions.
      throw new RuntimeException("Fetching page failed.", e);
    }
  }

  private Document getPage(CloseableHttpClient httpClient, String url) throws Exception {
    try (CloseableHttpResponse response = client.execute(new HttpGet(url))) {
      return Jsoup.parse(EntityUtils.toString(response.getEntity()));
    }
  }

  private CloseableHttpClient getAuthorizedClient() throws Exception {
    ensureWebauthAuthorization();
    return client;
  }

  /**
   * Ensures that webauth authorization exists, logging in if not.
   *
   * Currently only checks if cookies exist, not if they are also valid.
   * @throws ClientProtocolException 
   * @throws IOException
   * @throws URISyntaxException 
   */
  private void ensureWebauthAuthorization() throws Exception {
    if (isWebauthAuthenticated) return;

    // Simulate a long form!
    Document webauthLoginPage = getPage(client, WEBAUTH_LOGIN_URL);
    Element loginForm = webauthLoginPage.getElementById("fm1");
    Elements formInputList = loginForm.select("[name]");

    List<NameValuePair> formPostData = Lists.newArrayList();
    for (Element input : formInputList) {
      String inputName = input.attr("name");
      if (!"username".equals(inputName) && !"password".equals(inputName)) {
        formPostData.add(new BasicNameValuePair(inputName, input.attr("value")));
      }
    }

    // Fill in username/password
    formPostData.add(new BasicNameValuePair("username", WEBAUTH_USERNAME));
    formPostData.add(new BasicNameValuePair("password", WEBAUTH_PASSWORD));

    java.net.URI loginUri = new java.net.URI(WEBAUTH_LOGIN_URL).resolve(loginForm.attr("action"));
    HttpPost loginRequest = new HttpPost(loginUri);
    loginRequest.setEntity(new UrlEncodedFormEntity(formPostData, Consts.UTF_8));
    try (CloseableHttpResponse loginResponse = client.execute(loginRequest)) {
      // TODO 200 still occurs on login failure. Check for a better indicator.
      if (loginResponse.getStatusLine().getStatusCode() != 200) {
        throw new RuntimeException("Login failed!");
      }
    }
    isWebauthAuthenticated = true;

    // TODO find a better place for this
    // Start a session on the TCE website
    getPage("https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/logon.aspx");
  }
}
