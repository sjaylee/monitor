package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * The utility tool for file reading and writing.
 */
public final class FileRW {
    private FileRW() {
    }

    /**
     * Append list of lines to the file.
     *
     * @param path  path of the file.
     * @param lines need append lines.
     * @throws IOException
     */
    public static void appendLinesToFile(String path, List<String> lines) throws IOException {
//        FileUtils.writeLines(new File(path), StandardCharsets.UTF_8.name(), lines, true);
        //写入中文字符时解决中文乱码问题
        try (FileOutputStream fos = new FileOutputStream(new File(path),true);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8.name());
             BufferedWriter bw = new BufferedWriter(osw)) {

            for (String line : lines) {
                bw.write(line);
                bw.write(System.lineSeparator());
            }
        }
    }

    /**
     * Get all lines of the file.
     * 获得文本文件的行记录列表
     *
     * @param path path of file
     * @return
     * @throws IOException
     */
    public static List<String> getFileLines(String path) throws IOException {

        List<String> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(path);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8.name());
             BufferedReader br = new BufferedReader(isr)) {
            String line = "";
            String[] arrs = null;
            while ((line = br.readLine()) != null) {
                arrs = line.split(",");
                list.add(line);
                System.out.println(arrs[0] + " : " + arrs[1] + " : " + arrs[2]);
            }
        }
        return list;
    }




}
