package net.azisaba.namechange.utils;

public class FileNameUtils {

  public static String sanitize(String fileName) {
      return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
  }
}
