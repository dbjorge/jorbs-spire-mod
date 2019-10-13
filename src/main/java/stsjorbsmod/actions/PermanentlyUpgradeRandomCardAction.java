package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class PermanentlyUpgradeRandomCardAction extends AbstractGameAction {
    public PermanentlyUpgradeRandomCardAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractPlayer p = AbstractDungeon.player;

            CardGroup masterDeckCandidates = new CardGroup(CardGroupType.UNSPECIFIED);
            for (AbstractCard c : p.masterDeck.group) {
                if (c.canUpgrade()) {
                    masterDeckCandidates.addToBottom(c);
                }
            }

            if (!masterDeckCandidates.isEmpty()) {
                AbstractCard masterDeckCard = masterDeckCandidates.getRandomCard(AbstractDungeon.cardRandomRng);
                masterDeckCard.upgrade();
                masterDeckCard.superFlash();
                masterDeckCard.applyPowers();

                for (AbstractCard instance : GetAllInBattleInstances.get(masterDeckCard.uuid)) {
                    instance.upgrade();
                    instance.applyPowers();
                }
            }

            this.isDone = true;
        } else {
            this.tickDuration();
        }
    }
}
