package com.filestorage.utils;

public final class FileTagUtils {

    private FileTagUtils(){}

    public static String tagByFileName(String name){
        String extension = name.substring(name.indexOf(".") + 1);

        // New extensions can be added
        switch (extension){
            case "txt": case "doc": case "docx": case "pdf":
                return "Document";
            case "mp4": case "avi": case "mov":
                return "Video";
            case "png": case "jpg": case "jpeg": case "bmp":
                return "Image";
            case "mp3": case "ogg": case "wav":
                return "Audio";
            default:
                return "Other";
        }
    }
}
