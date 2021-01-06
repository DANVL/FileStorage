package com.filestorage.utils;

import java.util.Arrays;

public final class FileNameUtils {

    private static final String[] ILLEGAL_CHARACTERS =
            {"/", "\n", "\r", "\t", "\0", "\f", "`", "?", "*", "\\", "<", ">", "|", "\"", ":"};

    private FileNameUtils() {
    }

    public static String tagByFileName(String filename) {
        int index = filename.lastIndexOf(".");

        if (index >= 0) {
            String extension = filename.substring(index + 1).toLowerCase();

            // New extensions can be added
            switch (extension) {
                case "txt":
                case "doc":
                case "docx":
                case "pdf":
                    return "Document";
                case "mp4":
                case "avi":
                case "mov":
                    return "Video";
                case "png":
                case "jpg":
                case "jpeg":
                case "bmp":
                    return "Image";
                case "mp3":
                case "ogg":
                case "wav":
                    return "Audio";
                default:
                    return null;
            }
        }

        return null;
    }

    public static boolean isFileNameContainsInvalidCharacters(String filename) {
        return Arrays.stream(ILLEGAL_CHARACTERS).anyMatch(filename::contains);
    }
}
