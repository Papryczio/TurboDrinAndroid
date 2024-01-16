package com.example.nalewak.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.util.Log;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import com.example.nalewak.R;
import com.google.gson.JsonParser;

import java.util.LinkedList;
import java.util.Objects;

public class Control extends Fragment {

    public static LinkedList<String> requests = new LinkedList<>();
    public static String requestCallback = "";

    private SeekBar SEEKBAR_LIQUID1;
    private SeekBar SEEKBAR_LIQUID2;
    private TextView TEXTVIEW_LIQUID1;
    private TextView TEXTVIEW_LIQUID2;
    private ProgressBar PROGRESSBAR_LIQUID1;
    private ProgressBar PROGRESSBAR_LIQUID2;

    public Control() {
        // Required empty public constructor
    }

    public static Control newInstance(String param1, String param2) {
        Control fragment = new Control();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);

        // GUI elements
        Button BUTTON_START = rootView.findViewById(R.id.button_start);
        Button BUTTON_ABORT = rootView.findViewById(R.id.button_abort);
        SEEKBAR_LIQUID1 = rootView.findViewById(R.id.seekBar_liquid1);
        SEEKBAR_LIQUID2 = rootView.findViewById(R.id.seekBar_liquid2);
        TEXTVIEW_LIQUID1 = rootView.findViewById(R.id.textView_liquid1);
        TEXTVIEW_LIQUID2 = rootView.findViewById(R.id.textView_liquid2);
        PROGRESSBAR_LIQUID1 = rootView.findViewById(R.id.progressBar1);
        PROGRESSBAR_LIQUID2 = rootView.findViewById(R.id.progressBar2);

        // Listeners
        BUTTON_START.setOnClickListener(view -> {
            int liquid_1 = SEEKBAR_LIQUID1.getProgress();
            int liquid_2 = SEEKBAR_LIQUID2.getProgress();

            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("Action", "START_AUTO_PROGRAM");
            jsonRequest.addProperty("Liquid_1", liquid_1);
            jsonRequest.addProperty("Liquid_2", liquid_2);
            requests.add(jsonRequest.toString());
            Log.d("Control", jsonRequest.toString());

            while (Objects.equals(requestCallback, "")) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Log.d("Control", requestCallback);
            JsonObject jsonObject = JsonParser.parseString(requestCallback).getAsJsonObject();
            String action = jsonObject.get("Action").getAsString();

            switch (action) {
                case "START_AUTO_PROGRAM_SUCCESS":
                    double time_1 = jsonObject.get("Time_1").getAsDouble();
                    double time_2 = jsonObject.get("Time_2").getAsDouble();

                    new CountdownTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (long) 1, (long) (time_1 * 1000));
                    new CountdownTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (long) 2, (long) (time_2 * 1000));

                    PROGRESSBAR_LIQUID1.setMax((int) time_1 * 1000);
                    PROGRESSBAR_LIQUID2.setMax((int) time_2 * 1000);

                    Toast.makeText(this.getContext(), "Be patient, you're drink is being prepared.", Toast.LENGTH_LONG).show();

                    Log.d("Control", action + " " + time_1 + " " + time_2);
                    break;
                case "START_AUTO_PROGRAM_FAILURE":
                    String message = jsonObject.get("Message").getAsString();
                    switch (message) {
                        case "GLASS_NOT_DETECTED":
                            Toast.makeText(this.getContext(), "Glass has not been detected", Toast.LENGTH_LONG).show();
                            break;
                        case "PROGRAM_RUNNING":
                            Toast.makeText(this.getContext(), "Another program is running", Toast.LENGTH_LONG).show();
                            break;
                    }
                    Log.d("Control", action + " " + message);
                    break;
                default:

            }
            requestCallback = "";
        });

        BUTTON_ABORT.setOnClickListener(view -> {
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("Action", "ABORT_AUTO_PROGRAM");
            requests.add(jsonRequest.toString());
            Log.d("Control_fragment", jsonRequest.toString());
        });

        SEEKBAR_LIQUID1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TEXTVIEW_LIQUID1.setText(String.format("Liquid#1: %dml", i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SEEKBAR_LIQUID2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TEXTVIEW_LIQUID2.setText(String.format("Liquid#1: %dml", i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return rootView;
    }

    /**
     * Creates Async task that counts up to filling time (based on ESP32 BT response).
     * param[0] - order number of the progressBar to be updated (1 / 2)
     * param[1] - time to be counted up to.
     */
    private class CountdownTask extends AsyncTask<Long, Long, Void> {

        /**
         * Countdown duration increment value differs from Thread.sleep due to inaccuracy in count.
         * Average of measures was taken to define 65ms as most accurate value for iteration.
         */
        @Override
        protected Void doInBackground(Long... params) {
            long countdownDuration = 0;
            long countdownMaxTime = params[1];
            long progressBar = params[0];

            // Countdown logic
            while (countdownDuration <= countdownMaxTime) {
                publishProgress(countdownDuration, progressBar);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                countdownDuration += 65;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            // Update the TextView with the remaining time
            long secondsRemaining = values[0];
            int progressBar = values[1].intValue();
            switch (progressBar) {
                case 1:
                    PROGRESSBAR_LIQUID1.setProgress((int) secondsRemaining);
                    Log.d("Async#1", String.valueOf(secondsRemaining));
                    break;
                case 2:
                    PROGRESSBAR_LIQUID2.setProgress((int) secondsRemaining);
                    Log.d("Async#2", String.valueOf(secondsRemaining));
                    break;
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Code to be executed when the countdown finishes

        }
    }
}