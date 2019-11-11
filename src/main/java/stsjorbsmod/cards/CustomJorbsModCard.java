package stsjorbsmod.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.patches.EphemeralField;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class CustomJorbsModCard extends CustomCard {
    
    // Second magic number that isn't damage or block for card text display.
    public int urMagicNumber = 0;
    public int baseUrMagicNumber = 0;
    public boolean upgradedUrMagicNumber = false;
    public boolean isUrMagicNumberModified = false;
    
    // Third magic number that isn't damage or block for card text display.
    public int metaMagicNumber = 0;
    public int baseMetaMagicNumber = 0;
    public boolean upgradedMetaMagicNumber = false;
    public boolean isMetaMagicNumberModified = false;

    public CustomJorbsModCard(final String id,
                              final String img,
                              final int cost,
                              final CardType type,
                              final CardColor color,
                              final CardRarity rarity,
                              final CardTarget target) {
        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, color, rarity, target);

        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
    }

    @Override
    public void displayUpgrades() {
        super.displayUpgrades();

        if (upgradedUrMagicNumber) {
            urMagicNumber = baseUrMagicNumber;
            isUrMagicNumberModified = true;
        }

        if (upgradedMetaMagicNumber) {
            metaMagicNumber = baseMetaMagicNumber;
            isMetaMagicNumberModified = true;
        }
    }

    public void upgradeUrMagicNumber(int amount) {
        baseUrMagicNumber += amount;
        urMagicNumber = baseUrMagicNumber;
        upgradedUrMagicNumber = true;
    }

    public void upgradeMetaMagicNumber(int amount) {
        baseMetaMagicNumber += amount;
        metaMagicNumber = baseMetaMagicNumber;
        upgradedMetaMagicNumber = true;
    }

    public void upgradeDescription() {
        String upgradeDescription = languagePack.getCardStrings(cardID).UPGRADE_DESCRIPTION;
        if (upgradeDescription != null) {
            rawDescription = upgradeDescription;
        }
        initializeDescription();
    }

    protected void removeFromMasterDeck() {
        final AbstractCard masterDeckCard = StSLib.getMasterDeckEquivalent(this);
        if (masterDeckCard != null) {
            AbstractDungeon.player.masterDeck.removeCard(masterDeckCard);
        }
    }

    // Intended for cards with effects like "deals X damage + Y damage per Z (it should return "Y * Z")
    protected int calculateBonusBaseDamage() {
        return 0;
    }
    protected int calculateBonusBaseBlock() {
        return 0;
    }

    // Note: this base game method is misleadingly named, it's also used when calculating card block
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseBlock = baseBlock;
        baseBlock += calculateBonusBaseBlock();
        int realBaseDamage = baseDamage;
        baseDamage += calculateBonusBaseDamage();

        super.calculateCardDamage(mo);

        baseDamage = realBaseDamage;
        isDamageModified = damage != baseDamage;
        baseBlock = realBaseBlock;
        isBlockModified = block != baseBlock;
    }

    @Override
    public void applyPowers() {
        int realBaseBlock = baseBlock;
        baseBlock += calculateBonusBaseBlock();
        int realBaseDamage = baseDamage;
        baseDamage += calculateBonusBaseDamage();

        super.applyPowers();

        baseDamage = realBaseDamage;
        isDamageModified = damage != baseDamage;
        baseBlock = realBaseBlock;
        isBlockModified = block != baseBlock;
    }

    @Override
    public final void onMoveToDiscard() {
        if (EphemeralField.ephemeral.get(this)) {
            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(this, AbstractDungeon.player.discardPile, true));
        }
        this.onMoveToDiscardImpl();
    }

    public void onMoveToDiscardImpl() { }
}