package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dao.NotesDAO;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public String saveNote(Authentication auth, @ModelAttribute NotesDAO note, RedirectAttributes redirectAttributes) {
        Users userDb = userService.getUser(auth.getName());

        if (note.getNoteId() == null || note.getNoteId() <= 0) {
            Notes newNote = new Notes(0, note.getNoteTitle(), note.getNoteDescription(), userDb.getUserId());
            noteService.saveNote(newNote);
            redirectAttributes.addAttribute("createNote", true);
            redirectAttributes.addAttribute("success", true);
        } else {
            Notes updatedNote = new Notes(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription(), userDb.getUserId());
            noteService.updateNote(updatedNote);
            redirectAttributes.addAttribute("updateNote", true);
            redirectAttributes.addAttribute("success", true);
        }
        return "redirect:/result";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable(value = "noteId") Integer noteId, RedirectAttributes redirectAttributes) {
        this.noteService.deleteNote(noteId);
        redirectAttributes.addAttribute("deleteNote", true);
        redirectAttributes.addAttribute("success", true);
        return "redirect:/result";
    }

}
