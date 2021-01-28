public class Feature {
    public Feature next;
    public Feature prev;

    public String word;
    public int count;

    public Feature(String word, int value) {
        this.word = word;
        this.count = value;
    }
}