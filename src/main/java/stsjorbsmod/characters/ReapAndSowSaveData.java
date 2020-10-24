package stsjorbsmod.characters;

import basemod.abstracts.CustomSavable;

public class ReapAndSowSaveData implements CustomSavable<Integer> {
    public static int reapAndSowDamage = 0;

    @Override
    public Integer onSave() {
        return reapAndSowDamage;
    }

    @Override
    public void onLoad(Integer savedData) {
        reapAndSowDamage = savedData;
    }
}
