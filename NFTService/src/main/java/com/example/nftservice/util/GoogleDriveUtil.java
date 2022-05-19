package com.example.nftservice.util;

import com.example.nftservice.config.GoogleDriveConfig;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class GoogleDriveUtil {

    public static List<File> getGoogleSubFolders(String googleFolderIdParent) throws IOException {

        Drive driveService = GoogleDriveConfig.getGoogleDrive();

        String pageToken = null;
        List<File> list = new ArrayList<>();

        String query;
        if (googleFolderIdParent == null) {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and 'root' in parents";
        } else {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and '" + googleFolderIdParent + "' in parents";
        }

        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    // Fields will be assigned values: id, name, createdTime
                    .setFields("nextPageToken, files(id, name, createdTime)")//
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }

    // com.google.api.services.drive.model.File
    public static List<File> getGoogleRootFolders() throws IOException {
        return getGoogleSubFolders(null);
    }

    public static void main(String[] args) throws IOException {

        List<File> googleRootFolders = getGoogleRootFolders();
        for (File folder : googleRootFolders) {

            System.out.println("Folder ID: " + folder.getId() + " --- Name: " + folder.getName());
        }
    }
}
