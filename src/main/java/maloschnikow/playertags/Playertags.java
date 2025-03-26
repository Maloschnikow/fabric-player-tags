package maloschnikow.playertags;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.arguments.StringArgumentType;

import maloschnikow.playertags.commands.CommandCustomPlayerTagRemove;
import maloschnikow.playertags.commands.CommandCustomPlayerTagSet;
import maloschnikow.playertags.handler.ChatMessageHandler;


public class Playertags implements ModInitializer {
	public static final String MOD_ID = "player-tags";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//Register Command to set playertags
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(
				CommandManager.literal("playertag")
				.then(
					CommandManager.argument("player", EntityArgumentType.player())
					.then(
						CommandManager.literal("set")
						.then(
							CommandManager.argument("tag", StringArgumentType.string())
							.then(
								CommandManager.argument("color", ColorArgumentType.color())
								.executes(new CommandCustomPlayerTagSet())
							)
						)
					)
					.then(
						CommandManager.literal("remove")
						.executes(new CommandCustomPlayerTagRemove())
					)
				)
			);
		});
		
		//register event to intercept chat messages, style them and broadcast them
		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(new ChatMessageHandler());

		LOGGER.info("Initialized.");
	}
}