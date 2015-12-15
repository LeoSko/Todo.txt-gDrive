package com.leosko.todotxt_gdrive.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.leosko.todotxt_gdrive.ApiClientAsyncTask;
import com.leosko.todotxt_gdrive.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.FormatFlagsConversionMismatchException;

/**
 * Created by LeoSko on 14.12.2015.
 */
public class RemoteFileSync implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    MainActivity starter;
    private static final String TAG = "RemoteFileSync";

    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;

    /**
     * Next available request code.
     */
    protected static final int NEXT_AVAILABLE_REQUEST_CODE = 2;

    private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback;

    /**
     * Google API client.
     */
    private GoogleApiClient mGoogleApiClient;
    private DriveId existingFileDriveId = null;

    public RemoteFileSync(MainActivity starter)
    {
        this.starter = starter;
    }

    class MyRunnable implements Runnable
    {
        RemoteFileSync from;
        DriveId file;

        public MyRunnable(RemoteFileSync from, DriveId existingFile)
        {
            this.from = from;
            this.file = existingFile;
        }

        @Override
        public void run()
        {
            mGoogleApiClient = new GoogleApiClient.Builder(starter).addApi(Drive.API)
                                                                   .addScope(Drive.SCOPE_FILE)
                                                                   .addConnectionCallbacks(from)
                                                                   .addOnConnectionFailedListener(from)
                                                                   .build();
            mGoogleApiClient.connect();

            if (existingFileDriveId == null)
            {
                driveContentsCallback = new ResultCallback<DriveApi.DriveContentsResult>()
                {
                    @Override
                    public void onResult(DriveApi.DriveContentsResult result)
                    {
                        if (!result.getStatus().isSuccess())
                        {
                            showMessage("Error while trying to create new file contents");
                            return;
                        }
                        final DriveContents driveContents = result.getDriveContents();

                        final ResultCallback<DriveFolder.DriveFileResult> fileCallback = new ResultCallback<DriveFolder.DriveFileResult>()
                        {
                            @Override
                            public void onResult(DriveFolder.DriveFileResult result)
                            {
                                if (!result.getStatus().isSuccess())
                                {
                                    showMessage("Error while trying to create the file");
                                    return;
                                }
                                showMessage("Created a file Todo.txt on Google Drive");
                            }
                        };

                        // Perform I/O off the current thread.
                        new Thread()
                        {
                            @Override
                            public void run()
                            {
                                // write content to DriveContents
                                OutputStream outputStream = driveContents.getOutputStream();
                                Writer writer = new OutputStreamWriter(outputStream);
                                try
                                {
                                    for (Task task : MainActivity.model.getTasks())
                                    {
                                        writer.write(task.getRawText() + '\n');
                                    }
                                    writer.close();
                                }
                                catch (IOException e)
                                {
                                    Log.e(TAG, e.getMessage());
                                }

                                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                        .setTitle(LocalFileSync.TODO_FILE).setMimeType("text/plain")
                                        .setStarred(true).build();

                                // create a file on root folder
                                Drive.DriveApi.getRootFolder(getGoogleApiClient())
                                              .createFile(getGoogleApiClient(), changeSet, driveContents)
                                              .setResultCallback(fileCallback);
                            }
                        }.start();

                    }

                };
            }
            else
            {
                //here download goes

            }
        }
    }

    final private class RetrieveDriveFileContentsAsyncTask extends ApiClientAsyncTask<DriveId, Boolean, String>
    {

        public RetrieveDriveFileContentsAsyncTask(Context context)
        {
            super(context);
        }

        @Override
        protected String doInBackgroundConnected(DriveId... params)
        {
            String contents = null;
            DriveFile file = params[0].asDriveFile();
            DriveApi.DriveContentsResult driveContentsResult = file
                    .open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();
            if (!driveContentsResult.getStatus().isSuccess())
            {
                return null;
            }
            DriveContents driveContents = driveContentsResult.getDriveContents();
            BufferedReader reader = new BufferedReader(new InputStreamReader(driveContents
                    .getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            try
            {
                while ((line = reader.readLine()) != null)
                {
                    builder.append(line);
                }
                contents = builder.toString();
            }
            catch (IOException e)
            {
                Log.e(TAG, "IOException while reading from the stream", e);
            }

            driveContents.discard(getGoogleApiClient());
            return contents;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if (result == null)
            {
                showMessage("Error while reading from the file");
                return;
            }
            showMessage("File contents: " + result);
        }
    }

    public void performSync()
    {
        //start another thread for this
        MyRunnable myRunnable = new MyRunnable(this, existingFileDriveId);
        new Thread(myRunnable).run();
    }

    public void onConnected(Bundle connectionHint)
    {
        //try getting existing file
        final ResultCallback<DriveApi.MetadataBufferResult> metadataResult = new ResultCallback<DriveApi.MetadataBufferResult>()
        {
            @Override
            public void onResult(DriveApi.MetadataBufferResult result)
            {
                if (!result.getStatus().isSuccess())
                {
                    showMessage("Problem while retrieving files");
                    return;
                }
                existingFileDriveId = null;
                for (Metadata md : result.getMetadataBuffer())
                {
                    // we can find it right here!
                    if (md.getTitle().equals(LocalFileSync.TODO_FILE))
                    {
                        //check timestamps and choose what to use
                        if (md.getModifiedDate().after(LocalFileSync.lastModifiedDate()))
                        {
                            //then use remote file - download it first, then remove existing file and rename downloaded one

                        }
                        else
                        {
                            //then use local copy of file - upload it
                            //TEMP COMMENTED
                            //existingFileDriveId = md.getDriveId();
                            // create new contents resource
                            Drive.DriveApi.newDriveContents(getGoogleApiClient())
                                          .setResultCallback(driveContentsCallback);
                        }
                    }
                }
                //showMessage("Successfully viewed all files.");
            }
        };
        final ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>()
        {
            @Override
            public void onResult(DriveApi.DriveIdResult result)
            {
                if (!result.getStatus().isSuccess())
                {
                    showMessage("Cannot find DriveId. Are you authorized to view this file?");
                    return;
                }
                DriveId driveId = result.getDriveId();
                DriveFolder folder = driveId.asDriveFolder();
                folder.listChildren(getGoogleApiClient()).setResultCallback(metadataResult);
            }
        };
        //try looking for existing file
        Drive.DriveApi.fetchDriveId(getGoogleApiClient(), Drive.DriveApi
                .getRootFolder(getGoogleApiClient()).getDriveId().getResourceId())
                      .setResultCallback(idCallback);
    }

    /**
     * Called when {@code mGoogleApiClient} is disconnected.
     */
    public void onConnectionSuspended(int cause)
    {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    /**
     * Called when {@code mGoogleApiClient} is trying to connect but failed.
     * Handle {@code result.getResolution()} if there is a resolution is
     * available.
     */
    public void onConnectionFailed(ConnectionResult result)
    {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution())
        {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(starter, result.getErrorCode(), 0)
                                 .show();
            return;
        }
        try
        {
            result.startResolutionForResult(starter, REQUEST_CODE_RESOLUTION);
        }
        catch (IntentSender.SendIntentException e)
        {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    /**
     * Shows a toast message.
     */
    public void showMessage(String message)
    {
        Toast.makeText(starter, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Getter for the {@code GoogleApiClient}.
     */
    public GoogleApiClient getGoogleApiClient()
    {
        return mGoogleApiClient;
    }


    /**
     * Handles resolution callbacks.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == Activity.RESULT_OK)
        {
            mGoogleApiClient.connect();
        }
    }
}
