package com.udacity.jwdnd.course1.cloudstorage.service;

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

    public void saveNote(Notes note) {
        noteMapper.insert(note.getNoteTitle(), note.getNoteDescription(), note.getUserId());
    }

    public void updateNote(Notes note) {
        noteMapper.update(note.getNoteTitle(), note.getNoteDescription(), note.getNoteId());
    }

    public boolean deleteNote(Integer noteId) {
        noteMapper.delete(noteId);
        return true;
    }

    public List<Notes> getNotes(int userId) {
        return noteMapper.getNotes(userId);
    }

}
