package domain;

import java.util.Random;

public class Die {

    public void rollSingleDie(Random randomizer) {
        if (randomizer == null) 
            throw new NullPointerException("Randomizer object is null, cannot roll Die!");
    }

}
