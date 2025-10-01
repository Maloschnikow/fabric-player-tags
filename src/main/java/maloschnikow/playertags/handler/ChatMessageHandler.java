package maloschnikow.playertags.handler;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents.AllowChatMessage;
import net.minecraft.network.message.MessageType.Parameters;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


/**
 * "Prevent" server from broadcasting original message.
 * Instead broadcast a new message with modified styles and the content of the original message
 */

public class ChatMessageHandler implements AllowChatMessage {

    @Override
    public boolean allowChatMessage(SignedMessage message, ServerPlayerEntity sender, Parameters params) {

		Text senderCustomName = Text.literal("").formatted(Formatting.RESET).append(
			sender.getDisplayName().copy().setStyle(Style.EMPTY
			.withHoverEvent(new HoverEvent.ShowText(
				Text.literal("")
				.append(Text.literal("Click here to whisper to ").formatted(Formatting.GRAY, Formatting.ITALIC))
				.append(sender.getDisplayName())
			))
			.withClickEvent(new ClickEvent.SuggestCommand("/tell " + sender.getName().getString() + " "))
			.withInsertion(sender.getName().getString())
			));


		Text modifiedMessage = senderCustomName.copy().append(Text.literal(": ").formatted(Formatting.WHITE)).append(Text.literal(message.getContent().getString()).formatted(Formatting.WHITE));

		MinecraftServer server = sender.getServer();
		server.getPlayerManager().broadcast(modifiedMessage, false);

		return false;
    }
    
}
