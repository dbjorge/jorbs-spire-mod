package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.util.UniqueCardUtils;

import java.util.HashMap;
import java.util.Map;

public class SchoolsOfMagicAction extends AbstractGameAction {
    private AbstractPlayer owner;

    public SchoolsOfMagicAction(AbstractPlayer owner) {
        this.owner = owner;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        Map<String, Integer> countsByUpgradeInclusiveCardID = new HashMap<String, Integer>();
        boolean noDuplicates = true;
        for (AbstractCard card : owner.hand.group) {
            String id = UniqueCardUtils.getUpgradeInclusiveCardID(card);
            int count = countsByUpgradeInclusiveCardID.getOrDefault(id, 0);
            if (count == 1) {
                noDuplicates = false;
                AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, owner.hand, true));
            }
            countsByUpgradeInclusiveCardID.put(id, count + 1);
        }

        if (noDuplicates) {
            AbstractDungeon.actionManager.addToBottom(new GainClarityOfCurrentMemoryAction(owner));
        }

        this.isDone = true;
    }
}
