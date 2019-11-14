package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.cards.AutoExhumeBehavior;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.WrathMemory;
import stsjorbsmod.patches.AutoExhumeField;
import stsjorbsmod.patches.EntombedField;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.JorbsMod.JorbsCardTags.PERSISTENT_POSITIVE_EFFECT;
import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;

public class CorpseExplosion_Wanderer extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(CorpseExplosion_Wanderer.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Rares/corpse_explosion_wanderer.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 14;
    private static final int UPGRADE_PLUS_DAMAGE = 4;

    public CorpseExplosion_Wanderer() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        EntombedField.entombed.set(this, true);
        AutoExhumeField.autoExhumeBehavior.set(this, AutoExhumeBehavior.EXHUME_ON_KILL);
        exhaust = true;
        isEthereal = true;

        tags.add(PERSISTENT_POSITIVE_EFFECT);
        tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.FIRE));
        addToBot(new RememberSpecificMemoryAction(p, WrathMemory.STATIC.ID));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeDescription();
        }
    }
}
