package com.whichclasses.scraper;

public class Scraper {

  public static void main(String[] args) throws Exception {
    AuthenticatedClient client = new AuthenticatedClient();

    // First: log on to the TCE site
    client.getPage("https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/logon.aspx");

    DeptListPage page = new DeptListPage(client);
    System.out.println(page.getDepartmentPages());
  }

}
