package com.whichclasses.scraper.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PermanentHtmlDiskCache implements PageCache {

  private static final String TCE_BASE_URI =
      "https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/";
  private final Path diskLocationPath;

  public PermanentHtmlDiskCache(Path diskLocationPath) {
    this.diskLocationPath = diskLocationPath;
  }

  private File getCacheFileLocation(String key) {
    // Note: this assumes our URLs are TCE URLs. Safe if not a URL.
    String urlSuffix = key.substring(key.lastIndexOf('/') + 1);
    try {
      String safeBasename = java.net.URLEncoder.encode(urlSuffix, "UTF-8").replace('%', '-');
      Path storePath = diskLocationPath.resolve(safeBasename);
      return storePath.toFile();
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Shouldn't be an exception.", e);
    }
  }

  @Override
  public void store(String key, Document value) {
    File storeFile = getCacheFileLocation(key);
    try {
      FileUtils.writeStringToFile(storeFile, value.outerHtml());
    } catch (IOException e) {
      storeFile.delete();
      e.printStackTrace();
    }
  }

  @Override
  public Document get(String key) {
    File storeFile = getCacheFileLocation(key);
    if (!storeFile.exists())
      return null;

    try {
      return Jsoup.parse(new FileInputStream(storeFile), "UTF-8", TCE_BASE_URI);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
  
}
