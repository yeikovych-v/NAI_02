package pl.pja.s28201;

import lombok.SneakyThrows;
import pl.pja.s28201.model.Entry;
import pl.pja.s28201.repository.EntryRepository;
import pl.pja.s28201.service.CalculationService;
import pl.pja.s28201.service.EntryService;
import pl.pja.s28201.service.FileService;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PerceptronApp {

    private static final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
    private static double learningRate = 0.5;
    private static int numEpochs;
    private static double theta = 0.5;
    private final static List<BigDecimal> weights = new ArrayList<>();

    public static void main(String[] args) {
        startProgram();
    }

    public static void startProgram() {
        System.out.print("Enter the path to your train file: ");
        File trainFile = requestFile();

        EntryRepository.addAll(EntryService.parseToEntries(FileService.readTrainFile(trainFile)));

        initWeights(EntryRepository.entryAttributesCount());

        System.out.print("Enter the path to your test file: ");
        File testFile = requestFile();

        learningRate = requestLearningRate();

        numEpochs = requestEpochs();

        for (int i = 0; i < numEpochs; i++) {
            trainPerceptron();
            testPerceptron(EntryService.parseToEntries(FileService.readTrainFile(testFile)), i);
        }

        activateConsole();
    }

    private static void initWeights(int size) {
        for (int i = 0; i < size; i++) {
            BigDecimal next = BigDecimal.valueOf(new Random().nextDouble(-1, 1.01));
            weights.add(next);
        }
    }

    private static void testPerceptron(List<Entry> testEntries, int epoch) {
        int timesCorrect = 0;
        int size = testEntries.size();
        for (Entry entry : testEntries) {
            boolean output = CalculationService.calcOutput(weights, entry, theta);
            String out = output ? EntryRepository.findAllEntryTypes().getFirst() : EntryRepository.findAllEntryTypes().getLast();
            if (out.equalsIgnoreCase(entry.getEntryType())) timesCorrect++;
        }
        System.out.println("Epoch[" + epoch + "] -> Times correct/Size: " + timesCorrect + "/" + size);
        BigDecimal accuracy = BigDecimal.valueOf(timesCorrect)
                .divide(BigDecimal.valueOf(size), MathContext.DECIMAL128)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
        System.out.println("Accuracy: " + accuracy.doubleValue() + "%");
    }

    private static void trainPerceptron() {

        int called = 0;
        for (Entry entry : EntryRepository.findAll()) {

            boolean isPositiveResult = CalculationService.calcOutput(weights, entry, theta);
            if (isPositiveResult) {
                if (EntryRepository.findAllEntryTypes().getLast().equalsIgnoreCase(entry.getEntryType())) {
                    useDeltaFormula(entry, 0, 1);
                    called++;
                }
            } else {
                if (EntryRepository.findAllEntryTypes().getFirst().equalsIgnoreCase(entry.getEntryType())) {
                    useDeltaFormula(entry, 1, 0);
                    called++;
                }
            }
        }

        EntryRepository.shuffleAll();

        System.out.println("Called vs Entries: " + called + " vs " + EntryRepository.findAll().size());
    }

    private static void useDeltaFormula(Entry entry, int expected, int was) {
        int delta = expected - was;
        double lrD = delta * learningRate;
        List<BigDecimal> inputs = new ArrayList<>(entry.getInputs());
        theta += learningRate * delta * (-1);

        int index = 0;
        for (BigDecimal d : inputs) {
            inputs.set(index, d.multiply(BigDecimal.valueOf(lrD)));
            index++;
        }

        for (int i = 0; i < entry.inputCount(); i++) {
            weights.set(i, weights.get(i).add(inputs.get(i)));
        }
    }


    @SneakyThrows
    private static int requestEpochs() {
        System.out.print("Enter number of epochs: ");
        String epoch = console.readLine().trim();
        System.out.println();

        while (!CalculationService.isInteger(epoch)) {
            System.out.println("Epoch number must be integer.");
            System.out.print("Enter number of epochs: ");
            epoch = console.readLine().trim();
            System.out.println();
        }

        return Integer.parseInt(epoch);
    }

    @SneakyThrows
    private static double requestLearningRate() {
        System.out.print("Enter learning rate between (0 and 1): ");
        String lr = console.readLine().trim();
        System.out.println();

        while (!CalculationService.isDouble(lr) ||
                !CalculationService.isValidLr(Double.parseDouble(lr))) {
            System.out.println("Learning rate should be between (0 and 1).");
            System.out.print("Enter learning rate: ");
            lr = console.readLine().trim();
            System.out.println();
        }

        return Double.parseDouble(lr);
    }

    private static boolean executeCommand(String command) {
        command = command.trim();
        String[] splitCommands = command.split("\\s");
        return switch (splitCommands[0]) {
            case "help" -> executeHelp();
            case "test" -> {
                if (splitCommands.length != (EntryService.getParamsNum() + 1)) yield false;
                yield executeTest(splitCommands);
            }
            default -> false;
        };
    }

    private static boolean executeTest(String[] commands) {
        String[] attributes = Arrays.copyOfRange(commands, 1, commands.length);
        if (!CalculationService.allDecimals(attributes)) return false;
        return executeTest(CalculationService.stringsToDecimalList(attributes));
    }

    private static boolean executeTest(List<BigDecimal> decimals) {
        Entry entry = new Entry(decimals);
        boolean output = CalculationService.calcOutput(weights, entry, theta);
        String out = output ? EntryRepository.findAllEntryTypes().getFirst() : EntryRepository.findAllEntryTypes().getLast();
        entry.setEntryType(out);
        System.out.println("Is calculated to be: " + out);
        return true;
    }

    private static boolean executeHelp() {
        System.out.println("Commands List: ------------------------------->");
        System.out.println("help   <> list available commands.");
        System.out.println("test numbers...  <>  prints the label for the given case.");
        System.out.println("---------------------------------------------->");
        System.out.println();
        return true;
    }

    @SneakyThrows
    private static void activateConsole() {
        System.out.println("Use 'help' command for reference.");
        System.out.print("Type your command here: ");
        String command = console.readLine();
        System.out.println();

        while (!command.equalsIgnoreCase("exit")) {
            if (!executeCommand(command)) System.out.println("Invalid command. Use 'help' command for reference.");
            System.out.print("Type your command here: ");
            command = console.readLine();
            System.out.println();
        }
    }

    @SneakyThrows
    private static File requestFile() {
        String pathToFile = console.readLine().trim();
        File trainFile = new File(pathToFile);
        System.out.println();

        while (!trainFile.exists()) {
            System.out.println("Incorrect file path.");
            System.out.print("Enter the path to your file: ");
            pathToFile = console.readLine().trim();
            trainFile = new File(pathToFile);
            System.out.println();
        }

        return trainFile;
    }
}