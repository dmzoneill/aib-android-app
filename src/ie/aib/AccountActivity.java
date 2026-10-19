package ie.aib;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ie.aib.entities.Person;
import ie.aib.web.WebHelper;

/** Account overview and quick pay. */
public final class AccountActivity extends BankingList implements Runnable {
  /** Fetch the selected statement. */
  private static final int REQUEST_STATEMENT = 1;

  /** Fetch payees. */
  private static final int REQUEST_PAYEES = 2;

  /** Fetch the payment log. */
  private static final int REQUEST_PAYMENTS = 3;

  /** Fetch standing orders. */
  private static final int REQUEST_STANDING = 4;

  /** Log out. */
  private static final int REQUEST_LOGOUT = 5;

  /** Open the payment options. */
  private static final int REQUEST_PAYMENT_OPTIONS = 6;

  /** Reload quick-pay account lists. */
  private static final int REQUEST_CHANGE_PAY = 10;

  /** Submit quick pay. */
  private static final int REQUEST_QUICK_PAY = 11;

  /** How long a toast stays up. */
  private static final int TOAST_MS = 5000;

  /** Euro field advances after this many digits. */
  private static final int EURO_LIMIT = 4;

  /** Cent field advances at this length and keeps one digit when longer. */
  private static final int CENT_LIMIT = 2;

  /** Digits kept in the cent field when it overflows. */
  private static final int CENT_KEEP = 1;

  /** Digits in the quick-pay PAC field. */
  private static final int PAC_LIMIT = 1;

  /** Spinner rows that represent a real account start after the prompt. */
  private static final int ACCOUNT_OFFSET = 1;

  /** Overview progress dialog. */
  private ProgressDialog progress;

  /** Overview background worker. */
  private Thread thread;

  /** Overview job code. */
  private int runnableRequest;

  /** Set when overview work is cancelled. */
  private boolean interrupt;

  /** Quick-pay type chosen in the first spinner. */
  private String quickPayType;

  /** True while a quick-pay list refresh is in flight. */
  private boolean quickPayWorking;

  /** Whether the quick-pay panel is on screen. */
  private boolean quickPayVisible;

  /** Delivers background results onto the UI thread. */
  private final Handler backgroundHandler =
      new Handler() {
        @Override
        public void handleMessage(final Message msg) {
          AccountActivity.this.finishRequest(msg);
        }
      };

