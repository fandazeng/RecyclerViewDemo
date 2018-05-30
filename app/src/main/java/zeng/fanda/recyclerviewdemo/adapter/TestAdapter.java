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
public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TestBean> mTestBeanList;
    private OnItemClickListener mOnItemClickListener;

    public TestAdapter(Context context, List<TestBean> testBeanList) {
        mContext = context;
        mTestBeanList = testBeanList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new TestViewHolderSmall(LayoutInflater.from(mContext).inflate(R.layout.item_test_small, parent, false));
        } else {
            return new TestViewHolderBig(LayoutInflater.from(mContext).inflate(R.layout.item_test_big, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        int type = holder.getItemViewType();
        if (type == 0) {
            TestViewHolderSmall holderSmall = (TestViewHolderSmall) holder;
            holderSmall.mName.setText("我是帅哥");
            holderSmall.mContent.setText("是的"+mTestBeanList.get(position).getRank());

            if (mOnItemClickListener != null) {
                holderSmall.mRootVeiw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClick(view, holder.getLayoutPosition());
                    }
                });
            }
        } else {
            TestViewHolderBig holderBig = (TestViewHolderBig) holder;
            holderBig.mName.setText("我是帅哥");
            holderBig.mContent.setText("是的"+mTestBeanList.get(position).getRank());
            if (mOnItemClickListener != null) {
                holderBig.mRootVeiw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClick(view, holder.getLayoutPosition());
                    }
                });
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public int getItemCount() {
        return mTestBeanList.size();
    }

    class TestViewHolderSmall extends RecyclerView.ViewHolder {

        @BindView(R.id.root_view)
        View mRootVeiw;

        @BindView(R.id.tv_name)
        TextView mName ;

        @BindView(R.id.tv_content)
        TextView mContent ;

        public TestViewHolderSmall(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class TestViewHolderBig extends RecyclerView.ViewHolder {

        @BindView(R.id.root_view)
        View mRootVeiw;

        @BindView(R.id.tv_name)
        TextView mName ;

        @BindView(R.id.tv_content)
        TextView mContent ;

        public TestViewHolderBig(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void removeItem(int position) {
        mTestBeanList.remove(position);
        notifyItemRemoved(position);

    }

}
