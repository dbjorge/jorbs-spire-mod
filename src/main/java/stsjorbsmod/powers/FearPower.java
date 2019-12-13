package stsjorbsmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainSpecificClarityAction;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class FearPower extends AbstractPower {
    public static final String POWER_ID = JorbsMod.makeID(FearPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("fear_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("fear_power32.png"));

    public FearPower(AbstractMonster owner, int turnsBeforeFleeing) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        this.owner = owner;
        this.amount = turnsBeforeFleeing;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.updateDescription();
    }
    @Override
    public void atStartOfTurn() { // note: monster's turn, not player's
        AbstractMonster monsterOwner = (AbstractMonster)owner;
        if (!monsterOwner.isDeadOrEscaped()) {
            --this.amount;
            updateDescription();

            if (this.amount == 0) {
                int remainingMonsters = 0;
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    // We normally consider halfDead things to be remaining, assuming they'll have some condition by
                    // which they'll come back.
                    //
                    // Darklings are a special case where we consider them to be not-remaining when halfDead because
                    // DarklingEscapePatch will be specifically ending the fight if there are only halfDead ones left.
                    boolean isFunctionallyDead = m.isDying || m.isDead || m.isEscaping || (m.halfDead && m.id.equals(Darkling.ID));

                    // Checking intent is to cover for the "multiple FearPowers triggering on the same turn" case
                    if (!isFunctionallyDead && m.intent != Intent.ESCAPE) { ++remainingMonsters; }
                }

                AbstractDungeon.actionManager.addToBottom(new EscapeAction(monsterOwner));

                if (remainingMonsters == 0 && !AbstractDungeon.getCurrRoom().smoked) {
                    AbstractDungeon.getCurrRoom().smoked = true; // skips rewards
                    for (AbstractMemory clarity : MemoryManager.forPlayer(AbstractDungeon.player).currentClarities()) {
                        AbstractDungeon.actionManager.addToNextCombat(new GainSpecificClarityAction(AbstractDungeon.player, clarity.ID));
                    }
                }
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) { // note: monster's turn, not player's
        AbstractMonster monsterOwner = (AbstractMonster)owner;
        if (this.amount == 1) {
            monsterOwner.nextMove = (byte)-1;
            monsterOwner.setMove(monsterOwner.nextMove, Intent.ESCAPE);
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + (this.amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]) + DESCRIPTIONS[3];
    }
}
