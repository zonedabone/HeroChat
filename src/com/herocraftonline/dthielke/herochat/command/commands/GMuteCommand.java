package com.herocraftonline.dthielke.herochat.command.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.channels.ChannelManager;
import com.herocraftonline.dthielke.herochat.command.BaseCommand;

public class GMuteCommand extends BaseCommand {

    public GMuteCommand(HeroChat plugin) {
        super(plugin);
        name = "Global Mute";
        description = "Prevents a player from speaking in any channel";
        usage = "§e/ch gmute §8[player] §eOR /gmute §8[player]";
        minArgs = 0;
        maxArgs = 1;
        identifiers.add("ch gmute");
        identifiers.add("gmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ChannelManager cm = plugin.getChannelManager();
        if (args.length == 0) {
            displayMuteList(sender);
        } else {
            if (sender instanceof Player) {
                Player muter = (Player) sender;
                if (plugin.getPermissions().isAdmin(muter)) {
                    Player mutee = plugin.getServer().getPlayer(args[0]);
                    if (mutee != null) {
                        String name = mutee.getName();
                        if (!(plugin.getPermissions().isAdmin(mutee))) {
                            if (cm.getMutelist().contains(name)) {
                                cm.getMutelist().remove(name);
                                muter.sendMessage(plugin.getTag() + name + " has been globally unmuted");
                                mutee.sendMessage(plugin.getTag() + "You have been globally unmuted");
                            } else {
                                cm.getMutelist().add(name);
                                muter.sendMessage(plugin.getTag() + name + " has been globally muted");
                                mutee.sendMessage(plugin.getTag() + "You have been globally muted");
                            }
                        } else {
                            muter.sendMessage(plugin.getTag() + "You cannot globally mute " + name);
                        }
                    } else {
                        muter.sendMessage(plugin.getTag() + "Player not found");
                    }
                } else {
                    muter.sendMessage(plugin.getTag() + "You do not have sufficient permission");
                }
            } else {
                sender.sendMessage(plugin.getTag() + "You must be a player to use this command");
            }
        }
    }

    private void displayMuteList(CommandSender sender) {
        String muteListMsg;
        List<String> mutes = plugin.getChannelManager().getMutelist();
        if (mutes.isEmpty()) {
            muteListMsg = plugin.getTag() + "No one is currently muted";
        } else {
            muteListMsg = "Currently muted: ";
            for (String s : mutes) {
                muteListMsg += s + ",";
            }
            muteListMsg = muteListMsg.substring(0, muteListMsg.length() - 1);
        }
        sender.sendMessage(muteListMsg);
    }
}
