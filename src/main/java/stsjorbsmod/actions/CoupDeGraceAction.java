package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Nemesis;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import stsjorbsmod.powers.CoupDeGracePower;

public class CoupDeGraceAction extends AbstractGameAction {
    private boolean freeToPlayOnce = false;
    private boolean upgraded = false;
    private boolean targetingEnemy;
    private AbstractPlayer p;
    private AbstractMonster m;
    private int energyOnUse = -1;

    public CoupDeGraceAction(AbstractPlayer p, AbstractMonster m, boolean upgraded, boolean freeToPlayOnce, int energyOnUse, boolean targetingEnemy) {
        this.p = p;
        this.m = m;
        this.freeToPlayOnce = freeToPlayOnce;
        this.upgraded = upgraded;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.targetingEnemy = targetingEnemy;
    }

    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        if (this.upgraded) {
            ++effect;
        }

        if (effect > 0) {
            if (targetingEnemy && m != null) {
                this.addToBot(new ApplyPowerAction(this.m, this.m, new CoupDeGracePower(this.m, effect)));
                if (!m.id.equalsIgnoreCase(Nemesis.ID))
                    this.addToBot(new ApplyPowerAction(this.m, this.m, new IntangiblePlayerPower(this.m, effect)));
                else
                    this.addToBot(new ApplyPowerAction(this.m, this.m, new IntangiblePower(this.m, effect)));
            }
            else if (!targetingEnemy){
                this.addToBot(new ApplyPowerAction(this.p, this.p, new CoupDeGracePower(this.p, effect), effect));
                this.addToBot(new ApplyPowerAction(this.p, this.p, new IntangiblePlayerPower(this.p, effect), effect));
            }

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }

        this.isDone = true;
    }
}

