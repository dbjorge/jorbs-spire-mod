package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class EnvyMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(EnvyMemory.class);

    private static final int VULNERABLE_ON_REMEMBER = 1;
    private static final int VULNERABLE_ON_TARGET_ENEMY = 1;

    public EnvyMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.SIN, owner, isClarified);
        setDescriptionPlaceholder("!M!", VULNERABLE_ON_TARGET_ENEMY);
    }

    @Override
    public void onRemember() {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new VulnerablePower(owner, VULNERABLE_ON_REMEMBER, false), VULNERABLE_ON_REMEMBER));
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(monster, owner, new VulnerablePower(monster, VULNERABLE_ON_TARGET_ENEMY, false), VULNERABLE_ON_TARGET_ENEMY));
    }
}
