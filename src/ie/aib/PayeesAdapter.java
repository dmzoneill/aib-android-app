package ie.aib;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import ie.aib.entities.Payee;
import ie.aib.entities.Person;

/** Rows for the payee list. */
public final class PayeesAdapter extends ListRows {
  /** Which payee row is open. */
  private static final SingleSelection SELECTION = new SingleSelection();

  /** Customer who owns these payees. */
  private final Person person;

  /** Context used to inflate payee rows. */
  private final Context context;

  /**
   * @param owner signed-in customer
   * @param host inflater context
   */
  public PayeesAdapter(final Person owner, final Context host) {
    SELECTION.clear();
    this.person = owner;
    this.context = host;
  }

  /**
   * @param pos open payee row
   */
  public static void setSelected(final int pos) {
    SELECTION.set(pos);
  }

  /**
   * @return open payee row, or -1
   */
  public static int getSelected() {
    return SELECTION.get();
  }

  /**
   * @return sink that records the open payee
   */
  public static RowSelection.Sink sink() {
    return SELECTION.sink();
  }

  /**
   * @return how many payees are loaded
   */
  @Override
  public int getCount() {
    return this.person.getPayeeCount();
  }

  /**
   * @param position payee index @return that payee
   */
  @Override
  public Payee getItem(final int position) {
    return this.person.getPayees().get(position);
  }

  /**
   * @param position payee index
   * @param recycled ignored
   * @param parent payee list
   * @return row
   */
  @Override
  public View getView(
      final int position, // .
      final View recycled, // .
      final ViewGroup parent) {
    final Payee acc = this.person.getPayees().get(position);
    final LinearLayout itemLayout =
        (LinearLayout)
            LayoutInflater.from(this.context)
                .inflate(
                    R.layout.payeeslist, // .
                    parent, // .
                    false);
    final int layoutId = R.id.payeesLayout;
    final LinearLayout row = (LinearLayout) itemLayout.findViewById(layoutId);
    this.bind(itemLayout, acc);
    final int accountId = R.id.payeeRowAccount1;
    final View accountView = itemLayout.findViewById(accountId);
    final LinearLayout accountRow = (LinearLayout) accountView;
    final int sortId = R.id.payeeRowAccount2;
    final LinearLayout sortRow = (LinearLayout) itemLayout.findViewById(sortId);
    final int referenceId = R.id.payeeRowReference;
    final View referenceView = itemLayout.findViewById(referenceId);
    final LinearLayout referenceRow = (LinearLayout) referenceView;
    final int optionId = R.id.payeeRowOptions;
    final View optionView = itemLayout.findViewById(optionId);
    final LinearLayout options = (LinearLayout) optionView;
    row.setBackgroundColor(Color.argb(0, 0, 0, 0));
    if (acc.getNSC().compareToIgnoreCase("n/a") == 0
        || acc.getAccountNumber().compareToIgnoreCase("n/a") == 0) {
      row.removeView(accountRow);
      row.removeView(sortRow);
    } else {
      row.removeView(referenceRow);
    }
    options.setVisibility(LinearLayout.GONE);
    if (SELECTION.get() == position) {
      row.setBackgroundColor(
          Color.argb(
              RowSelection.TINT, // .
              0, // .
              RowSelection.TINT, // .
              0));
      options.setVisibility(LinearLayout.VISIBLE);
    }
    return itemLayout;
  }

  private void bind(final View itemLayout, final Payee acc) {
    label(itemLayout, R.id.payeeName, acc.getName());
    label(itemLayout, R.id.payeeType, acc.getPaymentType());
    label(itemLayout, R.id.payeeReference, acc.getReferenceNumber());
    label(itemLayout, R.id.payeeNSC, acc.getNSC());
    label(itemLayout, R.id.payeeAccount, acc.getAccountNumber());
  }
}
