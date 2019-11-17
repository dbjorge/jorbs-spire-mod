package stsjorbsmod.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.patches.EphemeralField;

import java.lang.reflect.Field;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class CustomJorbsModCard extends CustomCard {
    // These sentinel values are defined by the base game, we're just giving them more readable names.
    public static final int COST_X = -1;
    public static final int COST_UNPLAYABLE = -2;

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

    protected CardStrings cardStrings;

    public CustomJorbsModCard(final String id,
                              final String img,
                              final int cost,
                              final CardType type,
                              final CardColor color,
                              final CardRarity rarity,
                              final CardTarget target) {
        this(id, languagePack.getCardStrings(id), img, cost, type, color, rarity, target);
    }

    private CustomJorbsModCard(final String id,
                              final CardStrings cardStrings,
                              final String img,
                              final int cost,
                              final CardType type,
                              final CardColor color,
                              final CardRarity rarity,
                              final CardTarget target) {
        super(id, cardStrings.NAME, img, cost, cardStrings.DESCRIPTION, type, color, rarity, target);

        this.cardStrings = cardStrings;
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
            initializeDescription();
        }
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

    public boolean shouldGlowGold() { return false; }

    /* TODO: once beta branch releases, replace with the following:
        @Override
        public void triggerOnGlowCheck() {
            this.glowColor = shouldGlowGold() ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy() : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
     */
    private static final Color GOLD_BORDER_GLOW_COLOR = new Color(-2686721);
    private static final Color BLUE_BORDER_GLOW_COLOR = new Color(0.2F, 0.9F, 1.0F, 0.25F);
    public void triggerOnGlowCheck() {
        try {
            Field glowColorField = AbstractCard.class.getField("glowColor");
            Color newGlowColor = shouldGlowGold() ? GOLD_BORDER_GLOW_COLOR.cpy() : BLUE_BORDER_GLOW_COLOR.cpy();
            glowColorField.set(this, newGlowColor);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            // on stable branch where glowColor isn't implemented; ignore silently.
        }
    }
}