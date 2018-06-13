package zeng.fanda.recyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zeng.fanda.recyclerviewdemo.adapter.TestAdapter;
import zeng.fanda.recyclerviewdemo.bean.TestBean;
import zeng.fanda.recyclerviewdemo.utils.DpUtil;
import zeng.fanda.recyclerviewdemo.widget.GridItemDecoration;
import zeng.fanda.recyclerviewdemo.widget.LinearItemDecoration;
import zeng.fanda.recyclerviewdemo.widget.MyRecyclerView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_test)
    MyRecyclerView mRecyclerTest;
    @BindView(R.id.rl_top_layout)
    RelativeLayout mTopLayout;

    TestAdapter mTestAdapter;
    private List<TestBean> mTestBeanList;

    private int mLastY;
    private int mTopPlayLayoutHeight ;

    private FrameLayout.LayoutParams mTopPlayParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTestData();
        initRecyclerView();

        mTopPlayLayoutHeight = (int) DpUtil.dp2px(this, 64f);
        mTopPlayParams = (FrameLayout.LayoutParams) mTopLayout.getLayoutParams();

        mRecyclerTest.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
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
//                mTestAdapter.removeItem(position);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) event.getY();
                int deltaY = y - mLastY;
                if (Math.abs(deltaY) > 30) {
                    //是否完全显示
                    if (mTopPlayParams.topMargin >= -mTopPlayLayoutHeight && mTopPlayParams.topMargin <= 0) {
                        if (deltaY > 0) {//下滑
                            int translationY = (int) (mRecyclerTest.getY() + deltaY);
                            mRecyclerTest.setY(translationY);
//                            mRecyclerviewAudiio.animate().y(translationY).start();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
