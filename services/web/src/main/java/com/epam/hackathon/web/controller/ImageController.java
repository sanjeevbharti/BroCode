package com.epam.hackathon.web.controller;

import com.epam.hackathon.data.ImageRepository;
import com.epam.hackathon.domain.LostImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class ImageController {

    private final ImageRepository imageRepository;

    @Autowired
    ImageController(final ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping("/matches")
    @ResponseBody
    public List<LostImage> getMatches(){
        return imageRepository.findAllLostImages();
    }
}
