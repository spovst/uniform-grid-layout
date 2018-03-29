package org.spovst.swing.demo;

import org.spovst.swing.layout.UniformGridLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class UniformGridLayoutDemo extends JFrame implements ChangeListener, ActionListener {
    private ResizeDialog resizeDialog;

    private PopupListener componentPopupListener;
    private PopupListener layoutPopupListener;

    private JSpinner topInsetSpinner;
    private JSpinner bottomInsetSpinner;
    private JSpinner leftInsetSpinner;
    private JSpinner rightInsetSpinner;

    private JCheckBox hgapCheckBox;
    private JSpinner hgapSpinner;
    private JSpinner vgapSpinner;

    private JSpinner fixedWidthSpinner;
    private JSpinner fixedHeightSpinner;

    private Random rng = new Random();
    private JSpinner randomMinWidthSpinner;
    private JSpinner randomMaxWidthSpinner;
    private JSpinner randomMinHeightSpinner;
    private JSpinner randomMaxHeightSpinner;

    private UniformGridLayout layout;
    private JPanel layoutPanel;

    public UniformGridLayoutDemo() {
        resizeDialog = new ResizeDialog();
        componentPopupListener = new PopupListener(createComponentPopupMenu());
        layoutPopupListener = new PopupListener(createLayoutPopupMenu());
        setContentPane(createContentPane());
    }

    private JPopupMenu createComponentPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JMenuItem removeMenuItem = new JMenuItem(new RemoveAction(popupMenu));
        JMenuItem resizeMenuItem = new JMenuItem(new ResizeAction(popupMenu));
        JMenuItem hideMenuItem = new JMenuItem(new HideAction(popupMenu));

        popupMenu.add(removeMenuItem);
        popupMenu.add(resizeMenuItem);
        popupMenu.add(hideMenuItem);

        return popupMenu;
    }

    private JPopupMenu createLayoutPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JMenuItem showAllMenuItem = new JMenuItem(new ShowAllAction(popupMenu));

        popupMenu.add(showAllMenuItem);

        return popupMenu;
    }

    private JPanel createContentPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel controlPanel = createControlPanel();
        controlPanel.setBorder(BorderFactory.createTitledBorder("Control"));

        layout = createLayout();
        layoutPanel = new JPanel(layout);
        layoutPanel.addMouseListener(layoutPopupListener);
        JScrollPane layoutScrollPane = new JScrollPane(layoutPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        layoutScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        JPanel layoutPanelWrapper = new JPanel(new BorderLayout());
        layoutPanelWrapper.setBorder(BorderFactory.createTitledBorder("Layout"));
        layoutPanelWrapper.add(layoutScrollPane, BorderLayout.CENTER);

        panel.add(controlPanel);
        panel.add(layoutPanelWrapper);

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 0));

        JPanel insetsPanel = createInsetsPanel();
        JPanel gapPanel = createGapPanel();
        JTabbedPane componentPanel = createComponentPanel();

        insetsPanel.setBorder(BorderFactory.createTitledBorder("Insets"));
        gapPanel.setBorder(BorderFactory.createTitledBorder("Gap"));
        componentPanel.setBorder(BorderFactory.createTitledBorder("Component"));

        panel.add(insetsPanel);
        panel.add(gapPanel);
        panel.add(componentPanel);

        return panel;
    }

    private JPanel createInsetsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel topInsetLabel = new JLabel("Top:");
        topInsetSpinner = createSpinner(6);

        JLabel bottomInsetLabel = new JLabel("Bottom:");
        bottomInsetSpinner = createSpinner(6);

        JLabel leftInsetLabel = new JLabel("Left:");
        leftInsetSpinner = createSpinner(6);

        JLabel rightInsetLabel = new JLabel("Right:");
        rightInsetSpinner = createSpinner(6);

        topInsetSpinner.addChangeListener(this);
        bottomInsetSpinner.addChangeListener(this);
        leftInsetSpinner.addChangeListener(this);
        rightInsetSpinner.addChangeListener(this);

        GridBagConstraints c = new GridBagConstraints(
                0, 0,
                1, 1,
                0.0f, 0.0f,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(6, 6, 6, 6),
                0, 0
        );

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(topInsetLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(topInsetSpinner, c);

        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(bottomInsetLabel, c);

        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 0.5f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(bottomInsetSpinner, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(leftInsetLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(leftInsetSpinner, c);

        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(rightInsetLabel, c);

        c.gridx = 3;
        c.gridy = 1;
        c.weightx = 0.5f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(rightInsetSpinner, c);

        return panel;
    }

    private JPanel createGapPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        hgapCheckBox = new JCheckBox("", true);
        hgapCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hgapSpinner.setEnabled(hgapCheckBox.isSelected());
            }
        });
        hgapCheckBox.addActionListener(this);

        JLabel hgapLabel = new JLabel("Horizontal:");
        hgapSpinner = createSpinner(5);

        JLabel vgapLabel = new JLabel("Vertical:");
        vgapSpinner = createSpinner(5);

        hgapSpinner.addChangeListener(this);
        vgapSpinner.addChangeListener(this);

        GridBagConstraints c = new GridBagConstraints(
                0, 0,
                1, 1,
                0.0f, 0.0f,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(6, 6, 6, 6),
                0, 0
        );

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        panel.add(hgapCheckBox, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(hgapLabel, c);

        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 1.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(hgapSpinner, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(vgapLabel, c);

        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 1.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(vgapSpinner, c);

        return panel;
    }

    private JTabbedPane createComponentPanel() {
        JTabbedPane panel = new JTabbedPane();

        JPanel fixedPanel = createFixedComponentPanel();
        JPanel randomPanel = createRandomComponentPanel();

        panel.addTab("Fixed", fixedPanel);
        panel.addTab("Random", randomPanel);

        return panel;
    }

    private JPanel createFixedComponentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel widthLabel = new JLabel("Width:");
        fixedWidthSpinner = createSpinner(50);

        JLabel heightLabel = new JLabel("Height:");
        fixedHeightSpinner = createSpinner(50);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int width = (int) fixedWidthSpinner.getValue();
                int height = (int) fixedHeightSpinner.getValue();
                addComponent(width, height);
            }
        });

        GridBagConstraints c = new GridBagConstraints(
                0, 0,
                1, 1,
                0.0f, 0.0f,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(6, 6, 6, 6),
                0, 0
        );

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(widthLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(fixedWidthSpinner, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(heightLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(fixedHeightSpinner, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 1.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(addButton, c);

        return panel;
    }

    private JPanel createRandomComponentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel minWidthLabel = new JLabel("Min width:");
        randomMinWidthSpinner = createSpinner(20);

        JLabel maxWidthLabel = new JLabel("Max width:");
        randomMaxWidthSpinner = createSpinner(200);

        JLabel minHeightLabel = new JLabel("Min height:");
        randomMinHeightSpinner = createSpinner(20);

        JLabel maxHeightLabel = new JLabel("Max height:");
        randomMaxHeightSpinner = createSpinner(200);

        randomMinWidthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) randomMinWidthSpinner.getValue();
                int maxValue = (int) randomMaxWidthSpinner.getValue();
                if (value > maxValue) {
                    randomMinWidthSpinner.setValue(maxValue);
                }
            }
        });

        randomMaxWidthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) randomMaxWidthSpinner.getValue();
                int minValue = (int) randomMinWidthSpinner.getValue();
                if (value < minValue) {
                    randomMaxWidthSpinner.setValue(minValue);
                }
            }
        });

        randomMinHeightSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) randomMinHeightSpinner.getValue();
                int maxValue = (int) randomMaxHeightSpinner.getValue();
                if (value > maxValue) {
                    randomMinHeightSpinner.setValue(maxValue);
                }
            }
        });

        randomMaxHeightSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) randomMaxHeightSpinner.getValue();
                int minValue = (int) randomMinHeightSpinner.getValue();
                if (value < minValue) {
                    randomMaxHeightSpinner.setValue(minValue);
                }
            }
        });

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int minWidth = (int) randomMinWidthSpinner.getValue();
                int maxWidth = (int) randomMaxWidthSpinner.getValue();
                int minHeight = (int) randomMinHeightSpinner.getValue();
                int maxHeight = (int) randomMaxHeightSpinner.getValue();
                int width = minWidth + rng.nextInt(maxWidth - minWidth + 1);
                int height = minHeight + rng.nextInt(maxHeight - minHeight + 1);
                addComponent(width, height);
            }
        });

        GridBagConstraints c = new GridBagConstraints(
                0, 0,
                1, 1,
                0.0f, 0.0f,
                GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(6, 6, 6, 6),
                0, 0
        );

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(minWidthLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(randomMinWidthSpinner, c);

        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(maxWidthLabel, c);

        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 0.5f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(randomMaxWidthSpinner, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(minHeightLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(randomMinHeightSpinner, c);

        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(maxHeightLabel, c);

        c.gridx = 3;
        c.gridy = 1;
        c.weightx = 0.5f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        panel.add(randomMaxHeightSpinner, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 4;
        c.weightx = 1.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(addButton, c);

        return panel;
    }

    private JSpinner createSpinner(int value) {
        SpinnerNumberModel model = new SpinnerNumberModel(value, 0, Integer.MAX_VALUE, 1);
        JSpinner spinner = new JSpinner(model);
        Dimension size = spinner.getPreferredSize();
        size.width = spinner.getMaximumSize().width;
        spinner.setMaximumSize(size);
        return spinner;
    }

    private void addComponent(int width, int height) {
        JPanel component = new JPanel();
        component.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        component.setPreferredSize(new Dimension(width, height));
        component.addMouseListener(componentPopupListener);
        layoutPanel.add(component);
        layoutPanel.revalidate();
        layoutPanel.repaint();
    }

    private UniformGridLayout createLayout() {
        int hgap = hgapCheckBox.isSelected() ? ((int) hgapSpinner.getValue()) : -1;
        int vgap = (int) vgapSpinner.getValue();
        int top = (int) topInsetSpinner.getValue();
        int left = (int) leftInsetSpinner.getValue();
        int bottom = (int) bottomInsetSpinner.getValue();
        int right = (int) rightInsetSpinner.getValue();
        return new UniformGridLayout(hgap, vgap, top, left, bottom, right);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == topInsetSpinner || source == bottomInsetSpinner || source == leftInsetSpinner || source == rightInsetSpinner) {
            layout.setInsets(
                    (int) topInsetSpinner.getValue(),
                    (int) leftInsetSpinner.getValue(),
                    (int) rightInsetSpinner.getValue(),
                    (int) bottomInsetSpinner.getValue());
            layoutPanel.revalidate();
            layoutPanel.repaint();
        } else if (source == hgapSpinner) {
            if (hgapCheckBox.isSelected()) {
                layout.setHgap((int) hgapSpinner.getValue());
                layoutPanel.revalidate();
                layoutPanel.repaint();
            }
        } else if (source == vgapSpinner) {
            layout.setVgap((int) vgapSpinner.getValue());
            layoutPanel.revalidate();
            layoutPanel.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == hgapCheckBox) {
            if (hgapCheckBox.isSelected()) {
                layout.setHgap((int) hgapSpinner.getValue());
                layoutPanel.revalidate();
                layoutPanel.repaint();
            } else {
                layout.setHgap(UniformGridLayout.DYNAMIC_HGAP);
                layoutPanel.revalidate();
                layoutPanel.repaint();
            }
        }
    }

    private static class ResizeDialog {
        private JLabel widthLabel = new JLabel("Width:");
        private SpinnerNumberModel widthModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        private JSpinner widthSpinner = new JSpinner(widthModel);
        private JLabel heightLabel = new JLabel("Height:");
        private SpinnerNumberModel heightModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        private JSpinner heightSpinner = new JSpinner(heightModel);

        private JComponent[] inputs = new JComponent[]{
                widthLabel, widthSpinner,
                heightLabel, heightSpinner
        };

        public Dimension show(Component parent, int initWidth, int initHeight) {
            widthSpinner.setValue(initWidth);
            heightSpinner.setValue(initHeight);
            int result = JOptionPane.showConfirmDialog(parent, inputs, "Resize component", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                return new Dimension((int) widthSpinner.getValue(), (int) heightSpinner.getValue());
            } else {
                return new Dimension(initWidth, initHeight);
            }
        }
    }

    private class PopupListener extends MouseAdapter {
        private JPopupMenu target;

        public PopupListener(JPopupMenu target) {
            this.target = target;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                target.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private class RemoveAction extends AbstractAction {
        private JPopupMenu menu;

        public RemoveAction(JPopupMenu menu) {
            this.menu = menu;
            putValue(NAME, "Remove");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            layoutPanel.remove(menu.getInvoker());
            layoutPanel.revalidate();
            layoutPanel.repaint();
        }
    }

    private class ResizeAction extends AbstractAction {
        private JPopupMenu menu;

        public ResizeAction(JPopupMenu menu) {
            this.menu = menu;
            putValue(NAME, "Resize");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Component c = menu.getInvoker();
            Dimension size = c.getPreferredSize();
            size = resizeDialog.show(SwingUtilities.getWindowAncestor(UniformGridLayoutDemo.this), size.width, size.height);
            c.setPreferredSize(size);
            layoutPanel.revalidate();
            layoutPanel.repaint();
        }
    }

    private class HideAction extends AbstractAction {
        private JPopupMenu menu;

        public HideAction(JPopupMenu menu) {
            this.menu = menu;
            putValue(NAME, "Hide");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            menu.getInvoker().setVisible(false);
            layoutPanel.revalidate();
            layoutPanel.repaint();
        }
    }

    private class ShowAllAction extends AbstractAction {
        private JPopupMenu menu;

        public ShowAllAction(JPopupMenu menu) {
            this.menu = menu;
            putValue(NAME, "Show All");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Component component = menu.getInvoker();
            if (component instanceof Container) {
                Container container = (Container) component;
                for (Component c : container.getComponents()) {
                    c.setVisible(true);
                }
            }
            layoutPanel.revalidate();
            layoutPanel.repaint();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UniformGridLayoutDemo frame = new UniformGridLayoutDemo();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setSize(1024, 768);
                frame.setVisible(true);
            }
        });
    }
}
