package com.patchworksolutions.android.crashreporter;

/**
 * 
 */
public class MessageTemplate {

    private String msgSubjectTag = "Exception Report"; 	// "app title + this tag" 
    													// = email subject
    
    private String msgSentTo = "";
 
    private byte[] secretDesKeyData = null;
    
    private String msgBody = "Please help by sending this email. "
            + "No personal information is being sent (you can check by reading the rest of the email).";
    
    public MessageTemplate(String msgSentTo) {
        this.msgSentTo = msgSentTo;
    }
    
    public MessageTemplate(String msgSentTo, String msgSubjectTag) {
        this.msgSubjectTag = msgSubjectTag;
        this.msgSentTo = msgSentTo;
    }
    
    public MessageTemplate(String msgSentTo, String msgSubjectTag,
			byte[] secretDesKeyData) {
        this.msgSubjectTag = msgSubjectTag;
        this.msgSentTo = msgSentTo;
        this.secretDesKeyData = secretDesKeyData;
	}
	
    public String getMsgSubjectTag() {
        return msgSubjectTag;
    }
    
    public String getMsgSendTo() {
        return msgSentTo;
    }
    
    public String getMsgBody() {
        return msgBody;
    }

	public byte[] getSecretDesKeyData() {
		return secretDesKeyData;
	}
}
