package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = FilmServiceAPI.class,loadbalance = "roundrobin")
public class DefaultFilmServiceImpl implements FilmServiceAPI {

    @Resource
    private MoocBannerTMapper bannerTMapper;

    @Resource
    private MoocFilmTMapper filmTMapper;


    @Resource
    private MoocCatDictTMapper catDictTMapper;

    @Resource
    private MoocSourceDictTMapper sourceDictTMapper;

    @Resource
    private MoocYearDictTMapper yearDictTMapper;



    @Override
    public List<BannerVo> getBanners() {
        List<MoocBannerT> moocBannerTs = bannerTMapper.selectList(null);
        List<BannerVo> bannerVoList = new ArrayList<>();
        if (moocBannerTs.size()>0){
            for (MoocBannerT banner: moocBannerTs ) {
                BannerVo bannerVo = new BannerVo();
                bannerVo.setBannerId(banner.getUuid()+"");
                bannerVo.setBannerAddress(banner.getBannerAddress());
                bannerVo.setBannerUrl(banner.getBannerUrl());
                bannerVoList.add(bannerVo);
            }
        }
        return bannerVoList;
    }

    private  List<FilmInfoVo> getFilmInfos( List<MoocFilmT> moocFilmTS){
        List<FilmInfoVo> filmInfoVos = new ArrayList<>();

        for (MoocFilmT moocFilmT : moocFilmTS){
            FilmInfoVo filmInfoVo = new FilmInfoVo();
            filmInfoVo.setShowTime(DateUtil.getDay(moocFilmT.getFilmTime()));
            filmInfoVo.setScore(moocFilmT.getFilmScore());
            filmInfoVo.setImgAddress(moocFilmT.getImgAddress());
            filmInfoVo.setFilmType(moocFilmT.getFilmType());
            filmInfoVo.setFilmScore(moocFilmT.getFilmScore());
            filmInfoVo.setFilmName(moocFilmT.getFilmName());
            filmInfoVo.setFilmId(moocFilmT.getUuid()+"");
            filmInfoVo.setExpectNum(moocFilmT.getFilmPresalenum());
            filmInfoVo.setBoxNum(moocFilmT.getFilmBoxOffice());
            filmInfoVos.add(filmInfoVo);
        }

        return  filmInfoVos;
    }




    @Override
    public FilmVo getHotFilms(boolean isLimit, int nums,int nowPage,int sortId,int sourceId,int yearId,int catId) {
        FilmVo filmVo = new FilmVo();
        List<FilmInfoVo> filmInfos = new ArrayList<>();
        EntityWrapper<MoocFilmT> wrapper = new EntityWrapper<>();
        //添加约束条件  影片状态,1-正在热映，2-即将上映，3-经典影片
        wrapper.eq("film_status","1");

        if (isLimit){
            //判断是否是首页需要的内容
            Page<MoocFilmT> page = new Page<>(1,nums);
            List<MoocFilmT> moocFilmTS = filmTMapper.selectPage(page, wrapper);
            filmInfos = getFilmInfos(moocFilmTS);
            filmVo.setFilmNum(moocFilmTS.size());
        }else{
            //如果不是，返回列表  TODO

            // 如果不是，则是列表页，同样需要限制内容为热映影片
            Page<MoocFilmT> page = null;
            // 根据sortId的不同，来组织不同的Page对象
            // 1-按热门搜索，2-按时间搜索，3-按评价搜索
            switch (sortId){
                case 1 :
                    page = new Page<>(nowPage,nums,"film_box_office");
                    break;
                case 2 :
                    page = new Page<>(nowPage,nums,"film_time");
                    break;
                case 3 :
                    page = new Page<>(nowPage,nums,"film_score");
                    break;
                default:
                    page = new Page<>(nowPage,nums,"film_box_office");
                    break;
            }

            // 如果sourceId,yearId,catId 不为99 ,则表示要按照对应的编号进行查询
            if(sourceId != 99){
                wrapper.eq("film_source",sourceId);
            }
            if(yearId != 99){
                wrapper.eq("film_date",yearId);
            }
            if(catId != 99){
                // #2#4#22#
                String catStr = "%#"+catId+"#%";
                wrapper.like("film_cats",catStr);
            }

            List<MoocFilmT> moocFilms = filmTMapper.selectPage(page, wrapper);
            // 组织filmInfos
            filmInfos = getFilmInfos(moocFilms);
            filmVo.setFilmNum(moocFilms.size());

            // 需要总页数 totalCounts/nums -> 0 + 1 = 1
            // 每页10条，我现在有6条 -> 1
            int totalCounts = filmTMapper.selectCount(wrapper);
            int totalPages = (totalCounts/nums)+1;

            filmVo.setFilmInfo(filmInfos);
            filmVo.setTotalPages(totalPages);
            filmVo.setNowPage(nowPage);
        }

        return filmVo;
    }

