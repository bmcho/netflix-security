package com.bmcho.netflix.exception;

public class PasswordSecurityException extends NetflixException {

  public PasswordSecurityException(ErrorCode errorCode) {
    super(errorCode);
  }

  public static class PasswordEncryptionException extends PasswordSecurityException {
    public PasswordEncryptionException() {
      super(ErrorCode.PASSWORD_ENCRYPTION_FAILED);
    }
  }
}
