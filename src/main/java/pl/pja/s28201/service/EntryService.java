package pl.pja.s28201.service;

import lombok.Getter;
import pl.pja.s28201.model.Entry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EntryService {

    @Getter
    private static int paramsNum;

    public static List<Entry> parseToEntries(List<String> entries) {
        List<Entry> newEntries = new ArrayList<>();
        for (String entry : entries) newEntries.add(parseEntry(entry));
        return newEntries;
    }

    public static Entry parseEntry(String entryString) {
        String[] entryCase = entryString.split(",");
        paramsNum = entryCase.length - 1;
        List<BigDecimal> entryAttributes = new ArrayList<>();
        for (int i = 0; i < entryCase.length - 1; i++) {
            BigDecimal attr = new BigDecimal(entryCase[i]);
            entryAttributes.add(attr);
        }
        String entryType = entryCase[4];

        return new Entry(entryAttributes, entryType);
    }

}