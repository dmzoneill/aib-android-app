package ie.aib;

import java.util.ArrayList;

/** Positions that stay expanded in the statement and payment-log lists. */
public final class SelectionSet {
  /** Chosen positions. */
  private final ArrayList<Integer> selected = new ArrayList<Integer>();

  /**
   * Adds {@code position} or removes it when it is already selected.
   *
   * @param position row index
   */
  public void toggle(final int position) {
    if (this.selected.contains(position)) {
      this.selected.remove((Integer) position);
    } else {
      this.selected.add((Integer) position);
    }
  }

  /**
   * Whether {@code position} is expanded.
   *
   * @param position row index
   * @return true when selected
   */
  public boolean contains(final int position) {
    return this.selected.contains(position);
  }
}
