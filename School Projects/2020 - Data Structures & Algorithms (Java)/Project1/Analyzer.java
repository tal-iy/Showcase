import java.io.*;

public class Analyzer {

    public FeatureVector extractWordFeatures(String reviewPath, String dictPath) {

        FeatureVector features = new FeatureVector();
        FeatureVector dictionary = new FeatureVector();

        try {
            File reviewFile = new File(reviewPath);
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

            //System.out.print("Dictionary ");
            //dictionary.print();

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

            System.out.print("\nReview ");
            features.print();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return features;
    }

    private String cleanString(String str) {
        String result = str.toLowerCase();
        result = result.replaceAll("[^a-z\\d\\s]+", "");
        result = result.replaceAll("  ", " ");
        return result;
    }

    private class FeatureVector {

        private Feature[] alphabet = new Feature[26];

        public void add(String word) {
            add(word, 1);
        }

        public void add(String word, int value) {
            int pos = word.toLowerCase().charAt(0)-'a';

            // Look an existing feature with that name
            boolean found = false;
            Feature node = alphabet[pos];
            while(node != null && !found) {
                if (node.word.equals(word)) {
                    node.count++;
                    found = true;
                } else {
                    node = node.next;
                }
            }

            if (!found) {
                // Check if the list is empty
                if (alphabet[pos] == null) {
                    alphabet[pos] = new Feature(word, value);
                } else {
                    // Link a new feature to the front of the list
                    Feature addition = new Feature(word, value);
                    addition.next = alphabet[pos];
                    alphabet[pos].prev = addition;
                    alphabet[pos] = addition;
                }
            }
        }

        public void remove(String word) {
            int pos = word.toLowerCase().charAt(0)-'a';

            // Look for the word
            Feature node = alphabet[pos];
            while(node != null && !node.word.equals(word)) {
                node = node.next;
            }

            // Remove the word from the list of features
            if (node != null) {
                if (node.prev != null)
                    node.prev.next = node.next;
                if (node.next != null)
                    node.next.prev = node.prev;
                if (node == alphabet[pos])
                    alphabet[pos] = node.next;
            }
        }

        public Feature get(String word) {
            int pos = word.toLowerCase().charAt(0)-'a';
            Feature node = null;

            // Only search for words starting with an alphabetical character
            if (pos >= 0 && pos < 26) {
                // Look for the word
                node = alphabet[pos];
                while (node != null && !node.word.equals(word)) {
                    node = node.next;
                }
            }

            return node;
        }

        public void print() {
            System.out.println("Feature Vector:");

            for (int i=0; i<alphabet.length; i++) {

                System.out.print((char)(i+'a')+"{");

                Feature node = alphabet[i];
                while(node != null) {
                    System.out.print("("+node.word+":"+node.count+"),");
                    node = node.next;
                }

                System.out.print("}\n");

            }
        }
    }

    private class Feature {
        public Feature next;
        public Feature prev;

        public String word;
        public int count;

        public Feature(String word, int value) {
            this.word = word;
            this.count = value;
        }
    }

    public static void main(String[] args) {
        (new Analyzer()).extractWordFeatures("review.txt", "AFINN-en-165.txt");
    }
}
