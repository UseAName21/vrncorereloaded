package net.skeagle.vrncore.tab;

import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.List;

public class AnimTab {
    private final List<ChatComponentText> headers;
    private final List<ChatComponentText> footers;
    PacketPlayOutPlayerListHeaderFooter tab;

    public AnimTab() {
        headers = new ArrayList<>();
        footers = new ArrayList<>();
        tab = new PacketPlayOutPlayerListHeaderFooter();
    }

    public void run() {
        if (headers.isEmpty() && footers.isEmpty()) return;

        Common.runTimer(10, new Runnable() {
            int i = 0;
            int j = 0;

            @Override
            public void run() {
                if (i >= headers.size()) i = 0;
                if (j >= footers.size()) i = 0;
                tab.header = headers.get(i);
                tab.footer = footers.get(j);
                ++i;
                ++j;
            }
        });
    }
}
