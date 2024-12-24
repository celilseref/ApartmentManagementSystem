package com.apartment;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private ApartmentManager apartmentManager;

    public Main() {
        // ApartmentManager singleton instance'ını al
        apartmentManager = ApartmentManager.getInstance();

        // Frame ayarları
        setTitle("Apartman Yönetim Sistemi");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu Bar oluştur
        setJMenuBar(createMenuBar());

        // Ana layout
        setLayout(new BorderLayout());

        // Sol menü paneli
        add(createLeftPanel(), BorderLayout.WEST);

        // Ana içerik paneli
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Apartman Yönetim Sistemine Hoşgeldiniz", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(welcomeLabel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Dosya Menüsü
        JMenu fileMenu = new JMenu("Dosya");
        JMenuItem exitItem = new JMenuItem("Çıkış");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Yönetim Menüsü
        JMenu managementMenu = new JMenu("Yönetim");
        JMenuItem residentsItem = new JMenuItem("Sakinler");
        JMenuItem paymentsItem = new JMenuItem("Ödemeler");
        JMenuItem announcementsItem = new JMenuItem("Duyurular");
        announcementsItem.addActionListener(e -> showAnnouncementsPanel());

        managementMenu.add(residentsItem);
        managementMenu.add(paymentsItem);
        managementMenu.add(announcementsItem);

        // Yardım Menüsü
        JMenu helpMenu = new JMenu("Yardım");
        JMenuItem aboutItem = new JMenuItem("Hakkında");
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(managementMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.setPreferredSize(new Dimension(150, getHeight()));
        leftPanel.setBackground(new Color(240, 240, 240));

        // Menü butonları
        JButton residentsButton = new JButton("Sakinler");
        JButton paymentsButton = new JButton("Ödemeler");
        JButton duesButton = new JButton("Aidatlar");
        JButton announcementsButton = new JButton("Duyurular");

        // Butonların genişliklerini ayarla
        Dimension buttonSize = new Dimension(130, 30);
        residentsButton.setMaximumSize(buttonSize);
        paymentsButton.setMaximumSize(buttonSize);
        duesButton.setMaximumSize(buttonSize);
        announcementsButton.setMaximumSize(buttonSize);

        // Buton olayları
        residentsButton.addActionListener(e -> showResidentsPanel());
        paymentsButton.addActionListener(e -> showPaymentsPanel());
        duesButton.addActionListener(e -> showDuesPanel());
        announcementsButton.addActionListener(e -> showAnnouncementsPanel());

        // Butonları panele ekle
        leftPanel.add(residentsButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(paymentsButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(duesButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(announcementsButton);

        return leftPanel;
    }

    // Panel gösterme metodları
    private void showResidentsPanel() {
        JPanel contentPanel = (JPanel) getContentPane().getComponent(1); // Ana içerik paneli
        contentPanel.removeAll();
        contentPanel.add(new ResidentsPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showPaymentsPanel() {
        JPanel contentPanel = (JPanel) getContentPane().getComponent(1); // Ana içerik paneli
        contentPanel.removeAll();
        contentPanel.add(new PaymentsPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showDuesPanel() {
        JPanel contentPanel = (JPanel) getContentPane().getComponent(1);
        contentPanel.removeAll();
        contentPanel.add(new DuesPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAnnouncementsPanel() {
        JPanel contentPanel = (JPanel) getContentPane().getComponent(1);
        contentPanel.removeAll();
        contentPanel.add(new AnnouncementsPanel(), BorderLayout.CENTER);  // Yorum satırını kaldırdık
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    public static void main(String[] args) {
        // Swing uygulamasını Event Dispatch Thread'de çalıştır
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}