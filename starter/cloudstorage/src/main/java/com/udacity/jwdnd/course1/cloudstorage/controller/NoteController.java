package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, @ModelAttribute("noteForm") NoteForm noteForm, Model model) {
        Integer userId = userService.getUserId(authentication.getName());
        if (userId!=null) {
            model.addAttribute("notes", noteService.getNoteListings(userId));
        }
        return "redirect:/home";
    }
    @PostMapping("add-note")
    public String newNote(Authentication authentication, @ModelAttribute("fileForm") FileForm fileForm, @ModelAttribute("noteForm") NoteForm noteForm, @ModelAttribute("credentialForm") CredentialForm credentialForm, Model model) {
        Integer userId = userService.getUserId(authentication.getName());
        Integer noteId = noteForm.getNoteId();
        String title = noteForm.getTitle();
        String description = noteForm.getDescription();
        if (noteId==null) {
            noteService.addNote(title, description, userId);
        } else {
            Note currentNote = noteService.getNoteByNoteId(noteId);
            noteService.updateNote(currentNote.getNoteId(), title, description);
        }
        model.addAttribute("notes", noteService.getNoteListings(userId));
        model.addAttribute("result", "success");

        return "result";
    }

    @GetMapping("delete-note/{noteId}")
    public String deleteNote(Authentication authentication, @ModelAttribute("fileForm") FileForm fileForm, @ModelAttribute("noteForm") NoteForm noteForm, @PathVariable Integer noteId, Model model) {
        noteService.deleteNote(noteId);
        Integer userId = userService.getUserId(authentication.getName());
        if (userId!=null) {
            model.addAttribute("notes", noteService.getNoteListings(userId));
        }
        model.addAttribute("result", "success");
        return "result";
    }
}
