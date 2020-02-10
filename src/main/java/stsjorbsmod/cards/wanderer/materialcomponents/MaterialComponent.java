package stsjorbsmod.cards.wanderer.materialcomponents;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.cards.CustomJorbsModCard;

public abstract class MaterialComponent extends CustomJorbsModCard {

    public MaterialComponent(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, cost, type, color, rarity, target);
    }

    @Override
    public final void use(AbstractPlayer p, AbstractMonster m) {
        MaterialComponentsDeck.playedThisCombatCount++;
        useMaterialComponent(p, m);
    }

    public abstract void useMaterialComponent(AbstractPlayer p, AbstractMonster m);
}
