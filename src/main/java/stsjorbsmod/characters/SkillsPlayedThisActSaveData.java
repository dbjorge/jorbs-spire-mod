package stsjorbsmod.characters;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.patches.ManifestPatch;

public class SkillsPlayedThisActSaveData implements CustomSavable<Integer> {
    public static int skillsPlayed = 0;

    @Override
    public Integer onSave() {
        return skillsPlayed;
    }

    @Override
    public void onLoad(Integer savedData) {
        skillsPlayed = savedData;
    }
}
