package maloschnikow.playertags.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;


public class CommandCustomPlayerTagRemove implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        //get arguments
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

        Team playerTeam = player.getServer().getScoreboard().getTeam(player.getUuidAsString());
        player.getServer().getScoreboard().removeTeam(playerTeam);
        
        return Command.SINGLE_SUCCESS;
    }
    
}
