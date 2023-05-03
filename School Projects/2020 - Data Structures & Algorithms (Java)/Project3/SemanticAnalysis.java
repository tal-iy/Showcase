import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class SemanticAnalysis {

    public static final boolean LOAD_TFIDF = true;
    public static final boolean LOAD_WEIGHTS = true;
    public static final boolean TRAINING = false;

    public static FeatureVector extractWordFeatures(String reviewPath, ArrayList<String> dictionary) {

        FeatureVector features = new FeatureVector();

        try {
            File reviewFile = new File(reviewPath);

            // Read the review
            BufferedReader reviewReader = new BufferedReader(new FileReader(reviewFile));
            String reviewLine = reviewReader.readLine();
            while (reviewLine != null) {
                String[] tokens = cleanString(reviewLine).split("[ \t\n]");

                for (int i=0; i<tokens.length; i++) {
                    // Make sure the token isn't empty
                    if (tokens[i].length() > 0) {
                        // Check if three word, two word, or one word phrase exists in the dictionary
                        if (dictionary.contains(tokens[i])) {
                            if (i < tokens.length-1 && dictionary.contains(tokens[i]+" "+tokens[i+1])) {
                                if (i < tokens.length-2 && dictionary.contains(tokens[i]+" "+tokens[i+1]+" "+tokens[i+2])) {
                                    features.add(tokens[i]+" "+tokens[i+1]+" "+tokens[i+2]);
                                    i+=2;
                                } else {
                                    features.add(tokens[i]+" "+tokens[i+1]);
                                    i++;
                                }
                            } else {
                                features.add(tokens[i]);
                            }
                        }
                    }
                }
                reviewLine = reviewReader.readLine();
            }

            reviewReader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return features;
    }

    public static ArrayList<String> importDictionary(String dictPath) {
        ArrayList<String> dictionary = new ArrayList<>();
        try {
            File dictFile = new File(dictPath);

            // Read the whole dictionary file
            BufferedReader dictReader = new BufferedReader(new FileReader(dictFile));
            String dictLine = dictReader.readLine();
            while(dictLine != null) {

                // Split each line into tokens
                String tokens[] = dictLine.split("[ \t\n]");
                String word = tokens[0];
                int value = 0;

                // Add all words in the dictionary phrase
                for(int i=1; i<tokens.length; i++) {
                    // Make sure that the token isn't a semantic value
                    try {value = Integer.parseInt(tokens[i]); }
                    catch (NumberFormatException ex) {
                        word += " "+tokens[i];
                    }
                }

                if (!dictionary.contains(word))
                    dictionary.add(word);
                if (!dictionary.contains("not "+word))
                    dictionary.add("not "+word);

                dictLine = dictReader.readLine();
            }

            dictReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return dictionary;
    }

    public static String cleanString(String str) {
        String result = str.toLowerCase();
        result = result.replaceAll("[^a-z\\d-\\s]+", "");
        result = result.replaceAll("  ", " ");
        return result;
    }

    public static double[][] addDocuments(String directoryPositive, String directoryNegative, ArrayList<FeatureVector> documents, ArrayList<String> dictionary) throws Exception {

        // Get a list of positive and negative files
        String filesPos[] = (new File(directoryPositive)).list();
        String filesNeg[] = (new File(directoryNegative)).list();

        // Generate a list of labels for each document
        double[][] labels = new double[filesPos.length+filesNeg.length][2];
        int labelIndex = 0;

        // Go through all document files in the positive directory
        for (String fName : filesPos) {
            String path = directoryPositive+fName;

            // Get word features of the document
            FeatureVector features = SemanticAnalysis.extractWordFeatures(path, dictionary);

            // Add features to list of documents
            documents.add(features);

            // Set the label based on filename
            double rating = new Double(fName.split("[_.]")[1]);
            labels[labelIndex][0] = (rating-5.0)/5.0;
            labels[labelIndex][1] = -(rating-5.0)/5.0;

            labelIndex++;
        }

        // Go through all document files in the negative directory
        for (String fName : filesNeg) {
            String path = directoryNegative+fName;

            // Get word features of the document
            FeatureVector features = SemanticAnalysis.extractWordFeatures(path, dictionary);

            // Add features to list of documents
            documents.add(features);

            // Set the label based on filename
            double rating = new Double(fName.split("[_.]")[1]);
            labels[labelIndex][0] = -(5.0-rating)/5.0;
            labels[labelIndex][1] = (5.0-rating)/5.0;

            labelIndex++;
        }

        return labels;
    }

    public static void saveLabels(String fileName, double[][] labels) throws Exception {
        // Open labels file for writing
        File fileLabels = new File(fileName);
        fileLabels.createNewFile();
        FileWriter outLabels = new FileWriter(fileLabels);

        for (int i=0; i<labels.length; i++) {
            // Save the labels to file
            outLabels.write(labels[i][0]+"\n");
            outLabels.write(labels[i][1]+"\n");
        }

        outLabels.close();
    }

    public static double[][] calculateTrainingTfidf(ArrayList<FeatureVector> trainingDocuments, ArrayList<String> dictionary) throws Exception {

        // Open idf and tfidf files for writing
        File fileIdf = new File("idf_train.txt");
        fileIdf.createNewFile();
        FileWriter outIdf = new FileWriter(fileIdf);

        File fileTfIdf = new File("tfidf_train.txt");
        fileTfIdf.createNewFile();
        FileWriter outTfIdf = new FileWriter(fileTfIdf);

        double[][] result = new double[trainingDocuments.size()][dictionary.size()];

        // Go through every word in dictionary
        for (int i=0; i<dictionary.size(); i++) {

            // Calculate IDF
            double idf = Tfidf.getIdf(dictionary.get(i), trainingDocuments);
            outIdf.write(idf+" ");

            // Go through every document
            for (int j=0; j<trainingDocuments.size(); j++) {

                // Calculate TF and TFIDF
                double tf = Tfidf.getTf(dictionary.get(i), trainingDocuments.get(j));
                double tfidf = tf*idf;

                // Add tfidf entry to list
                result[j][i] = tfidf;
                outTfIdf.write(tfidf+" ");
            }

            // Write endline for next word
            outIdf.write("\n");
            outTfIdf.write("\n");
        }

        outIdf.close();
        outTfIdf.close();

        return result;
    }

    public static double[][] calculateTestingTfidf(ArrayList<FeatureVector> testingDocuments, ArrayList<String> dictionary) throws Exception{

        // Open testing tfidf file for writing
        File fileTfIdf = new File("tfidf_test.txt");
        fileTfIdf.createNewFile();
        FileWriter outTfIdf = new FileWriter(fileTfIdf);

        // Open training idf file for reading
        File fileIdf = new File("idf_train.txt");
        BufferedReader idfReader = new BufferedReader(new FileReader(fileIdf));

        double[][] result = new double[testingDocuments.size()][dictionary.size()];

        // Go through every word in dictionary
        for (int i=0; i<dictionary.size(); i++) {

            // Read training idf from file
            double idf = Double.parseDouble(idfReader.readLine());

            // Go through every testing document
            for (int j=0; j<testingDocuments.size(); j++) {

                // Calculate TF and TFIDF
                double tf = Tfidf.getTf(dictionary.get(i), testingDocuments.get(j));
                double tfidf = tf*idf;

                // Add tfidf entry to list
                result[j][i] = tfidf;
                outTfIdf.write(tfidf+" ");
            }

            // Write endline for next word
            outTfIdf.write("\n");
        }

        outTfIdf.close();
        idfReader.close();

        return result;
    }

    public static double[][] loadTfidf(String fileName, int docNum, ArrayList<String> dictionary) throws Exception {

        // Open idf and tfidf files for reading
        File fileTfIdf = new File(fileName);
        BufferedReader tfidfReader = new BufferedReader(new FileReader(fileTfIdf));
        double[][] result = new double[docNum][dictionary.size()];

        // Go through every word in dictionary
        for (int i=0; i<dictionary.size(); i++) {

            // Read a line of tfidfs
            String[] tfidfs = tfidfReader.readLine().split("[ \t\n]");

            // Go through every document
            for (int j=0; j<docNum; j++) {

                // Add tfidf entry to list
                result[j][i] = Double.parseDouble(tfidfs[j]);
            }
        }

        tfidfReader.close();
        return result;
    }

    public static double[][] loadLabels(String fileName) throws Exception {
        // Open labels file for reading
        File fileLabels = new File(fileName);
        BufferedReader labelsReader = new BufferedReader(new FileReader(fileLabels));
        ArrayList<Double> resList = new ArrayList<>();

        // Read the whole labels file
        String line = labelsReader.readLine();
        while(line != null) {
            resList.add(Double.parseDouble(line));
            line = labelsReader.readLine();
            resList.add(Double.parseDouble(line));
            line = labelsReader.readLine();
        }

        // Convert the [docNum x 2][1] array to a [docNum][2] array
        double[][] result = new double[resList.size()/2][2];
        for (int i=0;i<resList.size(); i+=2) {
            result[i/2][0] = resList.get(i);
            result[i/2][1] = resList.get(i+1);
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // Read the dictionary of terms
        System.out.println("Importing dictionary...");
        ArrayList<String> dictionary = SemanticAnalysis.importDictionary("AFINN-en-165.txt");

        // Create a single layer neural network
        NeuronLayer network = new NeuronLayer(dictionary.size(),2);

        double[][] trainingLabels;
        double[][] testingLabels;
        double[][] trainingData;
        double[][] testingData;

        if (LOAD_WEIGHTS) {
            // Load the old trained weights
            System.out.println("Loading previously trained weights...");
            network.loadNetwork("weights.txt");
        }

        if (TRAINING) {
            if (LOAD_TFIDF) {
                // Load labels
                System.out.println("Loading training labels...");
                trainingLabels = loadLabels("training_labels.txt");
                System.out.println("Loading testing labels...");
                testingLabels = loadLabels("testing_labels.txt");

                // Load pre-calculated tfidf for every word on every document
                System.out.println("Loading training tfidf for every word...");
                trainingData = loadTfidf("tfidf_train.txt", trainingLabels.length, dictionary);
                System.out.println("Loading testing tfidf for every word...");
                testingData = loadTfidf("tfidf_test.txt", testingLabels.length, dictionary);
            } else {

                // Create an array of feature vectors for every document
                ArrayList<FeatureVector> trainingDocuments = new ArrayList<>();
                ArrayList<FeatureVector> testingDocuments = new ArrayList<>();

                // Add training and testing documents to the corpus and extract their labels
                System.out.println("Adding training documents to corpus...");
                trainingLabels = addDocuments("documents\\training\\pos\\","documents\\training\\neg\\", trainingDocuments, dictionary);
                saveLabels("training_labels.txt", trainingLabels);
                System.out.println("Adding testing documents to corpus...");
                testingLabels = addDocuments("documents\\testing\\pos\\","documents\\testing\\neg\\", testingDocuments, dictionary);
                saveLabels("testing_labels.txt", testingLabels);

                // Calculate tfidf for every word on every document
                System.out.println("Calculating training tfidf for every word...");
                trainingData = calculateTrainingTfidf(trainingDocuments, dictionary);
                System.out.println("Calculating testing tfidf for every word...");
                testingData = calculateTestingTfidf(testingDocuments, dictionary);
            }

            // Test the starting weights
            double topAccuracy = network.getAccuracy(testingData, testingLabels);
            System.out.println("Starting test accuracy: " + topAccuracy + "%");

            // Iterate 100 times
            System.out.println("Training...\n");
            for (int i = 0; i < 100; i++) {
                // Train the network
                network.train(15, trainingData, trainingLabels);

                // Test the prediction accuracy on testing data
                double testAccuracy = network.getAccuracy(testingData, testingLabels);
                System.out.println("Test accuracy " + i + ": " + testAccuracy + "%");

                // Save the network if it's the best accuracy so far
                if (testAccuracy > topAccuracy) {
                    network.saveNetwork("weights.txt");
                    topAccuracy = testAccuracy;
                }
            }

            System.out.println("Top test accuracy: " + topAccuracy + "%");

        } else {
            // Load single review to test
            ArrayList<FeatureVector> finalDocuments = new ArrayList<>();
            finalDocuments.add(extractWordFeatures("review.txt", dictionary));
            double[][] finalData = calculateTestingTfidf(finalDocuments, dictionary);

            // Predict the semantic value of the review
            double[] outputs = network.calculate(finalData[0]);
            System.out.println("Prediction: "+String.format("%.2f",outputs[0]*100.0)+"% Positive and "+String.format("%.2f",outputs[1]*100.0)+"% Negative");
        }
    }
}
