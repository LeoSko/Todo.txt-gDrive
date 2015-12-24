package com.leosko.todotxt_gdrive.model;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.leosko.todotxt_gdrive.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

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

    private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallbackCreatingFile;

    /**
     * Google API client.
     */
    private GoogleApiClient mGoogleApiClient;

    public RemoteFileSync(MainActivity starter)
    {
        this.starter = starter;
    }

    class MyRunnable implements Runnable
    {
        RemoteFileSync from;

        public MyRunnable(RemoteFileSync from)
        {
            this.from = from;
        }

        @Override
        public void run()
        {
            mGoogleApiClient = new GoogleApiClient.Builder(starter)
                    .addApi(Drive.API)
                    //.addScope(new Scope("https://www.googleapis.com/auth/drive"))
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(from).addOnConnectionFailedListener(from).build();
            mGoogleApiClient.connect();

            driveContentsCallbackCreatingFile = new ResultCallback<DriveApi.DriveContentsResult>()
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
                            mGoogleApiClient.disconnect();
                            if (!result.getStatus().isSuccess())
                            {
                                showMessage("Error while trying to create the file");
                                return;
                            }
                            showMessage("Created a file Todo.txt on Google Drive");
                        }
                    };

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
            };
        }
    }

    public void performSync()
    {
        //start another thread for this
        MyRunnable myRunnable = new MyRunnable(this);
        new Thread(myRunnable).run();
    }

    public void onConnected(final Bundle connectionHint)
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

                for (Metadata md : result.getMetadataBuffer())
                {
                    // we can find it right here!
                    String orfn = md.getOriginalFilename();
                    String fn = md.getTitle();
                    String di = md.getDriveId().toString();
                    if (md.getTitle().equals(LocalFileSync.TODO_FILE) && !md.isTrashed())
                    {
                        Date local = LocalFileSync.lastModifiedDate();
                        Date remote = md.getModifiedDate();
                        showMessage("Local: " + local.toString() + ", remote: " + remote
                                .toString());
                        //check timestamps and choose what to use
                        if (remote.after(local))
                        {
                            //then use remote file - download it first, then remove existing file and rename downloaded one
                            // since we are already in another thread, perform downloading right here
                            //first create a callback on downloading finished
                            ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallbackForReading = new ResultCallback<DriveApi.DriveContentsResult>()
                            {
                                @Override
                                public void onResult(DriveApi.DriveContentsResult result)
                                {
                                    if (!result.getStatus().isSuccess())
                                    {
                                        // display an error saying file can't be opened
                                        showMessage("Can't open remote file for reading");
                                        return;
                                    }
                                    // DriveContents object contains pointers
                                    // to the actual byte stream
                                    DriveContents contents = result.getDriveContents();
                                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(contents
                                            .getInputStream()));
                                    StringBuilder builder = new StringBuilder();
                                    String line;
                                    try
                                    {
                                        while ((line = bufferedReader.readLine()) != null)
                                        {
                                            builder.append(line + '\n');
                                        }
                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    String contentsAsString = builder.toString();
                                    LocalFileSync.syncFromString(contentsAsString);
                                    MainActivity.lfs.load();
                                    showMessage("Downloaded updated todo.txt");
                                }
                            };
                            // now request downloading and wait for it (replace null with listener if needed)
                            md.getDriveId().asDriveFile()
                              .open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null)
                              .setResultCallback(contentsOpenedCallbackForReading);
                        }
                        else
                        {
                            //then use local copy of file - upload it
                            ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallbackForWriting = new ResultCallback<DriveApi.DriveContentsResult>()
                            {
                                @Override
                                public void onResult(DriveApi.DriveContentsResult result)
                                {
                                    if (!result.getStatus().isSuccess())
                                    {
                                        // display an error saying file can't be opened
                                        showMessage("Can't open remote file for reading");
                                        return;
                                    }
                                    // DriveContents object contains pointers
                                    // to the actual byte stream
                                    DriveContents contents = result.getDriveContents();
                                    // rewrite remote file
                                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(contents
                                            .getOutputStream()));
                                    try
                                    {
                                        String res = MainActivity.lfs.getRawString();
                                        bufferedWriter.write(res);
                                        bufferedWriter.flush();
                                        contents.commit(mGoogleApiClient, null)
                                                .setResultCallback(new ResultCallback<Status>()
                                                {
                                                    @Override
                                                    public void onResult(Status result)
                                                    {
                                                        if (result.isSuccess())
                                                        {
                                                            showMessage("Uploaded local changes");
                                                        }
                                                        else
                                                        {
                                                            showMessage("Failed to upload local changes");
                                                        }
                                                    }
                                                });
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            md.getDriveId().asDriveFile()
                              .open(mGoogleApiClient, DriveFile.MODE_WRITE_ONLY, null)
                              .setResultCallback(contentsOpenedCallbackForWriting);
                        }
                        return;
                    }
                }
                // create new contents resource
                Drive.DriveApi.newDriveContents(getGoogleApiClient())
                              .setResultCallback(driveContentsCallbackCreatingFile);
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
