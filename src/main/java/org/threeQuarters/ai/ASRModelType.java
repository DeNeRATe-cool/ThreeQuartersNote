package org.threeQuarters.ai;

/**
 * Indicates the model type used in speech recognition.
 * Quality: Small > Base > Tiny;
 * Speed: Tiny > Base > Small.
 */
public enum ASRModelType {
    TINY("tiny"), BASE("base"), SMALL("small");

    private String string;

    private ASRModelType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
