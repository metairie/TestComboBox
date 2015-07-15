package org.fxpart.mockserver;

import org.fxpart.KeyValueString;
import org.fxpart.KeyValueStringImpl;

import java.util.Arrays;
import java.util.List;

/**
 * Created by metairie on 24-Jun-15.
 */
public class MockDatas {

    public List<KeyValueString> loadLocation() {
        // data for Location
        KeyValueString lb1 = new KeyValueStringImpl("LO1", "Point of View");
        KeyValueString lb2 = new KeyValueStringImpl("LO2", "Poland");
        KeyValueString lb3 = new KeyValueStringImpl("LO3", "Forest");
        KeyValueString lb4 = new KeyValueStringImpl("LO4", "Office");
        KeyValueString lb5 = new KeyValueStringImpl("LO5", "Swimming pool");
        KeyValueString lb6 = new KeyValueStringImpl("LO6", "Tribune");
        KeyValueString lb7 = new KeyValueStringImpl("LO7", "Office");
        KeyValueString lb8 = new KeyValueStringImpl("LO8", "Garden");
        return Arrays.asList(lb1, lb2, lb3, lb4, lb5, lb6, lb7, lb8);
    }

}
