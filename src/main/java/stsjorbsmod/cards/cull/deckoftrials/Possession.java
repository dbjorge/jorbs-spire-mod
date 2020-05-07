package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.SelfExertField;
import stsjorbsmod.powers.PossessionPower;

public class Possession extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Possession.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int TIMES = 1;
    private static final int UPGRADE_TIMES = 1;

    public Possession() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = TIMES;
        SelfExertField.selfExert.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        if (AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDead && !monster.isDying) {
                    count += 1;
                }
            }
        }
        JorbsMod.logger.info(count);
        if (count != 1) {
            JorbsMod.logger.info("more than 1");
            addToBot(new ApplyPowerAction(m, p, new PossessionPower(m, this.magicNumber)));
            JorbsMod.logger.info("Applied Power");
        }
        else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, "Only one enemy!", true));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_TIMES);
            upgradeDescription();
        }
    }
}

