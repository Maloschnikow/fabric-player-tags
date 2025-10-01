package maloschnikow.playertags.handler;

import maloschnikow.playertags.Playertags;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDeath;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.scoreboard.Team;

/**
 * Remove the pet team of an owner after their last pet dies.
 */
public class AfterDeathHandler implements AfterDeath {

    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if ( !(entity instanceof TameableEntity) ) return;
        
        Team team = entity.getScoreboardTeam();
        if (team == null) return;

        if (team.getPlayerList().size() <= 1) { // 1 because the team skill contains the killed entity
            team.getScoreboard().removeTeam(team);
            Playertags.LOGGER.debug("Team " + team.getName() + " removed, bc the last pet in the team died.");
        }
    }
    
}
