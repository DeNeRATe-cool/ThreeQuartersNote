package org.threeQuarters.util;

import javafx.beans.property.SimpleStringProperty;

import java.util.function.Function;
import java.util.prefs.Preferences;

public class PrefsStringProperty
extends SimpleStringProperty {
    private Preferences prefs;
    private String key;
    private String def;
    private Function<String, String> loadConverter;

    public PrefsStringProperty() {
    }

    public PrefsStringProperty(Preferences prefs, String key, String def) {
        init(prefs, key, def);
    }

    public void init(Preferences prefs, String key, String def) {
        init(prefs, key, def, value -> value);
    }

    public void init(Preferences prefs, String key, String def, Function<String, String> loadConverter) {
        this.key = key;
        this.def = def;
        this.loadConverter = loadConverter;

        setPreferences(prefs);
        addListener((ob, o, n) -> {
            Utils.putPrefs(this.prefs, this.key, get(), this.def);
        });
    }

    public void setPreferences(Preferences prefs) {
        this.prefs = prefs;

        set(loadConverter.apply(prefs.get(key, def)));
    }
}
