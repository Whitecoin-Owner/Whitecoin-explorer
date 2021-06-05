package com.browser.wallet;

public class ContractArgDecodeException extends Exception {
    public ContractArgDecodeException() {
    }

    public ContractArgDecodeException(String message) {
        super(message);
    }

    public ContractArgDecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContractArgDecodeException(Throwable cause) {
        super(cause);
    }

    public ContractArgDecodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
