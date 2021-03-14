package com.udacity.jwdnd.course1.cloudstorage.constant;

public enum NoteMessageEnum {
    NOTE_CREATED("You successfully created a note."),
    NOTE_UPDATED("You updated a note."),
    NOTE_DELETED("You deleted a note."),
    NOTE_DUPLICATED("Note already available."),
    NOT_VALID_TITLE_LENGTH("Note can't be saved as title exceed 20 characters.\n"),
    NOT_VALID_DESCRIPTION_LENGTH("Note can't be saved as description exceed 1000 characters.\n");

    public final String message;

    NoteMessageEnum(String message) {
        this.message = message;
    }
}
