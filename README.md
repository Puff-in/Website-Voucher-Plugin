# Website Voucher Plugin

[![GitHub release (latest by date)](https://img.shields.io/github/v/release/Puff-in/Website-Balance-Plugin)](https://github.com/Puff-in/Website-Voucher-Plugin/releases)
[![GitHub stars](https://img.shields.io/github/stars/Puff-in/Website-Balance-Plugin?style=social)](https://github.com/Puff-in/Website-Balance-Plugin/stargazers)
[![GitHub license](https://img.shields.io/github/license/Puff-in/Website-Balance-Plugin)](https://github.com/Puff-in/Website-Balance-Plugin/blob/main/LICENSE)

## 📖 Introduction

The **Atom CMS Website Voucher Plugin** is a simple and useful tool to create a Shop Voucher on Atom CMS directly from your Habbo Retro Client. It's designed specifically for those using Atom CMS with Arcturus Morningstar.

## 🚀 Installation

1.  **Download the Plugin:** Download the `WebsiteVoucherCommand.jar` file from the [releases page](https://github.com/Puff-in/Website-Voucher-Plugin/releases/tag/v1.0.0).

2.  **Place in your "plugins" folder:** Copy the `.jar` file and place it inside the `plugins` folder of your emulator folder.

3.  **Reboot your Emulator:** Restart your emulator to load the new plugin.

## 🛠️ Usage

Once the plugin is running, you can update a user's Shop Balance with a simple command.

1.  **Update Permissions:**
    -   Go to your `permissions` table in the database.
    -   Find the `cmd_addwebsitevoucher` permission and set its value to `1` for the ranks you want to have access to this command.
    -   Run the `:update_permissions` command in your client to apply the changes.

2.  **Command Execution:**
    -   To use the command, simply type `:addvoucher <code> <amount>` in your client.
    -   `<code>` is the code a user will use to redeem the voucher with.
    -   `<amount>` is the amount of currency the user will receive in their shop balance from redeeming the code.

3.  **Balance Updated:**
    -   A message will appear in the client confirming the success or failure of the command.
