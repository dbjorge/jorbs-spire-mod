package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import javax.tools.Diagnostic;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class KindnessMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(KindnessMemory.class);

    private static final int POISON_ON_REMEMBER = 3;
    private static final int ENVENOM_MAGNITUDE = 1;

    public KindnessMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.VIRTUE, owner, isClarified);
        setDescriptionPlaceholder("!M!", ENVENOM_MAGNITUDE);
    }

    @Override
    public void onRemember() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            for(AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDead && !monster.isDying) {
                    AbstractDungeon.actionManager.addToBottom(
                            new ApplyPowerAction(monster, owner, new PoisonPower(monster, owner, POISON_ON_REMEMBER), POISON_ON_REMEMBER));
                }
            }
        }

        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, source, new EnvenomPower(owner, ENVENOM_MAGNITUDE), ENVENOM_MAGNITUDE));
    }

    @Override
    public void onForget() {
        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(owner, source, EnvenomPower.POWER_ID, ENVENOM_MAGNITUDE));
    }
}
