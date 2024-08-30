package com.oierbravo.createmechanicalextruder.foundation.utility;

import com.oierbravo.createmechanicalextruder.CreateMechanicalExtruder;
import com.simibubi.create.foundation.utility.LangBuilder;

public class ModLang extends com.simibubi.create.foundation.utility.Lang {
    public ModLang() {
        super();
    }
    public static LangBuilder builder() {
        return new LangBuilder(CreateMechanicalExtruder.MODID);
    }
    public static LangBuilder translate(String langKey, Object... args) {
        return builder().translate(langKey, args);
    }
}
