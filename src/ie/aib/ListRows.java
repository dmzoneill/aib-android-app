package ie.aib;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** List adapter that uses the row index as the stable id. */
public abstract class ListRows extends BaseAdapter {
  /**
   * @param position row index
   * @return that index
   */
  @Override
  public final long getItemId(final int position) {
    return position;
  }

  /**
   * Sets the text of a child view.
   *
   * @param root parent view
   * @param id child id
   * @param value text
   */
  protected static void label(
      final View root, // .
      final int id, // .
      final String value) {
    ((TextView) root.findViewById(id)).setText(value);
  }
}
