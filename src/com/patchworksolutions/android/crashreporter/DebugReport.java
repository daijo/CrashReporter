package com.patchworksolutions.android.crashreporter;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

public class DebugReport {

    public static String getDebugReport(Throwable thrown, Activity app) {
        NumberFormat formatter = new DecimalFormat("#0.");
        String report = "REPORT\n";

        report += getDebugDescription(thrown, app); // tested

        report += getDebugStackTrace(thrown, formatter); // tested
        
        report += getDebugMD5Hash(thrown); // tested

        report += getDebugCause(thrown, formatter); // tested

        report += getDebugEnvironment(app);

        report += "END REPORT";
        return report;
    }

	public static String getDebugDescription(Throwable thrown, Activity app) {
		String result = app.getPackageName()
                + " generated the following exception:\n";
		result += thrown.toString() + "\n\n";
		return result;
	}

	public static String getDebugCause(Throwable thrown, NumberFormat formatter) {
		StackTraceElement[] stackTrace;
		// if the exception was thrown in a background thread inside
        // AsyncTask, then the actual exception can be found with getCause
        Throwable cause = thrown.getCause();
        String result = "";
        if (cause != null) {
        	result += "CAUSE\n";
        	result += cause.toString() + "\n\n";
            stackTrace = cause.getStackTrace();
            if(stackTrace != null) {
            	for (int i = 0; i < stackTrace.length; i++) {
            		result += formatter.format(i + 1) + "\t"
            		+ stackTrace[i].toString() + "\n";
            	}// for
            }
            result += "END CAUSE\n\n";
        }// if
		return result;
	}

	public static String getDebugMD5Hash(Throwable thrown) {
		String result = "MD5_HASH\n";
		result += md5(thrown.toString() + getUnformatedStackTrace(thrown.getStackTrace())) + "\n";
		result += "END MD5_HASH\n\n";
		return result;
	}

	public static String getDebugStackTrace(Throwable thrown, NumberFormat formatter) {
		StackTraceElement[] stackTrace = thrown.getStackTrace();
		String result = "STACK_TRACE\n";
		if (stackTrace != null && stackTrace.length > 0) {
            for (int i = 0; i < stackTrace.length; i++) {
            	result += formatter.format(i + 1) + "\t"
                        + stackTrace[i].toString() + "\n";
            }// for   
        }
		result += "END STACK_TRACE\n\n";
		return result;
	}

	public static String getDebugEnvironment(Activity app) {
		String result = "";
		// app environment
        PackageManager pm = app.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(app.getPackageName(), 0);
        } catch (NameNotFoundException eNnf) {
            // doubt this will ever run since we want info about our own package
            pi = new PackageInfo();
            pi.versionName = "unknown";
            pi.versionCode = 69;
        }
        Date theDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss_zzz");
        result += "ENV\n";
        result += "Time=" + sdf.format(theDate) + "\n";
        result += "Device=" + Build.FINGERPRINT + "\n";
        try {
            Field manufacturer = Build.class.getField("MANUFACTURER");
            result += "Make=" + manufacturer.get(null) + "\n";
        } catch (SecurityException e) {
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        result += "Model=" + Build.MODEL + "\n";
        result += "Product=" + Build.PRODUCT + "\n";
        result += "App=" + app.getPackageName() + ", version "
                + pi.versionName + " (build " + pi.versionCode + ")\n";
        result += "Locale="
                + app.getResources().getConfiguration().locale
                        .getDisplayName() + "\n";
        result += "SDK: " + Build.VERSION.SDK + "\n";
        result += "Release: " + Build.VERSION.RELEASE + "\n";
        result += "Incremental: " + Build.VERSION.INCREMENTAL + "\n";
        result += "END ENV\n\n";
		return result;
	}	
	
	private static String getUnformatedStackTrace(StackTraceElement[] stackTrace) {
		String unformattedStackTrace = ""; 
        if (stackTrace != null && stackTrace.length > 0) {
            for (int i = 0; i < stackTrace.length; i++) {
                unformattedStackTrace += stackTrace[i].toString();
            }// for
        }
		return unformattedStackTrace;
	}
	
    private static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }    
   
}
