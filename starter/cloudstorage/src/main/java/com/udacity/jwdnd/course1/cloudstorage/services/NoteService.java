package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public Note[] getNoteListings(Integer userId) {
        return noteMapper.getNoteListings(userId);
    }

    public void addNote(String title, String description, Integer userId) {
        Note note = new Note(null, title, description,userId);
        noteMapper.insert(note);
    }

    public Note getNoteByNoteId(int noteId) {
        return noteMapper.getNoteByNoteId(noteId);
    }

    public void updateNote(Integer noteId, String title, String description) {
        noteMapper.updateNote(noteId, title, description);
    }
}
