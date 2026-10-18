package com.busreservation.database;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Consumer;

/** Primary Java Swing application with a clean, red travel-booking inspired interface. */
public class MainFrame extends JFrame {
    private static final Color BRAND = new Color(231, 65, 75);
    private static final Color BRAND_DARK = new Color(181, 37, 48);
    private static final Color NAVY = new Color(30, 53, 91);
    private static final Color APP_BACKGROUND = new Color(244, 247, 251);
    private static final Color SURFACE = Color.WHITE;
    private static final Color FIELD = new Color(255, 255, 255);
    private static final Color TEXT = new Color(28, 39, 58);
    private static final Color MUTED = new Color(91, 105, 126);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final BusDAO busDAO = new BusDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    public MainFrame() {
        configureLookAndFeel();
        setTitle("GoBus | Bus Reservation System");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1240, 850);
        setMinimumSize(new Dimension(1000, 720));
        setLocationRelativeTo(null);
        content.setBackground(APP_BACKGROUND);
        add(content);
        content.add(createHomePanel(), "HOME");
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent event) { confirmExit(); }
        });
        showCard("HOME");
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true)); }

    private void configureLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Keep Java's default look and feel if the system theme is unavailable.
        }

        UIManager.put("OptionPane.messageFont", BODY_FONT);
        UIManager.put("OptionPane.buttonFont", BUTTON_FONT);
        UIManager.put("ComboBox.font", BODY_FONT);
        UIManager.put("Spinner.font", BODY_FONT);
        UIManager.put("ToolTip.font", new Font("Segoe UI", Font.PLAIN, 12));
        UIManager.put("ToolTip.background", FIELD);
        UIManager.put("ToolTip.foreground", TEXT);
        UIManager.put("OptionPane.background", SURFACE);
        UIManager.put("OptionPane.messageForeground", TEXT);
        UIManager.put("OptionPane.foreground", TEXT);
        UIManager.put("OptionPane.buttonBackground", FIELD);
        UIManager.put("OptionPane.buttonForeground", TEXT);
        UIManager.put("Panel.background", APP_BACKGROUND);
    }

    private void showCard(String name) { cards.show(content, name); }
    private void openScreen(String key, JPanel panel) { content.add(panel, key); showCard(key); content.revalidate(); content.repaint(); }

    private JPanel createHomePanel() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(APP_BACKGROUND);

        JPanel hero = new JPanel(new BorderLayout(20, 0));
        hero.setBackground(SURFACE);
        hero.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(53, 64, 83)), BorderFactory.createEmptyBorder(18, 44, 18, 44)));
        JLabel brand = leftLabel("goBUS", 27, true, new Color(255, 122, 129));
        hero.add(brand, BorderLayout.WEST);
        JPanel heroText = new JPanel(new GridLayout(1, 1)); heroText.setOpaque(false);
        heroText.add(leftLabel("Bus tickets made simple", 17, true, TEXT));
        hero.add(heroText, BorderLayout.CENTER);
        hero.add(secondaryButton("My bookings", () -> openScreen("BOOKINGS", createAllBookingsPanel())), BorderLayout.EAST);
        page.add(hero, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 14)); body.setOpaque(false); body.setBorder(BorderFactory.createEmptyBorder(20, 48, 18, 48));
        JPanel welcome = new JPanel(new GridLayout(2, 1, 0, 4)); welcome.setOpaque(false);
        welcome.add(leftLabel("Book bus tickets", 25, true, TEXT));
        welcome.add(leftLabel("Search routes, check seats and complete a reservation in a few clicks.", 13, false, MUTED));
        JPanel top = new JPanel(new BorderLayout(0, 12)); top.setOpaque(false); top.add(welcome, BorderLayout.NORTH); top.add(homeRouteCard(), BorderLayout.CENTER); body.add(top, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(3, 2, 12, 12)); menu.setOpaque(false);
        menu.add(menuCard("01", "Search buses", "Find a route and see live seats.", "Search routes", () -> openScreen("SEARCH", createSearchPanel())));
        menu.add(menuCard("02", "Book a ticket", "Select a bus and reserve seats.", "Book now", () -> openScreen("BOOK", createBookPanel())));
        menu.add(menuCard("03", "View booking", "Retrieve a ticket using its ID.", "View ticket", () -> openScreen("VIEW", createViewBookingPanel())));
        menu.add(menuCard("04", "Cancel booking", "Cancel safely; seats are restored.", "Cancel ticket", () -> openScreen("CANCEL", createCancelPanel())));
        menu.add(menuCard("05", "All buses", "Browse routes, timings and fare.", "Browse buses", () -> openScreen("BUSES", createAllBusesPanel())));
        menu.add(menuCard("06", "All bookings", "Review reservations in one place.", "Open bookings", () -> openScreen("BOOKINGS", createAllBookingsPanel())));
        body.add(menu, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout()); footer.setOpaque(false);
        footer.add(leftLabel("BUS RESERVATION SYSTEM", 11, true, MUTED), BorderLayout.WEST);
        body.add(footer, BorderLayout.SOUTH);
        JScrollPane homeScroll = new JScrollPane(body);
        homeScroll.setBorder(BorderFactory.createEmptyBorder());
        homeScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        homeScroll.getViewport().setBackground(APP_BACKGROUND);
        homeScroll.getVerticalScrollBar().setUnitIncrement(18);
        page.add(homeScroll, BorderLayout.CENTER);
        return page;
    }

    private JPanel homeRouteCard() {
        SurfacePanel card = new SurfacePanel(); card.setLayout(new BorderLayout(12, 8)); card.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
        card.add(leftLabel("Where would you like to travel?", 15, true, TEXT), BorderLayout.NORTH);
        JComboBox<String> source = cityComboBox(15), destination = cityComboBox(15);
        JPanel routeFields = new JPanel(new BorderLayout(10, 0)); routeFields.setOpaque(false);
        JPanel locations = new JPanel(new GridLayout(1, 2, 10, 0)); locations.setOpaque(false); locations.add(fieldBlock("FROM", source)); locations.add(fieldBlock("TO", destination));
        JButton swap = secondaryButton("Swap", () -> { String oldSource = routeValue(source); setRouteValue(source, routeValue(destination)); setRouteValue(destination, oldSource); }); swap.setToolTipText("Swap source and destination");
        routeFields.add(locations, BorderLayout.CENTER); routeFields.add(swap, BorderLayout.EAST);
        JPanel bottom = new JPanel(new BorderLayout()); bottom.setOpaque(false); bottom.add(routeFields, BorderLayout.CENTER);
        JButton search = primaryButton("Search buses  →"); search.addActionListener(event -> {
            if (routeValue(source).isEmpty() || routeValue(destination).isEmpty()) { error("Please choose or enter both source and destination."); return; }
            openScreen("SEARCH", createSearchPanel(routeValue(source), routeValue(destination)));
        }); bottom.add(search, BorderLayout.EAST);
        card.add(bottom, BorderLayout.CENTER);
        return card;
    }

    private JComboBox<String> cityComboBox(int columns) {
        String[] cities = {"", "Ahmedabad", "Ajmer", "Delhi", "Jaipur", "Mumbai", "Nashik", "Pune", "Surat"};
        JComboBox<String> combo = new JComboBox<>(cities);
        combo.setEditable(true);
        combo.setOpaque(true);
        combo.setMaximumRowCount(8);
        combo.setPreferredSize(new Dimension(Math.max(130, columns * 11), 36));
        combo.setSelectedItem("");
        styleInput(combo);
        combo.setToolTipText("Choose a city from the list or type a city name");
        return combo;
    }

    private String routeValue(JComboBox<String> combo) {
        Object value = combo.isEditable() ? combo.getEditor().getItem() : combo.getSelectedItem();
        return value == null ? "" : value.toString().trim();
    }

    private void setRouteValue(JComboBox<String> combo, String value) {
        combo.setSelectedItem(value == null ? "" : value);
        if (combo.isEditable()) combo.getEditor().setItem(value == null ? "" : value);
    }

    private JPanel fieldBlock(String title, JComponent input) {
        JPanel block = new JPanel(new BorderLayout(0, 3)); block.setOpaque(false); block.add(fieldLabel(title), BorderLayout.NORTH); block.add(input, BorderLayout.CENTER); return block;
    }

    private JPanel menuCard(String index, String title, String description, String action, Runnable callback) {
        SurfacePanel card = new SurfacePanel(); card.setLayout(new BorderLayout(0, 8)); card.setBorder(BorderFactory.createEmptyBorder(17, 18, 16, 18));
        card.setPreferredSize(new Dimension(0, 158));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); top.setOpaque(false);
        JLabel number = new JLabel(index, SwingConstants.CENTER); number.setOpaque(true); number.setBackground(new Color(255, 231, 233)); number.setForeground(BRAND_DARK); number.setFont(new Font("Segoe UI", Font.BOLD, 12)); number.setPreferredSize(new Dimension(40, 30));
        number.setBorder(BorderFactory.createLineBorder(new Color(245, 190, 195)));
        top.add(number); top.add(leftLabel(title, 16, true, TEXT)); card.add(top, BorderLayout.NORTH);
        JLabel details = new JLabel("<html><div style='width:220px;'>" + description + "</div></html>"); details.setFont(new Font("Segoe UI", Font.PLAIN, 13)); details.setForeground(MUTED); card.add(details, BorderLayout.CENTER);
        JButton button = linkButton(action + "  →"); button.setHorizontalAlignment(SwingConstants.LEFT); button.addActionListener(event -> callback.run()); card.add(button, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createSearchPanel() { return createSearchPanel("", ""); }

    private JPanel createSearchPanel(String initialSource, String initialDestination) {
        JPanel page = screen("Search buses", "Enter your route to see the buses currently available.");
        JComboBox<String> source = cityComboBox(13), destination = cityComboBox(13);
        setRouteValue(source, initialSource); setRouteValue(destination, initialDestination);
        DefaultTableModel model = model("Bus Number", "Bus Name", "Source", "Destination", "Departure", "Arrival", "Available", "Fare");
        JTable busTable = table(model);
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4)); controls.setOpaque(false);
        controls.add(fieldLabel("FROM")); controls.add(source); controls.add(fieldLabel("TO")); controls.add(destination);
        JButton search = primaryButton("Search buses");
        search.addActionListener(event -> {
            if (routeValue(source).isEmpty() || routeValue(destination).isEmpty()) { error("Please choose or enter both source and destination."); return; }
            fillBusTable(model, busDAO.searchBuses(routeValue(source), routeValue(destination)), true);
            if (model.getRowCount() == 0) info("No buses found for this route.");
        });
        controls.add(search);
        JButton reset = secondaryButton("Clear", () -> { setRouteValue(source, ""); setRouteValue(destination, ""); model.setRowCount(0); source.requestFocusInWindow(); });
        controls.add(reset);
        page.add(contentCard(controls, scroll(busTable)), BorderLayout.CENTER);
        if (!initialSource.isBlank() && !initialDestination.isBlank()) SwingUtilities.invokeLater(search::doClick);
        return page;
    }

    private JPanel createBookPanel() {
        JPanel page = screen("Book your trip", "Choose a journey date, select a bus, then complete payment securely.");
        JComboBox<String> source = cityComboBox(11), destination = cityComboBox(11);
        JTextField passenger = textField(14);
        JSpinner journeyDate = journeyDateSpinner();
        JSpinner age = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1)); styleInput(age);
        JComboBox<String> gender = new JComboBox<>(new String[]{"Male", "Female", "Other"}); styleInput(gender);
        JSpinner seats = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1)); styleInput(seats);
        JLabel total = leftLabel("₹0.00", 18, true, Color.WHITE);
        DefaultTableModel model = model("Internal ID", "Bus Number", "Bus Name", "Departure", "Arrival", "Available", "Fare");
        JTable buses = table(model);
        hideColumn(buses, 0);
        buses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        buses.setToolTipText("Select a bus row to calculate the total fare.");
        // Default table renderer keeps available seats as a plain number; fare is shown in the final column.
        JLabel selectedBus = leftLabel("Select a bus to see its booking summary.", 12, false, MUTED);
        Runnable calculate = () -> { int viewRow = buses.getSelectedRow(); if (viewRow >= 0) { int row = buses.convertRowIndexToModel(viewRow); double fare = ((Number) model.getValueAt(row, 6)).doubleValue(); total.setText(String.format("₹%.2f", fare * (Integer) seats.getValue())); selectedBus.setText(model.getValueAt(row, 2) + "  •  " + model.getValueAt(row, 3) + " to " + model.getValueAt(row, 4)); } else { total.setText("₹0.00"); selectedBus.setText("Select a bus to see its booking summary."); } };
        ChangeListener listener = event -> calculate.run(); seats.addChangeListener(listener);
        buses.getSelectionModel().addListSelectionListener(event -> { if (!event.getValueIsAdjusting()) calculate.run(); });

        JPanel route = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5)); route.setOpaque(false);
        route.add(fieldLabel("FROM")); route.add(source); route.add(fieldLabel("TO")); route.add(destination); route.add(fieldLabel("JOURNEY DATE")); route.add(journeyDate);
        JButton search = primaryButton("Find buses");
        search.addActionListener(event -> {
            String from = routeValue(source);
            String to = routeValue(destination);
            if (from.isEmpty() && to.isEmpty()) {
                fillBookingBusTable(model, busDAO.getAllBuses());
            } else if (from.isEmpty() || to.isEmpty()) {
                error("Please choose both FROM and TO, or leave both empty to view all buses.");
                return;
            } else if (from.equalsIgnoreCase(to)) {
                error("FROM and TO cities must be different.");
                return;
            } else {
                fillBookingBusTable(model, busDAO.searchBuses(from, to));
            }
            calculate.run();
            if (model.getRowCount() == 0) info("No buses found for this route. Try another city pair.");
        });
        route.add(search);
        route.add(secondaryButton("Clear", () -> { setRouteValue(source, ""); setRouteValue(destination, ""); model.setRowCount(0); calculate.run(); source.requestFocusInWindow(); }));

        JPanel passengerFields = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5)); passengerFields.setOpaque(false);
        passengerFields.add(fieldLabel("PASSENGER")); passengerFields.add(passenger); passengerFields.add(fieldLabel("AGE")); passengerFields.add(age);
        passengerFields.add(fieldLabel("GENDER")); passengerFields.add(gender); passengerFields.add(fieldLabel("SEATS")); passengerFields.add(seats);
        JPanel fareBox = new JPanel(new GridLayout(2, 1, 0, 3)); fareBox.setBackground(new Color(255, 239, 240));
        fareBox.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(245, 190, 195)), BorderFactory.createEmptyBorder(7, 13, 7, 13)));
        total.setForeground(BRAND_DARK);
        fareBox.add(leftLabel("TOTAL FARE", 10, true, MUTED)); fareBox.add(total);
        JButton book = primaryButton("Continue to payment");
        book.addActionListener(event -> {
            int viewRow = buses.getSelectedRow();
            if (viewRow < 0) { error("Please select a bus before booking."); return; }
            int row = buses.convertRowIndexToModel(viewRow);
            if (passenger.getText().trim().isEmpty()) { error("Passenger name is required."); return; }
            int selectedSeats = (Integer) seats.getValue(); int available = ((Number) model.getValueAt(row, 5)).intValue();
            if (selectedSeats > available) { error("Only " + available + " seat(s) are available on this bus."); return; }
            int busId = ((Number) model.getValueAt(row, 0)).intValue();
            double totalAmount = ((Number) model.getValueAt(row, 6)).doubleValue() * selectedSeats;
            String travelDate = new SimpleDateFormat("dd MMM yyyy").format((Date) journeyDate.getValue());
            showPaymentDialog(totalAmount, travelDate, paymentMethod -> {
                if (bookingDAO.bookTicket(busId, passenger.getText(), (Integer) age.getValue(), (String) gender.getSelectedItem(), selectedSeats)) {
                    info(String.format("BOOKING CONFIRMED%n%nBooking ID: %d%nPassenger: %s%nJourney Date: %s%nPayment: %s%nSeats: %d%nTotal Fare: %s", bookingDAO.getLastBookingId(), passenger.getText().trim(), travelDate, paymentMethod, selectedSeats, total.getText()));
                    if (routeValue(source).isBlank() && routeValue(destination).isBlank()) {
                        fillBookingBusTable(model, busDAO.getAllBuses());
                    } else {
                        fillBookingBusTable(model, busDAO.searchBuses(routeValue(source), routeValue(destination)));
                    }
                    calculate.run();
                } else error("Booking could not be completed. Check the database connection and availability.");
            });
        });
        JPanel bookingActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); bookingActions.setOpaque(false); bookingActions.add(fareBox); bookingActions.add(book);

        JScrollPane busScroll = scroll(buses); busScroll.setPreferredSize(new Dimension(0, 235));
        JPanel body = new JPanel(new BorderLayout(10, 10)); body.setOpaque(false); body.add(route, BorderLayout.NORTH); body.add(busScroll, BorderLayout.CENTER);
        JPanel bookingSection = new JPanel(new BorderLayout(0, 4)); bookingSection.setOpaque(false); bookingSection.setPreferredSize(new Dimension(0, 145)); bookingSection.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(176, 190, 209), 1), " Passenger details ", 0, 0, new Font("Segoe UI", Font.BOLD, 12), TEXT));
        selectedBus.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        JPanel passengerRows = new JPanel(new GridLayout(2, 1, 0, 2)); passengerRows.setOpaque(false); passengerRows.add(passengerFields); passengerRows.add(bookingActions);
        bookingSection.add(selectedBus, BorderLayout.NORTH); bookingSection.add(passengerRows, BorderLayout.CENTER); body.add(bookingSection, BorderLayout.SOUTH);
        page.add(contentCard(body), BorderLayout.CENTER);
        fillBookingBusTable(model, busDAO.getAllBuses());
        return page;
    }

    private JPanel createViewBookingPanel() {
        JPanel page = screen("View booking", "Use a booking ID to retrieve a complete ticket summary.");
        JTextField id = textField(12); JTextArea result = new JTextArea(); result.setEditable(false); result.setFont(new Font("Segoe UI", Font.PLAIN, 15)); result.setForeground(TEXT); result.setBackground(SURFACE); result.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4)); controls.setOpaque(false); controls.add(fieldLabel("BOOKING ID")); controls.add(id);
        JButton search = primaryButton("View ticket"); search.addActionListener(event -> { Integer bookingId = positiveId(id.getText()); if (bookingId != null) result.setText(bookingDAO.viewBooking(bookingId)); }); controls.add(search);
        page.add(contentCard(controls, scroll(result)), BorderLayout.CENTER); return page;
    }

    private JPanel createCancelPanel() {
        JPanel page = screen("Cancel booking", "Cancel only when needed. Seats are restored automatically after cancellation.");
        SurfacePanel box = new SurfacePanel(); box.setLayout(new GridLayout(5, 1, 0, 10)); box.setBorder(BorderFactory.createEmptyBorder(28, 120, 28, 120));
        JLabel icon = new JLabel("×", SwingConstants.CENTER); icon.setFont(new Font("Segoe UI", Font.BOLD, 36)); icon.setForeground(BRAND); box.add(icon);
        box.add(label("Cancel a ticket", 20, true, TEXT)); box.add(label("Enter the booking ID below to continue.", 13, false, MUTED));
        JTextField id = textField(16); JPanel input = new JPanel(new FlowLayout(FlowLayout.CENTER)); input.setOpaque(false); input.add(id); box.add(input);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); actions.setOpaque(false); JButton cancel = dangerButton("Cancel booking"); cancel.addActionListener(event -> {
            Integer bookingId = positiveId(id.getText()); if (bookingId == null) return;
            if (confirmStyled("Confirm cancellation", "Are you sure you want to cancel Booking ID " + bookingId + "?")) {
                if (bookingDAO.cancelBooking(bookingId)) info("Booking ID " + bookingId + " has been cancelled successfully.\nThe booked seats have been restored."); else error("Cancellation failed. Please check the Booking ID.");
            }
        }); actions.add(cancel); actions.add(secondaryButton("Keep booking", () -> showCard("HOME"))); box.add(actions);
        JPanel holder = new JPanel(new GridLayout(1, 1)); holder.setOpaque(false); holder.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120)); holder.add(box); page.add(holder, BorderLayout.CENTER); return page;
    }

    private JPanel createAllBusesPanel() {
        JPanel page = screen("All available buses", "Live availability and fare details for every registered bus.");
        DefaultTableModel model = model("Bus Number", "Bus Name", "Source", "Destination", "Departure", "Arrival", "Total Seats", "Available", "Fare");
        fillBusTable(model, busDAO.getAllBuses(), false); page.add(contentCard(scroll(table(model))), BorderLayout.CENTER); return page;
    }

    private JPanel createAllBookingsPanel() {
        JPanel page = screen("All bookings", "A complete list of ticket reservations stored in the system.");
        DefaultTableModel model = model("Booking ID", "Passenger", "Age", "Gender", "Bus Name", "Source", "Destination", "Seats", "Total Fare", "Booking Date");
        for (Object[] row : bookingDAO.getAllBookingsForGUI()) model.addRow(row);
        page.add(contentCard(scroll(table(model))), BorderLayout.CENTER);
        if (model.getRowCount() == 0) SwingUtilities.invokeLater(() -> info("No bookings found yet."));
        return page;
    }

    private JPanel screen(String heading, String subheading) {
        JPanel page = new JPanel(new BorderLayout(0, 16)); page.setBackground(APP_BACKGROUND); page.setBorder(BorderFactory.createEmptyBorder(24, 40, 28, 40));
        JPanel header = new JPanel(new BorderLayout(12, 0)); header.setOpaque(false);
        JPanel headingBox = new JPanel(new GridLayout(2, 1, 0, 3)); headingBox.setOpaque(false); headingBox.add(leftLabel(heading, 25, true, TEXT)); headingBox.add(leftLabel(subheading, 13, false, MUTED)); header.add(headingBox, BorderLayout.CENTER);
        header.add(secondaryButton("← Dashboard", () -> showCard("HOME")), BorderLayout.WEST); page.add(header, BorderLayout.NORTH);
        return page;
    }

    private JPanel contentCard(Component... parts) {
        SurfacePanel card = new SurfacePanel(); card.setLayout(new BorderLayout(10, 10)); card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        if (parts.length == 1) card.add(parts[0], BorderLayout.CENTER);
        if (parts.length == 2) { card.add(parts[0], BorderLayout.NORTH); card.add(parts[1], BorderLayout.CENTER); }
        return card;
    }

    private JSpinner journeyDateSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd MMM yyyy"));
        spinner.setPreferredSize(new Dimension(126, 34));
        styleInput(spinner);
        return spinner;
    }

    /** Demo-only checkout: it records no financial details and does not contact a payment provider. */
    private void showPaymentDialog(double amount, String journeyDate, Consumer<String> onPayment) {
        JDialog dialog = new JDialog(this, "Demo payment", true);
        JPanel page = new JPanel(new BorderLayout(0, 14)); page.setBackground(APP_BACKGROUND); page.setBorder(BorderFactory.createEmptyBorder(20, 24, 22, 24));
        JPanel heading = new JPanel(new GridLayout(2, 1, 0, 4)); heading.setOpaque(false);
        heading.add(leftLabel("Choose a payment method", 21, true, TEXT));
        heading.add(leftLabel("Journey date: " + journeyDate + "  •  Total: ₹" + String.format("%.2f", amount), 13, false, MUTED));
        page.add(heading, BorderLayout.NORTH);

        SurfacePanel methods = new SurfacePanel(); methods.setLayout(new GridLayout(3, 1, 0, 8)); methods.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        ButtonGroup group = new ButtonGroup();
        JRadioButton upi = paymentOption("UPI", "Pay using any UPI application", true);
        JRadioButton card = paymentOption("Credit / Debit Card", "Secure demo card checkout", false);
        JRadioButton wallet = paymentOption("GoBus Wallet", "Demo wallet balance: ₹2,000", false);
        group.add(upi); group.add(card); group.add(wallet); methods.add(upi); methods.add(card); methods.add(wallet);
        page.add(methods, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setOpaque(false);
        JLabel note = leftLabel("Demo payment only — no real money is charged.", 11, false, MUTED);
        bottom.add(note, BorderLayout.NORTH);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton back = secondaryButton("Back", dialog::dispose);
        JButton pay = primaryButton("Pay ₹" + String.format("%.2f", amount) + " & confirm");
        pay.setToolTipText("Confirm this demo payment and create the booking.");
        pay.addActionListener(event -> {
            String method = upi.isSelected() ? "UPI"
                    : card.isSelected() ? "Credit / Debit Card" : "GoBus Wallet";
            dialog.dispose();
            onPayment.accept(method);
        });
        actions.add(back);
        actions.add(pay);
        bottom.add(actions, BorderLayout.CENTER);
        page.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(page);
        dialog.getRootPane().setDefaultButton(pay);
        dialog.getRootPane().registerKeyboardAction(
                event -> dialog.dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.setSize(500, 440);
        dialog.setMinimumSize(new Dimension(500, 440));
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JRadioButton paymentOption(String name, String detail, boolean selected) {
        JRadioButton option = new JRadioButton("<html><b>" + name + "</b><br><span style='color:#9faabd'>" + detail + "</span></html>", selected);
        option.setFont(BODY_FONT); option.setForeground(TEXT); option.setBackground(FIELD); option.setFocusPainted(false); option.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(75, 89, 114)), BorderFactory.createEmptyBorder(9, 12, 9, 12)));
        return option;
    }

    private JTextField textField(int columns) { JTextField field = new JTextField(columns); styleInput(field); return field; }
    private void styleInput(Component component) {
        component.setFont(BODY_FONT); component.setForeground(TEXT); component.setBackground(FIELD);
        if (component instanceof JComponent swingComponent) swingComponent.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(74, 88, 112)), BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        if (component instanceof JSpinner spinner && spinner.getEditor() instanceof JSpinner.DefaultEditor editor) {
            editor.getTextField().setForeground(TEXT); editor.getTextField().setBackground(FIELD); editor.getTextField().setCaretColor(TEXT);
        }
        if (component instanceof JComboBox<?> comboBox) {
            if (comboBox.isEditable() && comboBox.getEditor().getEditorComponent() instanceof JTextField editorField) {
                editorField.setForeground(TEXT);
                editorField.setBackground(FIELD);
                editorField.setCaretColor(TEXT);
                editorField.setSelectionColor(new Color(255, 224, 227));
                editorField.setSelectedTextColor(Color.WHITE);
                editorField.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            }
            comboBox.setRenderer(new DefaultListCellRenderer() {
                @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean focus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, selected, focus);
                    label.setBackground(selected ? new Color(255, 224, 227) : Color.WHITE); label.setForeground(TEXT); return label;
                }
            });
        }
    }
    private JLabel fieldLabel(String value) { JLabel label = new JLabel(value); label.setFont(new Font("Segoe UI", Font.BOLD, 11)); label.setForeground(MUTED); return label; }
    private JLabel label(String value, int size, boolean bold, Color color) { JLabel label = new JLabel(value, SwingConstants.CENTER); label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size)); label.setForeground(color); return label; }
    private JLabel leftLabel(String value, int size, boolean bold, Color color) { JLabel label = new JLabel(value); label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size)); label.setForeground(color); return label; }

    private JButton primaryButton(String text) { return button(text, BRAND, Color.WHITE, BRAND); }
    private JButton dangerButton(String text) { return button(text, BRAND_DARK, Color.WHITE, BRAND_DARK); }
    private JButton secondaryButton(String text, Runnable action) { JButton button = button(text, FIELD, BRAND_DARK, new Color(196, 207, 222)); button.addActionListener(event -> action.run()); return button; }
    private JButton linkButton(String text) { JButton button = new JButton(text); button.setFont(new Font("Segoe UI", Font.BOLD, 12)); button.setForeground(BRAND); button.setContentAreaFilled(false); button.setBorderPainted(false); button.setFocusPainted(false); button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); return button; }
    private JButton button(String text, Color background, Color foreground, Color border) { RoundedButton button = new RoundedButton(text, background, border); button.setFont(BUTTON_FONT); button.setForeground(foreground); button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); return button; }

    private DefaultTableModel model(String... columns) { return new DefaultTableModel(columns, 0) { @Override public boolean isCellEditable(int row, int column) { return false; } }; }
    private JTable table(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(BODY_FONT);
        table.setForeground(TEXT);
        table.setBackground(SURFACE);
        table.setGridColor(new Color(49, 62, 84));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(255, 224, 227));
        table.setSelectionForeground(TEXT);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setShowHorizontalLines(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(BRAND_DARK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setPreferredSize(new Dimension(0, 42));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable currentTable, Object value,
                                                           boolean selected, boolean focused, int row, int column) {
                JLabel header = (JLabel) super.getTableCellRendererComponent(
                        currentTable, value, selected, focused, row, column);
                header.setOpaque(true);
                header.setBackground(BRAND_DARK);
                header.setForeground(Color.WHITE);
                header.setFont(new Font("Segoe UI", Font.BOLD, 13));
                header.setHorizontalAlignment(SwingConstants.LEFT);
                header.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, BRAND),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)));
                return header;
            }
        });
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() { @Override public Component getTableCellRendererComponent(JTable currentTable, Object value, boolean selected, boolean focused, int row, int column) { Component cell = super.getTableCellRendererComponent(currentTable, value, selected, focused, row, column); cell.setBackground(selected ? new Color(255, 224, 227) : (row % 2 == 0 ? SURFACE : new Color(246, 249, 253))); cell.setForeground(TEXT); return cell; } }; renderer.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8)); table.setDefaultRenderer(Object.class, renderer);
        return table;
    }
    private JScrollPane scroll(Component component) {
        JScrollPane pane = new JScrollPane(component);
        pane.setBorder(BorderFactory.createLineBorder(new Color(53, 67, 91)));
        pane.getViewport().setBackground(SURFACE);
        pane.getVerticalScrollBar().setUnitIncrement(16);
        pane.getHorizontalScrollBar().setUnitIncrement(16);
        return pane;
    }

    private void hideColumn(JTable table, int index) { table.removeColumn(table.getColumnModel().getColumn(index)); }

    private void fillBusTable(DefaultTableModel model, ArrayList<Bus> buses, boolean searchTable) { model.setRowCount(0); for (Bus bus : buses) { if (searchTable) model.addRow(new Object[]{bus.getBusNumber(), bus.getBusName(), bus.getSource(), bus.getDestination(), bus.getDepartureTime(), bus.getArrivalTime(), bus.getAvailableSeats(), "₹" + String.format("%.2f", bus.getFare())}); else model.addRow(new Object[]{bus.getBusNumber(), bus.getBusName(), bus.getSource(), bus.getDestination(), bus.getDepartureTime(), bus.getArrivalTime(), bus.getTotalSeats(), bus.getAvailableSeats(), "₹" + String.format("%.2f", bus.getFare())}); } }
    private void fillBookingBusTable(DefaultTableModel model, ArrayList<Bus> buses) { model.setRowCount(0); for (Bus bus : buses) model.addRow(new Object[]{bus.getBusId(), bus.getBusNumber(), bus.getBusName(), bus.getDepartureTime(), bus.getArrivalTime(), bus.getAvailableSeats(), bus.getFare()}); }
    private Integer positiveId(String text) { try { int id = Integer.parseInt(text.trim()); if (id <= 0) throw new NumberFormatException(); return id; } catch (NumberFormatException exception) { error("Booking ID must be a positive number."); return null; } }
    private void info(String message) { showStyledMessage("GoBus", message, false); }
    private void error(String message) { showStyledMessage("GoBus", message, true); }

    private void showStyledMessage(String title, String message, boolean isError) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(74, 88, 112)),
                BorderFactory.createEmptyBorder(20, 22, 18, 22)));

        JLabel icon = new JLabel(isError ? "!" : "✓", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI", Font.BOLD, 24));
        icon.setForeground(Color.WHITE);
        icon.setOpaque(true);
        icon.setBackground(isError ? BRAND_DARK : new Color(28, 125, 91));
        icon.setPreferredSize(new Dimension(42, 42));
        panel.add(icon, BorderLayout.WEST);

        JTextArea messageText = new JTextArea(message);
        messageText.setEditable(false);
        messageText.setFocusable(false);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageText.setForeground(TEXT);
        messageText.setBackground(SURFACE);
        messageText.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        messageText.setColumns(30);
        messageText.setRows(Math.max(2, Math.min(8, message.split("\\n").length + 1)));
        panel.add(messageText, BorderLayout.CENTER);

        JButton ok = primaryButton("OK");
        ok.addActionListener(event -> dialog.dispose());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setOpaque(false);
        actions.add(ok);
        panel.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(360, dialog.getHeight()));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private void confirmExit() {
        if (confirmStyled("Exit GoBus", "Are you sure you want to exit?")) dispose();
    }

    private boolean confirmStyled(String title, String message) {
        final boolean[] confirmed = {false};
        JDialog dialog = new JDialog(this, title, true);
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(74, 88, 112)),
                BorderFactory.createEmptyBorder(20, 22, 18, 22)));
        JLabel text = leftLabel(message, 14, false, TEXT);
        panel.add(text, BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton no = secondaryButton("Keep", dialog::dispose);
        JButton yes = dangerButton("Yes, continue");
        yes.addActionListener(event -> { confirmed[0] = true; dialog.dispose(); });
        actions.add(no);
        actions.add(yes);
        panel.add(actions, BorderLayout.SOUTH);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(390, dialog.getHeight()));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        return confirmed[0];
    }

    private static class SurfacePanel extends JPanel {
        private static final Color OUTLINE = new Color(150, 166, 188);
        SurfacePanel() { setOpaque(false); }
        @Override protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(SURFACE);
            g.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);
            g.setColor(OUTLINE);
            g.setStroke(new BasicStroke(1.35f));
            g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);
            g.dispose();
        }
    }

    private static class RoundedButton extends JButton {
        private final Color background; private final Color border;
        RoundedButton(String text, Color background, Color border) { super(text); this.background = background; this.border = border; setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false); setOpaque(false); setBorder(BorderFactory.createEmptyBorder(9, 15, 9, 15)); }
        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fill = getModel().isPressed() ? background.darker()
                    : getModel().isRollover() ? background.brighter() : background;
            g.setColor(fill);
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            g.setColor(border);
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            g.dispose();
            super.paintComponent(graphics);
        }
    }
}
