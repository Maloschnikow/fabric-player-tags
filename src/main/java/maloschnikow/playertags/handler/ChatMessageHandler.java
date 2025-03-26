package maloschnikow.playertags.handler;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents.AllowChatMessage;
import net.minecraft.network.message.MessageType.Parameters;

import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


public class ChatMessageHandler implements AllowChatMessage {

    @Override
    public boolean allowChatMessage(SignedMessage message, ServerPlayerEntity sender, Parameters params) {
		
		//Check if player has a custom name aka playertag
		/* if ( !(sender.hasCustomName() && sender.isCustomNameVisible()) ) {
			return true;
		} */

		//If so, prevent server from broadcasting original message.
		//Instead broadcast a new message with the playertag name and the content of the original message

		MutableText senderCustomName = sender.getDisplayName().copy();
		Text modifiedMessage = senderCustomName.append(Text.literal(": ").formatted(Formatting.WHITE)).append(Text.literal(message.getContent().getString()).formatted(Formatting.WHITE));

		MinecraftServer server = sender.getServer();
		server.getPlayerManager().broadcast(modifiedMessage, false);

		return false;
    }
    
}
