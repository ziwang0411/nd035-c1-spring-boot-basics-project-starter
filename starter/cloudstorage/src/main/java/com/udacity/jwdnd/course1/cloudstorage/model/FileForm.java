package com.udacity.jwdnd.course1.cloudstorage.model;

import org.springframework.web.multipart.MultipartFile;

public class FileForm {
    MultipartFile multipartFile;

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
