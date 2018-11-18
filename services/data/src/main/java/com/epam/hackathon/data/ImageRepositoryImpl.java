package com.epam.hackathon.data;

import com.epam.hackathon.domain.LostImage;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageRepositoryImpl implements ImageRepository {

    private Datastore datastore;

    @Autowired
    public ImageRepositoryImpl(@Qualifier("dataSource") Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public List<LostImage> findAllLostImages() {
        return datastore.find(LostImage.class).asList();
    }

    @Override
    public void insert(List<LostImage> lostImages) {
        datastore.save(lostImages);
    }

    @Override
    public LostImage getLostImageById(Long lostImageId) {
        return datastore.find(LostImage.class).field("lostImageId").equal(lostImageId).get();
    }

    @Override
    public void delete(Long lostImageId) {
    	datastore.delete(datastore.createQuery(LostImage.class).filter("lostImageId =", lostImageId));
    }
}
