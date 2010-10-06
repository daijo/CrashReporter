package com.patchworksolutions.android.crashreporter;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;

/**
 * 
 * Use: protected ReportingExceptionHandler mDamageReport = new ReportingExceptionHandler(this, 
 *           new ReportingExceptionHandlerSettings("receiver@domain", "App Name Exception Report"));
 *
 *    public void onCreate(Bundle savedInstanceState) {
 *       super.onCreate(savedInstanceState);
 *
 *       // Set default exception handler and report old crash
 *       mDamageReport.run();
 *       Thread.setDefaultUncaughtExceptionHandler(mDamageReport);
 */
public class ReportingExceptionHandler implements UncaughtExceptionHandler, Runnable {

    private Thread.UncaughtExceptionHandler defaultUEH;
    private Activity app = null;
    private MessageTemplate template;

    public ReportingExceptionHandler(Activity aApp, MessageTemplate template) {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        app = aApp;
        this.template = template;
    }

    public void uncaughtException(Thread t, Throwable e) {
        submit(e);
        defaultUEH.uncaughtException(t, e);
    }

    public void run() {
    	DebugReport report = DebugReport.retrieve(app);
    	report.sendDebugReport(template);
    }

    public void submit(Throwable e) {
    	DebugReport report = DebugReport.create(e, app);
    	report.saveDebugReport();
        app.runOnUiThread(this);
    }

 }