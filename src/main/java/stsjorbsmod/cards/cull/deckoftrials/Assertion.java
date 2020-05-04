package stsjorbsmod.cards.cull.deckoftrials;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.characters.SkillsPlayedThisActSaveData;
import stsjorbsmod.patches.SelfExertField;

import java.util.ArrayList;

import static stsjorbsmod.JorbsMod.MOD_ID;

public class Assertion extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Assertion.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int MAX_DAMAGE = 50;
    private static final int UPGRADE_MAX_DAMAGE_AMOUNT = 25;

    public Assertion() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAX_DAMAGE;
        SelfExertField.selfExert.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        SkillsPlayedThisActSaveData sp =
                (SkillsPlayedThisActSaveData)BaseMod.getSaveFields().get(MOD_ID + ":SkillsPlayedThisAct");

        for (int i = 0; i < sp.skillsPlayed; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        SkillsPlayedThisActSaveData sp =
                (SkillsPlayedThisActSaveData)BaseMod.getSaveFields().get(MOD_ID + ":SkillsPlayedThisAct");

        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription += sp.skillsPlayed;
        if (sp.skillsPlayed == 1) {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        } else {
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[1];
        }
        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[2];
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MAX_DAMAGE_AMOUNT);
            upgradeDescription();
        }
    }
}
