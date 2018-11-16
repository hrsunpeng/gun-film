package com.stylefeng.guns.rest.modular.film.vo;

import com.stylefeng.guns.api.film.vo.CatVo;
import com.stylefeng.guns.api.film.vo.SourceVo;
import com.stylefeng.guns.api.film.vo.YearVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilmConditionVo implements Serializable{


    List<CatVo> catInfo;

    List<SourceVo> sourceInfo;

    List<YearVo> yearInfo;

}
