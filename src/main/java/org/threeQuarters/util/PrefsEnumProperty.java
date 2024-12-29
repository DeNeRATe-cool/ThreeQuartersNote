package org.threeQuarters.util;

import javafx.beans.property.SimpleObjectProperty;

import java.util.prefs.Preferences;

public class PrefsEnumProperty<T extends Enum<T>>
extends SimpleObjectProperty<T> {

    private Preferences prefs;
    private String key;
    private T def;

    public PrefsEnumProperty( T initial ) {
        // make sure that property is not null when used in JFormDesigner
        set( initial );
    }

    public PrefsEnumProperty(Preferences prefs, String key, T def) {
        init(prefs, key, def);
    }

    public void init(Preferences prefs, String key, T def) {
        this.key = key;
        this.def = def;

        setPreferences(prefs);
        addListener((ob, o, n) -> {
            Utils.putPrefsEnum(this.prefs, this.key, get(), this.def);
        });
    }

    public void setPreferences(Preferences prefs) {
        this.prefs = prefs;

        set(Utils.getPrefsEnum(prefs, key, def));
    }

}
