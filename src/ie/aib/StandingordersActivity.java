package ie.aib;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import ie.aib.entities.Person;

/** Standing-order list. */
public final class StandingordersActivity extends BankingList {
  /**
   * @param saved previous bundle, ignored
   */
  @Override
  public void onCreate(final Bundle saved) {
    super.onCreate(saved);
    this.setContentView(R.layout.paymentlogview);
    this.arm("Active standing orders");
    this.getListView();
    this.setListAdapter(new StandingordersAdapter(Person.getInstance(), this));
  }

  /**
   * @param orders order list
   * @param tapped row
   * @param slot row slot
   * @param rowId id
   */
  @Override
  protected void onListItemClick(
      final ListView orders, // .
      final View tapped, // .
      final int slot, // .
      final long rowId) {
    RowSelection.show(
        orders,
        tapped,
        slot,
        StandingordersAdapter.getSelected(),
        R.id.standingOrderRowOptions,
        StandingordersAdapter.sink());
  }

  /**
   * @param ordersMenu standing-order menu @return true
   */
  @Override
  public boolean onPrepareOptionsMenu(final Menu ordersMenu) {
    Menus.disable(ordersMenu, R.id.addNewStandingOrder);
    return super.onPrepareOptionsMenu(ordersMenu);
  }

  /**
   * @return standing-order options menu
   */
  @Override
  protected int optionsMenu() {
    return R.menu.standingordermenu;
  }
}
