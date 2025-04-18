package com.oierbravo.create_mechanical_extruder;

import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.lang.LangBuilder;

import static com.oierbravo.create_mechanical_extruder.ModConstants.MODID;

public class ModLang extends Lang {
    public ModLang() {
        super();
    }
    public static LangBuilder builder() {
        return new LangBuilder(MODID);
    }
    public static LangBuilder translate(String langKey, Object... args) {
        return builder().translate(langKey, args);
    }
}
