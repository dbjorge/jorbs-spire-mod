package stsjorbsmod.characters;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.patches.ManifestPatch;

public class ManifestSaveData implements CustomSavable<Integer> {
    @Override
    public Integer onSave() {
        return ManifestPatch.PlayerManifestField.manifestField.get(AbstractDungeon.player);
    }

    @Override
    public void onLoad(Integer savedData) {
        ManifestPatch.PlayerManifestField.manifestField.set(AbstractDungeon.player, savedData);
    }
}
