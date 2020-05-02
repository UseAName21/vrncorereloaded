package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.utils.storage.warps.WarpsManager;
import net.skeagle.vrncore.utils.storage.warps.WarpsResource;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class WarpsGUI extends MenuPagged<WarpsManager> {

    public WarpsGUI() {
        super(WarpsResource.getInstance().getManData());
        setTitle("Global warps list");
    }

    @Override
    protected ItemStack convertToItemStack(final WarpsManager warp) {
        return ItemCreator.of(getIcon(warp)).name("&7" + warp.getName()).build().make();
    }

    @Override
    protected void onPageClick(final Player p, final WarpsManager warp, final ClickType click) {
        if (click.isLeftClick()) {
            p.closeInventory();
            say(p, "Teleporting...");
            p.teleport(warp.getLoc());
        }
        if (click.isRightClick()) {
            CompSound.NOTE_PLING.play(p.getLocation(), 5f, 0.5f);
            new WarpsGUI.DeleteConfirm(warp.getName()).displayTo(getViewer());
        }
    }

    @Override
    protected String[] getInfo() {
        return null;
    }

    private final class DeleteConfirm extends Menu {

        private final Button cancel;
        private final Button confirm;
        private final Button info;

        private DeleteConfirm(final String warp) {
            super(null);
            setSize(9);
            setTitle("&c&lConfirm delete?");

            cancel = new ButtonMenu(new WarpsGUI(), CompMaterial.RED_WOOL, "&4Cancel");
            confirm = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                    WarpsResource.getInstance().delWarp(warp);
                    getViewer().closeInventory();
                    say(getViewer(), "&7Home &a" + warp + "&7 successfully deleted.");
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.LIME_WOOL,
                            "&aConfirm").build().make();
                }
            };
            info = new Button() {

                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.MAP,
                            "&6Are you sure?",
                            "",
                            "&eAre you sure you want to",
                            "&edelete this warp?").build().make();
                }
            };
        }

        @Override
        public ItemStack getItemAt(final int slot) {

            if (slot == 2)
                return cancel.getItem();

            if (slot == 4)
                return info.getItem();

            if (slot == 6)
                return confirm.getItem();

            return null;
        }

        @Override
        protected String[] getInfo() {
            return null;
        }
    }

    private boolean blockCheck(final Block b) {
        return (!b.getType().isAir() && b.getType().isSolid());
    }

    private CompMaterial getIcon(final WarpsManager warp) {
        final Location l = warp.getLoc().clone();
        final Block standing = l.add(0, -1, 0).getBlock();
        final double x = l.getX() - (double) l.getBlockX();
        final double z = l.getZ() - (double) l.getBlockZ();
        final Block b1 = standing.getLocation().clone().add(1, 0, 0).getBlock();
        final Block b2 = standing.getLocation().clone().add(1, 0, 1).getBlock();
        final Block b3 = standing.getLocation().clone().add(0, 0, 1).getBlock();
        final Block b4 = standing.getLocation().clone().add(-1, 0, 1).getBlock();
        final Block b5 = standing.getLocation().clone().add(-1, 0, 0).getBlock();
        final Block b6 = standing.getLocation().clone().add(-1, 0, -1).getBlock();
        final Block b7 = standing.getLocation().clone().add(0, 0, -1).getBlock();
        final Block b8 = standing.getLocation().clone().add(1, 0, -1).getBlock();
        //check direct block
        if (blockCheck(standing)) {
            return CompMaterial.fromBlock(standing);
        }
        //check adjacent
        if (x > 0.7 && blockCheck(b1)) {
            return CompMaterial.fromBlock(b1);
        }
        if (x < 0.3 && blockCheck(b5)) {
            return CompMaterial.fromBlock(b5);
        }
        if (z > 0.7 && blockCheck(b3)) {
            return CompMaterial.fromBlock(b3);
        }
        if (z < 0.3 && blockCheck(b7)) {
            return CompMaterial.fromBlock(b7);
        }
        //corners
        if (x > 0.7 && z > 0.7 && blockCheck(b2)) {
            return CompMaterial.fromBlock(b2);
        }
        if (x < 0.3 && z > 0.7 && blockCheck(b4)) {
            return CompMaterial.fromBlock(b4);
        }
        if (x > 0.7 && z < 0.3 && blockCheck(b8)) {
            return CompMaterial.fromBlock(b8);
        }
        if (x < 0.3 && z < 0.3 && blockCheck(b6)) {
            return CompMaterial.fromBlock(b6);
        }
        //no ground in 3x3 area
        return CompMaterial.BARRIER;
    }
}
