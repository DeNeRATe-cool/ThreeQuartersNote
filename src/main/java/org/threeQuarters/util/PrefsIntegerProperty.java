package org.threeQuarters.util;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.prefs.Preferences;

public class PrefsIntegerProperty
extends SimpleIntegerProperty {

    private Preferences prefs;
    private String key;
    private int def;

    public PrefsIntegerProperty() {
    }

    public PrefsIntegerProperty(Preferences prefs, String key, int def) {
        init(prefs, key, def);
    }

    public void init(Preferences prefs, String key, int def) {
        this.key = key;
        this.def = def;

        setPreferences(prefs);
        addListener((ob, o, n) -> {
            Utils.putPrefsInt(this.prefs, this.key, get(), this.def);
        });
    }

    public void setPreferences(Preferences prefs) {
        this.prefs = prefs;

        set(prefs.getInt(key, def));
    }

}
