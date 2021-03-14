package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.constant.NoteMessageEnum;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    private void validateValue(Notes note) throws Exception {
        String message = "";
        Boolean isError = false;
        if(note.getNoteTitle().length() > 20){
            isError = true;
            message += NoteMessageEnum.NOT_VALID_TITLE_LENGTH.message;
        }
        if(note.getNoteDescription().length() > 1000){
            isError = true;
            message += NoteMessageEnum.NOT_VALID_DESCRIPTION_LENGTH.message;
        }

        if(isError) {
            throw new Exception(message);
        }
    }

    public void saveNote(Notes note) throws Exception {
        validateValue(note);
        noteMapper.insert(note.getNoteTitle(), note.getNoteDescription(), note.getUserId());
    }

    public void updateNote(Notes note) throws Exception {
        validateValue(note);
        noteMapper.update(note.getNoteTitle(), note.getNoteDescription(), note.getNoteId());
    }

    public boolean deleteNote(Integer noteId) {
        noteMapper.delete(noteId);
        return true;
    }

    public List<Notes> getNotes(int userId) {
        return noteMapper.getNotes(userId);
    }

    public List<Notes> getNoteByTitle(String noteTitle) {
        return noteMapper.getNotesByTitle(noteTitle);
    }
}
