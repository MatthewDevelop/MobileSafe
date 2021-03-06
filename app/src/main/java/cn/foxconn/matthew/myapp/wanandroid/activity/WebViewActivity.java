package cn.foxconn.matthew.myapp.wanandroid.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.foxconn.matthew.myapp.R;
import cn.foxconn.matthew.myapp.wanandroid.base.WanAndroidBaseActivity;
import cn.foxconn.matthew.myapp.wanandroid.presenter.WebViewPresenter;
import cn.foxconn.matthew.myapp.wanandroid.view.CommonWebView;
import cn.foxconn.matthew.myapp.utils.ActivityUtil;
import cn.foxconn.matthew.myapp.utils.SharesUtils;
import cn.foxconn.matthew.myapp.wanandroid.widget.CustomPopWindow;
import cn.foxconn.matthew.myapp.wanandroid.widget.FontTextView;
import cn.foxconn.matthew.myapp.wanandroid.widget.WebViewFragment;

/**
 * @author:Matthew
 * @date:2018/3/5
 * @email:guocheng0816@163.com
 */

public class WebViewActivity
        extends WanAndroidBaseActivity<CommonWebView, WebViewPresenter>
        implements CommonWebView {

    public static final String WEB_URL = "web_url";

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ft_return)
    FontTextView mFtReturn;
    @BindView(R.id.ft_more)
    FontTextView mFtMore;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.rl_topbar)
    RelativeLayout mRlTopbar;
    @BindView(R.id.webView_container)
    NestedScrollView webContainer;

    private String url;
    private WebViewFragment mWebViewFragment;
    private WebView mWebView;
    private CustomPopWindow mMorePopWindow;

    /**
     * 启动Activity
     *
     * @param context
     * @param url
     */
    public static void runActivity(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WEB_URL, url);
        context.startActivity(intent);
    }

    @Override
    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    @Override
    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //get WebView
        mWebView = mWebViewFragment.getWebView();
        mPresenter.setWebView(mWebView, url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_webview;
    }

    @Override
    protected WebViewPresenter createPresenter() {
        return new WebViewPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        mWebViewFragment = new WebViewFragment();
        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), mWebViewFragment, R.id.webView_container);
    }

    @Override
    protected void init() {
        super.init();
        url = getIntent().getStringExtra(WEB_URL);
    }

    /**
     * 处理页面回退
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.ft_return, R.id.ft_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ft_return:
                finish();
                break;
            case R.id.ft_more:
                //TODO Android PobWindow
                View popView = View.inflate(this, R.layout.pupup_webview_more, null);
                mMorePopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                        .setView(popView)
                        .enableBackgroundDark(false)
                        .create()
                        .showAsDropDown(mFtMore, -430, -10);

                //设置点击事件
                popView.findViewById(R.id.tv_refresh).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWebView.reload();
                        mMorePopWindow.dissmiss();
                    }
                });
                //分享
                popView.findViewById(R.id.tv_share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharesUtils.share(WebViewActivity.this, mWebView.getUrl());
                        mMorePopWindow.dissmiss();
                    }
                });
                //复制链接
                popView.findViewById(R.id.tv_copy_link).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //todo 剪切板的使用
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(getString(R.string.copy_link), mWebView.getUrl()));
                        Snackbar.make(getWindow().getDecorView(), R.string.copy_link_success, Snackbar.LENGTH_SHORT).show();
                        mMorePopWindow.dissmiss();
                    }
                });
                //用系统浏览器打开
                popView.findViewById(R.id.tv_open_out).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO open with system Browser
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebView.getUrl()));
                        startActivity(intent);
                        mMorePopWindow.dissmiss();
                    }
                });
                break;
            default:
                break;
        }
    }
}
