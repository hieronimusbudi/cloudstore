package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public String uploadFile(Authentication auth, @RequestParam("fileToBeUploaded") MultipartFile fileUpload, RedirectAttributes redirectAttributes) {
        try {
            Users userDb = userService.getUser(auth.getName());
            Files checkedFile = fileService.getFileByName(fileUpload.getOriginalFilename());

            if (checkedFile != null && checkedFile.getFileName().equals(fileUpload.getOriginalFilename())) {
                redirectAttributes.addAttribute("duplicateUploadFile", true);
                redirectAttributes.addAttribute("danger", true);
            } else if (fileUpload.getSize() > 0) {
                fileService.uploadFile(fileUpload, userDb.getUserId());
                redirectAttributes.addAttribute("uploadFile", true);
                redirectAttributes.addAttribute("success", true);
            } else {
                redirectAttributes.addAttribute("selectFile", true);
                redirectAttributes.addAttribute("danger", true);
            }
        } catch (Exception e) {
            redirectAttributes.addAttribute("uploadFileError", true);
            redirectAttributes.addAttribute("danger", true);
        }

        return "redirect:/result";
    }

    @GetMapping("/view/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> viewFile(@PathVariable("fileId") Integer fileId) {
        Files fileById = fileService.getFileById(fileId);
        Resource file = fileService.loadFile(fileById.getFileName());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable(value = "fileId") Integer fileId, RedirectAttributes redirectAttributes) {
        try {
            Files fileById = fileService.getFileById(fileId);
            fileService.removeFileInPath(fileById.getFileName());
            fileService.deleteFile(fileId);
            redirectAttributes.addAttribute("deleteFile", true);
            redirectAttributes.addAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addAttribute("deleteFileError", true);
            redirectAttributes.addAttribute("danger", true);
        }

        return "redirect:/result";
    }
}
