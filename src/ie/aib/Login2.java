package ie.aib;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ie.aib.entities.Person;
import ie.aib.web.WebHelper;

/** PAC and challenge step of login. */
public final class Login2 extends BankingScreen
    implements OnClickListener, // .
        Runnable {
  /** Digits in one PAC box. */
  private static final int PAC_LENGTH = 1;

  /** Digits in the challenge answer. */
  private static final int CHALLENGE_LENGTH = 4;

  /** Home-phone challenge code. */
  private static final int CHALLENGE_HOME = 1;

  /** Work-phone challenge code. */
  private static final int CHALLENGE_WORK = 3;

  /** Mobile-phone challenge code. */
  private static final int CHALLENGE_MOBILE = 4;

  /** How long step-two error toasts stay up. */
  private static final int TOAST_MS = 5000;

  /** Step-two progress dialog. */
  private ProgressDialog progress;

  /** Handles a failed step two by returning to step one. */
  private final Handler handler =
      new Handler() {
        @Override
        public void handleMessage(final Message msg) {
          Login2.this.progress.dismiss();
          try {
            Toast.makeText(
                    Login2.this.getApplicationContext(), // .
                    msg.obj.toString(), // .
                    Login2.TOAST_MS)
                .show();
            Login2.this.startActivity(new Intent(Login2.this, Login1.class));
            Login2.this.finish();
          } catch (final Exception ignored) {
            ignored.getClass();
          }
        }
      };

  /**
   * Builds the PAC form.
   *
   * @param savedInstanceState unused framework state
   */
  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.login2);
    this.arm("Step 2 of 2");
    final WebHelper httpHelper = WebHelper.getInstance();
    final TextView firstPac = (TextView) this.findViewById(R.id.dpac1);
    final TextView secondPac = (TextView) this.findViewById(R.id.dpac2);
    final TextView thirdPac = (TextView) this.findViewById(R.id.dpac3);
    final TextView phrase = (TextView) this.findViewById(R.id.challengePhrase);
    firstPac.setText(String.valueOf(httpHelper.getPac1()));
    secondPac.setText(String.valueOf(httpHelper.getPac2()));
    thirdPac.setText(String.valueOf(httpHelper.getPac3()));
    phrase.setText(this.challenge(httpHelper));
    this.watch(R.id.pac1, R.id.pac2, PAC_LENGTH, Digits.SHOW_NEXT);
    this.watch(R.id.pac2, R.id.pac3, PAC_LENGTH, Digits.SHOW_NEXT);
    this.watch(R.id.pac3, R.id.challengeNumber, PAC_LENGTH, Digits.SHOW_NEXT);
    this.watch(
        R.id.challengeNumber, // .
        R.id.loginButton, // .
        CHALLENGE_LENGTH, // .
        Digits.HIDE_THEN_FOCUS);
  }

  private void watch(
      final int fieldId, // .
      final int nextId, // .
      final int limit, // .
      final int mode) {
    final EditText field = (EditText) this.findViewById(fieldId);
    field.addTextChangedListener(
        Digits.watcher(
            this, // .
            fieldId, // .
            limit, // .
            limit, // .
            nextId, // .
            mode));
  }

  private String challenge(final WebHelper httpHelper) {
    final int code = httpHelper.getChallenge();
    if (code == CHALLENGE_HOME) {
      return "Please enter the last four digits from your home phone number";
    } else if (code == CHALLENGE_WORK) {
      return "Please enter the last four digits from your work phone number";
    } else if (code == CHALLENGE_MOBILE) {
      return "Please enter the last four digits from your mobile phone number";
    }
    return "Please enter the last four digits from your primary AIB Visa Card";
  }

  /**
   * Submits step two.
   *
   * @param view tapped view
   */
  public void onClick(final View view) {
    if (view.getId() == R.id.loginButton) {
      this.progress =
          ProgressDialog.show(
              this, // .
              "Step 2 of 2", // .
              "Please standby while we complete your login", // .
              true, // .
              false);
      new Thread(this).start();
    }
  }

  /** Sends the PAC digits and challenge answer. */
  @Override
  public void run() {
    final WebHelper httpHelper = WebHelper.getInstance();
    final String pac1 = Forms.text(this, R.id.pac1);
    final String pac2 = Forms.text(this, R.id.pac2);
    final String pac3 = Forms.text(this, R.id.pac3);
    final String answer = Forms.text(this, R.id.challengeNumber);
    if (httpHelper.login2(pac1, pac2, pac3, answer)) {
      try {
        Login2.this.handler.sendEmptyMessage(0);
        Person.getInstance();
        Login2.this.startActivity(
            new Intent(
                Login2.this, // .
                AccountActivity.class));
        Login2.this.finish();
      } catch (final Exception ignored) {
        ignored.getClass();
      }
    } else {
      final Message msg = new Message();
      msg.obj = httpHelper.getLastError();
      Login2.this.handler.sendMessage(msg);
    }
  }

  /**
   * @param source ignored dialog @param button ignored button index
   */
  @Override
  public void onClick(final DialogInterface source, final int button) {
    source.getClass();
  }
}
