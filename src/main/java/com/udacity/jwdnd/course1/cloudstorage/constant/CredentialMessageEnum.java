package com.udacity.jwdnd.course1.cloudstorage.constant;

public enum CredentialMessageEnum {
    CREDENTIAL_CREATED("You successfully created a credential."),
    CREDENTIAL_UPDATED("You updated a credential."),
    CREDENTIAL_DELETED("You deleted a credential."),
    CREDENTIAL_DUPLICATED("User already available."),
    NOT_VALID_URL_LENGTH("Credential can't be saved as url exceed 100 characters.\n"),
    NOT_VALID_USERNAME_LENGTH("Credential can't be saved as username exceed 30 characters.\n"),
    NOT_VALID_PASSWORD_LENGTH("Credential can't be saved as password exceed 30 characters.\n");

    public final String message;

    CredentialMessageEnum(String message) {
        this.message = message;
    }
}
