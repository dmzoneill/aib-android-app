package ie.aib;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

/** Highlights one list row and reveals its option buttons. */
public final class RowSelection {
  /** Alpha used for a selected row. */
  public static final int TINT = 140;

  private RowSelection() {
    super();
  }

  /** Receives the selected position, or -1 when it cannot be applied. */
  public interface Sink {
    /**
     * Stores {@code position}.
     *
     * @param position selected row, or -1
     */
    void set(int position);
  }

  /**
   * Clears the previous row and opens {@code optionsId} on {@code chosen}.
   *
   * @param list list that owns the rows
   * @param chosen row that was tapped
   * @param position tapped position
   * @param previous previously selected position, or -1
   * @param optionsId option bar inside the row
   * @param sink selection store
   */
  public static void show(
      final ListView list,
      final View chosen,
      final int position,
      final int previous,
      final int optionsId,
      final Sink sink) {
    try {
      if (previous > -1) {
        final View child = list.getChildAt(previous);
        final LinearLayout previousRow = (LinearLayout) child;
        previousRow.setBackgroundColor(Color.argb(0, 0, 0, 0));
        final View optionView = previousRow.findViewById(optionsId);
        final LinearLayout previousOptions = (LinearLayout) optionView;
        previousOptions.setVisibility(LinearLayout.GONE);
      }
      final LinearLayout row = (LinearLayout) chosen;
      final LinearLayout options = (LinearLayout) row.findViewById(optionsId);
      options.setVisibility(LinearLayout.VISIBLE);
      row.setBackgroundColor(
          Color.argb(
              RowSelection.TINT, // .
              0, // .
              RowSelection.TINT, // .
              0));
      sink.set(position);
    } catch (final Exception ignored) {
      sink.set(-1);
    }
  }
}
