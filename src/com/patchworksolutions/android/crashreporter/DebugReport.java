package com.patchworksolutions.android.crashreporter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.crypto.SecretKey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class DebugReport {
	private static final String EXCEPTION_REPORT_FILENAME = "postmortem.trace";
	private Activity mApp;
	private String mReport = "";

	private DebugReport(Throwable thrown, Activity app) {
		this.mApp = app;
		this.mReport = DebugReportUtil.getDebugReport(thrown, mApp);
	}

	private DebugReport(Activity app) {
		this.mApp = app;
        String currentLine = "";
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(mApp.openFileInput(EXCEPTION_REPORT_FILENAME)));
            while ((currentLine = reader.readLine()) != null) {
                mReport += currentLine + "\n";
            }
        } catch (FileNotFoundException eFnf) {}
        catch (IOException eIo) {}
	}

	public void saveDebugReport() {
        try {
            FileOutputStream file = mApp.openFileOutput(
            		EXCEPTION_REPORT_FILENAME, Context.MODE_PRIVATE);
            file.write(mReport.getBytes());
            file.close();
        } catch (IOException ioe) {}
	}
	
	public void sendDebugReport(MessageTemplate template) {
        if (sendDebugReportToAuthor(template)) {
            mApp.deleteFile(EXCEPTION_REPORT_FILENAME);
        }
	}
	
    private Boolean sendDebugReportToAuthor(MessageTemplate template) {
        if (mReport != null && mReport.length() > 0) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String subject = mApp.getTitle() + " " + template.getMsgSubjectTag();
            String body = "\n" + template.getMsgBody() + "\n\n" + mReport + "\n\n";
            
            byte[] desKeyData = template.getSecretDesKeyData();
            if(desKeyData != null) {
            	SecretKey secretKey = DesEncrypter.buildSecretKey(desKeyData);
            	if(secretKey != null) {
            		body = new DesEncrypter(secretKey).encrypt(body);
            	}
            }
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { template.getMsgSendTo() });
            intent.putExtra(Intent.EXTRA_TEXT, body);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.setType("message/rfc822");
            Boolean hasSendRecipients = (mApp.getPackageManager()
                    .queryIntentActivities(intent, 0).size() > 0);
            if (hasSendRecipients) {
                mApp.startActivity(intent);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
	
	public static DebugReport create(Throwable thrown, Activity app) {
		return new DebugReport(thrown, app);
	}
	
	public static DebugReport retrieve(Activity app) {
		return new DebugReport(app);
	}
	
}
