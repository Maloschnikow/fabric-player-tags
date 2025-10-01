package maloschnikow.playertags.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import maloschnikow.playertags.WrongPermissionLevelMessage;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CommandCustomPlayerTagSet implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        
        ServerCommandSource commandSource = context.getSource();
        MinecraftServer server = commandSource.getServer();
        ServerScoreboard scoreboard = server.getScoreboard();
        
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
        String tagString          = StringArgumentType.getString(context, "tag");
        Formatting color          = ColorArgumentType.getColor(context, "color");

        //check if player has permission to change other players tag
        if( (!commandSource.hasPermissionLevel(2)) && player != commandSource.getPlayer() ) {
            throw new CommandSyntaxException(null, new WrongPermissionLevelMessage(2, "change another player's tag"));
        }

        MutableText teamPrefix   = Text.literal("[").formatted(Formatting.GRAY).append(
                            Text.literal(tagString).formatted(color)).append(
                            Text.literal("]").formatted(Formatting.GRAY));
        
        //prepare success message here, so the old display name is still shown
        Text sourceSuccessMsg = player.getDisplayName().copy().append(Text.literal("'s new tag is ")).append(teamPrefix).append(".");
        Text targetSuccessMsg = Text.literal("Your new tag is ").append(teamPrefix).append(".");

        String playerUUID = player.getUuidAsString();
        Team playerTeam = scoreboard.getTeam(playerUUID);
        if(playerTeam != null) {
            scoreboard.removeTeam(playerTeam);
        }
        playerTeam = scoreboard.addTeam(playerUUID);
        playerTeam.setPrefix(teamPrefix.append(Text.literal(" ").formatted(Formatting.RESET)));
        
        scoreboard.addScoreHolderToTeam(player.getName().getString(), playerTeam);

        if(commandSource.getPlayer() != player) {
            commandSource.sendMessage(sourceSuccessMsg);
        }
        player.sendMessage(targetSuccessMsg);
        
        return Command.SINGLE_SUCCESS;
    }
    
}
