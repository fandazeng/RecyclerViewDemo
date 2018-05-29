package zeng.fanda.recyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        for (int i = 0 ; i<10;i++) {
            mTestBeanList.add(new TestBean());
        }
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerTest.setLayoutManager(gridLayoutManager);
        mRecyclerTest.addItemDecoration(new GridItemDecoration.Builder(this).setSpan(20).setShowLastLine(true).build());
//        mRecyclerTest.addItemDecoration(new LinearItemDecoration.Builder(this).build());
        mTestAdapter = new TestAdapter(this, mTestBeanList);
        mRecyclerTest.setAdapter(mTestAdapter);
    }
}
