package com.attask.jenkins.imagebadge;

import hudson.tasks.junit.TestAction;

/**
 * User: joeljohnson
 * Date: 4/12/12
 * Time: 3:09 PM
 */
public class ImageBadge extends TestAction {
	private final String testName;
	private final String imageUrl;

	public ImageBadge(String testName, String imageUrl) {
		this.testName = testName;
		this.imageUrl = imageUrl;
	}

	public String getTestName() {
		return testName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getIconFileName() { return null; }
	public String getDisplayName() { return null; }
	public String getUrlName() { return null; }
}
