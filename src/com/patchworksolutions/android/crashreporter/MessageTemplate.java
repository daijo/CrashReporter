package com.patchworksolutions.android.crashreporter;


/**
 * 
 * 
 */
public class MessageTemplate {

    private String exceptionReportFilename = "postmortem.trace";

    private String msgSubjectTag = "Exception Report"; // "app title + this tag"
                                                                        // =
                                                                        // email
                                                                        // subject
    private String msgSentTo = ""; // email
                                                                        // will
                                                                        // be
                                                                        // sent
                                                                        // to
                                                                        // this
                                                                        // account
 
    private byte[] secretDesKeyData = null;
    
    // the following may be something you wish to consider localizing
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

	public String getExceptionReportFilename() {
        return exceptionReportFilename;
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

    // Note the following bytes are not realistic secret key data 
    // bytes but are simply supplied as an illustration of using data
    // bytes (key material) you already have to build a DESKeySpec.
    // byte[] desKeyData = { (byte)0x01, (byte)0x02, (byte)0x03, 
    //    (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07, (byte)0x08 };
	public byte[] getSecretDesKeyData() {
		return secretDesKeyData;
	}
}
