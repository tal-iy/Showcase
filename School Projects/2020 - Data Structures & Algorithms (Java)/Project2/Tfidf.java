import java.util.ArrayList;

public class Tfidf {

    public static double getTf(String term, FeatureVector document) {
        // Get count of term in document
        Feature word = document.get(term);
        double count = (word == null) ? 0:word.count;

        // Get total features in document
        double total = document.size();

        // Calculate tf
        return count/total;
    }

    public static double getIdf(String term, ArrayList<FeatureVector> documents) {
        // Get total number of documents
        double docNum = documents.size();

        // Go through each document to find term
        int count = 1;
        for (FeatureVector feats : documents)
            if (feats.get(term) != null)
                count++;

        // Calculate IDF
        return Math.log(docNum/count);
    }

    public static double getTfIdf(String term, FeatureVector document, ArrayList<FeatureVector> documents) {
        double tf = getTf(term, document);
        double idf = getIdf(term, documents);

        // Calculate TFIDF
        return tf*idf;
    }
}
