package com.patchworksolutions.android.crashreporter;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import net.iharder.Base64.Base64;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

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
    private SecretKey secretKey = null;

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