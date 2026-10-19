package ie.aib;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import ie.aib.entities.Person;

/** Payee list. */
public final class PayeesActivity extends BankingList {
  /**
   * @param state unused framework state
   */
  @Override
  public void onCreate(final Bundle state) {
    super.onCreate(state);
    this.setContentView(R.layout.payeesview);
    this.arm("Manage My Payees");
    this.getListView();
    this.setListAdapter(new PayeesAdapter(Person.getInstance(), this));
  }

  /**
   * @param list payees
   * @param row tapped payee
   * @param index payee index
   * @param rowId row id
   */
  @Override
  protected void onListItemClick(
      final ListView list, final View row, final int index, final long rowId) {
    RowSelection.show(
        list, // .
        row, // .
        index, // .
        PayeesAdapter.getSelected(), // .
        R.id.payeeRowOptions, // .
        PayeesAdapter.sink());
  }

  /**
   * @param menu payee menu @return true
   */
  @Override
  public boolean onPrepareOptionsMenu(final Menu menu) {
    Menus.disable(
        menu, // .
        R.id.addnewpayeeoption, // .
        R.id.addnewbilloption, // .
        R.id.addnewcreditcardoption);
    return super.onPrepareOptionsMenu(menu);
  }

  /**
   * @return payee options menu
   */
  @Override
  protected int optionsMenu() {
    return R.menu.payeemenu;
  }
}
