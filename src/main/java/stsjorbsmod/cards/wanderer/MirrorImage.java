package stsjorbsmod.cards.wanderer;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import kobting.friendlyminions.characters.AbstractPlayerWithMinions;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;
import kobting.friendlyminions.helpers.MinionConfigHelper;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.KindnessMemory;
import stsjorbsmod.monsters.MirrorImageMinion;
import stsjorbsmod.powers.NextAttackMissesPower;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;

public class MirrorImage extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(MirrorImage.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int MINIONS = 1;
    private static final int UPGRADE_PLUS_MINIONS = 1;

    public MirrorImage() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MINIONS;
        exhaust = true;
        tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(p, KindnessMemory.STATIC.ID));

        if(!BaseMod.isBaseGameCharacter(p) && !(p instanceof AbstractPlayerWithMinions)) {
            AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, 3.0F, EXTENDED_DESCRIPTION[0], true));
            return;
        }

        MinionConfigHelper.MinionAttackTargetChance = 1.0F;
        MinionConfigHelper.MinionPowerChance = 1.0F;

        MonsterGroup existingMinions = BaseMod.isBaseGameCharacter(p) ?
                BasePlayerMinionHelper.getMinions(p) :
                ((AbstractPlayerWithMinions) p).getMinions();

        int existingMinionCount = existingMinions.monsters.size();

        for (int i = 0; i < magicNumber; ++i) {
            int slot = existingMinionCount + i;

            // Intentionally omitting Settings.scale, applied later in AbstractMonster ctor
            float PADDING_BETWEEN_MINIONS = -10F;
            float offsetX =
                    slot * (MirrorImageMinion.HITBOX_WIDTH + PADDING_BETWEEN_MINIONS) +
                    (p.drawX / Settings.scale) +
                    -1124F;
            float offsetY = 0;

            AbstractFriendlyMonster minion = new MirrorImageMinion(offsetX, offsetY);

            boolean added = BaseMod.isBaseGameCharacter(p) ?
                    BasePlayerMinionHelper.addMinion(p, minion) :
                    ((AbstractPlayerWithMinions) p).addMinion(minion);

            if (!added) {
                AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, 3.0F, EXTENDED_DESCRIPTION[1], true));
                break;
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MINIONS);
            upgradeDescription();
        }
    }
}