    @Override
    public FilmVo getSoonFilms(boolean isLimit, int nums,int nowPage,int sortId,int sourceId,int yearId,int catId) {

        FilmVo filmVo = new FilmVo();
        List<FilmInfoVo> filmInfos = new ArrayList<>();
        EntityWrapper<MoocFilmT> wrapper = new EntityWrapper<>();
        //添加约束条件  影片状态,1-正在热映，2-即将上映，3-经典影片
        wrapper.eq("film_status","2");

        if (isLimit){
            //判断是否是首页需要的内容
            Page<MoocFilmT> page = new Page<>(1,nums);
            List<MoocFilmT> moocFilmTS = filmTMapper.selectPage(page, wrapper);
            filmInfos = getFilmInfos(moocFilmTS);
            filmVo.setFilmNum(moocFilmTS.size());
        }else{
            // 如果不是，则是列表页，同样需要限制内容为即将上映影片
            Page<MoocFilmT> page = null;
            // 根据sortId的不同，来组织不同的Page对象
            // 1-按热门搜索，2-按时间搜索，3-按评价搜索
            switch (sortId){
                case 1 :
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
                case 2 :
                    page = new Page<>(nowPage,nums,"film_time");
                    break;
                case 3 :
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
                default:
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
            }

            // 如果sourceId,yearId,catId 不为99 ,则表示要按照对应的编号进行查询
            if(sourceId != 99){
                wrapper.eq("film_source",sourceId);
            }
            if(yearId != 99){
                wrapper.eq("film_date",yearId);
            }
            if(catId != 99){
                // #2#4#22#
                String catStr = "%#"+catId+"#%";
                wrapper.like("film_cats",catStr);
            }

            List<MoocFilmT> moocFilms = filmTMapper.selectPage(page, wrapper);
            // 组织filmInfos
            filmInfos = getFilmInfos(moocFilms);
            filmVo.setFilmNum(moocFilms.size());

            // 需要总页数 totalCounts/nums -> 0 + 1 = 1
            // 每页10条，我现在有6条 -> 1
            int totalCounts = filmTMapper.selectCount(wrapper);
            int totalPages = (totalCounts/nums)+1;

            filmVo.setFilmInfo(filmInfos);
            filmVo.setTotalPages(totalPages);
            filmVo.setNowPage(nowPage);
        }

        return filmVo;
    }

    @Override
    public FilmVo getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVo filmVO = new FilmVo();
        List<FilmInfoVo> filmInfos = new ArrayList<>();

        // 即将上映影片的限制条件
        EntityWrapper<MoocFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","3");

        // 如果不是，则是列表页，同样需要限制内容为即将上映影片
        Page<MoocFilmT> page = null;
        // 根据sortId的不同，来组织不同的Page对象
        // 1-按热门搜索，2-按时间搜索，3-按评价搜索
        switch (sortId){
            case 1 :
                page = new Page<>(nowPage,nums,"film_box_office");
                break;
            case 2 :
                page = new Page<>(nowPage,nums,"film_time");
                break;
            case 3 :
                page = new Page<>(nowPage,nums,"film_score");
                break;
            default:
                page = new Page<>(nowPage,nums,"film_box_office");
                break;
        }

        // 如果sourceId,yearId,catId 不为99 ,则表示要按照对应的编号进行查询
        if(sourceId != 99){
                entityWrapper.eq("film_source",sourceId);
        }
        if(yearId != 99){
            entityWrapper.eq("film_date",yearId);
        }
        if(catId != 99){
            // #2#4#22#
            String catStr = "%#"+catId+"#%";
            entityWrapper.like("film_cats",catStr);
        }

