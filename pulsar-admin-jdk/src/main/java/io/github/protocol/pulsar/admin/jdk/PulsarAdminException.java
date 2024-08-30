package io.github.protocol.pulsar.admin.jdk;

public class PulsarAdminException extends Exception {
    private static final int DEFAULT_STATUS_CODE = 500;

    private final int statusCode;

    public PulsarAdminException(Throwable t) {
        super(t);
        statusCode = DEFAULT_STATUS_CODE;
    }

    public PulsarAdminException(String message) {
        this(message, DEFAULT_STATUS_CODE);
    }

    public PulsarAdminException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
