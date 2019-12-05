package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.DowngradeCardAction;
import stsjorbsmod.actions.DestroyCardAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.DowngradeableCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.patches.SelfExhumeFields;

import java.util.Optional;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class Patron extends CustomJorbsModCard implements DowngradeableCard {
    public static final String ID = JorbsMod.makeID(Patron.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 40;
    private static final int UPGRADE_PLUS_DAMAGE = 10;

    public Patron() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        EphemeralField.ephemeral.set(this, true);
        SelfExhumeFields.selfExhumeOnSnap.set(this, true);
        purgeOnUse = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));

        Optional<AbstractCard> cardOp = AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.uuid == uuid).findAny();
        if(!cardOp.isPresent()) {
            JorbsMod.logger.info("Failed to purge a card we didn't have. Perhaps Duplication Potion or Attack Potion or similar effect occurred.");
            return;
        }
        // In the highly unlikely event that UUIDs clash and the found card isn't actually the specific Patron card... oops
        AbstractCard masterCard = cardOp.get();
        if (upgraded) {
            addToTop(new DowngradeCardAction((Patron) masterCard));
        } else {
            addToBot(new DestroyCardAction(masterCard));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeDescription();
            purgeOnUse = false;
        }
    }

    @Override
    public void downgrade() {
        if (upgraded) {
            --timesUpgraded;
            upgraded = false;
            name = originalName;
            initializeTitle();
            baseDamage = DAMAGE;
            upgradedDamage = false;
            rawDescription = languagePack.getCardStrings(cardID).DESCRIPTION;
            initializeDescription();
            purgeOnUse = true;
        }
    }
}
