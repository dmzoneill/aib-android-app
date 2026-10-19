package ie.aib;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import ie.aib.entities.Person;

/** Payment log. */
public final class PaymentlogActivity extends BankingList {
  /**
   * @param bundle unused framework state
   */
  @Override
  public void onCreate(final Bundle bundle) {
    super.onCreate(bundle);
    this.setContentView(R.layout.paymentlogview);
    this.arm("Payment log");
    this.getListView();
    this.setListAdapter(new PaymentlogAdapter(Person.getInstance(), this));
  }

  /**
   * @param log payment list
   * @param entry tapped row
   * @param place row index
   * @param rowId id
   */
  @Override
  protected void onListItemClick(
      final ListView log, final View entry, final int place, final long rowId) {
    final LinearLayout line = (LinearLayout) entry;
    if (PaymentlogAdapter.isSelected(place)) {
      PaymentDetails.setOpen(line, false);
      line.setBackgroundColor(Color.argb(0, 0, 0, 0));
    } else {
      line.setBackgroundColor(
          Color.argb(
              RowSelection.TINT, // .
              0, // .
              RowSelection.TINT, // .
              0));
      PaymentDetails.setOpen(line, true);
    }
    PaymentlogAdapter.changeSelected(place);
  }

  /**
   * @param logMenu payment-log menu @return true
   */
  @Override
  public boolean onPrepareOptionsMenu(final Menu logMenu) {
    Menus.disable(logMenu, R.id.viewInternationalPayments);
    return super.onPrepareOptionsMenu(logMenu);
  }

  /**
   * @return payment-log options menu
   */
  @Override
  protected int optionsMenu() {
    return R.menu.paymentslogmenu;
  }
}
