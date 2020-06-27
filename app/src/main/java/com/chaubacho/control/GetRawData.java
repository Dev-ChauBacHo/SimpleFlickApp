package com.chaubacho.control;

import android.util.Log;
import android.util.MalformedJsonException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}

class GetRawData {
    private static final String TAG = "GetRawData";

    private DownloadStatus downloadStatus;

    GetRawData() {
    }

    String prepareData(String urlApi) {
        Log.d(TAG, "getAllData: starts");
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        if (urlApi == null) {
            downloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        try {
            downloadStatus = DownloadStatus.PROCESSING;

            URL url = new URL(urlApi);
            connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            Log.d(TAG, "getAllData: The response code was " + connection.getResponseCode());

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while (null != (line = reader.readLine())) {
                result.append(line).append("\n");
            }

            downloadStatus = DownloadStatus.OK;
            Log.d(TAG, "getAllData: JSON data: \n" + result);

            return result.toString();
        } catch (MalformedJsonException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "getAllData: IOException " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "getAllData: Security exception + " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "getAllData: Error closing steam " + e.getMessage());
                }
            }
        }

        downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
