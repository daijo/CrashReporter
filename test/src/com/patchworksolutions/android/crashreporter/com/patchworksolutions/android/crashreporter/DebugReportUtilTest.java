package com.patchworksolutions.android.crashreporter;

import java.text.DecimalFormat;

import com.patchworksolutions.android.crashreporter.tests.CrashReporterTestActivity;

import android.test.ActivityInstrumentationTestCase2;

public class DebugReportUtilTest extends ActivityInstrumentationTestCase2 <CrashReporterTestActivity>{
	private static final String ACTIVITY_PACKAGE = "com.patchworksolutions.android.crashreporter.tests";
	private static final String TEST_TEXT = "The cause to end all causes.";
	private static final String EMPTY_STRING_MD5_HASH = "d41d8cd98f0b24e980998ecf8427e";
 
    public DebugReportUtilTest() {
        super(ACTIVITY_PACKAGE, CrashReporterTestActivity.class);
    }

    @Override
    public void setUp() throws Exception {
      super.setUp();
    }
    
    public void testGetDebugDescription() throws Exception {
    	String expected = ACTIVITY_PACKAGE + " generated the following exception:\n" + TEST_TEXT + "\n\n";
    	String result = DebugReportUtil.getDebugDescription(new TestThrowable(TEST_TEXT), getActivity());
    	assertTrue(result.compareTo(expected) == 0);
    }
    
    public void testGetDebugStackTrace() throws Exception {
    	String expected = "STACK_TRACE\nEND STACK_TRACE\n\n";
    	String result = DebugReportUtil.getDebugStackTrace(new TestThrowable(""), new DecimalFormat("#0."));
    	assertTrue(result.compareTo(expected) == 0);
    }
    
    public void testGetDebugCause() throws Exception {
    	String expected = "CAUSE\n" + TEST_TEXT + "\n\nEND CAUSE\n\n";
    	String result = DebugReportUtil.getDebugCause(new TestThrowable(""), new DecimalFormat("#0."));
    	assertTrue(result.compareTo(expected) == 0);
    }
    
    public void testGetDebugMD5Hash() throws Exception {
    	String expected = "MD5_HASH\n" + EMPTY_STRING_MD5_HASH + "\nEND MD5_HASH\n\n";
    	String result = DebugReportUtil.getDebugMD5Hash(new TestThrowable(""));
    	assertTrue(result.compareToIgnoreCase(expected) == 0);
    }
    
    public void testGetDebugEnvironment() throws Exception {
    	String result = DebugReportUtil.getDebugEnvironment(getActivity());
    	assertTrue(result != null);
    	//TODO: verify!
    }
    
    private class TestThrowable extends Throwable {
		private static final long serialVersionUID = 1L;
		String mStr = "";
    	
    	public TestThrowable(String str) {
    		super();
    		mStr = str;
    	}
    	
    	@Override
    	public String toString() {
			return mStr;
    	}
    
    	@Override
    	public StackTraceElement[] getStackTrace() { //TODO: mock this
    		return null;
    	}
    	
    	@Override
    	public Throwable getCause() {
    		return new TestThrowable(TEST_TEXT);
    	}
    }

}