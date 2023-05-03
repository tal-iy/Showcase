package com;

import java.util.concurrent.ThreadLocalRandom;

public class Item {
    private char num;

    public Item() {
        num = (char)ThreadLocalRandom.current().nextInt(0, 127);
    }
}
