package cn.foxconn.matthew.mobilesafe.ui.activity;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.foxconn.matthew.mobilesafe.R;
import cn.foxconn.matthew.mobilesafe.bean.pojo.ArticleBean;
import cn.foxconn.matthew.mobilesafe.ui.adapter.CollectAtrcicleAdapter;
import cn.foxconn.matthew.mobilesafe.ui.base.BaseActivity;
import cn.foxconn.matthew.mobilesafe.ui.presenter.CollectPresenter;
import cn.foxconn.matthew.mobilesafe.ui.view.CollectView;
import cn.foxconn.matthew.mobilesafe.widget.FontTextView;

/**
 * @author:Matthew
 * @date:2018/3/13
 * @email:guocheng0816@163.com
 */

public class CollectActivity extends BaseActivity<CollectView, CollectPresenter>
        implements CollectView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.ft_return)
    FontTextView ft_return;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_no_collect)
    TextView tv_no_collect;

    CollectAtrcicleAdapter mAtrcicleAdapter;

    @Override
    protected int getContentResId() {
        return R.layout.activity_collect;
    }

    @Override
    protected CollectPresenter createPresenter() {
        return new CollectPresenter();
    }

    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("我的收藏");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAtrcicleAdapter=new CollectAtrcicleAdapter(this,null,tv_no_collect);
        mRecyclerView.setAdapter(mAtrcicleAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAtrcicleAdapter.setOnLoadMoreListener(this,mRecyclerView);

        onRefresh();
    }

    @OnClick(R.id.ft_return)
    public void onClick(View view){
        finish();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mPresenter.getRefreshData();
    }

    /**
     * 请求更多
     */
    @Override
    public void onLoadMoreRequested() {
        mPresenter.getMoreData();
    }


    @Override
    public void showRefreshView(final boolean isRefresh) {
//保证首次加载数据时，有加载动画效果
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(isRefresh);
            }
        });
    }

    @Override
    public void onRefreshSuccess(List<ArticleBean> data) {
        mAtrcicleAdapter.setNewData(data);
        tv_no_collect.setVisibility(data.size()==0? View.VISIBLE:View.GONE);
    }

    @Override
    public void onRefreshFail(String errorString) {
        Snackbar.make(mRecyclerView,errorString,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMoreSuccess(List<ArticleBean> data) {
        if(data.size()==0){
            mAtrcicleAdapter.loadMoreEnd();
        }else {
            mAtrcicleAdapter.addData(data);
            mAtrcicleAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onLoadMoreFail(String errorString) {
        mAtrcicleAdapter.loadMoreComplete();
        Snackbar.make(mRecyclerView,errorString,Snackbar.LENGTH_SHORT).show();
    }
}