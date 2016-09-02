package services;

import models.Feature;
import models.FeatureEntry;
import models.FeatureLabel;
import models.FeatureSet;
import play.mvc.Http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataFileParser {

    private String error;
    private FeatureSet featureSet;

    public void parse(final Http.MultipartFormData.FilePart<File> dataFile) {
        this.featureSet = new FeatureSet();

        if (!dataFile.getContentType().equals("text/csv")) {
            this.error = "The file must be of type \"text/csv\"";
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(dataFile.getFile()));
            String labelLine = br.readLine();

            if (labelLine == null) {
                this.error = "The file is empty";
                return;
            }

            String[] split = labelLine.split(",");
            this.featureSet.setLabels(
                Arrays.stream(Arrays.copyOf(split, split.length - 1))
                    .map(e -> new FeatureLabel(e, this.featureSet))
                    .collect(Collectors.toList())
            );
            int columnAmount = split.length - 1;

            String line = br.readLine();
            for (int count = 2; line != null; count++, line = br.readLine()) {
                split = line.split(",");
                if (split.length != columnAmount + 1) {
                    this.error = String.format(
                        "Could not parse line %d: expected %d elements but found %d",
                        count,
                        columnAmount + 1,
                        split.length
                    );
                    return;
                }

                String[] localSplit = split.clone();

                Feature feature = new Feature();
                feature.setEntries(
                    IntStream.range(0, split.length - 1)
                        .mapToObj(i -> new FeatureEntry(localSplit[i], feature, this.featureSet.getLabels().get(i)))
                        .collect(Collectors.toList())
                );
                try {
                    feature.setResult(Double.parseDouble(split[split.length - 1]));
                } catch (NumberFormatException e) {
                    this.error = String.format(
                        "Could not parse line %d: %s is not a valid numeric value but was specified as the result",
                        count,
                        split[split.length - 1]
                    );
                    return;
                }
                this.featureSet.addFeature(feature);
            }
        } catch (FileNotFoundException e) {
            this.error = "File not found";
        } catch (IOException e) {
            this.error = "Could not read file";
        }
    }

    public FeatureSet getFeatureSet() {
        return this.featureSet;
    }

    public String getError() {
        return error;
    }

    public boolean hasError() {
        return this.error != null;
    }
}
