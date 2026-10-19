package ie.aib;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

/** Payment-type picker. */
public final class MakePaymentActivity // payment picker
extends BankingScreen implements Runnable {
  /** How long pay-a-bill error toasts stay up. */
  private static final int TOAST_MS = 5000;

  /** Background job that opens pay-a-bill. */
  private static final int REQUEST_PAY_BILL = 1;

  /** Pay-a-bill progress dialog. */
  private ProgressDialog progress;

  /** Pay-a-bill worker. */
  private Thread thread;

  /** Pay-a-bill job code. */
  private int runnableRequest;

  /** Set when pay-a-bill is cancelled. */
  private boolean interrupt;

  /** Opens the statement after pay-a-bill data has loaded. */
  private final Handler payABillHandler =
      new Handler() {
        @Override
        public void handleMessage(final Message msg) {
          MakePaymentActivity.this.progress.dismiss();
          try {
            Toast.makeText(
                    MakePaymentActivity.this.getApplicationContext(),
                    msg.obj.toString(),
                    MakePaymentActivity.TOAST_MS)
                .show();
          } catch (final Exception ignored) {
            MakePaymentActivity.this.startActivity(
                new Intent(MakePaymentActivity.this, StatementActivity.class));
          }
        }
      };

  /**
   * Builds the payment-type list.
   *
   * @param savedInstanceState unused framework state
   */
  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.arm("Select payment option");
    this.setContentView(R.layout.makepaymentmain);
    final ArrayList<
            HashMap<
                String, // .
                String>>
        rows =
            new ArrayList<
                HashMap<
                    String, // .
                    String>>();
    this.addOption(
        rows,
        "I want to transfer money between my own AIB accounts",
        "Transfer money between the accounts on your Internet Banking");
    this.addOption(
        rows,
        "I want to pay a bill",
        "Pay a range of bills including credit cards,"
            + " gas, electricity, mobile and landline phones");
    final String roi =
        "Transfer money in euro to another" // .
            + " account within the Republic of Ireland";
    final String international =
        "Transfer money to an international" // .
            + " destination or in a foreign currency";
    final String standing =
        "Set up a regular payment to any" // .
            + " account within the Republic of Ireland";
    final String roiTitle = "I want to transfer money to another ROI account";
    this.addOption(rows, roiTitle, roi);
    final String worldTitle = "I want to transfer money internationally";
    this.addOption(rows, worldTitle, international);
    final String orderTitle = "I want to set up a standing order";
    this.addOption(rows, orderTitle, standing);
    final ListView list = (ListView) this.findViewById(R.id.paymenttypes);
    list.setAdapter(
        new SimpleAdapter(
            this,
            rows,
            R.layout.makepaymentlist,
            new String[] {"header", "description"},
            new int[] {R.id.makePaymentHeader, R.id.makePaymentDescription}));
  }

  private void addOption(
      final ArrayList<HashMap<String, String>> rows,
      final String header,
      final String description) {
    final HashMap<String, String> map = new HashMap<String, String>();
    map.put("header", header);
    map.put("description", description);
    rows.add(map);
  }

  /** Loads pay-a-bill data. */
  @Override
  public void run() {
    if (this.runnableRequest == REQUEST_PAY_BILL) {
      this.createCancelProgressDialog(
          "Pay a bill", // .
          "Please standby while we fetch your, 'pay a bill' options");
      Forms.fetchStatement();
      if (!this.interrupt) {
        this.payABillHandler.sendEmptyMessage(0);
      }
    }
  }

  private void createCancelProgressDialog(
      final String title, // .
      final String message) {
    this.progress =
        CancelDialog.show(
            this,
            title,
            message,
            new Runnable() {
              public void run() {
                MakePaymentActivity.this.interrupt = true;
                MakePaymentActivity.this.thread.interrupt();
              }
            });
  }

  /**
   * Disables the unwired payment action.
   *
   * @param menu open menu
   * @return true
   */
  @Override
  public boolean onPrepareOptionsMenu(final Menu menu) {
    Menus.disable(menu, R.id.paymentOption);
    return super.onPrepareOptionsMenu(menu);
  }

  /**
   * Creates the payment-options menu.
   *
   * @param menu menu to fill
   * @return true
   */
  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    return Menus.inflate(this, menu, R.menu.paymentoptionsmenu);
  }
}
