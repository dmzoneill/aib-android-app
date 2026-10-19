package ie.aib;

import android.app.Activity;
import android.app.Service;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/** Shared digit filtering for the login and quick-pay fields. */
public final class Digits {
  /** Focus the next view, then hide the keyboard. Used by registration. */
  public static final int FOCUS_THEN_HIDE = 1;

  /** Hide the keyboard, focus the next view, and show its keyboard. */
  public static final int SHOW_NEXT = 2;

  /** Hide the keyboard, then focus the next view. */
  public static final int HIDE_THEN_FOCUS = 3;

  /** Move on only after the text grows past the limit. */
  public static final int PASS_LIMIT = 4;

  /** At the limit, keep focus here and hide the following field. */
  public static final int HOLD_AND_HIDE = 5;

  /** Do not shorten the field. */
  public static final int KEEP = -1;

  private Digits() {
    super();
  }

  /**
   * Digits from {@code value}, or null when {@code value} is null.
   *
   * @param value raw text
   * @return digits only
   */
  public static String only(final String value) {
    if (value == null) {
      return null;
    }
    final StringBuffer buffer = new StringBuffer();
    for (int index = 0; index < value.length(); index++) {
      final char character = value.charAt(index);
      if (Character.isDigit(character)) {
        buffer.append(character);
      }
    }
    return buffer.toString();
  }

  /**
   * Whether every character in {@code value} is a digit.
   *
   * @param value raw text
   * @return true when the text is entirely digits
   */
  public static boolean onlyDigits(final String value) {
    if (value == null) {
      return false;
    }
    for (int index = 0; index < value.length(); index++) {
      if (!Character.isDigit(value.charAt(index))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Watches one numeric field.
   *
   * @param activity host screen
   * @param fieldId field to edit
   * @param limit length that advances focus
   * @param truncateTo kept length past the limit, or {@link #KEEP}
   * @param nextId view that receives focus
   * @param mode how focus and the keyboard move
   * @return text watcher
   */
  public static TextWatcher watcher(
      final Activity activity,
      final int fieldId,
      final int limit,
      final int truncateTo,
      final int nextId,
      final int mode) {
    return new TextWatcher() {
      public void afterTextChanged(final Editable editable) {
        editable.getClass();
      }

      public void beforeTextChanged(
          final CharSequence text, // .
          final int start, // .
          final int count, // .
          final int after) {
        text.getClass();
      }

      public void onTextChanged(
          final CharSequence text, // .
          final int start, // .
          final int before, // .
          final int count) {
        apply(activity, fieldId, limit, truncateTo, nextId, mode);
      }
    };
  }

  private static void apply(
      final Activity activity,
      final int fieldId,
      final int limit,
      final int truncateTo,
      final int nextId,
      final int mode) {
    final String service = Service.INPUT_METHOD_SERVICE;
    final Object serviceObject = activity.getSystemService(service);
    final InputMethodManager imm = (InputMethodManager) serviceObject;
    final EditText field = (EditText) activity.findViewById(fieldId);
    if (!onlyDigits(field.getText().toString())) {
      field.setText(only(field.getText().toString()));
    }
    final int counter = field.getText().toString().length();
    final View next = activity.findViewById(nextId);
    if (mode == PASS_LIMIT) {
      passLimit(imm, field, next, counter, limit);
    } else if (mode == HOLD_AND_HIDE) {
      holdAndHide(imm, field, next, counter, limit, truncateTo);
    } else if (counter == limit) {
      advance(imm, field, next, mode);
    } else if (counter > limit && truncateTo >= 0) {
      field.setText(field.getText().toString().substring(0, truncateTo));
    }
  }

  private static void passLimit(
      final InputMethodManager imm,
      final EditText field,
      final View next,
      final int counter,
      final int limit) {
    if (counter > limit) {
      imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
      next.requestFocus();
      imm.hideSoftInputFromWindow(next.getWindowToken(), 1);
    }
  }

  private static void holdAndHide(
      final InputMethodManager imm,
      final EditText field,
      final View next,
      final int counter,
      final int limit,
      final int truncateTo) {
    if (counter == limit) {
      imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
      field.requestFocus();
      imm.hideSoftInputFromWindow(next.getWindowToken(), 1);
    } else if (counter > limit && truncateTo >= 0) {
      field.setText(field.getText().toString().substring(0, truncateTo));
    }
  }

  private static void advance(
      final InputMethodManager imm, // .
      final EditText field, // .
      final View next, // .
      final int mode) {
    if (mode == FOCUS_THEN_HIDE) {
      next.requestFocus();
      imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
    } else if (mode == SHOW_NEXT) {
      imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
      next.requestFocus();
      imm.showSoftInput(next, 1);
    } else if (mode == HIDE_THEN_FOCUS) {
      imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
      next.requestFocus();
    }
  }
}
