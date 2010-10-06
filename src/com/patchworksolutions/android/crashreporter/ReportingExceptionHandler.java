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

    public ReportingExceptionHandler(Activity aApp, MessageTemplate settings) {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        app = aApp;
        this.template = settings;

        byte[] desKeyData = settings.getSecretDesKeyData();
        if(desKeyData != null) {
        	secretKey = DesEncrypter.buildSecretKey(desKeyData);
        }
    }

    public void uncaughtException(Thread t, Throwable e) {
        submit(e);
        // do not forget to pass this exception through up the chain
        defaultUEH.uncaughtException(t, e);
    }

    protected void saveDebugReport(String report) {
        // save report to file
        try {
            FileOutputStream file = app.openFileOutput(
                    template.getExceptionReportFilename(), Context.MODE_PRIVATE);
            file.write(report.getBytes());
            file.close();
        } catch (IOException ioe) {
            // error during error report needs to be ignored, do not wish to
            // start infinite loop
        }
    }

    public void sendDebugReportToAuthor() {
        String currentLine = "";
        String report = "";
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(app
                            .openFileInput(template.getExceptionReportFilename())));
            while ((currentLine = reader.readLine()) != null) {
                report += currentLine + "\n";
            }
            if (sendDebugReportToAuthor(report)) {
                app.deleteFile(template.getExceptionReportFilename());
            }
        } catch (FileNotFoundException eFnf) {
            // nothing to do
        } catch (IOException eIo) {
            // not going to report
        }
    }

    public Boolean sendDebugReportToAuthor(String report) {
        if (report != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String subject = app.getTitle() + " " + template.getMsgSubjectTag();
            String body = "\n" + template.getMsgBody() + "\n\n" + report + "\n\n";
            if(secretKey != null) {
            	body = new DesEncrypter(secretKey).encrypt(body);
            }
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { template.getMsgSendTo() });
            intent.putExtra(Intent.EXTRA_TEXT, body);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.setType("message/rfc822");
            Boolean hasSendRecipients = (app.getPackageManager()
                    .queryIntentActivities(intent, 0).size() > 0);
            if (hasSendRecipients) {
                app.startActivity(intent);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void run() {
        sendDebugReportToAuthor();
    }

    public void submit(Throwable e) {
        String theErrReport = DebugReport.getDebugReport(e, app);
        saveDebugReport(theErrReport);
        // try to send file contents via email (need to do so via the UI thread)
        app.runOnUiThread(this);
    }

 }