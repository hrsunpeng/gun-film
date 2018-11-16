package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;


public interface FilmServiceAPI {

    //获取banners
    List<BannerVo> getBanners();

    //获取热映影片
    FilmVo getHotFilms(boolean isLimit , int nums,int nowPage,int sortId,int sourceId,int yearId,int catId);

    //获取即将上映影片【受欢迎度排序】
    FilmVo getSoonFilms(boolean isLimit , int nums,int nowPage,int sortId,int sourceId,int yearId,int catId);

    // 获取经典影片
    FilmVo getClassicFilms(int nums,int nowPage,int sortId,int sourceId,int yearId,int catId);

    //获取票房排行榜
    List<FilmInfoVo> getBoxRanking();

    //获取人气排行榜
    List<FilmInfoVo> getExpectRanking();

    //获取Top100
    List<FilmInfoVo> getTop();


    //========获取影片调节接口

    /**分类条件**/
    List<CatVo> getCats();

    /**资源条件**/
    List<SourceVo> getSourcs();

    /**年代条件**/
    List<YearVo> getYears();
}
