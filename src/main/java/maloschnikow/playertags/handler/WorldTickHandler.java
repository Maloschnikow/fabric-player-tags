package maloschnikow.playertags.handler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.EndWorldTick;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Assigns seperate teams for pets, so their names won't get rendered with the decorated tag of their owner.
 */
public class WorldTickHandler implements EndWorldTick{

    @Override
    public void onEndTick(ServerWorld world) {
        for (Entity entity : world.iterateEntities()) {
            if (!(entity instanceof TameableEntity tameable)) continue;

            /**
             * isAlive() is necessary, because otherwise when the last pet dies,
             * it would be reassigned again to the pets team before the AfterDeathHandler
             * can remove the team
             */
            if (tameable.isTamed() && tameable.isAlive()) {
                assignToOwnerTeam(tameable);
            } else {
                removeFromTeam(tameable);
            }
        }
    }

    private static void assignToOwnerTeam(TameableEntity tameable) {
        if (!(tameable.getOwner() instanceof ServerPlayerEntity player)) return;

        Scoreboard scoreboard = ((ServerWorld) tameable.getEntityWorld()).getScoreboard();
        String teamName = player.getUuidAsString() + "_pets";

        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.addTeam(teamName);
        }

        scoreboard.addScoreHolderToTeam(tameable.getNameForScoreboard(), team);
    }

    private static void removeFromTeam(TameableEntity tameable) {
        Scoreboard scoreboard = ((ServerWorld) tameable.getEntityWorld()).getScoreboard();
        Team team = scoreboard.getScoreHolderTeam(tameable.getNameForScoreboard());
        if(team == null) { return; }

        scoreboard.clearTeam(tameable.getNameForScoreboard());
    }
    
}
