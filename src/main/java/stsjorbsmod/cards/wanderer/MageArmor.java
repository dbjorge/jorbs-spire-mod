package stsjorbsmod.cards.wanderer;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.OnPlayerHpLossCardSubscriber;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class MageArmor extends CustomJorbsModCard implements OnPlayerHpLossCardSubscriber {
    public static final String ID = JorbsMod.makeID(MageArmor.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Uncommons/mage_armor.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = COST_UNPLAYABLE;
    private static final int DAMAGE_REDUCTION = 5;
    private static final int UPGRADE_PLUS_DAMAGE_REDUCTION = 2;

    public MageArmor() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        AlwaysRetainField.alwaysRetain.set(this, true);
        magicNumber = baseMagicNumber = DAMAGE_REDUCTION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    // Intended to prevent THORNS/HP_LOSS damage and to take effect after block
    @Override
    public int onPlayerHpLossWhileInHand(int hpLoss) {
        int newHpLoss = hpLoss - 5;
        if (newHpLoss < 0) { newHpLoss = 0; }
        if (hpLoss - newHpLoss > 0) {
            AbstractPlayer p = AbstractDungeon.player;
            AbstractDungeon.effectList.add(new BlockedWordEffect(p, p.hb.cX, p.hb.cY, cardStrings.EXTENDED_DESCRIPTION[1]));
            // addToTop is to handle multi-attacks correctly
            addToTop(new DiscardSpecificCardAction(this));
        }
        return newHpLoss;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_REDUCTION);
            upgradeDescription();
        }
    }
}
