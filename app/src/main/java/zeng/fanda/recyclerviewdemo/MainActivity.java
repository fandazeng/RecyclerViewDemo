package zeng.fanda.recyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zeng.fanda.recyclerviewdemo.adapter.TestAdapter;
import zeng.fanda.recyclerviewdemo.bean.TestBean;
import zeng.fanda.recyclerviewdemo.widget.GridItemDecoration;
import zeng.fanda.recyclerviewdemo.widget.LinearItemDecoration;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_test)
    RecyclerView mRecyclerTest;

    TestAdapter mTestAdapter;

    private List<TestBean> mTestBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTestData();
        initRecyclerView();
    }

    private void initTestData() {
        mTestBeanList = new ArrayList<>();
        for (int i = 0 ; i<5;i++) {
            mTestBeanList.add(new TestBean(i));
        }
    }

    private void initRecyclerView() {
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerTest.setLayoutManager(layoutManager);
//        mRecyclerTest.addItemDecoration(new GridItemDecoration.Builder(this).setSpan(20).setShowLastLine(true).build());
        mRecyclerTest.addItemDecoration(new LinearItemDecoration.Builder(this).build());
        mRecyclerTest.setItemAnimator(new DefaultItemAnimator());
        mTestAdapter = new TestAdapter(this, mTestBeanList);
        mRecyclerTest.setAdapter(mTestAdapter);
        mTestAdapter.setOnItemClickListener(new TestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_SHORT).show();
                mTestAdapter.removeItem(position);
            }
        });
    }

    @OnClick({R.id.btn_remove})
    public void onClick(View view){
            switch (view.getId()){
                case R.id.btn_remove:
                    mTestAdapter.removeItem(1);
                    break;
                default:
                    break;
            }
    }
}
