package ie.aib;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ie.aib.web.WebHelper;

/** Registration-number step of login. */
public final class Login1 extends BankingScreen
    implements OnClickListener, // .
        Runnable {
  /** Digits required in a registration number. */
  private static final int REG_LENGTH = 8;

  /** How long an error toast stays up. */
  private static final int TOAST_MS = 5000;

  /** Live screen, used as an application context. */
  private static Login1 instance;

  /** Step-one progress dialog. */
  private ProgressDialog progress;

  /** Handles the result of step one. */
  private final Handler handler =
      new Handler() {
        @Override
        public void handleMessage(final Message msg) {
          Login1.this.progress.dismiss();
          Login1.this.finish();
          try {
            Toast.makeText(
                    Login1.this.getApplicationContext(), // .
                    msg.obj.toString(), // .
                    Login1.TOAST_MS)
                .show();
          } catch (final Exception ignored) {
            ignored.getClass();
          }
        }
      };

  /**
   * Application context captured from this screen.
   *
   * @return application context
   */
  public static Context getContext() {
    return Login1.instance.getApplicationContext();
  }

  /**
   * Builds the registration form.
   *
   * @param savedInstanceState unused framework state
   */
  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.login1);
    this.arm("Step 1 of 2");
    Login1.instance = this;
    final EditText regInput = Forms.edit(this, R.id.registrationNumber);
    regInput.addTextChangedListener(
        Digits.watcher(
            this,
            R.id.registrationNumber,
            REG_LENGTH,
            REG_LENGTH,
            R.id.loginButton,
            Digits.FOCUS_THEN_HIDE));
  }

  /**
   * Starts step one when the registration number is complete.
   *
   * @param view tapped view
   */
  public void onClick(final View view) {
    if (view.getId() == R.id.loginButton) {
      final EditText regInput = Forms.edit(this, R.id.registrationNumber);
      if (regInput.getText().toString().length() == REG_LENGTH) {
        this.progress =
            ProgressDialog.show(
                this,
                "Step 1 of 2",
                "Please standby while we start your banking session",
                true,
                false);
        new Thread(this).start();
      } else {
        Toast.makeText(
                Login1.this.getApplicationContext(),
                "Please enter your eight digit registration number",
                TOAST_MS)
            .show();
      }
    }
  }

  /** Submits the registration number. */
  @Override
  public void run() {
    final WebHelper httpHelper = WebHelper.getInstance();
    httpHelper.initHTTPClient();
    final EditText regText = Forms.edit(this, R.id.registrationNumber);
    if (httpHelper.login1(regText.getText().toString())) {
      Login1.this.handler.sendEmptyMessage(0);
      Login1.this.startActivity(new Intent(Login1.this, Login2.class));
    } else {
      final Message msg = new Message();
      msg.obj = httpHelper.getLastError();
      Login1.this.handler.sendMessage(msg);
    }
  }

  /**
   * Unused dialog callback required by {@link OnClickListener}.
   *
   * @param dialog dialog
   * @param which button
   */
  @Override
  public void onClick(final DialogInterface dialog, final int which) {
    dialog.getClass();
  }
}
