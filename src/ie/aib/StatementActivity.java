package ie.aib;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import ie.aib.entities.Account;
import ie.aib.entities.Person;

/** Account statement. */
public final class StatementActivity extends BankingList {
  /** Account chosen on the overview. */
  private static int accountNumber;

  /** Opaque black used when a row has neither a debit nor a credit. */
  private static final int OPAQUE = 255;

  /**
   * @return account index
   */
  public static int getAccountNumber() {
    return accountNumber;
  }

  /**
   * @param account account index
   */
  public static void setAccountNumber(final int account) {
    accountNumber = account;
  }

  /**
   * @param instance unused framework state
   */
  @Override
  public void onCreate(final Bundle instance) {
    super.onCreate(instance);
    this.setContentView(R.layout.statementview);
    final Person person = Person.getInstance();
    final Account account = person.getAccounts().get(accountNumber);
    final String accountName = account.getAccount();
    this.arm(accountName);
    this.getListView();
    this.setListAdapter(
        new StatementAdapter(
            Person.getInstance().getAccounts().get(accountNumber), // .
            this));
  }

  /**
   * @param lines statement list
   * @param cell tapped row
   * @param at index
   * @param rowId id
   */
  @Override
  protected void onListItemClick(
      final ListView lines, final View cell, final int at, final long rowId) {
    final LinearLayout line = (LinearLayout) cell;
    if (StatementAdapter.isSelected(at)) {
      this.paintClosedRow(line);
    } else {
      line.setBackgroundColor(
          Color.argb(
              RowSelection.TINT, // .
              0, // .
              0, // .
              RowSelection.TINT));
    }
    StatementAdapter.changeSelected(at);
  }

  private void paintClosedRow(final LinearLayout line) {
    final int creditId = R.id.creditRow;
    final LinearLayout credit = (LinearLayout) line.findViewById(creditId);
    if (credit != null) {
      line.setBackgroundColor(
          Color.argb(
              RowSelection.TINT, // .
              0, // .
              RowSelection.TINT, // .
              0));
    }
    final LinearLayout debit = (LinearLayout) line.findViewById(R.id.debitRow);
    if (debit != null) {
      line.setBackgroundColor(
          Color.argb(
              RowSelection.TINT, // .
              RowSelection.TINT, // .
              0, // .
              0));
    }
    if (debit == null && credit == null) {
      line.setBackgroundColor(Color.argb(OPAQUE, 0, 0, 0));
    }
  }

  /**
   * @param statementMenu statement menu @return true
   */
  @Override
  public boolean onPrepareOptionsMenu(final Menu statementMenu) {
    Menus.disable(
        statementMenu,
        R.id.statementSave,
        R.id.statementSend,
        R.id.statementRefresh,
        R.id.statementHideDebits,
        R.id.statementHideDeposits,
        R.id.statementHideOther);
    return super.onPrepareOptionsMenu(statementMenu);
  }

  /**
   * @return statement options menu
   */
  @Override
  protected int optionsMenu() {
    return R.menu.statementmenu;
  }
}
