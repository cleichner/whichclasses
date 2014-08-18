package com.whichclasses.scraper.page;

import java.util.Map;

/**
 * Parent class to manage logic consistent between pages that act as
 * "containers", or parents, for a ChildPage type.
 */
public interface ContainerPage<ChildPage> {
	public Map<String, ChildPage> getChildPages();
}
