package pl.pja.s28201.service;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    @SneakyThrows
    public static List<String> readTrainFile(File trainFile) {
        List<String> entries = new ArrayList<>();

        BufferedReader fileRead = new BufferedReader(new FileReader(trainFile));
        String line;
        while ((line = fileRead.readLine()) != null) {
            entries.add(line);
        }

        return entries;
    }
}
