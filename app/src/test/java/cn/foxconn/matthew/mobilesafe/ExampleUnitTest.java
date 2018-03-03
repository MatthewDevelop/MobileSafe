package cn.foxconn.matthew.mobilesafe;

import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import cn.foxconn.matthew.mobilesafe.api.WanService;
import cn.foxconn.matthew.mobilesafe.model.ResponseData;
import cn.foxconn.matthew.mobilesafe.model.pojo.BannerBean;
import cn.foxconn.matthew.mobilesafe.model.pojoVO.ArticleListVO;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void MD5Test() {
        //计算文件或字符串的特征码，不可逆，任何MD5都是32位,
        String str = "111";
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] digest = instance.digest(str.getBytes());
            StringBuffer sb=new StringBuffer();
            for (byte b : digest) {
                //获取字节低八位有效值
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if(hexString.length()<2)
                    hexString="0"+hexString;
                System.out.println(hexString);
                sb.append(hexString);
            }
            System.out.println(sb);
            System.out.println(sb.length());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void APITest(){
        String BASE_URL = "http://wanandroid.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WanService movieService = retrofit.create(WanService.class);
        boolean test=true;
        if(test){
        }else {
            movieService.getHomeBannerList().subscribe(new Subscriber<ResponseData<List<BannerBean>>>() {
                @Override
                public void onCompleted() {
                    System.out.println("finish");
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(ResponseData<List<BannerBean>> listResponseData) {
                    System.out.println(listResponseData.toString());
                }
            });
            movieService.getHomeAtricleList(5).subscribe(new Subscriber<ResponseData<ArticleListVO>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseData<ArticleListVO> articleListVOResponseData) {
                    System.out.println(articleListVOResponseData.toString());
                }
            });
        }
    }
}