package com.gearoenix.dbexim;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.DataOutputStream;
import java.util.UUID;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static Thread th;

    private static final String DB_DIR = File.separator + "data" + File.separator + "data" +
            File.separator + "com.gearoenix.dbexim" + File.separator + "databases" +
            File.separator + "dbexim";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void copyFile(String src, String dst) {
        InputStream in;
        try {
            in = new FileInputStream(src);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        OutputStream out;
        try {
            out = new FileOutputStream(dst);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        byte[] buf = new byte[8192];
        int len;
        try {
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onExport(View view) {
        Log.d("fghjgdfvvbvbvcbnmnbvc", "kljjkljhhjkjhjkhjkhj");
        String exdir = Environment.getExternalStorageDirectory()+File.separator+"dbexim";
        File directory = new File(exdir);
        directory.mkdirs();
        copyFile(DB_DIR, exdir + File.separator + "db1");
        directory = new File(DB_DIR);
        byte[] bytes = new byte[(int) directory.length()];
        FileInputStream n;
        try {
            n = new FileInputStream(directory);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        try {
            n.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        send(bytes);
    }

    public void onImport(View view) {
        String imdir = Environment.getExternalStorageDirectory()+File.separator+"dbexim";
        File f = new File(DB_DIR);
        f.delete();
        copyFile(imdir + File.separator + "db1", DB_DIR);
    }

    private void send(byte [] data) {
        class SendThread implements Runnable {
            private byte [] data;
            @Override
            public void run() {
                URL url;
                try {
                    url = new URL("http://109.200.1.139:8082/api/test/filesNoContentType");
//                    url = new URL("http://10.0.3.2:8888");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return;
                }
                HttpURLConnection conn;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if (conn == null) {
                    return;
                }
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                conn.setUseCaches(false);
                try {
                    conn.setRequestMethod("POST");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                    return;
                }
                conn.setRequestProperty("Connection", "Keep-Alive");
                String boundary = "************";
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                String bin_part_header = "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"fileInput\"; filename=\"db\"\r\n" +
                        "Content-Type: application/octet-stream\r\n\r\n";
                String bin_part_footer = "\r\n--" + boundary + "--\r\n";
                DataOutputStream dos;
                try {
                    dos = new DataOutputStream(conn.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                try {
                    dos.writeBytes(bin_part_header);
                    dos.write(data);
                    dos.writeBytes(bin_part_footer);
                    dos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                InputStream ins;
                byte[] buffer = new byte[8 * 1024];
                byte[] response = new byte[0];
                try {
                    ins = conn.getInputStream();
                    for (int read = ins.read(buffer); read != -1; read = ins.read(buffer)) {
                        byte[] tmp = new byte[response.length + read];
                        System.arraycopy(response, 0, tmp, 0, response.length);
                        System.arraycopy(buffer, 0, tmp, response.length, read);
                        response = tmp;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                return;
            }
        }
        SendThread st = new SendThread();
        st.data = data;
        th = new Thread(st);
        th.start();
    }

    public void onDbCreate(View v) {
        SQLiteDatabase database = (new DBCreator(this)).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", "1111111111");
        database.insert("alaki", null, values);
        values.put("name", "2222222222");
        database.insert("alaki", null, values);
        values.put("name", "3333333333");
        database.insert("alaki", null, values);
        values.put("name", "4444444444");
        database.insert("alaki", null, values);
    }
}
