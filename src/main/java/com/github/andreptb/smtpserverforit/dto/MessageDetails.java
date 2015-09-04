
package com.github.andreptb.smtpserverforit.dto;

public class MessageDetails {

	private MessageMetadata messageMetadata;
	private byte[] body;

	public MessageMetadata getMessageMetadata() {
		return messageMetadata;
	}

	public void setMessageMetadata(MessageMetadata messageMetadata) {
		this.messageMetadata = messageMetadata;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}
}
