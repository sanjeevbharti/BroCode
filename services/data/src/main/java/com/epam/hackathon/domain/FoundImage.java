package com.epam.hackathon.domain;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class FoundImage {

    @Id
    public ObjectId id;
    private Long foundImageId;
    private String foundImagePath;
    private Double matchPercentage;

    public Long getFoundImageId() {
        return foundImageId;
    }

    public void setFoundImageId(Long foundImageId) {
        this.foundImageId = foundImageId;
    }

    public String getFoundImagePath() {
        return foundImagePath;
    }

    public void setFoundImagePath(String foundImagePath) {
        this.foundImagePath = foundImagePath;
    }

    public Double getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(Double matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
