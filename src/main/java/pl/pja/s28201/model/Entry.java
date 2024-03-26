package pl.pja.s28201.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@AllArgsConstructor
public class Entry {

    private List<BigDecimal> inputs;
    private String entryType;
//    private List<BigDecimal> weights = new ArrayList<>();

    public Entry(List<BigDecimal> inputs) {
        this.inputs = inputs;
//        initWeights(inputs.size());
    }

    public int inputCount() {
        return inputs.size();
    }
}