        List<MoocFilmT> moocFilms = filmTMapper.selectPage(page, entityWrapper);
        // 组织filmInfos
        filmInfos = getFilmInfos(moocFilms);
        filmVO.setFilmNum(moocFilms.size());

        // 需要总页数 totalCounts/nums -> 0 + 1 = 1
        // 每页10条，我现在有6条 -> 1
        int totalCounts = filmTMapper.selectCount(entityWrapper);
        int totalPages = (totalCounts/nums)+1;

        filmVO.setFilmInfo(filmInfos);
        filmVO.setTotalPages(totalPages);
        filmVO.setNowPage(nowPage);
        return filmVO;
    }

    @Override
    public List<FilmInfoVo> getBoxRanking() {
        //条件 ->正在上映的，票房前10名
        EntityWrapper<MoocFilmT> wrapper = new EntityWrapper<>();
        //添加约束条件  影片状态,1-正在热映，2-即将上映，3-经典影片
        wrapper.eq("film_status","1");
        Page<MoocFilmT> page = new Page<>(1,10,"film_box_office");
        List<MoocFilmT> moocFilmTS = filmTMapper.selectPage(page, wrapper);

        List<FilmInfoVo> filmInfos =    getFilmInfos(moocFilmTS);

        return filmInfos;
    }

    @Override
    public List<FilmInfoVo> getExpectRanking() {
        //条件 ->即将上映的，预售前10名
        EntityWrapper<MoocFilmT> wrapper = new EntityWrapper<>();
        //添加约束条件  影片状态,1-正在热映，2-即将上映，3-经典影片
        wrapper.eq("film_status","2");
        Page<MoocFilmT> page = new Page<>(1,10,"film_preSaleNum");
        List<MoocFilmT> moocFilmTS = filmTMapper.selectPage(page, wrapper);

        List<FilmInfoVo> filmInfos =    getFilmInfos(moocFilmTS);
        return filmInfos;
    }

    @Override
    public List<FilmInfoVo> getTop() {
        //条件 ->正在上映的，评分前10名
        EntityWrapper<MoocFilmT> wrapper = new EntityWrapper<>();
        //添加约束条件  影片状态,1-正在热映，2-即将上映，3-经典影片
        wrapper.eq("film_status","1");
        Page<MoocFilmT> page = new Page<>(1,10,"film_score");
        List<MoocFilmT> moocFilmTS = filmTMapper.selectPage(page, wrapper);
        List<FilmInfoVo> filmInfos =    getFilmInfos(moocFilmTS);
        return filmInfos;
    }



    @Override
    public List<CatVo> getCats() {
        List<CatVo> catVos = new ArrayList<>();
        List<MoocCatDictT> moocCatDictTS = catDictTMapper.selectList(null);

        if(moocCatDictTS.size()>0){
            for (MoocCatDictT catDict:  moocCatDictTS  ) {
                    CatVo catVo = new CatVo();
                    catVo.setCatId(catDict.getUuid()+"");
                    catVo.setCatName(catDict.getShowName());

                    catVos.add(catVo);
            }
        }
        return catVos;
    }

    @Override
    public List<SourceVo> getSourcs() {
        List<SourceVo> sourceVos = new ArrayList<>();

        List<MoocSourceDictT> moocSourceDictTS = sourceDictTMapper.selectList(null);
        if (moocSourceDictTS.size()>0){
            for(MoocSourceDictT sourceDictT : moocSourceDictTS){
                SourceVo sourceVo = new SourceVo();
                sourceVo.setSourceId(sourceDictT.getUuid()+"");
                sourceVo.setSourceName(sourceDictT.getShowName());

                sourceVos.add(sourceVo);
            }
        }
        return sourceVos;
    }

    @Override
    public List<YearVo> getYears() {
        List<YearVo> yearVos = new ArrayList<>();

        List<MoocYearDictT> moocYearDictTS = yearDictTMapper.selectList(null);
        if (moocYearDictTS.size()>0){
            for (MoocYearDictT yearDictT: moocYearDictTS  ) {
                YearVo yearVo = new YearVo();
                yearVo.setYearId(yearDictT.getUuid()+"");
                yearVo.setYearName(yearDictT.getShowName());

                yearVos.add(yearVo);

            }
        }
        return yearVos;
    }
}
