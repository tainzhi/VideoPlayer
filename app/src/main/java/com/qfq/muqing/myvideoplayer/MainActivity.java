package com.qfq.muqing.myvideoplayer;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.qfq.muqing.myvideoplayer.adapters.StaggeredAdapter;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private static String TAG = "qfq" + "VideoPlayer/MainActivity";
    private Context mContext;
    private CharSequence mTitle;
    private RecyclerView mList;
    private StaggeredAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = getTitle();
        mContext = getApplicationContext();

        mList = (RecyclerView)findViewById(R.id.selection_list);
        mList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mList.addItemDecoration(new InsetDecoration(mContext));

        mList.getItemAnimator().setAddDuration(1000);
        mList.getItemAnimator().setChangeDuration(1000);
        mList.getItemAnimator().setMoveDuration(1000);
        mList.getItemAnimator().setRemoveDuration(1000);

        mAdapter = new StaggeredAdapter(mContext);
        mAdapter.setmOnItemClickListener(this);
        mList.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "To be delete", Toast.LENGTH_SHORT).show();
    }

}
