package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.constant.FileMessageEnum;
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

import javax.naming.SizeLimitExceededException;
import java.io.File;

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
                redirectAttributes.addAttribute("message", FileMessageEnum.DUPLICATE_FILE.message);
                redirectAttributes.addAttribute("danger", true);
            } else if (fileUpload.getSize() > 0) {
                fileService.uploadFile(fileUpload, userDb.getUserId());
                redirectAttributes.addAttribute("message", FileMessageEnum.FILE_UPLOADED.message);
                redirectAttributes.addAttribute("success", true);
            } else {
                redirectAttributes.addAttribute("message", FileMessageEnum.SELECT_FILE.message);
                redirectAttributes.addAttribute("danger", true);
            }
        }
        catch (Exception e) {
            redirectAttributes.addAttribute("message", e.getMessage());
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
            redirectAttributes.addAttribute("message", FileMessageEnum.FILE_DELETED.message);
            redirectAttributes.addAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", e.getMessage());
            redirectAttributes.addAttribute("danger", true);
        }

        return "redirect:/result";
    }
}
