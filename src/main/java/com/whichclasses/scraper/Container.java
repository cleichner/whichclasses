package com.whichclasses.scraper;

import java.util.Map;

/**
 * Parent class to manage logic consistent between objects that act as
 * "containers", or parents, for a Child type.
 */
public interface Container<Child> {
	public Map<String, Child> getChildren();
}
