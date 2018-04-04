package com.jeremy.demo.utils;

/**
 * Created by changqing on 2017/12/15.
 */

public class Metadata_Extractor {

//    public static void getMetadata(File file) {
//        try {
//            Metadata metadata = ImageMetadataReader.readMetadata(file);
//            getDate(metadata);
//            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
//            if (directory != null && directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL) != null) {
//                System.out.println(file.getName() + " " + directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL).toString());
//            } else {
//                System.out.println(file.getName() + " null");
//            }
////            print(metadata, "Using ImageMetadataReader");
//        } catch (ImageProcessingException e) {
////            print(e);
//        } catch (IOException e) {
////            print(e);
//        }
//    }
//
//    public static Date getDate(File file) {
//        try {
//            Metadata metadata = ImageMetadataReader.readMetadata(file);
//            return getDate(metadata);
//        } catch (ImageProcessingException e) {
//
//        } catch (IOException e) {
//
//        }
//        return null;
//    }
//
//    public static Date getDate(Metadata metadata) {
//        if (metadata != null) {
//            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
//            if (directory != null && directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL) != null) {
//                Date date = new Date(directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL).toString());
//                if (date != null) {
//                    int year = date.getYear();
//                    year += 1900;
//                    return date;
//                }
//            }
//        }
//        return null;
//    }
//
//    private static void print(Metadata metadata, String method) {
//        System.out.println();
//        System.out.println("-------------------------------------------------");
//        System.out.print(' ');
//        System.out.print(method);
//        System.out.println("-------------------------------------------------");
//        System.out.println();
//
//        for (Directory directory : metadata.getDirectories()) {
//            for (Tag tag : directory.getTags()) {
//                System.out.println(tag);
//            }
//            for (String error : directory.getErrors()) {
//                System.err.println("ERROR: " + error);
//            }
//        }
//
//        //[Exif IFD0] Date/Time - 2017:05:17 14:30:50
//        //[Exif SubIFD] Date/Time Original - 2017:05:17 14:16:36
//        //[Exif SubIFD] Date/Time Digitized - 2017:05:17 14:16:36
//        //[IPTC] Date Created - 2017:05:17
//        //[IPTC] Time Created - 14:16:36+0000
//    }
//
//    private static void print(Exception exception) {
//        System.err.println("EXCEPTION: " + exception);
//    }

}
