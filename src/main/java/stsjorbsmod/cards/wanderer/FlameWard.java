package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.FlameWardPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

/**
 * 2 Cost
 * If attacked, gain 8(10) block and apply 8(10) Burning to all attacking enemies
 */
public class FlameWard extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(FlameWard.class.getSimpleName());
    public static final String IMG = makeCardPath("AoE_Uncommons/flame_ward.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int BASE_AMOUNT = 8;
    private static final int UPGRADE_PLUS = 2;

    public FlameWard() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BASE_AMOUNT;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // for now, use flame barrier's VFX, maybe we get something else later, but it looks pretty sweet anyways.
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new FlameBarrierEffect(p.hb.cX, p.hb.cY), 0.5F));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, m, new FlameWardPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS);
            upgradeDescription();
        }
    }
}
