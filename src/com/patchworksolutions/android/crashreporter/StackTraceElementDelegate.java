package com.patchworksolutions.android.crashreporter;

public class StackTraceElementDelegate implements StackTraceElementInterface {
	private StackTraceElement delegate;

	public StackTraceElementDelegate(StackTraceElement stackTraceElement) {
		this.delegate = stackTraceElement;
	}
	
	public String toString() {
		return delegate.toString();
	}

}
