package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.BannerVo;
import com.stylefeng.guns.api.film.vo.CatVo;
import com.stylefeng.guns.api.film.vo.SourceVo;
import com.stylefeng.guns.api.film.vo.YearVo;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVo;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVo;
import com.stylefeng.guns.rest.modular.film.vo.FilmRequestVo;
import com.stylefeng.guns.rest.modular.vo.ResponseVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
@Controller
@RequestMapping("/film/")
public class FilmController {

    private static final  String IMG_PRE = "http://img.meetingshop.cn";


    @Reference(interfaceClass = FilmServiceAPI.class)
    private FilmServiceAPI filmServiceAPI;

    @RequestMapping(value = "getIndex",method = RequestMethod.POST)
    @ResponseBody
    public ResponseVo getIndex(){

        FilmIndexVo filmIndexVo = new FilmIndexVo();
        //获取banner信息
        filmIndexVo.setBanners(filmServiceAPI.getBanners());
        //获取正在上映的电影信息
        filmIndexVo.setHotFilms(filmServiceAPI.getHotFilms(true,8));
        //获取即将上映的电影信息
        filmIndexVo.setSoonFilms(filmServiceAPI.getSoonFilms(true,10));
        //票房排行
        filmIndexVo.setBoxRanking(filmServiceAPI.getBoxRanking());
        //获取受欢迎的榜单
        filmIndexVo.setExpectRanking(filmServiceAPI.getExpectRanking());
        //获取前100
        filmIndexVo.setTop100(filmServiceAPI.getTop());
        return ResponseVo.success(IMG_PRE,filmIndexVo) ;
    }


    @RequestMapping(value = "getConditionList",method = RequestMethod.GET)
    @ResponseBody
    public ResponseVo getConditionList(@RequestParam(name = "catId",required = false,defaultValue = "99") String catId,
                                       @RequestParam(name = "sourceId",required = false,defaultValue = "99") String sourceId,
                                       @RequestParam(name = "yearId",required = false,defaultValue = "99") String yearId){

        FilmConditionVo filmConditionVO = new FilmConditionVo();

        // 标识位
        boolean flag = false;
        // 类型集合
        List<CatVo> cats = filmServiceAPI.getCats();
        List<CatVo> catResult = new ArrayList<>();
        CatVo cat = null;
        for(CatVo catVO : cats){
            // 判断集合是否存在catId，如果存在，则将对应的实体变成active状态
            // 6
            // 1,2,3,99,4,5 ->
            /*
                优化：【理论上】
                    1、数据层查询按Id进行排序【有序集合 -> 有序数组】
                    2、通过二分法查找
             */
            if(catVO.getCatId().equals("99")){
                cat = catVO;
                continue;
            }
            if(catVO.getCatId().equals(catId)){
                flag = true;
                catVO.setActive(true);
            }else{
                catVO.setActive(false);
            }
            catResult.add(catVO);
        }
        // 如果不存在，则默认将全部变为Active状态
        if(!flag){
            cat.setActive(true);
            catResult.add(cat);
        }else{
            cat.setActive(false);
            catResult.add(cat);
        }


        // 片源集合
        flag=false;
        List<SourceVo> sources = filmServiceAPI.getSourcs();
        List<SourceVo> sourceResult = new ArrayList<>();
        SourceVo sourceVO = null;
        for(SourceVo source : sources){
            if(source.getSourceId().equals("99")){
                sourceVO = source;
                continue;
            }
            if(source.getSourceId().equals(catId)){
                flag = true;
                source.setActive(true);
            }else{
                source.setActive(false);
            }
            sourceResult.add(source);
        }
        // 如果不存在，则默认将全部变为Active状态
        if(!flag){
            sourceVO.setActive(true);
            sourceResult.add(sourceVO);
        }else{
            sourceVO.setActive(false);
            sourceResult.add(sourceVO);
        }

        // 年代集合
        flag=false;
        List<YearVo> years = filmServiceAPI.getYears();
        List<YearVo> yearResult = new ArrayList<>();
        YearVo yearVO = null;
        for(YearVo year : years){
            if(year.getYearId().equals("99")){
                yearVO = year;
                continue;
            }
            if(year.getYearId().equals(catId)){
                flag = true;
                year.setActive(true);
            }else{
                year.setActive(false);
            }
            yearResult.add(year);
        }
        // 如果不存在，则默认将全部变为Active状态
        if(!flag){
            yearVO.setActive(true);
            yearResult.add(yearVO);
        }else{
            yearVO.setActive(false);
            yearResult.add(yearVO);
        }

        filmConditionVO.setCatInfo(catResult);
        filmConditionVO.setSourceInfo(sourceResult);
        filmConditionVO.setYearInfo(yearResult);

        return ResponseVo.success(filmConditionVO);
    }


    @RequestMapping(value = "getFilms",method = RequestMethod.GET)
    @ResponseBody
    public ResponseVo getFilms(FilmRequestVo filmRequestVo){



        return null;
    }










}
