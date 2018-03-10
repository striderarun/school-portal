package com.school.beans;

import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;

import java.util.List;

public class BoxDocuments {

    private List<String> folders;
    private List<FileBean> files;
    private String accessToken;

    public List<String> getFolders() {
        return folders;
    }

    public void setFolders(List<String> folders) {
        this.folders = folders;
    }

    public List<FileBean> getFiles() {
        return files;
    }

    public void setFiles(List<FileBean> files) {
        this.files = files;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
