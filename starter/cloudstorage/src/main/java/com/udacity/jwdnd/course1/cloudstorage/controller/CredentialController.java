package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credential")
public class CredentialController {
    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, Model model) {
        Integer userId = userService.getUserId(authentication.getName());
        if (userId!=null) {
            model.addAttribute("credentials", credentialService.getCredentialListings(userId));
        }
        return "redirect:/home";
    }

    @PostMapping("add-credential")
    public String newCredential(Authentication authentication, @ModelAttribute("credentialForm") CredentialForm credentialForm, Model model) {
        Integer userId = userService.getUserId(authentication.getName());
        Integer credentialId = credentialForm.getCredentialId();
        String url = credentialForm.getUrl();
        String username = credentialForm.getUsername();
        String password = credentialForm.getPassword();
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        if (credentialId==null) {
            credentialService.addCredential(url, username, encryptedPassword, encodedKey, userId);
        } else {
            Credential currentCredential = credentialService.getCredentialByCredentialId(credentialId);
            credentialService.updateCredential(currentCredential.getCredentialId(), url, username,encryptedPassword,encodedKey);
        }
        model.addAttribute("credentials", credentialService.getCredentialListings(userId));
        model.addAttribute("result", "success");
        model.addAttribute("encryptionService", encryptionService);

        return "result";
    }

    @GetMapping(value = "/delete-credential/{credentialId}")
    public String deleteCredential(
            Authentication authentication, @PathVariable Integer credentialId,
            @ModelAttribute("credentialForm") CredentialForm credentialForm, Model model) {
        credentialService.deleteCredential(credentialId);
        Integer userId = userService.getUserId(authentication.getName());
        model.addAttribute("credentials", credentialService.getCredentialListings(userId));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");

        return "result";
    }
}
