package com.example.nalewak.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.nalewak.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.LinkedList;
import java.util.Objects;

public class ManualControl extends Fragment {

    public static LinkedList<String> requests = new LinkedList<>();
    public static String requestCallback = "";

    public ManualControl() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManualControl.
     */
    // TODO: Rename and change types and number of parameters
    public static ManualControl newInstance(String param1, String param2) {
        ManualControl fragment = new ManualControl();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_manual_control, container, false);

        Button BUTTON_START_MOTOR1 = rootView.findViewById(R.id.buttonStartMotor1);
        Button BUTTON_KILL_MOTOR1 = rootView.findViewById(R.id.buttonKillMotor1);
        Button BUTTON_START_MOTOR2 = rootView.findViewById(R.id.buttonStartMotor2);
        Button BUTTON_KILL_MOTOR2 = rootView.findViewById(R.id.buttonKillMotor2);
        Button BUTTON_START_MOTORS = rootView.findViewById(R.id.buttonStartMotors);
        Button BUTTON_KILL_MOTORS = rootView.findViewById(R.id.buttonKillMotors);
        Button BUTTON_PREPUMP1 = rootView.findViewById(R.id.buttonPrePump1);
        Button BUTTON_PREPUMP2 = rootView.findViewById(R.id.buttonPrePump2);

        BUTTON_START_MOTOR1.setOnClickListener(view -> {
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("Action", "START_MOTOR1");
            requests.add(jsonRequest.toString());

            Log.d("ManualControl_fragment", jsonRequest.toString());

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
                case "START_MOTOR1_SUCCESS":
                    Toast.makeText(this.getContext(), "MOTOR #1 TURNED ON", Toast.LENGTH_LONG).show();
                    break;
                case "START_MOTOR1_FAILURE":
                    String message = jsonObject.get("Message").getAsString();
                    if (Objects.equals(message, "GLASS_NOT_DETECTED")) {
                        Toast.makeText(this.getContext(), "Glass has not been detected", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this.getContext(), "Unknown error occurred", Toast.LENGTH_LONG).show();
                    }
                    break;
            }

            requestCallback = "";
        });

        BUTTON_START_MOTOR2.setOnClickListener(view -> {
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("Action", "START_MOTOR2");
            requests.add(jsonRequest.toString());

            Log.d("ManualControl_fragment", jsonRequest.toString());

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

            if (Objects.equals(action, "KILL_MOTOR1_SUCCESS")) {
                Toast.makeText(this.getContext(), "MOTOR #1 KILLED", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.getContext(), "Unknown error occurred", Toast.LENGTH_LONG).show();
            }

            requestCallback = "";
        });

        BUTTON_KILL_MOTOR1.setOnClickListener(view -> {
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("Action", "KILL_MOTOR1");
            requests.add(jsonRequest.toString());

            Log.d("ManualControl_fragment", jsonRequest.toString());

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
                case "START_MOTOR2_SUCCESS":
                    Toast.makeText(this.getContext(), "MOTOR #2 TURNED ON", Toast.LENGTH_LONG).show();
                    break;
                case "START_MOTOR2_FAILURE":
                    String message = jsonObject.get("Message").getAsString();
                    if (Objects.equals(message, "GLASS_NOT_DETECTED")) {
                        Toast.makeText(this.getContext(), "Glass has not been detected", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this.getContext(), "Unknown error occurred", Toast.LENGTH_LONG).show();
                    }
                    break;
            }

            requestCallback = "";
        });

        BUTTON_KILL_MOTOR2.setOnClickListener(view -> {
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("Action", "KILL_MOTOR2");
            requests.add(jsonRequest.toString());

            Log.d("ManualControl_fragment", jsonRequest.toString());

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

            if (Objects.equals(action, "KILL_MOTOR2_SUCCESS")) {
                Toast.makeText(this.getContext(), "MOTOR #2 KILLED", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.getContext(), "Unknown error occurred", Toast.LENGTH_LONG).show();
            }

            requestCallback = "";
        });

        BUTTON_START_MOTORS.setOnClickListener(view -> {
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("Action", "START_MOTOR12");
            requests.add(jsonRequest.toString());

            Log.d("ManualControl_fragment", jsonRequest.toString());

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
                case "START_MOTOR12_SUCCESS":
                    Toast.makeText(this.getContext(), "MOTORS TURNED ON", Toast.LENGTH_LONG).show();
                    break;
                case "START_MOTOR12_FAILURE":
                    String message = jsonObject.get("Message").getAsString();
                    if (Objects.equals(message, "GLASS_NOT_DETECTED")) {
                        Toast.makeText(this.getContext(), "Glass has not been detected", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this.getContext(), "Unknown error occurred", Toast.LENGTH_LONG).show();
                    }
                    break;
            }

            requestCallback = "";
        });

        BUTTON_KILL_MOTORS.setOnClickListener(view -> {
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("Action", "KILL_MOTOR12");
            requests.add(jsonRequest.toString());

            Log.d("ManualControl_fragment", jsonRequest.toString());

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

            if (Objects.equals(action, "KILL_MOTOR12_SUCCESS")) {
                Toast.makeText(this.getContext(), "MOTORS KILLED", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.getContext(), "Unknown error occurred", Toast.LENGTH_LONG).show();
            }

            requestCallback = "";
        });

        BUTTON_PREPUMP1.setOnClickListener(view -> {
            int liquid_1 = 5;

            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("Action", "START_AUTO_PROGRAM");
            jsonRequest.addProperty("Liquid_1", liquid_1);
            jsonRequest.addProperty("Liquid_2", 0);
            requests.add(jsonRequest.toString());

            Log.d("ManualControl_fragment", jsonRequest.toString());

            while (Objects.equals(requestCallback, "")) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            JsonObject jsonObject = JsonParser.parseString(requestCallback).getAsJsonObject();
            String action = jsonObject.get("Action").getAsString();

            switch (action) {
                case "START_AUTO_PROGRAM_SUCCESS":
                    Log.d("ManualControl", "PrePump activated");
                    Toast.makeText(this.getContext(), "Machine is getting ready by prepumping liquid #1", Toast.LENGTH_LONG).show();
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
                    Log.d("ManualControl", action + " " + message);
                    break;
            }

            requestCallback = "";
        });

        BUTTON_PREPUMP2.setOnClickListener(view -> {
            int liquid_2 = 5;

            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("Action", "START_AUTO_PROGRAM");
            jsonRequest.addProperty("Liquid_1", 0);
            jsonRequest.addProperty("Liquid_2", liquid_2);
            requests.add(jsonRequest.toString());

            Log.d("ManualControl_fragment", jsonRequest.toString());

            while (Objects.equals(requestCallback, "")) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            JsonObject jsonObject = JsonParser.parseString(requestCallback).getAsJsonObject();
            String action = jsonObject.get("Action").getAsString();

            switch (action) {
                case "START_AUTO_PROGRAM_SUCCESS":
                    Log.d("ManualControl", "PrePump activated");
                    Toast.makeText(this.getContext(), "Machine is getting ready by prepumping liquid #2", Toast.LENGTH_LONG).show();
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
                    Log.d("ManualControl", action + " " + message);
                    break;
            }

            requestCallback = "";
        });

        return rootView;
    }
}