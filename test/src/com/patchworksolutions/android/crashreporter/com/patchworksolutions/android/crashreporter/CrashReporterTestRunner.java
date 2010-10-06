package com.patchworksolutions.android.crashreporter;

import com.patchworksolutions.android.crashreporter.DesEncrypterTest;

import junit.framework.TestSuite;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

public class CrashReporterTestRunner extends InstrumentationTestRunner {
    @Override
    public TestSuite getAllTests() {
        InstrumentationTestSuite suite = new InstrumentationTestSuite(this);

        suite.addTestSuite(DesEncrypterTest.class);
        suite.addTestSuite(DebugReportTest.class);
        return suite;
    }

    @Override
    public ClassLoader getLoader() {
        return CrashReporterTestRunner.class.getClassLoader();
    }
}