package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.DamageWithOnKillEffectAction;
import stsjorbsmod.actions.PermanentlyModifyDamageAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.OnDrawCardSubscriber;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.powers.WitherPower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class Withering extends CustomJorbsModCard implements OnDrawCardSubscriber {
    public static final String ID = JorbsMod.makeID(Withering.class);

    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.BASIC;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.CURSE;
    public static final AbstractCard.CardColor COLOR = CardColor.CURSE;


    private static final int COST = -2;
    private static final int DAMAGE = 10;
    private static final int DRAW_MINUS_DAMAGE = 1;
    private static final int KILL_PLUS_DAMAGE = 3;
    private static final int UPGRADE_PLUS_KILL_PLUS_DAMAGE = 1;

    public Withering() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        EphemeralField.ephemeral.set(this, true);
        tags.add(LEGENDARY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }


    @Override
    public void onDraw() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, null, new WitherPower(AbstractDungeon.player)));
    }

    @Override
    public void upgrade() {
    }

}
