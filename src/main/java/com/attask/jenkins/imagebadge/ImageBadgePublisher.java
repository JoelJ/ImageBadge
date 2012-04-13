package com.attask.jenkins.imagebadge;

import com.google.common.collect.ImmutableList;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.junit.*;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: joeljohnson
 * Date: 4/12/12
 * Time: 3:06 PM
 */
public class ImageBadgePublisher extends TestDataPublisher {
	private final String regex;

	@DataBoundConstructor
	public ImageBadgePublisher(String regex) {
		this.regex = regex;
	}

	@Override
	public TestResultAction.Data getTestData(final AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener, TestResult testResult) throws IOException, InterruptedException {
		return new TestResultAction.Data() {
			@Override
			@SuppressWarnings("deprecation")
			public List<? extends TestAction> getTestAction(TestObject testObject) {
				ImmutableList.Builder<TestAction> result = ImmutableList.builder();

				if(testObject instanceof CaseResult) {
					CaseResult caseResult = (CaseResult)testObject;
					String testName =  caseResult.getClassName() + "." + caseResult.getName();
					String regex = getRegex().replaceAll("(\\$\\{TEST_NAME\\}|$TEST_NAME)", "\\\\Q" + testName + "\\\\E"); //Couldn't use Pattern.quote here because the replaceAll was stripping out the escape characters

					for (Run<?, ? extends AbstractBuild<?, ?>>.Artifact artifact : build.getArtifacts()) {
						if(artifact.getFileName().matches(regex)) {
							String url = Hudson.getInstance().getRootUrl() + build.getUrl() + "artifact/" + artifact.getHref();
							result.add(new ImageBadge(testName, url));
						}
					}
				}

				return result.build();
			}
		};
	}

	public String getRegex() {
		return regex;
	}

	@Extension
	public static class DescriptorImpl extends Descriptor<TestDataPublisher> {
		@Override
		public String getDisplayName() {
			return "Include links to screenshots of failures (if available)";
		}
	}
}
