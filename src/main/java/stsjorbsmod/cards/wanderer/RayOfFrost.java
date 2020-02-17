package stsjorbsmod.cards.wanderer;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RayOfFrostAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.effects.ScalingLaserEffect;

public class RayOfFrost extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(RayOfFrost.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_PLUS_DMG = 3;

    private static final int WEAK = 2;
    private static final int WEAK_APPLICATIONS = 1;
    private static final int UPGRADE_PLUS_WEAK = -1;
    private static final int UPGRADE_PLUS_WEAK_APPLICATIONS = 2;

    public RayOfFrost() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = WEAK;
        metaMagicNumber = baseMetaMagicNumber = WEAK_APPLICATIONS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RayOfFrostAction(m, new DamageInfo(p, damage)));

        for (int i=0; i<this.metaMagicNumber; ++i) {
            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_WEAK);
            upgradeMetaMagicNumber(UPGRADE_PLUS_WEAK_APPLICATIONS);
            upgradeDescription();
        }
    }
}
