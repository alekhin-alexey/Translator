package service.files;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    public static void copyFile(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    public static String getFileHash(final String filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(filePath);
        byte[] dataBytes = new byte[1024];
        int nRead;
        while ((nRead = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nRead);
        }
        byte[] mdBytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte elem : mdBytes) {
            sb.append(Integer.toString((elem & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static void writeFile(final String file, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(content);
        writer.close();
    }

    public static Set<String> getListFiles(final String dir, final String fileEndsWith) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory() && file.toString().endsWith(fileEndsWith))
                .map(File::getName)
                .collect(Collectors.toSet());
    }
}

