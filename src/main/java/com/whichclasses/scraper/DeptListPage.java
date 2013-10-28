package com.whichclasses.scraper;

import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.whichclasses.http.HttpUtils;
import com.whichclasses.scraper.DepartmentPage.DepartmentPageFactory;

public class DeptListPage {

  private static final String DEPARTMENT_LIST_URL =
      "https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/DeptList.aspx";
  private final AuthenticatedClient client;
  private final DepartmentPageFactory departmentPageFactory;
  private Document document;

  @Inject
  DeptListPage(AuthenticatedClient client, DepartmentPageFactory departmentPageFactory) {
    this.client = client;
    this.departmentPageFactory = departmentPageFactory;
  }

  private Document getDocument() {
    if (document == null) {
      document = client.getPage(DEPARTMENT_LIST_URL);
    }
    return document;
  }

  /**
   * @return a list of individual department pages from the department list page
   */
  public List<DepartmentPage> getDepartmentPages() {
    Document document = getDocument();
    Elements departmentLinks = document.select("#GV1 a[href]");
    List<DepartmentPage> departmentPages = Lists.newLinkedList();
    for (Element departmentLink : departmentLinks) {
      String departmentId = HttpUtils.getFirstQueryParameter(departmentLink.attr("href"), "crssub");
      if (departmentId != null && departmentId.length() > 0) {
        String departmentName = departmentLink.text();
        departmentPages.add(departmentPageFactory.create(departmentId, departmentName));
      }
    }

    return departmentPages;
  }
}
