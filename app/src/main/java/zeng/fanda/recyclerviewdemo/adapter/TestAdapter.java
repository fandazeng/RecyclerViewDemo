package zeng.fanda.recyclerviewdemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zeng.fanda.recyclerviewdemo.R;
import zeng.fanda.recyclerviewdemo.bean.TestBean;

/**
 * @author 曾凡达
 * @date 2018/5/29
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private Context mContext;
    private List<TestBean> mTestBeanList;

    public TestAdapter(Context context, List<TestBean> testBeanList) {
        mContext = context;
        mTestBeanList = testBeanList;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_test_small, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        holder.mName.setText("我是帅哥");
        holder.mContent.setText("是的");
    }

    @Override
    public int getItemCount() {
        return mTestBeanList.size();
    }

    class TestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView mName ;

        @BindView(R.id.tv_content)
        TextView mContent ;

        public TestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
