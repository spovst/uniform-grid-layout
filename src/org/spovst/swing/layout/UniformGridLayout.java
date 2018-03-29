package org.spovst.swing.layout;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * The <code>UniformGridLayout</code> class is a layout manager that
 * arranges components in a uniform grid, and dynamically calculates
 * horizontal gap between components and grid cell size.
 * <p>
 * When a component is added or removed, the cell size is
 * recalculated. The cell width is a maximum width among all
 * components, and the cell height is a maximum height among all
 * components.
 * <p>
 * The <code>UniformGridLayout</code> tries to place the maximum
 * number of cells in the row. When the layout manager in a fixed
 * horizontal gap mode, a cell that does not fit into the current row
 * is wrapped to the next row. When the horizontal gap  is specified
 * as dynamic, the layout manager calculates horizontal gap so that
 * the distance between all components, as well as between the
 * leading and trailing components and the container borders (taking
 * into account the insets), is the same.
 * <p>
 * The vertical gap is fixed and never calculated automatically.
 * <p>
 * A <code>UniformGridLayout</code> always considers only the
 * preferred size of the components.
 * <p>
 * Note that if the container that is managed by the
 * <code>UniformGridLayout</code> is placed in the
 * {@link JScrollPane}, the horizontal scrollbar will only be shown
 * if the container width becomes less than the cell width, plus the
 * left and right insets. Otherwise, the layout manager will
 * automatically place the components so that they do not occupy more
 * space than allowed by the container width (this behavior is one of
 * the main purposes of using the <code>UniformGridLayout</code>).
 *
 * @author Sergey Povstyanov
 * @version 1.0
 */
public class UniformGridLayout implements LayoutManager2, Serializable {
    /**
     * Used for serialization.
     */
    private static final long serialVersionUID = -8074524482056790118L;

    /**
     * This value indicates that the horizontal gap should be
     * calculated by the layout manager automatically.
     */
    public static final int DYNAMIC_HGAP = -1;

    /**
     * The horizontal gap will specify the space between the grid
     * columns.
     * <p>
     * If the <code>hgap</code> is negative, the horizontal gap will
     * be calculated automatically. In this case, the calculated
     * <code>hgap</code> value will be added between the first column
     * and the left border of the container, and between the last
     * column and the right border of the container. The left and the
     * right insets also will be added to the left and to the right
     * borders of the container.
     * <p>
     * Else, horizontal gap is fixed. In this case, horizontal gap
     * will be added only between the columns, but not before the
     * first column and after the last column.
     *
     * @serial
     * @see #getHgap()
     * @see #setHgap(int)
     */
    protected int hgap;

    /**
     * The vertical gap will specify the space between the grid rows.
     *
     * @serial
     * @see #getVgap()
     * @see #setVgap(int)
     */
    protected int vgap;

    /**
     * The insets will specify the space between the container
     * borders and the outside rows/columns of the layout grid.
     *
     * @serial
     * @see #getInsets()
     * @see #setInsets(Insets)
     * @see #setInsets(int, int, int, int)
     */
    protected Insets insets;

    /**
     * The grid metrics contain information such as cell size,
     * visible components count, number of grid rows and columns, and
     * dynamically calculated horizontal gap.
     *
     * @see org.spovst.swing.layout.UniformGridLayout.GridMetrics
     */
    protected transient GridMetrics gridMetrics;

    /**
     * Creates a <code>UniformGridLayout</code> manager with the
     * dynamic horizontal gap, zero vertical gap, and zero insets.
     */
    public UniformGridLayout() {
        this(0);
    }

    /**
     * Creates a <code>UniformGridLayout</code> manager with the
     * dynamic horizontal gap, zero vertical gap, and the specified
     * insets.
     *
     * @param top    inset between the first row of the grid and the
     *               top border of the container
     * @param left   inset between the first column of the grid and
     *               the left border of the container
     * @param bottom inset between the last row of the grid and the
     *               bottom border of the container
     * @param right  inset between the last column of the grid and
     *               the right border of the container
     */
    public UniformGridLayout(int top, int left, int bottom, int right) {
        this(0, top, left, bottom, right);
    }

    /**
     * Creates a <code>UniformGridLayout</code> manager with the
     * dynamic horizontal gap, zero vertical gap, and the specified
     * insets.
     *
     * @param insets the instance of the {@link Insets} class that
     *               specifies the top, the left, the bottom and the
     *               right insets values
     */
    public UniformGridLayout(Insets insets) {
        this(0, insets);
    }

