package com.puffin.websitevoucher;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.commands.CommandHandler;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.plugin.EventHandler;
import com.eu.habbo.plugin.EventListener;
import com.eu.habbo.plugin.HabboPlugin;
import com.eu.habbo.plugin.events.emulator.EmulatorLoadedEvent;
import com.puffin.websitevoucher.commands.AddWebsiteVoucherCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class WebsiteVoucher extends HabboPlugin implements EventListener {
    public static WebsiteVoucher INSTANCE = null;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Emulator.getPluginManager().registerEvents(this, this);

        if (Emulator.isReady) {
            this.checkDatabase();
        }

        Emulator.getLogging().logStart("[WebsiteVoucher] Started Website Voucher Plugin!");
    }

    @Override
    public void onDisable() {
        Emulator.getLogging().logShutdownLine("[WebsiteVoucher] Stopped Website Voucher Plugin!");
    }

    @EventHandler
    public static void onEmulatorLoaded(EmulatorLoadedEvent event) {
        INSTANCE.checkDatabase();
    }

    @Override
    public boolean hasPermission(Habbo habbo, String s) {
        return false;
    }

    private void checkDatabase() {
        boolean reloadPermissions = false;

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection(); Statement statement = connection.createStatement()) {
            statement.execute("ALTER TABLE  `emulator_texts` CHANGE  `value`  `value` VARCHAR( 4096 ) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL");
        } catch (SQLException e) {
        }

        Emulator.getTexts().register("commands.keys.cmd_shopvoucher", "shopvoucher;voucher");
        Emulator.getTexts().register("commands.description.cmd_shopvoucher", ":shopvoucher <code> <amount>");
        Emulator.getTexts().register("commands.error.cmd_shopvoucher.missing_parameters", "Usage: :shopvoucher <code> <amount>");
        Emulator.getTexts().register("commands.error.cmd_shopvoucher.invalid_amount", "The amount you use must be a valid number");
        Emulator.getTexts().register("commands.error.cmd_shopvoucher.invalid_code_format", "Voucher code must be at least 3 characters (letters, numbers, or special characters allowed, no spaces)");
        Emulator.getTexts().register("commands.error.cmd_shopvoucher.invalid_amount_range", "Amount must be between 1 and 500");
        Emulator.getTexts().register("commands.error.cmd_shopvoucher.positive_amount", "The amount you use must be a positive number");
        Emulator.getTexts().register("commands.error.cmd_shopvoucher.code_exists", "A voucher with code '%code%' already exists");
        Emulator.getTexts().register("commands.error.cmd_shopvoucher.database_error", "Error creating shop voucher");
        Emulator.getTexts().register("commands.success.cmd_shopvoucher.created", "You have successfully created the voucher '%code%' for %amount% USD");
        
        reloadPermissions = this.registerPermission("cmd_shopvoucher", "'0', '1'", "0", reloadPermissions);
        
        CommandHandler.addCommand(new AddWebsiteVoucherCommand("cmd_shopvoucher", Emulator.getTexts().getValue("commands.keys.cmd_shopvoucher").split(";")));

        if (reloadPermissions) {
            Emulator.getGameEnvironment().getPermissionsManager().reload();
        }
    }

    private boolean registerPermission(String name, String options, String defaultValue, boolean defaultReturn) {
        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("ALTER TABLE  `permissions` ADD  `" + name + "` ENUM(  " + options + " ) NOT NULL DEFAULT  '" + defaultValue + "'")) {
                statement.execute();
                return true;
            }
        } catch (SQLException e) {
        }

        return defaultReturn;
    }

    public static void main(String[] args) {
        System.out.println("Don't run this separately");
    }
}