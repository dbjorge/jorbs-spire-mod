package stsjorbsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.memories.OnModifyMemoriesListener;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.*;

public class MindglassRelic extends CustomRelic implements OnModifyMemoriesListener {
    public static final String ID = JorbsMod.makeID(MindglassRelic.class.getSimpleName());

    // TODO: relic path
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int ONE_CLARITY_DAMAGE = 5;
    private static final int TEN_CLARITY_DAMAGE = 500;

    public MindglassRelic() {
        // TODO verify relic tier
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onModifyMemories() {
        JorbsMod.logger.info("I wish :(");
    }

    @Override
    public void onTrigger() {
        ++this.counter;
        int damage = this.counter == 10 ? TEN_CLARITY_DAMAGE : ONE_CLARITY_DAMAGE;
        if (this.counter == 9) {
            this.beginPulse();
        }
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(
                        (AbstractCreature)null,
                        DamageInfo.createDamageMatrix(damage, true),
                        DamageInfo.DamageType.THORNS,
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT
                )
        );
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.stopPulse();
    }

    @Override
    public void initializeTips() {
        this.description = DESCRIPTIONS[0];
        super.initializeTips();
        this.description = getUpdatedDescription();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replaceAll(JorbsMod.MOD_ID + ":", "#y");
    }
}
