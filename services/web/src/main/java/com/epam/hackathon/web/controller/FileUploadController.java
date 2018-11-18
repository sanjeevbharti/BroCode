package com.epam.hackathon.web.controller;

import com.epam.hackathon.file.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final FileService fileService;

    FileUploadController(final FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("found/upload")
    @ResponseBody
    public String uploadFoundImage(@RequestPart MultipartFile file) throws IOException {
        logger.info("Found image uploaded: {}", file.getOriginalFilename());
        String filename = fileService.saveFound(file.getOriginalFilename(), file.getBytes());
        return filename;
    }

    @PostMapping("lost/upload")
    @ResponseBody
    public String uploadLostImage(@RequestPart MultipartFile file) throws IOException {
        logger.info("Lost image uploaded: {}", file.getOriginalFilename());
        String filename = fileService.saveLost(file.getOriginalFilename(), file.getBytes());
        return filename;
    }

}
