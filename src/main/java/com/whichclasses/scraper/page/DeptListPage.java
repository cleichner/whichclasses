package com.whichclasses.scraper.page;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.whichclasses.http.HttpUtils;
import com.whichclasses.scraper.Department;
import com.whichclasses.scraper.DeptList;
import com.whichclasses.scraper.page.DepartmentPage.DepartmentPageFactory;

public class DeptListPage extends CacheableLazyLoadedPage implements DeptList {

  private static final String DEPARTMENT_LIST_URL =
      "https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/DeptList.aspx";
  private final DepartmentPageFactory departmentPageFactory;

  @Inject
  DeptListPage(DepartmentPageFactory departmentPageFactory) {
    this.departmentPageFactory = departmentPageFactory;
  }

  @Override
  String getHtmlUrl() {
    return DEPARTMENT_LIST_URL;
  }

  @Override
  public Map<String, Department> getChildren() {
    Document document = getDocument();
    Elements departmentLinks = document.select("#GV1 a[href]");
    Map<String, Department> departmentPages = Maps.newHashMap();
    for (Element departmentLink : departmentLinks) {
      String departmentId = HttpUtils.getFirstQueryParameter(departmentLink.attr("href"), "crssub");
      if (departmentId != null && departmentId.length() > 0) {
        String departmentName = departmentLink.text();
        // TODO(gunsch): Reduce name in map to department code.
        departmentPages.put(departmentName,
        		departmentPageFactory.create(departmentId, departmentName));
      }
    }

    return departmentPages;
  }
}
