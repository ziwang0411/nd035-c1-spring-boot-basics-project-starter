package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, @ModelAttribute("fileForm") FileForm fileForm, @ModelAttribute("noteForm") NoteForm noteForm, @ModelAttribute("credentialForm") CredentialForm credentialForm, Model model) throws IOException {
        Integer userId = userService.getUserId(authentication.getName());
        if (userId!=null) {
            model.addAttribute("files", fileService.getFilelistings(userId));
            model.addAttribute("notes", noteService.getNoteListings(userId));
            model.addAttribute("credentials", credentialService.getCredentialListings(userId));
            model.addAttribute("encryptionService", encryptionService);

        }

        return "home";
    }

    @PostMapping
    public String newFile(Authentication authentication, @ModelAttribute("fileForm") FileForm fileForm, @ModelAttribute("noteForm") NoteForm noteForm, @ModelAttribute("credentialForm") CredentialForm credentialForm, Model model) throws IOException {
        Integer userId = userService.getUserId(authentication.getName());
        String[] fileListings = fileService.getFilelistings(userId);
        MultipartFile multipartFile = fileForm.getMultipartFile();
        String fileName = multipartFile.getOriginalFilename();
        if (fileName==null || fileName.equals("")) return "home";
        boolean fileIsDuplicate = false;
        for (String name: fileListings) {
            if (name.equals(fileName)) {
                fileIsDuplicate = true;
                break;
            }
        }
        if (!fileIsDuplicate) {
            fileService.addFile(multipartFile, userId);
            model.addAttribute("result", "success");

        } else {
            model.addAttribute("result", "error");
            model.addAttribute("message", "You have tried to add a duplicate file");
        }
        model.addAttribute("files", fileService.getFilelistings(userId));
        return "result";
    }

//    @GetMapping(
//            value = "/get-file/{fileName}",
//            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
//    )
//    public @ResponseBody
//    byte[] getFile(@PathVariable String fileName) {
//        return fileService.getFile(fileName).getFileData();
//    }

    @GetMapping(
            value = "/get-file/{fileName}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody
    ResponseEntity<byte[]> getFile(@PathVariable String fileName) {
        byte[] bytes=fileService.getFile(fileName).getFileData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        ResponseEntity<byte[]> response =new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
        return response;
    }
    @GetMapping(value = "/delete-file/{fileName}")
    public String deleteFile(
            Authentication authentication, @PathVariable String fileName, @ModelAttribute("fileForm") FileForm fileForm, @ModelAttribute("noteForm") NoteForm noteForm, @ModelAttribute("credentialForm") CredentialForm credentialForm,
            Model model) {
        fileService.deleteFile(fileName);
        Integer userId = userService.getUserId(authentication.getName());
        model.addAttribute("files", fileService.getFilelistings(userId));
        model.addAttribute("result", "success");

        return "result";
    }
}
