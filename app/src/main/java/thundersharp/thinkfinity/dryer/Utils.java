package thundersharp.thinkfinity.dryer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Objects;

import thundersharp.thinkfinity.dryer.boot.utils.TimeUtils;
import thundersharp.thinkfinity.dryer.boot.serverStat.BootServerUtil;
import thundersharp.thinkfinity.dryer.boot.serverStat.OnServerStatus;

public class Utils {

    public static boolean validateInputs(EditText usernameEmailField, EditText passwordField) {
        boolean isValid = true;

        // Validate username/email (treated as email)
        String usernameEmail = usernameEmailField.getText().toString().trim();
        if (usernameEmail.isEmpty()) {
            usernameEmailField.setError("Username/Email cannot be empty");
            isValid = false;
        } else if (!isValidEmail(usernameEmail)) {
            usernameEmailField.setError("Invalid email address");
            isValid = false;
        }

        // Validate password
        String password = passwordField.getText().toString().trim();
        if (password.isEmpty()) {
            passwordField.setError("Password cannot be empty");
            isValid = false;
        } else if (password.length() < 6) {
            passwordField.setError("Password must be at least 6 characters");
            isValid = false;
        }

        return isValid;
    }

    private static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void performServerCheck(Context context){
            View layoutInflater = LayoutInflater.from(context).inflate(R.layout.dialog_server_stat,null);
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setView(layoutInflater)
                    .setCancelable(false)
                    .create();
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView info = layoutInflater.findViewById(R.id.info);
            AppCompatButton close = layoutInflater.findViewById(R.id.close);
            ProgressBar progressBar = layoutInflater.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            StringBuilder builder = new StringBuilder();
            builder.append("[INFO]  Starting ping process").append("\n\n");
            builder.append("[INFO]  Pinging host on /alive").append("\n\n");
            builder.append("[INFO]  Request sent waiting for response...").append("\n\n");

            BootServerUtil
                    .getInstance(context)
                    .checkIfServerIsReachable(new OnServerStatus() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onServerAlive(String time) {
                            builder.append("[DATA]  Response received server reachable, Time : ")
                                    .append(TimeUtils.getTimeFromTimeStamp(time))
                                    .append("\n\n")
                                    .append("[RAW] Raw data is :").append(time);
                            close.setText("CLOSE");
                            info.setText(builder.toString());
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onServerUnReachable(Exception e) {
                            builder.append("[ERROR] ").append(e.getMessage());
                            info.setText(builder.toString());
                        }
                    });

            info.setText(builder.toString());
            if (close != null) {
                close.setOnClickListener((d)-> alertDialog.dismiss());
            }

            alertDialog.show();

    }

    public static void startActivity(Activity context, Class<?> cls){
        context.startActivity(new Intent(context, cls));
        context.finish();
    }
}
