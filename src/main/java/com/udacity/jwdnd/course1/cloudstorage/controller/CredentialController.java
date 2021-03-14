package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.constant.CredentialMessageEnum;
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
import java.util.List;

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
        List<Credentials> existingCredentials = credentialService.getCredentialsByUsername(credential.getUsername());

        if (!existingCredentials.isEmpty()) {
            redirectAttributes.addAttribute("message", CredentialMessageEnum.CREDENTIAL_DUPLICATED.message);
            redirectAttributes.addAttribute("warning", true);
            return "redirect:/result";
        }

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        if (credential.getCredentialId() == null || credential.getCredentialId() <= 0) {
            Credentials newCredential = new Credentials(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), encodedKey, credential.getPassword(), userDb.getUserId());
            try {
                credentialService.saveCredential(newCredential);
                redirectAttributes.addAttribute("message", CredentialMessageEnum.CREDENTIAL_CREATED.message);
                redirectAttributes.addAttribute("success", true);
            } catch (Exception e) {
                redirectAttributes.addAttribute("message", e.getMessage());
                redirectAttributes.addAttribute("danger", true);
            }

        } else {
            Credentials updatedCredential = new Credentials(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), encodedKey, credential.getPassword(), userDb.getUserId());
            try {
                credentialService.updateCredential(updatedCredential);
                redirectAttributes.addAttribute("message", CredentialMessageEnum.CREDENTIAL_UPDATED.message);
                redirectAttributes.addAttribute("success", true);
            } catch (Exception e) {
                redirectAttributes.addAttribute("message", e.getMessage());
                redirectAttributes.addAttribute("danger", true);
            }
        }

        return "redirect:/result";
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable(value = "credentialId") int credentialId, RedirectAttributes redirectAttributes) {
        this.credentialService.deleteCredential(credentialId);
        redirectAttributes.addAttribute("message", CredentialMessageEnum.CREDENTIAL_DELETED.message);
        redirectAttributes.addAttribute("success", true);
        return "redirect:/result";
    }
}
