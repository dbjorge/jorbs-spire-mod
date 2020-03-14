package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ConsumeCardAction;
import stsjorbsmod.actions.DamageWithOnKillEffectAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

import static stsjorbsmod.JorbsMod.JorbsCardTags.DECK_OF_TOILS;
import static stsjorbsmod.JorbsMod.JorbsCardTags.PERSISTENT_POSITIVE_EFFECT;

public class Frostbite extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Frostbite.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 3;

    public Frostbite() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        tags.add(PERSISTENT_POSITIVE_EFFECT);
        tags.add(DECK_OF_TOILS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Runnable onKillEffect = () -> addToBot(new ConsumeCardAction(this));
        this.addToBot(new DamageWithOnKillEffectAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), onKillEffect, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rarity = CardRarity.RARE;
            this.bannerImageRarity = CardRarity.RARE;
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