    /**
     * Creates a <code>UniformGridLayout</code> manager with the
     * dynamic horizontal gap, the specified vertical gap, and zero
     * insets.
     *
     * @param vgap the space between the rows of the grid
     */
    public UniformGridLayout(int vgap) {
        this(vgap, 0, 0, 0, 0);
    }

    /**
     * Creates a <code>UniformGridLayout</code> manager with the
     * dynamic horizontal gap, the specified vertical gap, and the
     * specified insets.
     *
     * @param vgap   the space between the rows of the grid
     * @param insets the instance of the {@link Insets} class that
     *               specifies the top, the left, the bottom and the
     *               right insets values
     */
    public UniformGridLayout(int vgap, Insets insets) {
        this(vgap, insets.top, insets.left, insets.bottom, insets.right);
    }

    /**
     * Creates a <code>UniformGridLayout</code> manager with the
     * dynamic horizontal gap, the specified vertical gap, and the
     * specified insets.
     *
     * @param vgap   the space between the rows of the grid
     * @param top    inset between the first row of the grid and the
     *               top border of the container
     * @param left   inset between the first column of the grid and
     *               the left border of the container
     * @param bottom inset between the last row of the grid and the
     *               bottom border of the container
     * @param right  inset between the last column of the grid and
     *               the right border of the container
     */
    public UniformGridLayout(int vgap, int top, int left, int bottom, int right) {
        this(DYNAMIC_HGAP, vgap, top, left, bottom, right);
    }

    /**
     * Creates a <code>UniformGridLayout</code> manager with the
     * specified horizontal gap, the specified vertical gap, and zero
     * insets.
     *
     * @param hgap the space between the columns of the grid.
     *             Negative value of the hgap indicates that real
     *             hgap will be calculated automatically. In this
     *             case, this value also will be added between the
     *             first column and the left border of container, and
     *             between the last column and the right border of
     *             the container
     * @param vgap the space between the rows of the grid
     */
    public UniformGridLayout(int hgap, int vgap) {
        this(hgap, vgap, 0, 0, 0, 0);
    }

    /**
     * Creates a <code>UniformGridLayout</code> manager with the
     * specified horizontal gap, the specified vertical gap, and the
     * specified insets.
     *
     * @param hgap   the space between the columns of the grid.
     *               Negative value of the hgap indicates that real
     *               hgap will be calculated automatically. In this
     *               case, this value also will be added between the
     *               first column and the left border of container,
     *               and between the last column and the right border
     *               of the container
     * @param vgap   the space between the rows of the grid
     * @param insets the instance of the {@link Insets} class that
     *               specifies the top, the left, the bottom and the
     *               right insets values
     */
    public UniformGridLayout(int hgap, int vgap, Insets insets) {
        this(hgap, vgap, insets.top, insets.left, insets.bottom, insets.right);
    }

    /**
     * Creates a <code>UniformGridLayout</code> manager with the
     * specified horizontal gap, the specified vertical gap, and the
     * specified insets.
     *
     * @param hgap   the space between the columns of the grid.
     *               Negative value of the hgap indicates that real
     *               hgap will be calculated automatically. In this
     *               case, this value also will be added between the
     *               first column and the left border of container,
     *               and between the last column and the right border
     *               of the container
     * @param vgap   the space between the rows of the grid
     * @param top    inset between the first row of the grid and the
     *               top border of the container
     * @param left   inset between the first column of the grid and
     *               the left border of the container
     * @param bottom inset between the last row of the grid and the
     *               bottom border of the container
     * @param right  inset between the last column of the grid and
     *               the right border of the container
     */
    public UniformGridLayout(int hgap, int vgap, int top, int left, int bottom, int right) {
        this.hgap = hgap;
        this.vgap = vgap;
        this.insets = new Insets(top, left, bottom, right);
        this.gridMetrics = new GridMetrics();
    }

    /**
     * Gets the space between the columns of the grid. Negative value
     * indicates that real hgap will be calculated automatically.
     *
     * @return the space between the columns of the grid
     *
     * @see #setHgap(int)
     */
    public int getHgap() {
        return hgap;
    }

    /**
     * Sets the space between the columns of the grid. Negative value
     * indicates that real hgap will be calculated automatically.
     *
     * @param hgap the space between the columns of the grid
     *
     * @see #getHgap()
     * @see #DYNAMIC_HGAP
     */
    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    /**
     * Gets the space between the rows of the grid.
     *
     * @return the space between the rows of the grid
     *
     * @see #setVgap(int)
     */
    public int getVgap() {
        return vgap;
    }

