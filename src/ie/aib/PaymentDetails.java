package ie.aib;

import android.view.View;
import android.widget.TextView;

/** Shows or hides the extra lines on a payment-log row. */
public final class PaymentDetails {
  private PaymentDetails() {
    super();
  }

  /**
   * Opens or closes the detail lines on {@code row}.
   *
   * <p>An empty due date stays hidden. The original uses == on "".
   *
   * @param row payment row
   * @param open whether the details should be visible
   */
  public static void setOpen(final View row, final boolean open) {
    final int visibility = open ? View.VISIBLE : View.GONE;
    row.findViewById(R.id.paymentDetails1).setVisibility(visibility);
    row.findViewById(R.id.paymentDetails2).setVisibility(visibility);
    final View dueRow = row.findViewById(R.id.paymentDetails3);
    dueRow.setVisibility(visibility);
    if (open) {
      final int dueId = R.id.paymentDuedate;
      final TextView dueDate = (TextView) dueRow.findViewById(dueId);
      if (dueDate.getText().toString() == "") {
        dueRow.setVisibility(View.GONE);
      } else {
        dueRow.setVisibility(View.VISIBLE);
      }
    }
  }
}
