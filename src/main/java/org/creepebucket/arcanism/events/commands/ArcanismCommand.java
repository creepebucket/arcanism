package org.creepebucket.arcanism.events.commands;

import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.commands.Commands.literal;
import static org.creepebucket.arcanism.Arcanism.MODID;

@EventBusSubscriber(modid = MODID)
public class ArcanismCommand {
	public static final List<CommandHandler> HANDLERS = new ArrayList<>();

	static {
		registerHandler(new CommandHandler.HelpHandler());
		registerHandler(new CommandHandler.ManaNetworkInfoHandler());
		registerHandler(new CommandHandler.ManaNetworkSetHandler());
		registerHandler(new CommandHandler.SpellHandler());
		registerHandler(new CommandHandler.ItemManaHandler());
	}

	public static void registerHandler(CommandHandler handler) {
		HANDLERS.add(handler);
	}

	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event) {
		var root = literal("arcanism");
		for (var handler : HANDLERS) {
			root.then(handler.requiresPermission
				? handler.build().requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
				: handler.build());
		}
		event.getDispatcher().register(root);
	}
}
