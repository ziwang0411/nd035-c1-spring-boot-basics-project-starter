package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return "home";
    }
    @PostMapping
    public String newNote(Authentication authentication, @ModelAttribute("noteForm") NoteForm noteForm, Model model) {
        Integer userId = userService.getUserId(authentication.getName());
        String noteId = noteForm.getNoteId();
        String title = noteForm.getTitle();
        String description = noteForm.getDescription();
        if (noteId.isEmpty()) {
            noteService.addNote(title, description, userId);
        } else {
            Note currentNote = noteService.getNoteByNoteId(Integer.parseInt(noteId));
            noteService.updateNote(currentNote.getNoteId(), title, description);
        }
        model.addAttribute("notes", noteService.getNoteListings(userId));
        model.addAttribute("results", "success");

        return "result";
    }
}
