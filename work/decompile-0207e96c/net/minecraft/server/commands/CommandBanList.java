package net.minecraft.server.commands;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.players.ExpirableListEntry;
import net.minecraft.server.players.PlayerList;

public class CommandBanList {

    public CommandBanList() {}

    public static void register(CommandDispatcher<CommandListenerWrapper> commanddispatcher) {
        commanddispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) net.minecraft.commands.CommandDispatcher.literal("banlist").requires((commandlistenerwrapper) -> {
            return commandlistenerwrapper.hasPermission(3);
        })).executes((commandcontext) -> {
            PlayerList playerlist = ((CommandListenerWrapper) commandcontext.getSource()).getServer().getPlayerList();

            return showList((CommandListenerWrapper) commandcontext.getSource(), Lists.newArrayList(Iterables.concat(playerlist.getBans().getEntries(), playerlist.getIpBans().getEntries())));
        })).then(net.minecraft.commands.CommandDispatcher.literal("ips").executes((commandcontext) -> {
            return showList((CommandListenerWrapper) commandcontext.getSource(), ((CommandListenerWrapper) commandcontext.getSource()).getServer().getPlayerList().getIpBans().getEntries());
        }))).then(net.minecraft.commands.CommandDispatcher.literal("players").executes((commandcontext) -> {
            return showList((CommandListenerWrapper) commandcontext.getSource(), ((CommandListenerWrapper) commandcontext.getSource()).getServer().getPlayerList().getBans().getEntries());
        })));
    }

    private static int showList(CommandListenerWrapper commandlistenerwrapper, Collection<? extends ExpirableListEntry<?>> collection) {
        if (collection.isEmpty()) {
            commandlistenerwrapper.sendSuccess(IChatBaseComponent.translatable("commands.banlist.none"), false);
        } else {
            commandlistenerwrapper.sendSuccess(IChatBaseComponent.translatable("commands.banlist.list", collection.size()), false);
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                ExpirableListEntry<?> expirablelistentry = (ExpirableListEntry) iterator.next();

                commandlistenerwrapper.sendSuccess(IChatBaseComponent.translatable("commands.banlist.entry", expirablelistentry.getDisplayName(), expirablelistentry.getSource(), expirablelistentry.getReason()), false);
            }
        }

        return collection.size();
    }
}
