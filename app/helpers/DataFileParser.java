package helpers;

import models.Feature;
import play.mvc.Http;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataFileParser {

    private final Http.MultipartFormData.FilePart<File> dataFile;
    private String error;
    private List<Feature> features;
    private int columnAmount;
    private String[] labels;

    public DataFileParser(Http.MultipartFormData.FilePart<File> dataFile) {
        this.dataFile = dataFile;
        this.features = new ArrayList<>();

        this.parse();
    }

    private void parse() {
        if (!this.dataFile.getContentType().equals("text/csv")) {
            this.error = "The file must be of type \"text/csv\"";
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(this.dataFile.getFile()));
            String line = br.readLine();
            String[] split;

            if (line == null) {
                this.error = "The file is empty";
                return;
            }

            split = line.split(",");
            this.labels = Arrays.copyOf(split, split.length - 1);
            this.columnAmount = split.length - 1;

            for (int count = 2; (line = br.readLine()) != null; count++) {
                split = line.split(",");
                if (split.length != this.columnAmount + 1) {
                    this.error = String.format("Could not parse line %d: expected %d elements but found %d", count, this.columnAmount + 1, split.length);
                    return;
                }

                Feature feature = new Feature();
                feature.setColumnAmount(this.columnAmount);
                feature.setData(Arrays.asList(Arrays.copyOf(split, split.length - 1)));
                try {
                    feature.setResult(Double.parseDouble(split[split.length - 1]));
                } catch(NumberFormatException e) {
                    this.error = String.format("Could not parse line %d: %s is not a valid numeric value but was specified as the result", count, split[split.length - 1]);
                    return;
                }
                this.features.add(feature);
            }


        } catch (FileNotFoundException e) {
            this.error = "File not found";
        } catch (IOException e) {
            this.error = "Could not read file";
        }
    }

    public String getError() {
        return error;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public int getColumnAmount() {
        return columnAmount;
    }

    public String[] getLabels() {
        return labels;
    }

    public boolean hasError() {
        return this.error != null;
    }
}
