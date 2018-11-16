package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 热映 即将上映
 */
@Data
public class FilmVo implements Serializable{

    private int filmNum;

    private List<FilmInfoVo> filmInfo;

    private int totalPages;

    private int  nowPage;


}
