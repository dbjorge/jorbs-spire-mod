package stsjorbsmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.SmokeBomb;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

// 0: Escape from a non-boss combat and receive no rewards. If you escape, you may ignore paths when choosing the next room to travel to. Destroy this card.
public class DimensionDoor extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(DimensionDoor.class.getSimpleName());
    public static final String IMG = makeCardPath("Manipulation_Rares/dimension_door.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 0;

    private static boolean activatedMapTravel = false;

    public static boolean isMapTravelActive() {
        return activatedMapTravel;
    }

    public static void deactivateMapTravel() {
        activatedMapTravel = false;
    }

    public DimensionDoor() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.upgraded = true;
        this.purgeOnUse = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        activatedMapTravel = true;

        p.masterDeck.removeCard(this.cardID);

        (new SmokeBomb()).use(p);
    }

    @Override
    public void upgrade() {}
}
