package com.epam.hackathon.domain;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Collections;
import java.util.List;

@Entity
public class LostImage {

    @Id
    public ObjectId id;
    private Long lostImageId;
    private String lostImagePath;
    private List<FoundImage> foundImages = Collections.emptyList();

    public Long getLostImageId() {
        return lostImageId;
    }

    public void setLostImageId(Long lostImageId) {
        this.lostImageId = lostImageId;
    }

    public String getLostImagePath() {
        return lostImagePath;
    }

    public void setLostImagePath(String lostImagePath) {
        this.lostImagePath = lostImagePath;
    }

    public List<FoundImage> getFoundImages() {
        return foundImages;
    }

    public void setFoundImages(List<FoundImage> foundImages) {
        this.foundImages = foundImages;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
