package cn.foxconn.matthew.myapp.wanandroid.presenter;

import cn.foxconn.matthew.myapp.wanandroid.bean.pojoVO.ArticleListVO;
import cn.foxconn.matthew.myapp.wanandroid.helper.RxObserverHelper;
import cn.foxconn.matthew.myapp.wanandroid.model.DataModel;
import cn.foxconn.matthew.myapp.wanandroid.model.DataModelImpl;
import cn.foxconn.matthew.myapp.wanandroid.base.BasePresenter;
import cn.foxconn.matthew.myapp.wanandroid.view.CollectView;

/**
 * @author:Matthew
 * @date:2018/3/13
 * @email:guocheng0816@163.com
 */

public class CollectPresenter extends BasePresenter<CollectView> {

    DataModel mDataModel;
    private int mCurrentPage;

    public CollectPresenter(){
        mDataModel=new DataModelImpl();
    }

    /**
     * 获取收藏列表
     */
    public void getRefreshData(){
        mCurrentPage=0;
        mDataModel.getCollectList(mCurrentPage, new RxObserverHelper<ArticleListVO>() {
            @Override
            protected void _onCompleted() {
                super._onCompleted();
                getView().showRefreshView(false);
            }

            @Override
            protected void _onSubscribe() {
                super._onSubscribe();
                getView().showRefreshView(true);
            }

            @Override
            protected void _onNext(ArticleListVO articleListVO) {
                getView().onRefreshSuccess(articleListVO.getDatas());
            }

            @Override
            protected void _onError(String message) {
                getView().onRefreshFail(message);
                getView().showRefreshView(false);
            }
        });
    }

    /**
     * 加载更多
     */
    public void getMoreData(){
        mCurrentPage=mCurrentPage+1;
        mDataModel.getCollectList(mCurrentPage, new RxObserverHelper<ArticleListVO>() {
            @Override
            protected void _onNext(ArticleListVO articleListVO) {
                getView().onLoadMoreSuccess(articleListVO.getDatas());
            }

            @Override
            protected void _onError(String message) {
                getView().onLoadMoreFail(message);
            }
        });

    }

}