  /**
   * Builds the overview.
   *
   * @param savedInstanceState unused framework state
   */
  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.accountview);
    this.arm(WebHelper.getInstance().getRegNumber());
    this.getListView();
    this.setListAdapter(new AccountAdapter(Person.getInstance(), this));
    this.watch(
        R.id.amountEuro, // .
        EURO_LIMIT, // .
        Digits.KEEP, // .
        R.id.amountCent, // .
        Digits.PASS_LIMIT);
    this.watch(
        R.id.amountCent, // .
        CENT_LIMIT, // .
        CENT_KEEP, // .
        R.id.quickPayPacDigit, // .
        Digits.HOLD_AND_HIDE);
    this.watch(
        R.id.quickPayPacDigit, // .
        PAC_LIMIT, // .
        PAC_LIMIT, // .
        R.id.quickPayButton, // .
        Digits.HIDE_THEN_FOCUS);
    this.prepareQuickPayFields();
    this.quickPayVisible = true;
  }

  private void watch(
      final int fieldId, // .
      final int limit, // .
      final int truncateTo, // .
      final int nextId, // .
      final int mode) {
    final EditText field = (EditText) this.findViewById(fieldId);
    field.addTextChangedListener(
        Digits.watcher(
            this, // .
            fieldId, // .
            limit, // .
            truncateTo, // .
            nextId, // .
            mode));
  }

  private void prepareQuickPayFields() {
    int selected = -1;
    final Spinner typeSpinner = (Spinner) this.findViewById(R.id.spinner1);
    final Spinner fromSpinner = (Spinner) this.findViewById(R.id.spinner2);
    final Spinner toSpinner = (Spinner) this.findViewById(R.id.spinner3);
    if (!this.quickPayWorking) {
      final ArrayAdapter<String> types =
          new ArrayAdapter<String>(
              this, // .
              Forms.DROP, // .
              WebHelper.getInstance().getPaymentTypes());
      types.setDropDownViewResource(Forms.DROP);
      typeSpinner.setAdapter(types);
      final WebHelper helper = WebHelper.getInstance();
      final String chosenType = helper.getPaymentTypesSelected();
      selected = Forms.number(chosenType);
      selected = (selected == -1 ? 0 : selected);
      typeSpinner.setSelection(selected);
      typeSpinner.setOnItemSelectedListener(
          new OnItemSelectedListener() {
            @Override
            public void onItemSelected(
                final AdapterView<?> p, // .
                final View v, // .
                final int n, // .
                final long row) {
              AccountActivity.this.onTypeSelected(n);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
              parent.getClass();
            }
          });
    } else {
      selected = typeSpinner.getSelectedItemPosition();
      this.quickPayWorking = false;
    }
    this.bindAccountSpinner(
        fromSpinner, // .
        WebHelper.getInstance().getPaymentFrom());
    fromSpinner.setEnabled(selected != 0);
    selected = Forms.number(WebHelper.getInstance().getPaymentFromSelected());
    selected = (selected == -1 ? 0 : selected + ACCOUNT_OFFSET);
    fromSpinner.setSelection(selected);
    fromSpinner.setOnItemSelectedListener(
        new OnItemSelectedListener() {
          @Override
          public void onItemSelected(
              final AdapterView<?> p, // .
              final View v, // .
              final int n, // .
              final long row) {
            AccountActivity.this.onFromSelected(position);
          }

          @Override
          public void onNothingSelected(final AdapterView<?> parent) {
            parent.getClass();
          }
        });
    this.bindAccountSpinner(toSpinner, WebHelper.getInstance().getPaymentTo());
    toSpinner.setOnItemSelectedListener(
        new OnItemSelectedListener() {
          @Override
          public void onItemSelected(
              final AdapterView<?> p, // .
              final View v, // .
              final int n, // .
              final long row) {
            AccountActivity.this.enableAmounts(position > 0);
          }

          @Override
          public void onNothingSelected(final AdapterView<?> parent) {
            parent.getClass();
          }
        });
    toSpinner.setEnabled(selected != 0);
    if (selected != 0) {
      this.enableAmounts(true);
    }
    selected = Forms.number(WebHelper.getInstance().getPaymentToSelected());
    selected = (selected == -1 ? 0 : selected + ACCOUNT_OFFSET);
    toSpinner.setSelection(selected);
    this.updateQuickPayPacDigit();
  }

  private void bindAccountSpinner(
      final Spinner spinner, // .
      final String[] values) {
    final ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(
            this, // .
            Forms.DROP, // .
            values);
    adapter.setDropDownViewResource(Forms.DROP);
    spinner.setAdapter(adapter);
  }

  private void onTypeSelected(final int position) {
    if (position > 0) {
      this.quickPayWorking = true;
      this.quickPayType = String.valueOf(position);
      this.thread = new Thread(this);
      this.runnableRequest = REQUEST_CHANGE_PAY;
      this.createCancelProgressDialog(
          "Fetching payment from options",
          "Please standby while we fetch a list of payment from accounts");
      this.thread.start();
    } else {
      this.enableViews(
          false,
          R.id.spinner2,
          R.id.spinner3,
          R.id.amountEuro,
          R.id.amountCent,
          R.id.quickPayPacDigit,
          R.id.quickPayButton);
    }
  }

  private void onFromSelected(final int position) {
    ((Spinner) this.findViewById(R.id.spinner3)).setEnabled(position > 0);
    if (position == 0) {
      this.enableAmounts(false);
    }
  }

  private void enableAmounts(final boolean enabled) {
    this.enableViews(
        enabled, // .
        R.id.amountEuro, // .
        R.id.amountCent, // .
        R.id.quickPayPacDigit, // .
        R.id.quickPayButton);
  }

  private void enableViews(final boolean enabled, final int... ids) {
    for (int index = 0; index < ids.length; index++) {
      this.findViewById(ids[index]).setEnabled(enabled);
    }
  }

  private void updateQuickPayPacDigit() {
    final TextView pacView = (TextView) this.findViewById(R.id.quickPayPacview);
    final String digit = WebHelper.getInstance().getQuickPayPacDigit();
    pacView.setText("PAC Digit " + digit);
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
                AccountActivity.this.interrupt = true;
                AccountActivity.this.thread.interrupt();
              }
            });
  }

  /**
   * @param list accounts @param view row @param position index @param id row id
   */
  @Override
  protected void onListItemClick(
      final ListView list, final View view, final int position, final long id) {
    this.runnableRequest = REQUEST_STATEMENT;
    StatementActivity.setAccountNumber(position);
    this.createCancelProgressDialog(
        "Requesting Statement", "Please standby while we fetch your statement");
    this.thread = new Thread(this);
    this.thread.start();
  }

  /**
   * @param menu overview menu @return true
   */
  @Override
  public boolean onPrepareOptionsMenu(final Menu menu) {
    Menus.disable(menu, R.id.mobileTopup);
    final MenuItem quickPayItem = menu.findItem(R.id.quickPay);
    if (this.quickPayVisible) {
      quickPayItem.setTitle("Hide quick pay");
    } else {
      quickPayItem.setTitle("Show quick pay");
    }
    return super.onPrepareOptionsMenu(menu);
  }

  /**
   * @return overview options menu
   */
  @Override
  protected int optionsMenu() {
    return R.menu.overviewmenu;
  }

  /**
   * Starts the job for the chosen overview action.
   *
   * @param item chosen item
   * @return true when handled
   */
  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    this.thread = new Thread(this);
    this.interrupt = false;
    switch (item.getItemId()) {
      case R.id.managePayees:
        this.startJob(
            REQUEST_PAYEES,
            "Fetching Payees",
            "Please standby while we fetch a list of your payees");
        return true;
      case R.id.paymentLog:
        this.startJob(
            REQUEST_PAYMENTS, // .
            "Fetching log", // .
            "Please standby while we fetch your payment log");
        return true;
      case R.id.standingOrders:
        this.startJob(
            REQUEST_STANDING,
            "Fetching standing orders",
            "Please standby while we fetch a list of your standing orders");
        return true;
      case R.id.logout:
        this.startJob(
            REQUEST_LOGOUT,
            "Closing AIB Session",
            "Please standby while you are logged out of AIB");
        return true;
      case R.id.makePayment:
        this.startJob(
            REQUEST_PAYMENT_OPTIONS,
            "Requesting payments options",
            "Please standby while fetch available payment options");
        return true;
      case R.id.quickPay:
        this.toggleQuickPay();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void startJob(
      final int request, // .
      final String title, // .
      final String message) {
    this.runnableRequest = request;
    this.createCancelProgressDialog(title, message);
    this.thread.start();
  }

  private void toggleQuickPay() {
    final int panelId = R.id.quickPayControlBar;
    final LinearLayout panel = (LinearLayout) this.findViewById(panelId);
    if (this.quickPayVisible) {
      panel.setVisibility(LinearLayout.GONE);
      this.quickPayVisible = false;
    } else {
      panel.setVisibility(LinearLayout.VISIBLE);
      this.quickPayVisible = true;
    }
  }

  /** Asks before logging out via the back key. */
  @Override
  public void onBackPressed() {
    new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Exit")
        .setMessage("Are you sure you want to exit?")
        .setPositiveButton(
            "yes",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(
                  final DialogInterface dialog, // .
                  final int which) {
                AccountActivity.this.thread = new Thread(AccountActivity.this);
                AccountActivity.this.runnableRequest = REQUEST_LOGOUT;
                AccountActivity.this.createCancelProgressDialog(
                    "Closing AIB Session", // .
                    "Please standby while you are logged out of AIB");
                AccountActivity.this.thread.start();
              }
            })
        .setNegativeButton("no", null)
        .show();
  }

  /** Runs the overview background jobs. */
  @Override
  public void run() {
    if (this.runnableRequest == REQUEST_STATEMENT) {
      Forms.fetchStatement();
      this.deliver(null);
    } else if (this.runnableRequest == REQUEST_PAYEES) {
      Person.getInstance().getPayees();
      this.deliver(null);
    } else if (this.runnableRequest == REQUEST_PAYMENTS) {
      Person.getInstance().getPayments();
      this.deliver(null);
    } else if (this.runnableRequest == REQUEST_STANDING) {
      Person.getInstance().getStandingorders();
      this.deliver(null);
    } else if (this.runnableRequest == REQUEST_LOGOUT) {
      WebHelper.getInstance().logout();
      this.deliver(null);
    } else if (this.runnableRequest == REQUEST_PAYMENT_OPTIONS) {
      WebHelper.getInstance().goToTransferAndPayments();
      this.deliver(null);
    } else if (this.runnableRequest == REQUEST_CHANGE_PAY) {
      WebHelper.getInstance().changeQuickPayOptions(this.quickPayType);
      this.deliver(null);
    } else if (this.runnableRequest == REQUEST_QUICK_PAY) {
      this.submitQuickPay();
    }
  }

  private void submitQuickPay() {
    final String euro = Forms.text(this, R.id.amountEuro);
    final String cent = Forms.text(this, R.id.amountCent);
    final String pac = Forms.text(this, R.id.quickPayPacDigit);
    final Spinner typeSpinner = (Spinner) this.findViewById(R.id.spinner1);
    final Spinner fromSpinner = (Spinner) this.findViewById(R.id.spinner2);
    final Spinner toSpinner = (Spinner) this.findViewById(R.id.spinner3);
    final String type = String.valueOf(typeSpinner.getSelectedItemPosition());
    final int fromPos = fromSpinner.getSelectedItemPosition();
    final int toPos = toSpinner.getSelectedItemPosition();
    final int fromIndex = fromPos - ACCOUNT_OFFSET;
    final int toIndex = toPos - ACCOUNT_OFFSET;
    final String from = String.valueOf(fromIndex);
    final String to = String.valueOf(toIndex);
    if (WebHelper.getInstance()
        .submitQuickPayOptions(
            from, // .
            to, // .
            euro, // .
            cent, // .
            pac, // .
            type)) {
      this.deliver("Transaction completed successfully");
    } else {
      this.deliver(WebHelper.getInstance().getLastError());
    }
  }

  private void deliver(final Object result) {
    if (!this.interrupt) {
      final Message msg = new Message();
      msg.what = this.runnableRequest;
      msg.obj = result;
      this.backgroundHandler.sendMessage(msg);
    }
  }

  private void finishRequest(final Message msg) {
    this.progress.dismiss();
    if (msg.what == REQUEST_QUICK_PAY) {
      ((Spinner) this.findViewById(R.id.spinner1)).setSelection(0);
    }
    try {
      Toast.makeText(
              this.getApplicationContext(), // .
              msg.obj.toString(), // .
              TOAST_MS)
          .show();
    } catch (final Exception ignored) {
      this.onRequestOk(msg.what);
    }
  }

  private void onRequestOk(final int action) {
    if (action == REQUEST_QUICK_PAY || action == REQUEST_CHANGE_PAY) {
      this.prepareQuickPayFields();
    } else if (action == REQUEST_LOGOUT) {
      this.finish();
    } else if (action == REQUEST_STATEMENT) {
      this.openNext(StatementActivity.class);
    } else if (action == REQUEST_PAYEES) {
      this.openNext(PayeesActivity.class);
    } else if (action == REQUEST_PAYMENTS) {
      this.openNext(PaymentlogActivity.class);
    } else if (action == REQUEST_STANDING) {
      this.openNext(StandingordersActivity.class);
    } else if (action == REQUEST_PAYMENT_OPTIONS) {
      this.openNext(MakePaymentActivity.class);
    }
  }

  private void openNext(final Class<?> screen) {
    this.updateQuickPayPacDigit();
    this.startActivity(new Intent(this, screen));
  }

  /**
   * Submits quick pay.
   *
   * @param view tapped view
   */
  public void onClick(final View view) {
    if (view.getId() == R.id.quickPayButton) {
      this.runnableRequest = REQUEST_QUICK_PAY;
      this.progress =
          ProgressDialog.show(
              this,
              "Quick pay processing",
              "Please standby while we complete your transaction",
              true,
              false);
      new Thread(this).start();
    }
  }
}
