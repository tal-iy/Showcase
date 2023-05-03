import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class SemanticAnalysis {

    public static final boolean DEBUG = false;

    public static FeatureVector extractWordFeatures(String reviewPath, FeatureVector dictionary) {

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
                        if (dictionary.get(tokens[i]) != null) {
                            if (i < tokens.length-1 && dictionary.get(tokens[i]+" "+tokens[i+1]) != null) {
                                if (i < tokens.length-2 && dictionary.get(tokens[i]+" "+tokens[i+1]+" "+tokens[i+2]) != null) {
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

            if (SemanticAnalysis.DEBUG) {
                System.out.print("\nReview ");
                features.print();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return features;
    }

    public static FeatureVector importDictionary(String dictPath) {
        FeatureVector dictionary = new FeatureVector();
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

                dictionary.add(word, value);
                dictLine = dictReader.readLine();
            }

            dictReader.close();

            if (SemanticAnalysis.DEBUG) {
                System.out.print("Dictionary ");
                dictionary.print();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return dictionary;
    }

    public static String cleanString(String str) {
        String result = str.toLowerCase();
        result = result.replaceAll("[^a-z\\d\\s]+", "");
        result = result.replaceAll("  ", " ");
        return result;
    }

    public static void addDocuments(String directory, ArrayList<FeatureVector> documents, FeatureVector dictionary) {
        // Go through all document files in the directory
        File dir = new File(directory);
        String files[] = dir.list();
        for (String fName : files) {
            String path = directory+fName;

            // Get word features of the document
            FeatureVector features = SemanticAnalysis.extractWordFeatures(path, dictionary);

            // Add features to list of documents
            documents.add(features);
        }
    }

    public static void main(String[] args) throws Exception {
        // Read the dictionary of terms
        System.out.println("Importing dictionary...");
        FeatureVector dictionary = SemanticAnalysis.importDictionary("AFINN-en-165.txt");

        // Create an array of feature vectors for every document
        ArrayList<FeatureVector> documents = new ArrayList<>();

        // Add negative documents
        System.out.println("Adding negative documents to corpus...");
        SemanticAnalysis.addDocuments("documents\\neg\\", documents, dictionary);

        // Add positive documents
        System.out.println("Adding positive documents to corpus...");
        SemanticAnalysis.addDocuments("documents\\pos\\", documents, dictionary);

        // Add untagged documents
        System.out.println("Adding untagged documents to corpus...");
        SemanticAnalysis.addDocuments("documents\\unsup\\", documents, dictionary);

        // Open output files for writing
        File fileTf = new File("tf.txt");
        fileTf.createNewFile();
        FileWriter outTf = new FileWriter(fileTf);

        File fileIdf = new File("idf.txt");
        fileIdf.createNewFile();
        FileWriter outIdf = new FileWriter(fileIdf);

        File fileTfIdf = new File("tfidf.txt");
        fileTfIdf.createNewFile();
        FileWriter outTfIdf = new FileWriter(fileTfIdf);

        // Get vocabulary list from dictionary
        ArrayList<Feature> vocab = dictionary.toArrayList();

        // Go through every word in dictionary
        for (Feature word : vocab) {
            System.out.println("Word: "+word.word);

            // Calculate IDF
            double idf = Tfidf.getIdf(word.word, documents);
            outIdf.write(idf+" ");

            // Go through every document
            int i=0;
            for (FeatureVector doc : documents) {
                if (i%10000 == 0) System.out.println("Doc "+i);

                // Calculate TF
                double tf = Tfidf.getTf(word.word, doc);
                outTf.write(tf+" ");

                // Calculate TFIDF
                double tfidf = tf*idf;
                outTfIdf.write(tfidf+" ");

                i++;
            }

            // Write endline for next word
            outTf.write("\n");
            outIdf.write("\n");
            outTfIdf.write("\n");
        }

        outTf.close();
        outIdf.close();
        outTfIdf.close();
        System.out.println("Done!");
    }
}
