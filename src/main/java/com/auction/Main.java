package com.auction;

import com.auction.ui.AuctionSystemGUI;

import javax.swing.*;

/**
 * Application entry point for the Online Auction System.
 */
public class Main {
    public static void main(String[] args) {
        // Set modern system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            AuctionSystemGUI gui = new AuctionSystemGUI();
            gui.setVisible(true);
        });
    }
}
