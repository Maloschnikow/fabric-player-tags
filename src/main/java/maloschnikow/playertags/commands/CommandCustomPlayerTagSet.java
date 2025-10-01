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
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
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
                            
        Text teamPrefix    = Text.literal("")
        .append(Text.literal("[").formatted(Formatting.GRAY)
        .append(Text.literal(tagString).formatted(color)
        .append(Text.literal("]").formatted(Formatting.GRAY)
        )));
        
        Text prefixSpaced = teamPrefix.copy().append(Text.literal(" "));
        
        Text fakeDisplayName = prefixSpaced.copy().append(player.getName());
        
        Text hoverTextRoot = Text.literal("").setStyle(Style.EMPTY.withHoverEvent(new HoverEvent.ShowText(fakeDisplayName)));

        Text finalPrefix = hoverTextRoot.copy().append(teamPrefix);

        //prepare success message here, so the old display name is still shown
        Text sourceSuccessMsg = player.getDisplayName().copy().append(Text.literal("'s new tag is ")).append(finalPrefix).append(".");
        Text targetSuccessMsg = Text.literal("Your new tag is ").append(finalPrefix).append(".");

        String playerUUID = player.getUuidAsString();
        Team playerTeam = scoreboard.getTeam(playerUUID);
        if(playerTeam != null) {
            scoreboard.removeTeam(playerTeam);
        }
        playerTeam = scoreboard.addTeam(playerUUID);
        playerTeam.setPrefix(prefixSpaced);
        
        scoreboard.addScoreHolderToTeam(player.getName().getString(), playerTeam);

        if(commandSource.getPlayer() != player) {
            commandSource.sendMessage(sourceSuccessMsg);
        }
        player.sendMessage(targetSuccessMsg);
        
        return Command.SINGLE_SUCCESS;
    }
    
}
