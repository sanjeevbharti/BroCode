package com.epam.hackathon.data;

import com.epam.hackathon.domain.LostImage;

import java.util.List;

public interface ImageRepository {

    List<LostImage> findAllLostImages();
    LostImage getLostImageById(Long lostImageId);
    void insert(List<LostImage> lostImages);
    void delete(Long lostImageId);
}
