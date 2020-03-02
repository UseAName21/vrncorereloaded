package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Resources;
import net.skeagle.vrncore.utils.WarpsHomesUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class homes extends SimpleCommand {
    private Resources r;
    private WarpsHomesUtil util;

    public homes(final Resources r) {
        super("homes");
        this.r = r;
        util = new WarpsHomesUtil(r);
        setDescription("List all available homes.");
        setPermission("vrn.homes");
        setPermissionMessage(VRNcore.noperm);
    }

    public void onCommand() {
        checkConsole();
        FileConfiguration config = this.r.getWarps();
        if (config.get("homes." + getPlayer().getUniqueId()) != null) {
            List<String> homes = util.returnArray("homes." + getPlayer().getUniqueId());
            String homeslist = String.join("&7,&a ", homes);
            say(getPlayer(), "&7Currently showing a list of &a" + homes.size() + "&7 home(s): &a" + homeslist + "&7.");
        } else {
            say(getPlayer(),"&cYou do not have any homes available.");
        }
    }
}
