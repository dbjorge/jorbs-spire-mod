package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class ChainLightning extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ChainLightning.class.getSimpleName());
    public static final String IMG = makeCardPath("AOE_Commons/chain_lightning.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int DAMAGE_PLUS_PER_HOP = 2;
    private static final int UPGRADE_PLUS_PER_HOP = 2;

    private int currentChainHopIndex = 0;

    public ChainLightning() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE_PLUS_PER_HOP;
    }

    @Override
    protected int calculateBonusBaseDamage() {
        return magicNumber * currentChainHopIndex;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.currentChainHopIndex = 0;

        getRandomOrderMonsters(AbstractDungeon.getMonsters().monsters, m)
                .forEach(monster -> {
                    this.calculateCardDamage(monster);
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(
                            monster,
                            new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                            AttackEffect.NONE));
                    addLightningEffect(monster, this.currentChainHopIndex);
                    this.currentChainHopIndex += 1;
                });

        this.currentChainHopIndex = 0;
    }

    private ArrayList<AbstractMonster> getRandomOrderMonsters(ArrayList<AbstractMonster> targets, AbstractMonster initialTarget) {
        ArrayList<AbstractMonster> finalTargets = new ArrayList<>(Arrays.asList(initialTarget));
        ArrayList<AbstractMonster> chainCandidates = targets.stream()
                .filter(t -> !(t.halfDead || t.isDying || t.isEscaping || t == initialTarget))
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(chainCandidates, AbstractDungeon.cardRandomRng.random);
        finalTargets.addAll(chainCandidates);

        return finalTargets;
    }

    private void addLightningEffect(AbstractMonster monster, int multiplier) {
        float duration = (0.05F * multiplier);
        AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP", duration));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(monster.drawX, monster.drawY), duration));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_PER_HOP);
            upgradeDescription();
        }
    }
}
