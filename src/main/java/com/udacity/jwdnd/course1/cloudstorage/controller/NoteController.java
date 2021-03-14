package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.constant.NoteMessageEnum;
import com.udacity.jwdnd.course1.cloudstorage.dao.NotesDAO;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
        List<Notes> existingNotes = noteService.getNoteByTitle(note.getNoteTitle());

        if(!existingNotes.isEmpty()){
            Boolean isDuplicated = false;
            for (Notes existingNote : existingNotes){
                if(existingNote.getNoteDescription().equals(note.getNoteDescription())){
                    redirectAttributes.addAttribute("message", NoteMessageEnum.NOTE_DUPLICATED.message);
                    redirectAttributes.addAttribute("warning", true);
                    isDuplicated = true;
                    break;
                }
            }

            if(isDuplicated){
                return "redirect:/result";
            }
        }

        if (note.getNoteId() == null || note.getNoteId() <= 0) {
            Notes newNote = new Notes(0, note.getNoteTitle(), note.getNoteDescription(), userDb.getUserId());
            try {
                noteService.saveNote(newNote);
                redirectAttributes.addAttribute("message", NoteMessageEnum.NOTE_CREATED.message);
                redirectAttributes.addAttribute("success", true);
            }catch (Exception e){
                redirectAttributes.addAttribute("message", e.getMessage());
                redirectAttributes.addAttribute("danger", true);
            }


        } else {
            Notes updatedNote = new Notes(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription(), userDb.getUserId());
            try {
                noteService.updateNote(updatedNote);
                redirectAttributes.addAttribute("message", NoteMessageEnum.NOTE_UPDATED.message);
                redirectAttributes.addAttribute("success", true);
            }catch (Exception e){
                redirectAttributes.addAttribute("message", e.getMessage());
                redirectAttributes.addAttribute("danger", true);
            }
        }
        return "redirect:/result";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable(value = "noteId") Integer noteId, RedirectAttributes redirectAttributes) {
        this.noteService.deleteNote(noteId);
        redirectAttributes.addAttribute("message", NoteMessageEnum.NOTE_DELETED.message);
        redirectAttributes.addAttribute("success", true);
        return "redirect:/result";
    }

}
