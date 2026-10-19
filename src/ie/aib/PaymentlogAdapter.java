package ie.aib;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import ie.aib.entities.Person;

/** Rows for the payment log. */
public final class PaymentlogAdapter extends ListRows {
  /** Date column. */
  private static final int DATE = 0;

  /** Type column. */
  private static final int TYPE = 1;

  /** From-account column. */
  private static final int FROM = 2;

  /** Receiver column. */
  private static final int RECEIVER = 3;

  /** Due-date column. */
  private static final int DUE = 4;

  /** Amount column. */
  private static final int AMOUNT = 5;

  /** Expanded payment rows. */
  private static SelectionSet selected;

  /** Customer who owns these payments. */
  private final Person person;

  /** Context used to inflate payment rows. */
  private final Context context;

  /**
   * @param owner customer with payments
   * @param host context that inflates payment rows
   */
  public PaymentlogAdapter(final Person owner, final Context host) {
    this.person = owner;
    this.context = host;
    selected = new SelectionSet();
  }

  /**
   * @param pos payment row to toggle
   */
  public static void changeSelected(final int pos) {
    selected.toggle(pos);
  }

  /**
   * @param pos payment row
   * @return whether that payment is expanded
   */
  public static boolean isSelected(final int pos) {
    return selected.contains(pos);
  }

  /**
   * @return how many payments are loaded
   */
  @Override
  public int getCount() {
    return this.person.getPaymentsCount();
  }

  /**
   * @param position payment index @return payment cells
   */
  @Override
  public String[] getItem(final int position) {
    return this.person.getPayments().get(position);
  }

  /**
   * @param position payment index
   * @param recycled ignored
   * @param parent log
   * @return row
   */
  @Override
  public View getView(
      final int position, // .
      final View recycled, // .
      final ViewGroup parent) {
    final String[] logEntry = this.person.getPayments().get(position);
    final LinearLayout itemLayout =
        (LinearLayout)
            LayoutInflater.from(this.context)
                .inflate(
                    R.layout.paymentloglist, // .
                    parent, // .
                    false);
    this.bind(itemLayout, logEntry);
    if (selected.contains(position)) {
      itemLayout.setBackgroundColor(
          Color.argb(
              RowSelection.TINT, // .
              0, // .
              RowSelection.TINT, // .
              0));
      PaymentDetails.setOpen(itemLayout, true);
    } else {
      PaymentDetails.setOpen(itemLayout, false);
    }
    return itemLayout;
  }

  private void bind(final View itemLayout, final String[] logEntry) {
    label(itemLayout, R.id.paymentDate, logEntry[DATE]);
    label(itemLayout, R.id.paymentType, logEntry[TYPE]);
    label(itemLayout, R.id.paymentFrom, logEntry[FROM]);
    label(itemLayout, R.id.paymentReceiver, logEntry[RECEIVER]);
    label(itemLayout, R.id.paymentDuedate, logEntry[DUE]);
    label(itemLayout, R.id.paymentAmount, logEntry[AMOUNT]);
  }
}
