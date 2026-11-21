package com.auction.ui;

import com.auction.model.AuctionItem;
import com.auction.model.User;
import com.auction.service.AuctionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
import java.util.Optional;

/**
 * Modern Swing graphical interface for the Online Auction System.
 */
public class AuctionSystemGUI extends JFrame {
    private final AuctionService auctionService;
    private final DefaultTableModel tableModel;
    private final JTable itemTable;
    private final JLabel statusLabel;
    private final JLabel timerLabel;

    public AuctionSystemGUI() {
        this(new AuctionService());
    }

    public AuctionSystemGUI(AuctionService auctionService) {
        this.auctionService = auctionService;

        setTitle("Online Auction Management System");
        setSize(960, 720);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 76, 120));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Online Auction System", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        timerLabel = new JLabel("Auction Timer: Inactive", JLabel.RIGHT);
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        timerLabel.setForeground(new Color(255, 215, 0));
        headerPanel.add(timerLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Center: Items Table
        String[] columnNames = {"Lot ID", "Item Description", "Current Bid ($)", "Highest Bidder", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        itemTable = new JTable(tableModel);
        itemTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemTable.setRowHeight(28);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        itemTable.getTableHeader().setBackground(new Color(230, 235, 245));
        itemTable.setGridColor(new Color(220, 220, 220));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        itemTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        itemTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        itemTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(itemTable);
        scrollPane.setBorder(new EmptyBorder(10, 15, 10, 15));
        add(scrollPane, BorderLayout.CENTER);

        // South Panel: Status and Action Buttons
        JPanel southPanel = new JPanel(new BorderLayout());

        statusLabel = new JLabel("Status: Guest (Not logged in)");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statusLabel.setBorder(new EmptyBorder(5, 20, 5, 20));
        southPanel.add(statusLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 15, 15, 15));
        buttonPanel.setBackground(new Color(245, 247, 250));

        JButton registerBtn = createStyledButton("Register", new Color(46, 117, 182));
        JButton loginBtn = createStyledButton("Login", new Color(46, 117, 182));
        JButton refreshBtn = createStyledButton("Refresh Lots", new Color(70, 130, 180));
        JButton bidBtn = createStyledButton("Place Bid", new Color(34, 139, 34));
        JButton addItemBtn = createStyledButton("Add Lot (Admin)", new Color(204, 102, 0));
        JButton timerBtn = createStyledButton("Set Timer (Admin)", new Color(178, 34, 34));
        JButton logoutBtn = createStyledButton("Logout", new Color(112, 128, 144));
        JButton exitBtn = createStyledButton("Exit", new Color(80, 80, 80));

        registerBtn.addActionListener(e -> onRegister());
        loginBtn.addActionListener(e -> onLogin());
        refreshBtn.addActionListener(e -> refreshTable());
        bidBtn.addActionListener(e -> onPlaceBid());
        addItemBtn.addActionListener(e -> onAddItem());
        timerBtn.addActionListener(e -> onSetTimer());
        logoutBtn.addActionListener(e -> onLogout());
        exitBtn.addActionListener(e -> System.exit(0));

        buttonPanel.add(registerBtn);
        buttonPanel.add(loginBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(bidBtn);
        buttonPanel.add(addItemBtn);
        buttonPanel.add(timerBtn);
        buttonPanel.add(logoutBtn);
        buttonPanel.add(exitBtn);

        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Register Service Listeners
        auctionService.addTimerTickListener(remaining -> SwingUtilities.invokeLater(() -> {
            if (remaining > 0) {
                timerLabel.setText("Time Remaining: " + remaining + "s");
            } else {
                timerLabel.setText("Auction Closed");
            }
        }));

        auctionService.addAuctionEndListener(winners -> SwingUtilities.invokeLater(() -> {
            refreshTable();
            StringBuilder sb = new StringBuilder("Auction Complete! Winning Bids:\n\n");
            for (Map.Entry<AuctionItem, String> entry : winners.entrySet()) {
                sb.append(String.format("• %s: %s ($%.2f)\n",
                        entry.getKey().getItemName(),
                        entry.getValue(),
                        entry.getKey().getCurrentBid()));
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Auction Winners", JOptionPane.INFORMATION_MESSAGE);
        }));

        refreshTable();
    }

    private JButton createStyledButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(background.darker(), 1, true));
        button.setPreferredSize(new Dimension(140, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (AuctionItem item : auctionService.getAllItems()) {
            Object[] row = {
                    item.getItemId(),
                    item.getItemName(),
                    String.format("%.2f", item.getCurrentBid()),
                    item.getHighestBidder(),
                    item.isActive() ? "Active" : "Closed"
            };
            tableModel.addRow(row);
        }
    }

    private void onRegister() {
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();

        Object[] fields = {
                "Username:", userField,
                "Password:", passField,
                "Mobile Phone:", phoneField,
                "Delivery Address:", addressField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Register New Bidder", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String u = userField.getText().trim();
            String p = new String(passField.getPassword()).trim();
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean success = auctionService.registerUser(u, p, phoneField.getText(), addressField.getText());
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful! You may now log in.", "Registered", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose another.", "Registration Failed", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void onLogin() {
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        Object[] fields = {
                "Username:", userField,
                "Password:", passField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Bidder Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String u = userField.getText().trim();
            String p = new String(passField.getPassword()).trim();
            Optional<User> userOpt = auctionService.login(u, p);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                statusLabel.setText("Status: Logged in as " + user.getUsername() + (user.isAdmin() ? " [ADMIN]" : ""));
                JOptionPane.showMessageDialog(this, "Welcome, " + user.getUsername() + "!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onLogout() {
        auctionService.logout();
        statusLabel.setText("Status: Guest (Not logged in)");
        JOptionPane.showMessageDialog(this, "Logged out successfully.", "Session Ended", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onPlaceBid() {
        if (!auctionService.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Please log in before placing a bid.", "Authentication Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = itemTable.getSelectedRow();
        String defaultId = selectedRow >= 0 ? itemTable.getValueAt(selectedRow, 0).toString() : "";

        String itemIdStr = JOptionPane.showInputDialog(this, "Enter Lot ID to bid on:", defaultId);
        if (itemIdStr == null || itemIdStr.trim().isEmpty()) return;

        String bidAmountStr = JOptionPane.showInputDialog(this, "Enter your bid amount ($):");
        if (bidAmountStr == null || bidAmountStr.trim().isEmpty()) return;

        try {
            int itemId = Integer.parseInt(itemIdStr.trim());
            double bid = Double.parseDouble(bidAmountStr.trim());

            auctionService.placeBid(itemId, bid);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Your bid of $" + bid + " was accepted!", "Bid Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Bid Rejected", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAddItem() {
        if (!auctionService.isAdmin()) {
            JOptionPane.showMessageDialog(this, "Admin credentials required to add lots.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField startingBidField = new JTextField("0.0");

        Object[] fields = {
                "Lot ID (Positive integer):", idField,
                "Lot Item Description:", nameField,
                "Starting Bid ($):", startingBidField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add New Auction Lot", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                double start = Double.parseDouble(startingBidField.getText().trim());

                boolean added = auctionService.addItem(new AuctionItem(id, name, start));
                if (added) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Item '" + name + "' added successfully.", "Item Added", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lot ID " + id + " already exists.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Adding Item", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onSetTimer() {
        if (!auctionService.isAdmin()) {
            JOptionPane.showMessageDialog(this, "Admin credentials required to control the timer.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String secondsStr = JOptionPane.showInputDialog(this, "Enter auction duration (in seconds):", "60");
        if (secondsStr == null || secondsStr.trim().isEmpty()) return;

        try {
            int seconds = Integer.parseInt(secondsStr.trim());
            auctionService.startAuctionTimer(seconds);
            JOptionPane.showMessageDialog(this, "Auction timer started for " + seconds + " seconds.", "Timer Initialized", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Timer Configuration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
