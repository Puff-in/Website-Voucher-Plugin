package com.puffin.websitevoucher.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.commands.Command;
import com.eu.habbo.habbohotel.gameclients.GameClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddWebsiteVoucherCommand extends Command {

    public AddWebsiteVoucherCommand(String permission, String[] keys) {
        super(permission, keys);
    }

    @Override
    public boolean handle(GameClient gameClient, String[] strings) throws Exception {
        if (strings.length != 3) {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_shopvoucher.missing_parameters"));
            return true;
        }

        String voucherCode = strings[1].trim();
        int amount;

        if (voucherCode.length() < 3) {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_shopvoucher.invalid_code_format"));
            return true;
        }

        if (!voucherCode.matches("\\S{3,}")) {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_shopvoucher.invalid_code_format"));
            return true;
        }

        try {
            amount = Integer.parseInt(strings[2]);
        } catch (NumberFormatException e) {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_shopvoucher.invalid_amount"));
            return true;
        }

        if (amount <= 0 || amount > 500) {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_shopvoucher.invalid_amount_range"));
            return true;
        }

        if (voucherCodeExists(voucherCode)) {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_shopvoucher.code_exists").replace("%code%", voucherCode));
            return true;
        }

        if (createVoucher(voucherCode, amount)) {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.success.cmd_shopvoucher.created")
                    .replace("%code%", voucherCode)
                    .replace("%amount%", String.valueOf(amount)));
        } else {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_shopvoucher.database_error"));
        }

        return true;
    }

    private boolean voucherCodeExists(String code) {
        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id FROM website_shop_vouchers WHERE code = ?")) {
            
            statement.setString(1, code);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            Emulator.getLogging().logSQLException(e);
            return true;
        }
    }

    private boolean createVoucher(String code, int amount) {
        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO website_shop_vouchers (code, amount, max_uses, use_count) VALUES (?, ?, 1, 0)")) {
            
            statement.setString(1, code);
            statement.setInt(2, amount);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            Emulator.getLogging().logSQLException(e);
            return false;
        }
    }
}