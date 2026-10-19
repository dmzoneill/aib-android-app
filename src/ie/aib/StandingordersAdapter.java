package ie.aib;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import ie.aib.entities.Person;
import ie.aib.entities.StandingOrder;
import java.util.List;

/** Rows for the standing-order list. */
public final class StandingordersAdapter extends ListRows {
  /** Which standing order is open. */
  private static final SingleSelection SELECTION = new SingleSelection();

  /** Customer who owns these orders. */
  private final Person person;

  /** Context used to inflate order rows. */
  private final Context context;

  /**
   * @param owner customer with standing orders
   * @param host context that inflates order rows
   */
  public StandingordersAdapter(final Person owner, final Context host) {
    SELECTION.clear();
    this.person = owner;
    this.context = host;
  }

  /**
   * @param pos open standing-order row
   */
  public static void setSelected(final int pos) {
    SELECTION.set(pos);
  }

  /**
   * @return open standing-order row, or -1
   */
  public static int getSelected() {
    return SELECTION.get();
  }

  /**
   * @return sink that records the open standing order
   */
  public static RowSelection.Sink sink() {
    return SELECTION.sink();
  }

  /**
   * @return how many standing orders are loaded
   */
  @Override
  public int getCount() {
    return this.person.getStandingOrdersCount();
  }

  /**
   * @param position order index @return that standing order
   */
  @Override
  public StandingOrder getItem(final int position) {
    return this.person.getStandingorders().get(position);
  }

  /**
   * @param position order index
   * @param recycled ignored
   * @param parent order list
   * @return row
   */
  @Override
  public View getView(
      final int position, // .
      final View recycled, // .
      final ViewGroup parent) {
    final List<StandingOrder> orders = this.person.getStandingorders();
    final StandingOrder logEntry = orders.get(position);
    final LinearLayout itemLayout =
        (LinearLayout)
            LayoutInflater.from(this.context)
                .inflate(
                    R.layout.standingorderslist, // .
                    parent, // .
                    false);
    this.bind(itemLayout, logEntry);
    final LinearLayout options =
        (LinearLayout) itemLayout.findViewById(R.id.standingOrderRowOptions);
    options.setVisibility(LinearLayout.GONE);
    itemLayout.setBackgroundColor(Color.argb(0, 0, 0, 0));
    if (SELECTION.get() == position) {
      itemLayout.setBackgroundColor(
          Color.argb(
              RowSelection.TINT, // .
              0, // .
              RowSelection.TINT, // .
              0));
      options.setVisibility(LinearLayout.VISIBLE);
    }
    return itemLayout;
  }

  private void bind(final View itemLayout, final StandingOrder logEntry) {
    ((TextView) itemLayout.findViewById(R.id.standingordersFrequency))
        .setText(logEntry.getFrequency());
    ((TextView) itemLayout.findViewById(R.id.standingordersFromAccount))
        .setText(logEntry.getFromAccount());
    ((TextView) itemLayout.findViewById(R.id.standingordersReceiver))
        .setText(logEntry.getReceiver());
    ((TextView) itemLayout.findViewById(R.id.standingordersNextAmount))
        .setText(logEntry.getNextAmount());
    ((TextView) itemLayout.findViewById(R.id.standingordersNextDate))
        .setText(logEntry.getNextDate());
  }
}
