package com.udacity.jwdnd.course1.cloudstorage.constant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;

public enum FileMessageEnum {
    FILE_UPLOADED("File uploaded!"),
    FILE_DELETED("You deleted a file."),
    DUPLICATE_FILE("File already exists!"),
    SELECT_FILE("Please a select a File!"),
    UPLOAD_FILE_ERROR("Failed to upload file."),
    DELETE_FILE_ERROR("Failed to delete file."),
    EXCEEDED_FILE_SIZE_LIMIT("file size limit exceeded, maximum size is 10mb\n");

    public final String message;
    @Autowired
    private MultipartProperties multipartProperties;

    FileMessageEnum(String message) {
        this.message = message;
    }
}
