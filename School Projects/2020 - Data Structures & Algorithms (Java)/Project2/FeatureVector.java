import java.util.ArrayList;

public class FeatureVector {

    private Feature[] alphabet = new Feature[26];
    private int numFeatures = 0;

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

        // Increase features counter
        numFeatures++;
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
            numFeatures -= node.count;

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

    public int size() {
        return numFeatures;
    }

    public ArrayList<Feature> toArrayList() {
        ArrayList<Feature> list = new ArrayList<>();

        // Traverse the array or hashed lists
        for (int i=0; i<alphabet.length; i++) {

            // Go through every feature in the linked list
            Feature node = alphabet[i];
            while(node != null) {

                // Add the feature to the result list
                list.add(node);
                node = node.next;
            }
        }

        return list;
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