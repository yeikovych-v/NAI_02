package pl.pja.s28201.repository;

import pl.pja.s28201.exception.ClassNumberIsNotSupported;
import pl.pja.s28201.model.Entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntryRepository {

    private static final List<Entry> decisionMatrix = new ArrayList<>();
    private static final List<String> entryTypes = new ArrayList<>();
    public static void addAll(List<Entry> entries) {
        decisionMatrix.addAll(entries);
        entryTypes.addAll(getEntryTypesFromEntries(entries));
        if (entryTypes.size() > 2) {
            throw new ClassNumberIsNotSupported("Perceptron cannot support more than 2 classes.");
        }
    }

    private static List<String> getEntryTypesFromEntries(List<Entry> entries) {
        List<String> entryTypes = new ArrayList<>();
        for (Entry e : entries) {
            if (!entryTypes.contains(e.getEntryType())) entryTypes.add(e.getEntryType());
        }
        return entryTypes;
    }

    public static List<Entry> findAll() {
        return decisionMatrix;
    }

    public static List<String> findAllEntryTypes() {
        return entryTypes;
    }

    public static int entryAttributesCount() {
        return decisionMatrix.stream().findFirst().get().getInputs().size();
    }

    public static void shuffleAll() {
        Collections.shuffle(decisionMatrix);
    }
}