    /**
     * Sets the space between the rows of the grid.
     *
     * @param vgap the space between the rows of the grid
     *
     * @see #getVgap()
     */
    public void setVgap(int vgap) {
        this.vgap = vgap;
    }

    /**
     * Gets the insets between the first/last row/column of the grid
     * and the borders of the container.
     *
     * @return the insets between the first/last row/column of the
     *         grid nad the borders of the container
     *
     * @see #setInsets(Insets)
     * @see #setInsets(int, int, int, int)
     */
    public Insets getInsets() {
        return insets;
    }

    /**
     * Gets the insets between the first/last row/column of the grid
     * and the borders of the container.
     *
     * @param insets the instance of the {@link Insets} class that
     *               specifies the top, the left, the bottom and the
     *               right insets values
     *
     * @see #getInsets()
     */
    public void setInsets(Insets insets) {
        setInsets(insets.top, insets.left, insets.right, insets.bottom);
    }

    /**
     * Gets the insets between the first/last row/column of the grid
     * and the borders of the container.
     *
     * @param top    inset between the first row of the grid and the
     *               top border of the container
     * @param left   inset between the first column of the grid and
     *               the left border of the container
     * @param bottom inset between the last row of the grid and the
     *               bottom border of the container
     * @param right  inset between the last column of the grid and
     *               the right border of the container
     *
     * @see #getInsets()
     */
    public void setInsets(int top, int left, int right, int bottom) {
        this.insets.top = top;
        this.insets.left = left;
        this.insets.right = right;
        this.insets.bottom = bottom;
    }

