package com.oierbravo.createmechanicalextruder.foundation.utility;

import com.oierbravo.createmechanicalextruder.ModConstants;
import net.createmod.catnip.lang.LangBuilder;

public class ModLang extends net.createmod.catnip.lang.Lang {
    public ModLang() {
        super();
    }
    public static LangBuilder builder() {
        return new LangBuilder(ModConstants.MODID);
    }
    public static LangBuilder translate(String langKey, Object... args) {
        return builder().translate(langKey, args);
    }
}
