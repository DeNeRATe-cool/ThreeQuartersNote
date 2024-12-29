package org.threeQuarters.scrapper;

public class NoneResourcesException extends Exception {
    public NoneResourcesException() {
        super("no resources found in the span");
    }
}
