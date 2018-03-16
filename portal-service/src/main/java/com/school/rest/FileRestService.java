package com.school.rest;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.school.beans.BoxDocuments;
import com.school.beans.FileBean;
import com.school.box.BoxHelper;
import com.school.logging.Loggable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/services/files")
public class FileRestService {

    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public BoxDocuments getFilesAndFolders(@RequestParam String boxId) {
        BoxAPIConnection userClient = BoxHelper.userClient(boxId);
        List<BoxFolder.Info> folders = new ArrayList<>();
        List<BoxFile.Info> files = new ArrayList<>();

        BoxFolder rootFolder = BoxFolder.getRootFolder(userClient);
        for (BoxItem.Info itemInfo : rootFolder) {
            if (itemInfo instanceof BoxFile.Info) {
                files.add((BoxFile.Info) itemInfo);
            } else if (itemInfo instanceof BoxFolder.Info) {
                folders.add((BoxFolder.Info) itemInfo);
            }
        }
        BoxDocuments boxDocuments = new BoxDocuments();
        boxDocuments.setFiles(files.stream().map(s -> new FileBean(s.getID(), s.getName())).collect(Collectors.toList()));
        boxDocuments.setFolders(folders.stream().map(s -> s.getName()).collect(Collectors.toList()));
        boxDocuments.setAccessToken(userClient.getAccessToken());
        return boxDocuments;
    }

    @Loggable
    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> preview(@RequestParam String boxId, @RequestParam String fileId) {
        BoxAPIConnection userClient = BoxHelper.userClient(boxId);
        BoxFile boxFile = new BoxFile(userClient, fileId);
        URL previewUrl = boxFile.getPreviewLink();
        System.out.println(previewUrl);
        return Collections.singletonMap("url", previewUrl.toString());
    }

    @Loggable
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> download(@RequestParam String boxId, @RequestParam String fileId) {
        BoxAPIConnection userClient = BoxHelper.userClient(boxId);
        BoxFile boxFile = new BoxFile(userClient, fileId);
        URL fileDownloadURL = boxFile.getDownloadURL();
        return Collections.singletonMap("url", fileDownloadURL.toString());
    }
}
