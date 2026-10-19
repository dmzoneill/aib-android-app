package ie.aib;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import ie.aib.entities.Account;

/** Rows for an account statement. */
public final class StatementAdapter extends ListRows {
  /** Shortest amount that is still shown. */
  private static final int MIN_AMOUNT = 2;

  /** Debit column. */
  private static final int DEBIT = 2;

  /** Credit column. */
  private static final int CREDIT = 3;

  /** Balance column. */
  private static final int BALANCE = 4;

  /** Expanded statement rows. */
  private static SelectionSet selected;

  /** Account being shown. */
  private final Account account;

  /** Context used to inflate statement rows. */
  private final Context context;

  /**
   * @param source account whose statement is shown
   * @param host context that inflates statement rows
   */
  public StatementAdapter(final Account source, final Context host) {
    this.account = source;
    this.context = host;
    selected = new SelectionSet();
  }

  /**
   * @param pos statement row to toggle
   */
  public static void changeSelected(final int pos) {
    selected.toggle(pos);
  }

  /**
   * @param pos statement row
   * @return whether that line is expanded
   */
  public static boolean isSelected(final int pos) {
    return selected.contains(pos);
  }

  /**
   * @return how many statement lines are loaded
   */
  @Override
  public int getCount() {
    return this.account.getStatement().size();
  }

  /**
   * @param position statement index @return statement cells
   */
  @Override
  public String[] getItem(final int position) {
    return this.account.getStatement().get(position);
  }

  /**
   * @param position line index
   * @param recycled ignored
   * @param parent statement
   * @return row
   */
  @Override
  public View getView(
      final int position, // .
      final View recycled, // .
      final ViewGroup parent) {
    final String[] statement = this.account.getStatement().get(position);
    final LinearLayout itemLayout =
        (LinearLayout)
            LayoutInflater.from(this.context)
                .inflate(
                    R.layout.statementlist, // .
                    parent, // .
                    false);
    final int layoutId = R.id.statementLayout;
    final LinearLayout row = (LinearLayout) itemLayout.findViewById(layoutId);
    label(itemLayout, R.id.transactionName1, statement[0]);
    label(itemLayout, R.id.transactionName2, statement[1]);
    this.paintAmount(
        itemLayout,
        row,
        R.id.transactionName3,
        R.id.debitRow,
        statement[DEBIT],
        RowSelection.TINT,
        0);
    this.paintAmount(
        itemLayout,
        row,
        R.id.transactionName4,
        R.id.creditRow,
        statement[CREDIT],
        0,
        RowSelection.TINT);
    label(itemLayout, R.id.transactionName5, "€" + statement[BALANCE]);
    if (statement[BALANCE].length() < MIN_AMOUNT) {
      row.removeView(itemLayout.findViewById(R.id.balanceRow));
    }
    if (selected.contains(position)) {
      row.setBackgroundColor(
          Color.argb(
              RowSelection.TINT, // .
              0, // .
              0, // .
              RowSelection.TINT));
    }
    return itemLayout;
  }

  private void paintAmount(
      final View itemLayout,
      final LinearLayout row,
      final int valueId,
      final int lineId,
      final String amount,
      final int red,
      final int green) {
    ((TextView) itemLayout.findViewById(valueId)).setText("€" + amount);
    if (amount.length() < MIN_AMOUNT) {
      row.removeView(itemLayout.findViewById(lineId));
    } else {
      row.setBackgroundColor(Color.argb(RowSelection.TINT, red, green, 0));
    }
  }
}
