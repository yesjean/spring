package com.example.demo.entity;

import lombok.Data;

import java.util.List;

@Data
public class Item {

    private Long id;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public UploadFile getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(UploadFile attachFile) {
        this.attachFile = attachFile;
    }

    public List<UploadFile> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(List<UploadFile> imageFiles) {
        this.imageFiles = imageFiles;
    }
}
