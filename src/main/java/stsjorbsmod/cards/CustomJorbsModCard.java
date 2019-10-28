package stsjorbsmod.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class CustomJorbsModCard extends CustomCard {
    
    // Second magic number that isn't damage or block for card text display.
    public int urMagicNumber;
    public int baseUrMagicNumber;
    public boolean upgradedUrMagicNumber;
    public boolean isUrMagicNumberModified;
    
    // Third magic number that isn't damage or block for card text display.
    public int metaMagicNumber;
    public int baseMetaMagicNumber;
    public boolean upgradedMetaMagicNumber;
    public boolean isMetaMagicNumberModified;

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
        isUrMagicNumberModified = false;
        isMetaMagicNumberModified = false;
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
        String upgradeDescription = languagePack.getCardStrings(this.cardID).UPGRADE_DESCRIPTION;
        if (upgradeDescription != null) {
            this.rawDescription = upgradeDescription;
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
    protected int calculateBonusBlock() {
        return 0;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage;
        this.baseDamage += calculateBonusBaseDamage();
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void applyPowers() {
        int realBaseBlock = this.baseBlock;
        this.baseBlock += calculateBonusBlock();
        int realBaseDamage = this.baseDamage;
        this.baseDamage += calculateBonusBaseDamage();

        super.applyPowers();

        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
        this.baseBlock = realBaseBlock;
        this.isBlockModified = this.block != this.baseBlock;
    }
}