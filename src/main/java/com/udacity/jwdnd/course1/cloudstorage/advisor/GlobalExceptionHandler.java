package com.udacity.jwdnd.course1.cloudstorage.advisor;

import com.udacity.jwdnd.course1.cloudstorage.constant.FileMessageEnum;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MultipartException.class)
    public String handleMultipartException(MultipartException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("message", e.getCause().getMessage());
        redirectAttributes.addAttribute("danger", true);
        return "redirect:/result";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("message", FileMessageEnum.EXCEEDED_FILE_SIZE_LIMIT.message);
        redirectAttributes.addAttribute("danger", true);
        return "redirect:/result";
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public String handleSizeLimitExceededException(SizeLimitExceededException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("message", FileMessageEnum.EXCEEDED_FILE_SIZE_LIMIT.message);
        redirectAttributes.addAttribute("danger", true);
        return "redirect:/result";
    }
}
