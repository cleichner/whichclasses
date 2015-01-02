package com.whichclasses.scraper.page;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.whichclasses.model.Department;
import com.whichclasses.model.DeptList;
import com.whichclasses.scraper.http.HttpUtils;
import com.whichclasses.scraper.model.DeptListModel;
import com.whichclasses.scraper.page.DepartmentPage.DepartmentPageFactory;

public class DeptListPage extends CacheableLazyLoadedPage implements DeptList {

  private static final String DEPARTMENT_LIST_URL =
      "https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/DeptList.aspx";
  private final DepartmentPageFactory departmentPageFactory;
  private DeptListModel model;

  @Inject
  DeptListPage(DepartmentPageFactory departmentPageFactory) {
    this.departmentPageFactory = departmentPageFactory;
  }

  @Override
  String getHtmlUrl() {
    return DEPARTMENT_LIST_URL;
  }

  private DeptListModel getModel() {
    if (model != null) {
      return model;
    }

    Document document = getDocument();
    Elements departmentLinks = document.select("#GV1 a[href]");
    Map<String, Department> departmentPages = Maps.newHashMap();
    for (Element departmentLink : departmentLinks) {
      String departmentId = HttpUtils.getFirstQueryParameter(departmentLink.attr("href"), "crssub");
      if (departmentId != null && departmentId.length() > 0) {
        String departmentName = departmentLink.text();
        departmentId = normalizeDepartmentId(departmentId);
        departmentPages.put(departmentId,
                departmentPageFactory.create(departmentId, departmentName));
      }
    }

    return model = new DeptListModel.Builder()
        .setChildren(departmentPages)
        .build();
  }

  /**
   * Strips all spaces and underscores from department id. E.g. "A_ED" --> "AED".
   * @param name
   * @return
   */
  private String normalizeDepartmentId(String name) {
    return name.replaceAll("[\\s_]", "");
  }

  @Override public Map<String, Department> getChildren() { return getModel().getChildren(); }
}
