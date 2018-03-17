package cn.foxconn.matthew.myapp.wanandroid.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.foxconn.matthew.myapp.R;
import cn.foxconn.matthew.myapp.wanandroid.base.BaseActivity;
import cn.foxconn.matthew.myapp.wanandroid.base.BasePresenter;
import cn.foxconn.matthew.myapp.wanandroid.fragment.HomeFragment;
import cn.foxconn.matthew.myapp.wanandroid.fragment.TypeFragment;
import cn.foxconn.matthew.myapp.wanandroid.fragment.UserFragment;
import cn.foxconn.matthew.myapp.utils.UIUtil;
import cn.foxconn.matthew.myapp.wanandroid.widget.FontTextView;

/**
 * @author:Matthew
 * @date:2018/3/2
 * @email:guocheng0816@163.com
 */

public class WanAndroidActivity extends BaseActivity {
    @BindView(R.id.ft_search)
    FontTextView ft_search;
    @BindView(R.id.ft_hot)
    FontTextView ft_hot;
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ft_home)
    FontTextView ft_home;
    @BindView(R.id.tv_home)
    TextView tv_home;
    @BindView(R.id.ll_type)
    LinearLayout ll_type;
    @BindView(R.id.ft_type)
    FontTextView ft_type;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.ll_user)
    LinearLayout ll_user;
    @BindView(R.id.ft_user)
    FontTextView ft_user;
    @BindView(R.id.tv_user)
    TextView tv_user;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private static List<android.support.v4.app.Fragment> mFragments = new ArrayList<>();

    @Override
    protected int getContentResId() {
        return R.layout.activity_wan_android;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        setTabColor(ft_home, tv_home);
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(TypeFragment.newInstance());
        mFragments.add(UserFragment.newInstance());
        //TODO 还需添加其他碎片布局
        viewPager.setAdapter(new cn.foxconn.matthew.myapp.wanandroid.adapter.PagerAdapter(getSupportFragmentManager(), mFragments));
        viewPager.setCurrentItem(0, false);
        //设置偏移的页数
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setTabColor(ft_home, tv_home);
                        break;
                    case 1:
                        setTabColor(ft_type, tv_type);
                        break;
                    case 2:
                        setTabColor(ft_user,tv_user);
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.ll_home, R.id.ll_type, R.id.ll_user, R.id.ft_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                viewPager.setCurrentItem(0);
                setTabColor(ft_home, tv_home);
                break;
            case R.id.ll_type:
                viewPager.setCurrentItem(1);
                setTabColor(ft_type, tv_type);
                break;
            case R.id.ll_user:
                viewPager.setCurrentItem(2);
                setTabColor(ft_user, tv_user);
                break;
            case R.id.ft_search:
                startActivity(new Intent(this,SearchActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 设置导航栏的选择状态和颜色
     *
     * @param fontTextView
     * @param textView
     */
    private void setTabColor(FontTextView fontTextView, TextView textView) {
        ft_home.setTextColor(UIUtil.getColor(R.color.tab_bar_normal));
        tv_home.setTextColor(UIUtil.getColor(R.color.tab_bar_normal));
        ft_type.setTextColor(UIUtil.getColor(R.color.tab_bar_normal));
        tv_type.setTextColor(UIUtil.getColor(R.color.tab_bar_normal));
        ft_user.setTextColor(UIUtil.getColor(R.color.tab_bar_normal));
        tv_user.setTextColor(UIUtil.getColor(R.color.tab_bar_normal));
        fontTextView.setTextColor(UIUtil.getColor(R.color.tab_bar_selected));
        textView.setTextColor(UIUtil.getColor(R.color.tab_bar_selected));

    }

    public static List<android.support.v4.app.Fragment> getFragments(){
        return mFragments;
    }
}