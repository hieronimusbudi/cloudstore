package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dao.CredentialsDAO;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private final CredentialService credentialService;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public CredentialController(CredentialService credentialService, UserService userService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @PostMapping
    public String saveCredential(Authentication auth, @ModelAttribute CredentialsDAO credential, RedirectAttributes redirectAttributes) {
        Users userDb = userService.getUser(auth.getName());
        SecureRandom random = new SecureRandom();

        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        if (credential.getCredentialId() == null || credential.getCredentialId() <= 0) {
            Credentials newCredential = new Credentials(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), encodedKey, encryptedPassword, userDb.getUserId());
            credentialService.saveCredential(newCredential);
            redirectAttributes.addAttribute("createCredential", true);
            redirectAttributes.addAttribute("success", true);
        } else {
            Credentials updatedCredential = new Credentials(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), encodedKey, encryptedPassword, userDb.getUserId());
            credentialService.updateCredential(updatedCredential);
            redirectAttributes.addAttribute("updateCredential", true);
            redirectAttributes.addAttribute("success", true);
        }

        return "redirect:/result";
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable(value = "credentialId") int credentialId, RedirectAttributes redirectAttributes) {
        this.credentialService.deleteCredential(credentialId);
        redirectAttributes.addAttribute("deleteCredential", true);
        redirectAttributes.addAttribute("success", true);
        return "redirect:/result";
    }
}
