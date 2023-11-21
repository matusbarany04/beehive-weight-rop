package com.buzzybees.master.language;

public enum Language {
    DE("de"),
    EN("en"),
    SK("sk"),
    HU("hu");

    final String prefix;

    Language(String prefix) {
        this.prefix = prefix;
    }

    static Language getDefault(){
        return Language.SK;
    }


    public String getPrefix() {
        return prefix;
    }

    public String getBundleName(){
        return getPrefix() + "_bundle.js";
    }


    public String getDictionaryName(){
        return getPrefix() + "_dict.json";
    }

}
