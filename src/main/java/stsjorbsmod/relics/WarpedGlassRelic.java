package stsjorbsmod.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import stsjorbsmod.JorbsMod;

import static stsjorbsmod.characters.Cull.Enums.CULL_CARD_COLOR;

/**
 * When a card exerts, deals 3 damage to all enemies.
 * TODO: relic stats mod support
 */
public class WarpedGlassRelic extends CustomJorbsModRelic implements OnExertSubscriber{
    public static final String ID = JorbsMod.makeID(WarpedGlassRelic.class);

    public static final int DAMAGE = 3;

    public WarpedGlassRelic() {
        super(ID, CULL_CARD_COLOR, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void onExert() {
        flash();
        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(DAMAGE, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    public AbstractRelic makeCopy() {
        return new WarpedGlassRelic();
    }
}
