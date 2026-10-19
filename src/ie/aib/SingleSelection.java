package ie.aib;

/** One expanded row, shared by the payee and standing-order lists. */
public final class SingleSelection {
  /** Open row, or -1. */
  private int selected = -1;

  /** Clears the open row. */
  public void clear() {
    this.selected = -1;
  }

  /**
   * @param pos open row
   */
  public void set(final int pos) {
    this.selected = pos;
  }

  /**
   * @return open row, or -1
   */
  public int get() {
    return this.selected;
  }

  /**
   * @return sink that writes into this selection
   */
  public RowSelection.Sink sink() {
    return new RowSelection.Sink() {
      public void set(final int pos) {
        SingleSelection.this.set(pos);
      }
    };
  }
}
