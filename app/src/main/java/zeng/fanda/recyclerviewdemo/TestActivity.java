package zeng.fanda.recyclerviewdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zeng.fanda.recyclerviewdemo.adapter.TestAdapter;
import zeng.fanda.recyclerviewdemo.bean.TestBean;
import zeng.fanda.recyclerviewdemo.widget.LinearItemDecoration;
import zeng.fanda.recyclerviewdemo.widget.MyRecyclerView;
import zeng.fanda.recyclerviewdemo.widget.MyStickyLayout;

/**
 * @author 曾凡达
 * @date 2018/6/13
 */
public class TestActivity extends AppCompatActivity {

    @BindView(R.id.sticky_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.sticky_layout)
    MyStickyLayout mStickyLayout;

    TestAdapter mTestAdapter;
    private List<TestBean> mTestBeanList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initTestData();
        initRecyclerView();
        mStickyLayout.setTopTouchEventListener(new MyStickyLayout.TopTouchEventListener() {
            @Override
            public boolean isBottomViewTop() {
                return isRecyclerViewTop(mRecyclerView);
            }
        });
    }

    private boolean isRecyclerViewTop(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null || (firstVisibleItemPosition == 0 && childAt.getTop() >= 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new LinearItemDecoration.Builder(this).build());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTestAdapter = new TestAdapter(this, mTestBeanList);
        mRecyclerView.setAdapter(mTestAdapter);
        mTestAdapter.setOnItemClickListener(new TestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(TestActivity.this, "ksks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initTestData() {
        mTestBeanList = new ArrayList<>();
        for (int i = 0 ; i<15;i++) {
            mTestBeanList.add(new TestBean(i));
        }
    }
}
