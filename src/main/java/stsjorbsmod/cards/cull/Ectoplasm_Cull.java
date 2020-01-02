package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Ectoplasm_Cull extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Ectoplasm_Cull.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int GOLD_PER_MANIFEST = 10;
    private static final int UPGRADE_GOLD_PER_MANIFEST = 3;
    private static final int UPGRADE_PLUS_DAMAGE = 3;

    public Ectoplasm_Cull() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = 0;
        metaMagicNumber = baseMetaMagicNumber = GOLD_PER_MANIFEST;
        this.exhaust = true;
    }

    @Override
    public int calculateBonusMagicNumber() {
        AbstractPlayer p = AbstractDungeon.player;
       return p instanceof Cull ? ((Cull) p).manifest * metaMagicNumber : 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.POISON));

        for(int i = 0; i < magicNumber; ++i) {
            AbstractDungeon.effectList.add(new GainPennyEffect(p, p.hb.cX, p.hb.cY, p.hb.cX, p.hb.cY, true));
        }
        AbstractDungeon.player.gainGold(magicNumber);
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMetaMagicNumber(UPGRADE_GOLD_PER_MANIFEST);
            upgradeDescription();
        }
    }
}