    /**
     * Determines the minimum size of the <code>target</code>
     * container using this <code>UniformGridLayout</code> manager.
     * <p>
     * The minimum width of the <code>target</code> container must
     * accommodate one column of the grid, plus the left and the
     * right insets.
     * <p>
     * The minimum height of the <code>target</code> container must
     * accommodate as many rows as there are components in the
     * <code>target</code> container, plus the top and the bottom
     * insets, and plus the vertical gap value between all rows of
     * the grid.
     *
     * @param target the container in which to do the layout
     *
     * @return the minimum size of the <code>target</code> container
     *
     * @see #maximumLayoutSize(Container)
     * @see #preferredLayoutSize(Container)
     */
    @Override
    public Dimension minimumLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            gridMetrics.calculate(target, insets, hgap);
            int width = insets.left
                    + gridMetrics.leadingHgap
                    + gridMetrics.cellSize.width
                    + gridMetrics.trailingHgap
                    + insets.right;
            int height = insets.top
                    + ((gridMetrics.cellSize.height + vgap) * gridMetrics.componentsCount - vgap)
                    + insets.bottom;
            return new Dimension(width, height);
        }
    }

    /**
     * Determines the maximum size of the <code>parent</code>
     * container using this <code>UniformGridLayout</code> manager.
     * <p>
     * The <code>UniformGridLayout</code> in its maximum
     * configuration seeks to occupy any dimension.
     *
     * @param target the container in which to do the layout
     *
     * @return the maximum size of the <code>target</code> container
     *
     * @see #minimumLayoutSize(Container)
     * @see #preferredLayoutSize(Container)
     */
    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Determines the preferred size of the <code>target</code>
     * container using this <code>UniformGridLayout</code> manager.
     * <p>
     * The preferred width of the <code>target</code> container will
     * never exceeds its current width. In this width, the
     * <code>UniformGridLayout</code> manager will try to place the
     * maximum number of the grid cells, taking into account the left
     * and the right insets and the horizontal gap between the
     * columns of the grid. Thus, the row of the grid is formed.
     * <p>
     * The preferred height of the <code>target</code> container
     * should be sufficient to accommodate as many rows of the
     * preferred width as possible so that all the components of the
     * <code>target</code> container are placed, taking into account
     * the top and the bottom insets and the vertical gap between the
     * rows of the grid.
     *
     * @param target the container in which to do the layout
     *
     * @return the preferred size of the <code>target</code>
     *         container
     *
     * @see #minimumLayoutSize(Container)
     * @see #maximumLayoutSize(Container)
     */
    @Override
    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            gridMetrics.calculate(target, insets, hgap);

            int width = insets.left + gridMetrics.leadingHgap
                    + ((gridMetrics.cellSize.width + gridMetrics.innerHgap) * gridMetrics.cols - gridMetrics.innerHgap)
                    + gridMetrics.trailingHgap + insets.right;
            int height = insets.top
                    + ((gridMetrics.cellSize.height + vgap) * gridMetrics.rows - vgap)
                    + insets.bottom;

            // Special handling of JScrollPane as target:
            Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, target);
            if (scrollPane != null && target.isValid()) {
                width -= 1;
            }

            return new Dimension(width, height);
        }
    }

    /**
     * Returns the alignment along the x axis. This specifies how
     * the component would like to be aligned relative to other
     * components. The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target the container in which to do the layout
     *
     * @return the value <code>0.5f</code> to indicate centered
     */
    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    /**
     * Returns the alignment along the y axis. This specifies how
     * the component would like to be aligned relative to other
     * components. The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target the container in which to do the layout
     *
     * @return the value <code>0.5f</code> to indicate centered
     */
    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    /**
     * Has no effect, since this layout manager does not use a
     * per-component string.
     *
     * @param name ignored
     * @param comp ignored
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        // Do nothing.
    }

    /**
     * Has no effect, since this layout manager does not store
     * currently presented components.
     *
     * @param comp        ignored
     * @param constraints ignored
     */
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        // Do nothing.
    }

    /**
     * Has no effect, since this layout manager does not store
     * currently presented components.
     *
     * @param comp ignored
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        // Do nothing.
    }

    /**
     * Lays out the container. This method lets each <i>visible</i>
     * component take its preferred size by reshaping the components
     * in the target container in order to satisfy the insets and
     * gaps of this <code>UniformGridLayout</code> object.
     *
     * @param target the container in which to do the layout
     */
    @Override
    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            gridMetrics.calculate(target, insets, hgap);

            int x = insets.left + gridMetrics.leadingHgap; // Current x coordinate.
            int y = insets.top; // Current y coordinate.

            int laidOutCols = 0; // Currently laid out components count in current row.
            for (Component component : target.getComponents()) {
                // Do not laid out invisible components.
                if (component.isVisible()) {
                    // Go to the next row.
                    if (laidOutCols >= gridMetrics.cols) {
                        x = insets.left + gridMetrics.leadingHgap;
                        y += gridMetrics.cellSize.height + vgap;
                        laidOutCols = 0;
                    }

                    // We always use preferred components size.
                    Dimension size = component.getPreferredSize();
                    int cx = x + (gridMetrics.cellSize.width - size.width) / 2;
                    int cy = y + (gridMetrics.cellSize.height - size.height) / 2;
                    component.setBounds(cx, cy, size.width, size.height);

                    x += gridMetrics.cellSize.width + gridMetrics.innerHgap;
                    laidOutCols++;
                }
            }
        }
    }

    /**
     * Has no effect, since this layout manager does not store
     * any cached state that need to be invalidated.
     *
     * @param target ignored
     */
    @Override
    public void invalidateLayout(Container target) {
        // Do nothing.
    }

    /**
     * The <code>GridMetrics</code> class is a helper class that
     * allows to calculate all dynamic grid parameters, such as the
     * cell size, the visible components count, the number of the
     * grid rows and columns, and the horizontal gap (if it is
     * necessary to calculate automatically).
     *
     * @author Sergey Povstyanov
     * @version 1.0
     */
    protected static class GridMetrics {
        /**
         * The cell size is the property that determines the layout grid
         * cell size. The cell size should be sufficient to accommodate
         * the largest of the components, but not larger.
         *
         * @serial
         */
        public Dimension cellSize;

        /**
         * The number of the visible components.
         */
        public int componentsCount;

        /**
         * The number of the grid rows.
         *
         * @see #cols
         */
        public int rows;

        /**
         * The number of the grid columns.
         *
         * @see #rows
         */
        public int cols;

        /**
         * The space that will be added between the grid columns.
         *
         * @see #leadingHgap
         * @see #trailingHgap
         */
        public int innerHgap;

        /**
         * The space that will be added before the first grid column.
         *
         * @see #innerHgap
         * @see #trailingHgap
         */
        public int leadingHgap;

        /**
         * The space that will be added after the last grid column.
         *
         * @see #innerHgap
         * @see #leadingHgap
         */
        public int trailingHgap;

        /**
         * Creates a <code>GridMetrics</code> with the default
         * <i>empty</i> state.
         * The {@link #calculate(Container, Insets, int)} method will
         * calculate the actual grid metrics.
         *
         * @see #calculate(Container, Insets, int)
         */
        public GridMetrics() {
            cellSize = new Dimension(0, 0);
            rows = 0;
            cols = 0;
            innerHgap = 0;
            leadingHgap = 0;
            trailingHgap = 0;
        }

        /**
         * Calculates actual grid metrics based on the count,
         * preferred size, and visibility of the
         * <code>container</code> components. Insets and gaps are
         * also taking into account.
         *
         * @param container the container whose components are taken
         *                  into account when calculating grid
         *                  metrics
         * @param insets    the instance of the {@link Insets} class
         *                  that specifies the top, the left, the
         *                  bottom and the right insets values
         * @param hgap      the initial value of the space between
         *                  the grid columns. Negative value of the
         *                  hgap indicates that real hgap will be
         *                  calculated automatically; the free space
         *                  is uniformly distributed between the grid
         *                  columns ({@link #innerHgap}), and the
         *                  remaining space is distributed between
         *                  the {@link #leadingHgap} and the
         *                  {@link #trailingHgap}.
         *                  Positive value indicates the fixed hgap;
         *                  this value will be copied to the
         *                  {@link #innerHgap} filed, and the
         *                  {@link #leadingHgap} and the
         *                  {@link #trailingHgap} fields will be set
         *                  to zero.
         */
        public void calculate(Container container, Insets insets, int hgap) {
            // Calculate the actual grid cell size and the visible components count.
            cellSize.width = 0;
            cellSize.height = 0;
            componentsCount = 0;
            for (Component c : container.getComponents()) {
                // Taking into account only the visible components.
                if (c.isVisible()) {
                    Dimension size = c.getPreferredSize();
                    cellSize.width = Math.max(cellSize.width, size.width);
                    cellSize.height = Math.max(cellSize.height, size.height);
                    componentsCount += 1;
                }
            }

            // If there is no components to be laying out.
            if (componentsCount == 0) {
                rows = 0;
                cols = 0;
                innerHgap = 0;
                leadingHgap = 0;
                trailingHgap = 0;
                return;
            }

            // Find the topmost container that provides the real width.
            while (container.getSize().width == 0 && container.getParent() != null) {
                container = container.getParent();
            }

            int parentWidth = container.getSize().width;
            // If the container width not yet been calculated, request the maximum width.
            if (parentWidth == 0) {
                parentWidth = Integer.MAX_VALUE;
            }

            // Leave the space for the insets.
            parentWidth -= insets.left + insets.right;

            // We need to calculate the dynamic horizontal gap if the initial horizontal gap is negative.
            if (hgap < 0) {
                // Minimum columns is 1, maximum columns is equals to the components count.
                int cols = normalizeValue(parentWidth / cellSize.width, 1, componentsCount);
                int restSpace = Math.max(parentWidth - cellSize.width * cols, 0);
                hgap = restSpace / (cols + 1);
                restSpace = Math.max(restSpace - hgap * (cols - 1), 0);

                innerHgap = hgap;
                leadingHgap = restSpace / 2;
                trailingHgap = restSpace - leadingHgap;
            } else {
                innerHgap = hgap;
                leadingHgap = 0;
                trailingHgap = 0;
            }

            // Leave the space for the leading and the trailing horizontal gaps.
            parentWidth -= leadingHgap + trailingHgap;

            // We need to calculate columns count (cols variable). We know if we layout cols cells with width is
            // cellSize.width, and (cols - 1) horizontal gaps (innerHgap) between cells, we will get exactly
            // parentWidth. Lets calculate cols value:
            // cols * cellSize.width + (cols - 1) * innerHgap == parentWidth,
            // cols * cellSize.width + cols * innerHgap - innerHgap == parentWidth,
            // cols * (cellSize.width + innerHgap) == parentWidth + innerHgap,
            // cols == (parentWidth + innerHgap) / (cellSize.width + innerHgap).
            cols = normalizeValue((parentWidth + innerHgap) / (cellSize.width + innerHgap), 1, componentsCount);
            // We get the components count and append to it (cols - 1) imaginary components to handle wrapping. Then we
            // divide this sum to the columns count to get the finally rows count.
            rows = (componentsCount + cols - 1) / cols;
        }

        /**
         * Returns the <code>value</code>, if the <code>value</code>
         * is in the range (<code>min</code>, <code>max</code>);
         * returns the <code>min</code>, if the <code>value</code> is
         * less than the <code>min</code>; returns the
         * <code>max</code>, if the <code>value</code> is greater
         * than the <code>max</code>.
         *
         * @param value the value to be normalized
         * @param min   the lower bound of the normalization range
         * @param max   the upper bound of the normalization range
         *
         * @return the normalized <code>value</code>
         */
        private static int normalizeValue(int value, int min, int max) {
            return Math.min(Math.max(min, value), max);
        }
    }
}
