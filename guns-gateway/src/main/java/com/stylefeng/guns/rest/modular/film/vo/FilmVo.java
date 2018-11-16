package com.stylefeng.guns.rest.modular.film.vo;

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

}
