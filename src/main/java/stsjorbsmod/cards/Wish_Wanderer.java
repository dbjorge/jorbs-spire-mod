package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RestartCombatAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryUtils;

import java.util.function.Predicate;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Wish_Wanderer extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Wish_Wanderer.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Rares/wish_wanderer.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 1;

    public Wish_Wanderer() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.purgeOnUse = true; // this is what StSLib calls the "destroy on use" effect
    }

    private static boolean isBuffOrDebuff(AbstractPower power) {
        return !(power instanceof AbstractMemory);
    }
    private static boolean isBuffDebuffOrClarity(AbstractPower power) {
        return !(power instanceof AbstractMemory && !((AbstractMemory)power).isClarified);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Predicate<AbstractPower> shouldRetainPowerPredicate = upgraded ? Wish_Wanderer::isBuffDebuffOrClarity : Wish_Wanderer::isBuffOrDebuff;

        enqueueAction(new RestartCombatAction(shouldRetainPowerPredicate));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
        }
    }
}
