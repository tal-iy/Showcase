import java.net.DatagramPacket;

public class TftpWindow {

    public Node first;
    public Node last;
    public int size;
    public int max;

    private long SRTT = 1000;
    private long RTTVAR = SRTT/2;
    private double alpha = 0.125;
    private double beta = 0.25;
    private long K = 4;
    private long G = 17;

    private long rttStart = 0;
    private long RTT = 0;
    private long timeStart = 0;
    private int rttBlock = 0;
    public int RTO = 100;

    public TftpWindow(int max) {
        this.max = max;
        this.size = 0;
        this.first = null;
        this.last = null;
    }

    public void startRtt(int block) {
        // Start the RTT timer
        rttStart = System.currentTimeMillis();
        rttBlock = block;
    }

    public void endRtt(int block) {
        // Ignore if RTT of the wrong block
        if (block != rttBlock)
            return;

        // Calculate SRTT
        RTT = System.currentTimeMillis() - rttStart;
        SRTT = (long)((RTT * alpha) + (SRTT * (1.0-alpha)));

        // Calculate RTTVAR
        RTTVAR = (long)((Math.abs(RTT - SRTT) * beta) + (RTTVAR * (1.0-beta)));

        // Calculate RTO
        RTO = (int)(SRTT + Math.max(K * RTTVAR, G));
    }

    public void startTimeout() {
        // Start the timeout timer
        timeStart = System.currentTimeMillis();
    }

    public boolean isTimeout() {
        // Return whether the timeout timer exceeds RTO
        return (System.currentTimeMillis()-timeStart > RTO);
    }

    public boolean full() {
        return size == max;
    }

    public boolean push(DatagramPacket packet, int id) {
        if (size == max)
            return false;

        // Add the packet to the end of the window queue
        Node node = new Node(packet, id);
        if (first == null) {
            first = node;
            last = node;
        } else {
            last.next = node;
            last = node;
        }

        size ++;

        return true;
    }

    public DatagramPacket get(int id) {
        // Search for a node with id
        Node n = first;
        while (n != null && n.id != id)
            n = n.next;

        if (n == null)
            return null;
        return n.packet;
    }

    public DatagramPacket firstPacket() {
        // Return the first packet in the window queue
        if (first == null)
            return null;
        return first.packet;
    }

    public int firstId() {
        // Return the block id of the first packet in the window queue
        if (first == null)
            return -1;
        return first.id;
    }

    public void validate(int id) {
        // Search through window for block id
        Node n = first;
        while (n != null && n.id < id) {
            // Validate all nodes up to the given block id
            n.valid = true;
            n = n.next;
        }

        // Pop all front valid nodes
        popAllValid();
    }

    public DatagramPacket popValid(){
        if (first == null || !first.valid)
            return null;
        return first.packet;
    }

    public void popAllValid() {
        // Continuously pop the first node if it's been validated
        while (first != null && first.valid) {
            first = first.next;
            size --;

            if (size == 1)
                last = first;
            else if (size == 0)
                last = null;
        }
    }

    public class Node {
        public Node next;
        public int id;
        public DatagramPacket packet;
        public boolean valid;

        public Node(DatagramPacket packet, int id) {
            this.packet = packet;
            this.id = id;
            this.next = null;
            valid = false;
        }
    }
}
