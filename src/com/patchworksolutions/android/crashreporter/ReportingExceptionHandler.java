/* Copyright 2010 Patchwork Solutions AB. All rights reserved.
*
*  Redistribution and use in source and binary forms, with or without modification, are
*  permitted provided that the following conditions are met:
*
*     1. Redistributions of source code must retain the above copyright notice, this list of
*       conditions and the following disclaimer.
*
*     2. Redistributions in binary form must reproduce the above copyright notice, this list
*       of conditions and the following disclaimer in the documentation and/or other materials
*       provided with the distribution.
*
*  THIS SOFTWARE IS PROVIDED BY Patchwork Solutions AB ``AS IS'' AND ANY EXPRESS OR IMPLIED
*  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
*  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> OR
*  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
*  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
*  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
*  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
*  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
*  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
* 
*  The views and conclusions contained in the software and documentation are those of the
*  authors and should not be interpreted as representing official policies, either expressed
*  or implied, of Patchwork Solutions AB.*/

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