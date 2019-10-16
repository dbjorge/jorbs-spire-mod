package stsjorbsmod.cards;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import stsjorbsmod.actions.enqueue.EnqueueActions;

public abstract class CustomJorbsModCard extends CustomCard {
	
	private EnqueueActions enqueueActions;
	
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
    
    protected final EnqueueActions getEnqueueActions() {
    	if (enqueueActions == null) {
    		enqueueActions = createEnqueueActions();
    	}
    	return enqueueActions;
    }
    
    protected EnqueueActions createEnqueueActions() {
    	// subclasses have to create the EnqueueActions instance and configure it
    	return null;
    }

    protected void enqueueAction(AbstractGameAction action) {
    	// this bypasses the enqueueActions instance on purpose (i.e. this method should not be called in createEnqueueActions())
        AbstractDungeon.actionManager.addToBottom(action);
    }
    protected void enqueueActionToTop(AbstractGameAction action) {
    	// this bypasses the enqueueActions instance on purpose (i.e. this method should not be called in createEnqueueActions())
        AbstractDungeon.actionManager.addToTop(action);
    }

    public void upgradeDescription() {
        String upgradeDescription = languagePack.getCardStrings(this.cardID).UPGRADE_DESCRIPTION;
        if (upgradeDescription != null) {
            this.rawDescription = upgradeDescription;
        }
        initializeDescription();
    }

    // Intended for cards with effects like "deals X damage + Y damage per Z (it should return "Y * Z")
    protected int calculateBonusBaseDamage() {
    	// subtracting baseDamage because this method is to calculate the bonus damage (would have to refactor a lot to avoid this)
        return enqueueActions == null ? 0 : (enqueueActions.getSavedCalculatedValue() - this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
    	getEnqueueActions();
        int realBaseDamage = this.baseDamage;
        this.baseDamage += calculateBonusBaseDamage();
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void applyPowers() {
    	getEnqueueActions();
        int realBaseDamage = this.baseDamage;
        this.baseDamage += calculateBonusBaseDamage();
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	// REMIND: if neither calculateCardDamage nor applyPowers is called, we have to call getEnqueueActions() here
    	if (enqueueActions != null) {
    		enqueueActions.enqueueActions();
    	}
    }
    
}