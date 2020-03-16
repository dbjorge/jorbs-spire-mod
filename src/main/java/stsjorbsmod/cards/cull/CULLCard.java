package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.OnWrathStackReceivedSubscriber;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.memories.WrathMemory;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class CULLCard extends CustomJorbsModCard implements OnWrathStackReceivedSubscriber {
    public static final String ID = JorbsMod.makeID(CULLCard.class);

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 12;

    public CULLCard() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.exhaust = true;

        tags.add(LEGENDARY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if(!upgraded) {
            upgradeName();
            upgradeDescription();
        }
    }

    @Override
    public void onWrathStackReceived() {
        if (this.upgraded) {
            WrathMemory.permanentlyIncreaseCardDamage(this);
        }

    }
}
