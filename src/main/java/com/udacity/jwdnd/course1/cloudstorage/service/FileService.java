package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;
    private final Path root = Paths.get("uploads");

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @PostConstruct
    public void init() {
        try {
            if (java.nio.file.Files.notExists(root)) {
                java.nio.file.Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public Integer uploadFile(MultipartFile file, int userId) {
        try {
            java.nio.file.Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));

            Files newFile = new Files(null, file.getOriginalFilename(), file.getContentType(), Long.toString(file.getSize()), userId, file.getBytes());
            return fileMapper.saveFile(newFile);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void deleteFile(Integer fileId) {
        fileMapper.deleteFileByFileId(fileId);
    }

    public List<Files> getFiles(int userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public Files getFileById(Integer fileId) {
        return fileMapper.getFileByFileId(fileId);
    }

    public Files getFileByName(String fileName) {
        return fileMapper.getFileByName(fileName);
    }

    public Resource loadFile(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void removeFileInPath(String filename) {
        try {
            Path file = root.resolve(filename);
            java.nio.file.Files.delete(file);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } catch (NoSuchFileException e) {
            throw new RuntimeException("Error: No such file or directory");
        } catch (DirectoryNotEmptyException ex) {
            throw new RuntimeException("Error: Directory is not empty");
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
